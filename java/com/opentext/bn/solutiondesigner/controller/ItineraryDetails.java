/***
 * Proxy implementation for content server. Supports major activities provided by content server.
 * 
 */
package com.opentext.bn.solutiondesigner.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.gxs.orch.rest.client.OrchRestException;
import com.gxs.orch.rest.client.contentserver.OrchContentServerRestClient;
import com.opentext.bn.solutiondesigner.util.OrchContentServerUtils;
import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;
import com.opentext.bn.solutiondesigner.vo.ErrorResponse;
import com.opentext.bn.solutiondesigner.vo.FileUploadResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/api/v1/itineraries")

public class ItineraryDetails extends BaseController {

	@Value("${ldap.enabled}")
	private String ldapEnabled;

	final Logger logger = LoggerFactory.getLogger(ItineraryDetails.class);

	@ApiOperation(value = "Download itinerary from CS")
	@RequestMapping(value = { "/download/{nodeID}/{version}", "/download/{nodeID}" }, method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, response = String.class, message = "Success"),
			@ApiResponse(code = 400, response = ErrorResponse.class, message = "Bad Request"),
			@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error") })

	public ResponseEntity<?> downloadItinerary(

			final HttpSession session,

			@ApiParam(value = "Internal node id", required = true, example = "41877")
			final @PathVariable
			int nodeID,

			@ApiParam(value = "Itinerary version", required = false, example = "2.0")
			final @PathVariable
			Optional<Integer> version) throws JSONException, OrchRestException, IOException {

		ControllerUtil.hasRoleInSession(session, SolutionDesignerConstant.ROLE_ITINERARY_DEVELOPER);

		logger.info("Node ID is: {}", nodeID);

		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		final OrchContentServerRestClient orchContentServerRestClient = OrchContentServerUtils
				.getNewContentServerRestClient(session, Boolean.parseBoolean(ldapEnabled));

		if (!(version.isPresent())) {
			orchContentServerRestClient.downloadFile(byteArrayOutputStream, nodeID);
		} else {
			logger.info("Version number is: {}", version.get());
			orchContentServerRestClient.downloadFile(byteArrayOutputStream, nodeID, version.get());
		}

		if (byteArrayOutputStream.size() == 0) {
			logger.warn("File does not have information. Please validate request and Content Server");

			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					"Please validate request parameters and Content Server settings".getBytes(), null);
		}
		logger.trace("Successfully received data from Content Server ");

		final HttpHeaders headers = new HttpHeaders();
		ControllerUtil.setCacheHeaders(headers);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream")).headers(headers)
				.body(new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
	}

	@ApiOperation(value = "Check existence of a file by name in parent folder")
	@RequestMapping(value = { "/{parent}/files/{filename}" }, method = RequestMethod.HEAD)

	public ResponseEntity<?> hasFile(@ApiIgnore
	HttpSession session,
			@ApiParam(value = "Parent could be common / personal / solution name", required = false, example = "Personal")
			final @PathVariable
			String parent,

			@ApiParam(value = "File name to search", example = "test01.oid")
			final @PathVariable
			String filename) throws JSONException, IllegalArgumentException, OrchRestException

	{

		ControllerUtil.hasRoleInSession(session, SolutionDesignerConstant.ROLE_ITINERARY_DEVELOPER);

		final OrchContentServerRestClient orchContentServerRestClient = OrchContentServerUtils
				.getNewContentServerRestClient(session, Boolean.parseBoolean(ldapEnabled));

		int item = -1;

		if (StringUtils.compare(parent, "global") == 0) {

			final String searchResponse = orchContentServerRestClient.search("OTMeta:OTName:" + filename, 50, 1);
			JSONObject jsonObj = new JSONObject(searchResponse);
			item = jsonObj.getInt("total_count");
			logger.debug("SolutionDesigner Services, searchResponse: "+searchResponse);																  
		} else {

			final int parentID = OrchContentServerUtils.getParentID(orchContentServerRestClient, parent,
					orchContentServerRestClient.getContentServerOTDSToken());
			item = orchContentServerRestClient.getNodeId(parentID, filename);
			logger.debug("SolutionDesigner Services, parentID: "+parentID);													  
		}
		logger.info("Request parent ID is: {}", parent);
		logger.info("Itinerary name string to search for: {}", filename);

		return (item > 0) ? ResponseEntity.ok().header("x-ottg-file-found", Boolean.toString(true)).build()
				: ResponseEntity.notFound().header("x-ottg-file-found", Boolean.toString(false)).build();
	}

	@ApiOperation(value = "List itineraries based on parent information")
	@RequestMapping(value = { "/list/{parent}" }, method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, response = String.class, message = "Success"),
			@ApiResponse(code = 400, response = ErrorResponse.class, message = "Bad Request"),
			@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error") })

