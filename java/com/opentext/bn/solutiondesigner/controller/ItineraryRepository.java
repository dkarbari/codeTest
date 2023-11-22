package com.opentext.bn.solutiondesigner.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gxs.orch.rest.client.OrchRestException;
import com.gxs.orch.rest.client.contentserver.OrchContentServerRestClient;
import com.opentext.bn.solutiondesigner.util.OrchContentServerUtils;
import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/api/v1/folders")
public class ItineraryRepository extends BaseController {

	@Value("${ldap.enabled}")
	private String ldapEnabled;

	final Logger logger = LoggerFactory.getLogger(ItineraryRepository.class);

	/***
	 * 
	 * Get files in a given folder.
	 * 
	 * Example: http://localhost:8080/api/v1/folders/{id}/files
	 * 
	 * @param filename - File to be searched.
	 * @param parent   - Parent folder where filename be searched
	 * @return - List of filename found
	 * @throws ResponseStatusException
	 * @throws JSONException
	 * @throws OrchRestException
	 */
	@ApiOperation(value = "Check existence of a file by name in parent folder (Roles=ITINERARY_DEVELOPER)")
	@RequestMapping(value = { "/{parent}/files/{filename}" }, method = RequestMethod.HEAD)
	public ResponseEntity<?> hasFile(@ApiIgnore
	HttpSession session,
			@ApiParam(value = "Parent could be common / personal / solution name.  Optional, by default personal folder")
			@PathVariable
			String parent, @ApiParam(value = "File name to search", example = "test01.oid")
			@PathVariable
			String filename) throws ResponseStatusException, JSONException, OrchRestException {

		ControllerUtil.hasRoleInSession(session, SolutionDesignerConstant.ROLE_ITINERARY_DEVELOPER);

		final OrchContentServerRestClient orchContentServerRestClient = OrchContentServerUtils
				.getNewContentServerRestClient(session, Boolean.parseBoolean(ldapEnabled));

		int item = -1;

		if (StringUtils.compare(parent, "global") == 0) {

			int personalFolderId = orchContentServerRestClient
					.validateToken(orchContentServerRestClient.getContentServerOTDSToken());
			final String searchResponse = orchContentServerRestClient
					.search("OTMeta:OTName:" + filename + " AND-NOT " + "OTParentID:" + personalFolderId, 50, 1);
			JSONObject jsonObj = new JSONObject(searchResponse);
			item = jsonObj.getInt("total_count");
		} else {

			final int parentID = OrchContentServerUtils.getParentID(orchContentServerRestClient, parent,
					orchContentServerRestClient.getContentServerOTDSToken());
			item = orchContentServerRestClient.getNodeId(parentID, filename);
		}
		logger.info("Request parent ID is: {}", parent);
		logger.info("Itinerary name string received: {}", filename);

		return (item > 0) ? ResponseEntity.ok().header("x-ottg-file-found", Boolean.toString(true)).build()
				: ResponseEntity.notFound().header("x-ottg-file-found", Boolean.toString(false)).build();
	}
}