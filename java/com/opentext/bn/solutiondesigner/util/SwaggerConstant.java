package com.opentext.bn.solutiondesigner.util;

public class SwaggerConstant {

	public static final String SCRIPT_SERVICE_INFORMATION = "{\r\n"
			+ "   \"scriptId\":\"e7321c3c-93a2-4585-a8d5-f3e67f8294ff\",\r\n"
			+ "   \"serviceCode\":\"Script Service\",\r\n" + "   \"taskName\":\"Script Service\",\r\n"
			+ "   \"taskEmbeddedProperties\":{\r\n" + "      \"childServiceCodes\":[\r\n" + "         {\r\n"
			+ "            \"type\":\"connector\",\r\n"
			+ "            \"serviceCode\":\"a2aSforceService(34)v1.0.0\",\r\n"
			+ "            \"endpoint\":\"8db1f63b-2c45-4483-8587-14e9cec268cd\"\r\n" + "         }\r\n" + "      ]\r\n"
			+ "   }\r\n" + "}";

	public static final String ITINERARY_INFORMATION = "{\r\n"
			+ "   \"itineraryId\":\"ea2174a5-25cd-4851-96da-e8afc591cf41\",\r\n"
			+ "   \"scriptId\":\"e7321c3c-93a2-4585-a8d5-f3e67f8294ff\",\r\n" + "   \"processUserInputs\":{\r\n"
			+ "      \"sender\":\"ZZ:LCLPRODEDI\",\r\n" + "      \"receiver\":\"08:925753CB01\",\r\n"
			+ "      \"solutionId\":\"C101SolnID\",\r\n" + "      \"docType\":\"999\",\r\n"
			+ "      \"snrf\":\"000001954\",\r\n" + "      \"dataType\":\"BIN\",\r\n"
			+ "      \"payloadKey\":\"Q14E-202008000000000189636237\",\r\n"
			+ "      \"payloadUri\":\"dsmkey=Q14E-202008000000000189636237&amp;nodeID=14&amp;dataStore=DSM\"\r\n"
			+ "   }\r\n" + "}";

	public static final String SCRIPT_CACHE_SAMPLE = "function process() {\r\n" + "    console.log(\"here\");\r\n"
			+ "}";

	public static final String SCRIPT_SERVICE_DESCRIPTION = "The field contains information relevant to script service.\r\n"
			+ "\r\nScript Id - Pass script id returned by \"/api/v1/scriptcaches\" .\r\n"
			+ "\r\nService Code - Use service code provided at the time of registration.\r\n"
			+ "\r\nTask Name - Use service name provided at the time of registration.\r\n" + "\r\n";

	public static final String ITINERARY_SERVICE_DESCRIPTION = "The field contains information relevant to itinerary execution.\r\n"
			+ "\r\nItinerary Id - Pass itinerary id returned by \"/api/v1/itinerarydefinitions\" .\r\n"
			+ "\r\nScript Id - Pass script id returned by \"/api/v1/scriptcaches\" .\r\n"
			+ "\r\nPlease refer http://orch-engine-qa.fulcrum.cfcr-lab.bp-paas.otxlab.net/orchEnginePublic/swaggerUI/index.html#/processes/post_v1_processes for more details\r\n"
			+ "\r\n";

	public static final String CREATE_SERVICE_DESCRIPTION = "The field contains information about services to be created."
			+ "For more information, please check with CMD team";