	public ResponseEntity<?> listItinerary(HttpSession session,
			@ApiParam(value = "Parent could be common / personal / solution name.  Optional, by default personal folder")
			final @PathVariable
			Optional<String> parent,

			@ApiParam(value = "Number of entries per page", example = "50")
			final @RequestParam(required = false, defaultValue = "50")
			int limit,

			@ApiParam(value = "Page to be fetched", example = "1")
			final @RequestParam(required = false, defaultValue = "1")
			int page

	) throws ResponseStatusException, OrchRestException, JSONException {

		ControllerUtil.hasRoleInSession(session, SolutionDesignerConstant.ROLE_ITINERARY_DEVELOPER);

		final OrchContentServerRestClient orchContentServerRestClient = OrchContentServerUtils
				.getNewContentServerRestClient(session, Boolean.parseBoolean(ldapEnabled));

		final int parentID = OrchContentServerUtils.getParentID(orchContentServerRestClient,
				parent.isPresent() ? parent.get() : null, orchContentServerRestClient.getContentServerOTDSToken());
		logger.info("Parent ID is: {}", parentID);

		final String listResponse = orchContentServerRestClient.listDirectory(parentID, limit, page);
		if (listResponse == null || listResponse.trim().equals("")) {
			logger.warn("Content Server does not return any input");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"List does not have any information. Please validate input parameter");
		}
		logger.trace("List directory response: {}", listResponse);
		return new ResponseEntity<String>(listResponse, HttpStatus.OK);
	}

	@ApiOperation(value = "Search shared itineraries by Name")
	@RequestMapping(value = { "/searchByName" }, method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, response = String.class, message = "Success"),
			@ApiResponse(code = 400, response = ErrorResponse.class, message = "Bad Request"),
			@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error") })

	public ResponseEntity<?> searchByName(HttpSession session,
			@ApiParam(value = "Itinerary name to be searched", example = "printHello")
			final @RequestParam(required = true)
			String searchName,

			@ApiParam(value = "Parent could be common / personal / solution name.  Optional, by default personal folder")
			final @RequestParam(required = false)
			String parent,

			@ApiParam(value = "Number of entries per page", example = "50")
			final @RequestParam(required = false, defaultValue = "50")
			int limit,

			@ApiParam(value = "Page to be fetched", example = "1")
			final @RequestParam(required = false, defaultValue = "1")
			int page

	) throws ResponseStatusException, JSONException, OrchRestException {

		ControllerUtil.hasRoleInSession(session, SolutionDesignerConstant.ROLE_ITINERARY_DEVELOPER);

		final OrchContentServerRestClient orchContentServerRestClient = OrchContentServerUtils
				.getNewContentServerRestClient(session, Boolean.parseBoolean(ldapEnabled));

		final int parentID = OrchContentServerUtils.getParentID(orchContentServerRestClient, parent,
				orchContentServerRestClient.getContentServerOTDSToken());
		logger.info("Parent ID is: {}", parentID);

		logger.info("Search string received: {}", searchName);
		final StringBuffer modifiedSearchString = new StringBuffer();
		modifiedSearchString.append("OTName:");
		modifiedSearchString.append(searchName);
		modifiedSearchString.append(" AND OTLocation:");
		modifiedSearchString.append(parentID);
		logger.info("Modified search string: {}", modifiedSearchString.toString());

		final String searchResponse = orchContentServerRestClient.search(modifiedSearchString.toString(), limit, page);
		if (searchResponse == null || searchResponse.trim().equals("")) {
			logger.warn("Content Server does not return any input");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Search does not have any information. Please validate input parameter");

		}
		logger.trace("List directory response: {}", searchResponse);

		return new ResponseEntity<String>(searchResponse, HttpStatus.OK);
	}

	@ApiOperation(value = "Upload itinerary to CS")
	@RequestMapping(value = { "/upload/{parent}", "/upload" }, method = RequestMethod.POST)
	public ResponseEntity<?> uploadItinerary(HttpSession session, final @RequestParam("file")
	MultipartFile uploadfile, final @PathVariable
	Optional<String> parent) throws ResponseStatusException, JSONException, OrchRestException, IOException {

		ControllerUtil.hasRoleInSession(session, SolutionDesignerConstant.ROLE_ITINERARY_DEVELOPER);

		final OrchContentServerRestClient orchContentServerRestClient = OrchContentServerUtils
				.getNewContentServerRestClient(session, Boolean.parseBoolean(ldapEnabled));

		
		if (uploadfile.getOriginalFilename() == null || uploadfile.getOriginalFilename().trim().equals("")) {
			logger.warn("File name received from client is invalid");
			throw HttpClientErrorException.create(HttpStatus.BAD_REQUEST, null, null,
					SolutionDesignerConstant.ITINERARY_REG_EXP_MESSAGE.getBytes(), null);
		}
		
		final boolean bValidateItineraryName = FilenameUtils.removeExtension(uploadfile.getOriginalFilename())
				.matches(SolutionDesignerConstant.ITINERARY_NAME_REG_EXP);
		if (!bValidateItineraryName) {
			logger.warn("File name received from client [" + uploadfile.getOriginalFilename()
					+ "] cannot contain spaces and special characters");
			throw HttpClientErrorException.create(HttpStatus.BAD_REQUEST, null, null,
					SolutionDesignerConstant.ITINERARY_REG_EXP_MESSAGE.getBytes(), null);
		}

		final int parentID = OrchContentServerUtils.getParentID(orchContentServerRestClient,
				parent.isPresent() ? parent.get() : null, orchContentServerRestClient.getContentServerOTDSToken());
		logger.info("Parent ID is: {}", parentID);

		final String uploadedResponse = orchContentServerRestClient.uploadFile(uploadfile.getInputStream(), parentID,
				uploadfile.getOriginalFilename());
		if (uploadedResponse == null || uploadedResponse.trim().equals("")) {
			logger.warn("Issue while uploading file to content server. Try after sometime");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Issue while uploading file to content server. Try after sometime");
		}
		logger.trace("File uploaded successfully. Response is: {} ", uploadedResponse);

		final FileUploadResponse fileUploadResponse = new FileUploadResponse();
		fileUploadResponse.setFileName(uploadfile.getOriginalFilename());
		fileUploadResponse.setMessage("Successfully uploaded");
		fileUploadResponse.setUploadedTime(Calendar.getInstance().getTime());

		return new ResponseEntity<FileUploadResponse>(fileUploadResponse, HttpStatus.OK);
	}

}
