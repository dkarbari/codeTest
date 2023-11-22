/***
 * Scripts mentioned in itineraries is stored in gitlab.
 * This proxy class communicates with gitlab to support major activities.
 */
package com.opentext.bn.solutiondesigner.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import org.gitlab4j.api.GitLabApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import com.gxs.orch.rest.client.gitlab.OrchGitlabRestClient;
import com.opentext.bn.solutiondesigner.exception.LdapTemplateException;
import com.opentext.bn.solutiondesigner.service.UserDetailsService;
import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;
import com.opentext.bn.solutiondesigner.vo.ErrorResponse;
import com.opentext.bn.solutiondesigner.vo.FileUploadResponse;
import com.opentext.bn.solutiondesigner.vo.UserDetails;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/v1/scripts")
@Api(tags = { "GitLab operations" })

public class ScriptController extends BaseController {

	@Autowired
	private OrchGitlabRestClient orchGitlabRestClient;

	@Autowired
	UserDetailsService userDetailsService;

	final Logger logger = LoggerFactory.getLogger(ItineraryDetails.class);

	@ApiOperation(value = "Download script content from GitLab")
	@RequestMapping(value = { "/{fileName}" }, method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, response = Resource.class, message = "Success"),
			@ApiResponse(code = 400, response = ErrorResponse.class, message = "Bad Request"),
			@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error") })

	public ResponseEntity<?> downloadScript(
			@ApiParam(value = "File name to be downloaded", required = true, example = "Script Service.js") @PathVariable final String fileName)
			throws GitLabApiException, IOException {

		ByteArrayInputStream byteArrayInputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		try {

			if (!(fileName.matches(SolutionDesignerConstant.SCRIPT_FILE_NAME_REG_EXP))) {
				throw new IllegalArgumentException(SolutionDesignerConstant.SCRIPT_FILE_NAME_REG_EXP_MESSAGE);
			}

			final HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
			orchGitlabRestClient.downloadFile(byteArrayOutputStream, orchGitlabRestClient.getProjectId(), fileName);
			byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(new InputStreamResource(byteArrayInputStream));

		} finally {
			closeInputStream(byteArrayInputStream);
			closeOutputStream(byteArrayOutputStream);
		}
	}

	@ApiOperation(value = "Upload script content in GitLab")
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, response = FileUploadResponse.class, message = "Success"),
			@ApiResponse(code = 400, response = ErrorResponse.class, message = "Bad Request"),
			@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error") })

	public FileUploadResponse uploadScript(

			@ApiParam(value = "JS Content") @RequestPart(name = "javaScriptContent", required = true) final MultipartFile javaScriptContent,

			@ApiParam(value = "TS Content") @RequestPart(name = "typeScriptContent", required = false) final MultipartFile typeScriptContent)
			throws GitLabApiException, IOException, LdapTemplateException {

		UserDetails userDetails = null;
		userDetails = userDetailsService.getUserDetails();
		if (!(javaScriptContent.getOriginalFilename().matches(SolutionDesignerConstant.SCRIPT_FILE_NAME_REG_EXP))) {
			throw new IllegalArgumentException(SolutionDesignerConstant.SCRIPT_FILE_NAME_REG_EXP_MESSAGE);
		}

		if (typeScriptContent != null && (!(typeScriptContent.getOriginalFilename()
				.matches(SolutionDesignerConstant.SCRIPT_FILE_NAME_REG_EXP)))) {
			throw new IllegalArgumentException(SolutionDesignerConstant.SCRIPT_FILE_NAME_REG_EXP_MESSAGE);
		}

		logger.info("Javascript content", new String(javaScriptContent.getBytes()));
		String javaScriptUploadedResponse = null;
		if (userDetails != null) {
			javaScriptUploadedResponse = orchGitlabRestClient.uploadFile(javaScriptContent.getInputStream(),
					orchGitlabRestClient.getProjectId(), javaScriptContent.getOriginalFilename(),
					userDetails.getAttrMap().get(userDetails.getUserDisplayNameAttr()),
					userDetails.getAttrMap().get(userDetails.getUserMailAttr()));
		} else {
			javaScriptUploadedResponse = orchGitlabRestClient.uploadFile(javaScriptContent.getInputStream(),
					orchGitlabRestClient.getProjectId(), javaScriptContent.getOriginalFilename());
		}
		if (javaScriptUploadedResponse == null || javaScriptUploadedResponse.trim().equals("")) {
			logger.error("Unable to upload java script content into GitLab.");
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					"Internal server issue while uploading script content. Please try after some time. If this issue continues, please escalate to operations"
							.getBytes(),
					null);
		}
		logger.debug("Javascript content uploaded successfully. Response is: {} ", javaScriptUploadedResponse);

		/* Create Response Object */
		final FileUploadResponse fileUploadResponse = new FileUploadResponse();
		fileUploadResponse.setFileName(javaScriptContent.getOriginalFilename());
		fileUploadResponse.setMessage("Successfully uploaded");
		fileUploadResponse.setUploadedTime(Calendar.getInstance().getTime());

		/* TypeScript content not exists */
		if (typeScriptContent == null) {
			logger.trace("No typescript content available.");
			return fileUploadResponse;
		}

		/* Upload TypeScript content into GitLab */
		String typeScriptUploadedResponse = null;
		if (userDetails != null) {
			typeScriptUploadedResponse = orchGitlabRestClient.uploadFile(typeScriptContent.getInputStream(),
					orchGitlabRestClient.getProjectId(), typeScriptContent.getOriginalFilename(),
					userDetails.getAttrMap().get(userDetails.getUserDisplayNameAttr()),
					userDetails.getAttrMap().get(userDetails.getUserMailAttr()));
		} else {
			typeScriptUploadedResponse = orchGitlabRestClient.uploadFile(typeScriptContent.getInputStream(),
					orchGitlabRestClient.getProjectId(), typeScriptContent.getOriginalFilename());
		}
		if (typeScriptUploadedResponse == null || typeScriptUploadedResponse.trim().equals("")) {
			logger.error("Unable to upload type script content into GitLab.");
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					"Internal server issue while uploading script content. Please try after some time. If this issue continues, please escalate to operations"
							.getBytes(),
					null);
		}
		logger.debug("Typescript content uploaded successfully. Response is: {} ", typeScriptUploadedResponse);
		return fileUploadResponse;
	}

	private void closeInputStream(final InputStream inputStream) {
		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (IOException eGenIoException) {
			logger.error("Unable to close input stream.", eGenIoException);
		}
	}

	private void closeOutputStream(final OutputStream outputStream) {
		try {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (IOException eGenIoException) {
			logger.error("Unable to close input stream.", eGenIoException);
		}
	}

}