	public static final String CREATE_SERVICE_EXAMPLE = "{\r\n" + "  \"serviceInfo\": {\r\n"
			+ "    \"serviceCode\": \"string\",\r\n" + "    \"serviceName\": \"string\",\r\n"
			+ "    \"entryType\": \"string\",\r\n" + "    \"description\": \"string\",\r\n"
			+ "    \"serviceVersion\": \"string\",\r\n" + "    \"serviceDeploymentVersion\": \"string\",\r\n"
			+ "    \"computeZoneId\": \"string\",\r\n" + "    \"processingCell\": \"string\"\r\n" + "  },\r\n"
			+ "  \"orchestrationInfo\": {\r\n" + "    \"serviceClassName\": \"string\",\r\n"
			+ "    \"orchestrationType\": \"string\",\r\n" + "    \"serviceEnabled\": \"string\",\r\n"
			+ "    \"supportsDiagnostics\": true,\r\n" + "    \"invocationMode\": \"string\",\r\n"
			+ "    \"parentLogicalService\": \"string\"\r\n" + "  },\r\n" + "  \"provisioningInfo\": {\r\n"
			+ "    \"provisioningCategory\": 0,\r\n" + "    \"dataCollectionCategory\": 0,\r\n"
			+ "    \"tgaProvisioning\": true,\r\n" + "    \"tgaProvisioningMethod\": 0,\r\n"
			+ "    \"tgaProvisioningResource\": \"string\",\r\n" + "    \"externalProvisioningURL\": \"string\",\r\n"
			+ "    \"cartridgeApiVersion\": \"string\",\r\n" + "    \"cartridgeVersion\": \"string\",\r\n"
			+ "    \"cartridgeValidateURL\": \"string\",\r\n" + "    \"cartridgeProvisioningURL\": \"string\",\r\n"
			+ "    \"parameterSchemaUri\": \"string\",\r\n" + "    \"parameterSchema\": \"string\",\r\n"
			+ "    \"serviceMetaInfoId\": \"string\",\r\n" + "    \"bulkUploadTemplate\": \"string\",\r\n"
			+ "    \"assetInfo\": \"string\"\r\n" + "  },\r\n" + "  \"designerInfo\": {\r\n"
			+ "    \"designerDefinition\": \"string\",\r\n" + "    \"designerImage\": [\r\n" + "      \"string\"\r\n"
			+ "    ],\r\n" + "    \"embeddedProperties\": \"string\",\r\n" + "    \"legacyIcon\": [\r\n"
			+ "      \"string\"\r\n" + "    ]\r\n" + "  }\r\n" + "}";

	public static final String UPDATE_SERVICE_DESCRIPTION = "The field contains information about services to be update."
			+ "For more information, please check with CMD team";

	public static final String UPDATE_SERVICE_EXAMPLE = "{\r\n" + "  \"serviceInfo\": {\r\n"
			+ "    \"serviceCode\": \"string\",\r\n" + "    \"serviceName\": \"string\",\r\n"
			+ "    \"entryType\": \"string\",\r\n" + "    \"description\": \"string\",\r\n"
			+ "    \"serviceVersion\": \"string\",\r\n" + "    \"serviceDeploymentVersion\": \"string\",\r\n"
			+ "    \"computeZoneId\": \"string\",\r\n" + "    \"processingCell\": \"string\"\r\n" + "  },\r\n"
			+ "  \"orchestrationInfo\": {\r\n" + "    \"serviceClassName\": \"string\",\r\n"
			+ "    \"orchestrationType\": \"string\",\r\n" + "    \"serviceEnabled\": \"string\",\r\n"
			+ "    \"supportsDiagnostics\": true,\r\n" + "    \"invocationMode\": \"string\",\r\n"
			+ "    \"parentLogicalService\": \"string\"\r\n" + "  },\r\n" + "  \"provisioningInfo\": {\r\n"
			+ "    \"provisioningCategory\": 0,\r\n" + "    \"dataCollectionCategory\": 0,\r\n"
			+ "    \"tgaProvisioning\": true,\r\n" + "    \"tgaProvisioningMethod\": 0,\r\n"
			+ "    \"tgaProvisioningResource\": \"string\",\r\n" + "    \"externalProvisioningURL\": \"string\",\r\n"
			+ "    \"cartridgeApiVersion\": \"string\",\r\n" + "    \"cartridgeVersion\": \"string\",\r\n"
			+ "    \"cartridgeValidateURL\": \"string\",\r\n" + "    \"cartridgeProvisioningURL\": \"string\",\r\n"
			+ "    \"parameterSchemaUri\": \"string\",\r\n" + "    \"parameterSchema\": \"string\",\r\n"
			+ "    \"serviceMetaInfoId\": \"string\",\r\n" + "    \"bulkUploadTemplate\": \"string\",\r\n"
			+ "    \"assetInfo\": \"string\"\r\n" + "  },\r\n" + "  \"designerInfo\": {\r\n"
			+ "    \"designerDefinition\": \"string\",\r\n" + "    \"designerImage\": [\r\n" + "      \"string\"\r\n"
			+ "    ],\r\n" + "    \"embeddedProperties\": \"string\",\r\n" + "    \"legacyIcon\": [\r\n"
			+ "      \"string\"\r\n" + "    ]\r\n" + "  }\r\n" + "}";

}
