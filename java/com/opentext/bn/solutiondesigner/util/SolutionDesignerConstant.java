package com.opentext.bn.solutiondesigner.util;

import java.util.Map;
import java.util.TreeMap;

public class SolutionDesignerConstant {
	public static final String ORCH_ENTITY_TYPE = "orchestration_service";
	public static final String ORCH_SCRIPT_SERVICE = "orch-script";
	public static final String ORCH_ADAPTER_SERVICE = "orch-adapter";
	public static final String ROLE_ITINERARY_DEVELOPER = "ITINERARY_DEVELOPER";
	public static final String ROLE_SERVICE_DEVELOPER = "SERVICE_DEVELOPER";

	public static final Map<String, String> KEY_ROLES_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	static {
		KEY_ROLES_MAP.put(ROLE_ITINERARY_DEVELOPER, ROLE_ITINERARY_DEVELOPER);
		KEY_ROLES_MAP.put(ROLE_SERVICE_DEVELOPER, ROLE_SERVICE_DEVELOPER);
	}

	/*
	 * Regular expression for EDI Address. Use https://regexr.com to understand the
	 * structure of EDI Address
	 */
	public static final String EDI_ADDRESS_REG_EXP = "^((([A-Za-z0-9]{0,4}):([A-Za-z0-9]{0,34})){0,35})$";
	public static final String EDI_ADDRESS_ERROR_MESSAGE = " The format for Edi address is Qualifier:Address. Qualifier has maximum of 4 chars. Address has maximum of 34chars. So totally Edi address must have maximum of 35";

	/*
	 * Regular expression for SNRF. Explanation: length must be between 4 to 10
	 * characters. Must contain alphanumeric values only
	 */
	public static final String SNRF_REG_EXP = "^([A-Za-z0-9]){4,10}$";
	public static final String SNRF_REG_EXP_MESSAGE = " SNRF length must be between 4 to 10 characters. Must contain alphanumeric values only";

	/*
	 * Regular expression for DOC TYPE. Explanation: type length must be 3
	 * characters. Must contain numeric values only
	 */
	public static final String DOC_TYPE_REG_EXP = "^([0-9]){3}$";
	public static final String DOC_TYPE_REG_EXP_MESSAGE = " Document type length must be 3 characters. Must contain numeric values only";

	/*
	 * Regular expression for DATA TYPE. Explanation: length must be between 3 to 6
	 * characters. Must contain alphabet values only
	 */
	public static final String DATA_TYPE_REG_EXP = "^([A-za-z]){3,6}$";
	public static final String DATA_TYPE_REG_EXP_MESSAGE = " Data type length must be between 3 to 6 characters. Must contain alphabet values only";

	/*
	 * Regular expression for Solution ID. Explanation: length must be between 3 to
	 * 40 characters. Must contain alphanumeric values only
	 */
	public static final String SID_REG_EXP = "^([A-za-z0-9]){3,40}$";
	public static final String SID_REG_EXP_MESSAGE = " Data type length must be between 3 to 40 characters. Must contain alphanumeric values only";

	/*
	 * Regular expression for Pay load Key. Explanation: The format for Payload Key
	 * is Qualifier-Address. Qualifier has minimum of 4 chars and maximum 6 chars.
	 * Qualifier has minimum of 6 chars and maximum 34 chars
	 */
	public static final String PAY_KEY_REG_EXP = "^((([A-Z0-9]{4,6})-([A-Za-z0-9]{6,34})))$";
	public static final String PAY_KEY_REG_EXP_MESSAGE = " The format for Payload Key is Qualifier-Address. Qualifier has minimum of 4 chars and maximum 6 chars. Qualifier has minimum of 6 chars and maximum 34 chars.";

	/*
	 * Regular expression for Pay load URI. Explanation: Must contain alphanumeric
	 * values and the following special characters -,=,; and & only
	 */
	public static final String PAY_URI_REG_EXP = "^[A-Za-z0-9=;&_-]+$";
	public static final String PAY_URI_REG_EXP_MESSAGE = " Must contain alphanumeric values and the following special characters -,=,; and & only";

	/* Regular expression for UUID */
	public static final String UUID_REG_EXP = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
	public static final String UUID_REG_EXP_MESSAGE = " Must follow UUID format";

	/*
	 * Regular expression for itinerary name. Explanation: length must be between 4
	 * to 100 characters. Only alphanumeric values are allowed
	 */
	public static final String ITINERARY_NAME_REG_EXP = "^([A-Za-z0-9_\\-]){4,100}$";
	public static final String ITINERARY_REG_EXP_MESSAGE = " Itinerary name length must be between 4 to 100 characters. Alphanumeric characters and '_', '-' characters are allowed";

	/*
	 * Regular expression for Task name. Explanation: length must be between 4 to 50
	 * characters. Alphanumeric values and white space are allowed
	 */
	public static final String TASK_NAME_REG_EXP = "^([A-Za-z0-9\\s]){4,50}$";
	public static final String TASK_NAME_REG_EXP_MESSAGE = " Task name length must be between 4 to 50 characters. Must contain alphanumeric values and white space";

	/*
	 * Regular expression for Service Code. Explanation: length must be between 4 to
	 * 100 characters. Alphanumeric values and the following special characters
	 * !@#$%^&()=+{}[];'\",._- are allowed
	 * 
	 * A2A connectors allow non-alphanumerical characters for service code and service name.
	 * But scripts created via SD is not allowed to have non-alphanumerical characters for service code, and service name.
	 * As both Script service and A2A connectors share the same functionality to retrieve the list from CMD, declared regular expression contains special characters.
	 * 
	 */
	public static final String SCODE_REG_EXP = "^([A-Za-z0-9!@#$%^&()=+{}\\[\\];'\",._\\-\\s]){4,100}$";
	public static final String SCODE_REG_EXP_MESSAGE = " Service code / name length must be between 4 to 100 characters. Only alphanumeric are allowed";

	/*
	 * Regular expression for Connector ID. Explanation: length must be between 4 to
	 * 50 characters. Alphanumeric values and the following special characters
	 * !@#$%^&()=+{}[];'\",._- are allowed
	 */
	public static final String CONN_SCODE_REG_EXP = "^([A-Za-z0-9!@#$%^&()=+{}\\[\\];'\",._\\-\\s]){4,100}$";
	public static final String CONN_SCODE_REG_EXP_MESSAGE = " Connector service code length must be between 0 to 10 characters. Alphanumeric characters and the following special characters are:!@#$%^&()=+{}[];'\\\",._-  allowed";

	/*
	 * Regular expression for generic message. Explanation: length must be minimum 4
	 * characters. Alphanumeric values and the following special characters
	 * !@#$%^&()=+{}[];'\",._- are allowed
	 */
	public static final String MSG_REG_EXP = "^([A-Za-z0-9!*@#$%^&()=+{}\\[\\];'\",._\\-\\s]){4,}$";
	public static final String MSG_REG_EXP_MESSAGE = "Message length must be minimum 4 characters.  Alphanumeric characters and the following special characters are:!@#$%^&()=+{}[];'\\\",._-  allowed";

	/*
	 * Regular expression for script name. Explanation: length must be between 4 to
	 * 50 characters. Alphanumeric values and the special characters . are allowed
	 */
	public static final String SCRIPT_FILE_NAME_REG_EXP = "^([A-Za-z0-9\\s]{4,50}).([j|t]{1})s$";
	public static final String SCRIPT_FILE_NAME_REG_EXP_MESSAGE = "File between 4 to 50 characters. Ex : test.js or test.js";

	/*
	 * Generic regular expression. Explanation: length must be between 0 to 10
	 * characters. Alphanumeric values and the following special characters
	 * !@#$%^&()=+{}[];'\",._- are allowed
	 */
	public static final String GENERAL_REG_EXP_LEN_10 = "^([A-Za-z0-9!@#$%^&()=+{}\\[\\];'\",._\\-\\s]){0,10}$";
	public static final String GENERAL_REG_EXP_MESSAGE_LEN_10 = " length must be between 0 to 10 characters. Alphanumeric characters and the following special characters are:!@#$%^&()=+{}[];'\",._-  allowed";

	/*
	 * Generic regular expression. Explanation: length must be between 0 to 15
	 * characters. Alphanumeric values and the following special characters
	 * !@#$%^&()=+{}[];'\",._- are allowed
	 */
	public static final String GENERAL_REG_EXP_LEN_15 = "^([A-Za-z0-9!@#$%^&()=+{}\\[\\];'\",._\\-\\s]){0,15}$";
	public static final String GENERAL_REG_EXP_MESSAGE_LEN_15 = " length must be between 0 to 15 characters. Alphanumeric characters and the following special characters are:!@#$%^&()=+{}[];'\",._-  allowed";

	/*
	 * Generic regular expression. Explanation: length must be between 0 to 20
	 * characters. Alphanumeric values and the following special characters
	 * !@#$%^&()=+{}[];'\",._- are allowed
	 */
	public static final String GENERAL_REG_EXP_LEN_20 = "^([A-Za-z0-9!@#$%^&()=+{}\\[\\];'\",._\\-\\s]){0,20}$";
	public static final String GENERAL_REG_EXP_MESSAGE_LEN_20 = " length must be between 0 to 20 characters. Alphanumeric characters and the following special characters are:!@#$%^&()=+{}[];'\",._-  allowed";

	/*
	 * Generic regular expression. Explanation: length must be between 0 to 50
	 * characters. Alphanumeric values and the following special characters
	 * !@#$%^&()=+{}[];'\",._- are allowed
	 */
	public static final String GENERAL_REG_EXP_LEN_50 = "^([A-Za-z0-9!@#$%^&()=+{}\\[\\];'\",._\\-\\s]){0,50}$";
	public static final String GENERAL_REG_EXP_MESSAGE_LEN_50 = " length must be between 0 to 50 characters. Alphanumeric characters and the following special characters are:!@#$%^&()=+{}[];'\",._-  allowed";

	/*
	 * Generic regular expression. Explanation: length must be between 0 to 100
	 * characters. Alphanumeric values and the following special characters
	 * !@#$%^&()=+{}[];'\",._- are allowed
	 */
	public static final String GENERAL_REG_EXP_LEN_100 = "^([A-Za-z0-9!@#$%^&()=+{}\\[\\];'\",._\\-\\s]){0,100}$";
	public static final String GENERAL_REG_EXP_MESSAGE_LEN_100 = " length must be between 0 to 100 characters. Alphanumeric characters and the following special characters are:!@#$%^&()=+{}[];'\",._-  allowed";

	/*
	 * Generic regular expression. Explanation: length must be between 0 to 200
	 * characters. Alphanumeric values and the following special characters
	 * !@#$%^&()=+{}[];'\",._- are allowed
	 */
	public static final String GENERAL_REG_EXP_LEN_200 = "^([A-Za-z0-9!@#$%^&()=+{}\\[\\];'\",._\\-\\s]){0,200}$";
	public static final String GENERAL_REG_EXP_MESSAGE_LEN_200 = " length must be between 0 to 100 characters. Alphanumeric characters and the following special characters are:!@#$%^&()=+{}[];'\",._-  allowed";

	/*
	 * Generic regular expression. Explanation: length must be between 0 to 250
	 * characters. Alphanumeric values and the following special characters
	 * !@#$%^&()=+{}[];'\",._- are allowed
	 */
	public static final String GENERAL_REG_EXP_LEN_250 = "^([A-Za-z0-9!@#$%^&()=+{}\\[\\];'\",._\\-\\s]){0,250}$";
	public static final String GENERAL_REG_EXP_MESSAGE_LEN_250 = " length must be between 0 to 250 characters. Alphanumeric characters and the following special characters are:!@#$%^&()=+{}[];'\",._-  allowed";

	/*
	 * Generic regular expression. Explanation: length must be between 0 to 500
	 * characters. Alphanumeric values and the following special characters
	 * !@#$%^&()=+{}[];'\",._- are allowed
	 */
	public static final String GENERAL_REG_EXP_LEN_500 = "^([A-Za-z0-9!@#$%^&()=+{}\\[\\];'\",._\\-\\s]){0,500}$";
	public static final String GENERAL_REG_EXP_MESSAGE_LEN_500 = " length must be between 0 to 500 characters. Alphanumeric characters and the following special characters are:!@#$%^&()=+{}[];'\",._-  allowed";

	/*
	 * Generic regular expression. Explanation: length must be between 0 to 1000
	 * characters. Alphanumeric values and the following special characters
	 * !@#$%^&()=+{}[];'\",._- are allowed
	 */
	public static final String GENERAL_REG_EXP_LEN_1000 = "^([A-Za-z0-9!@#$%^&()=+{}\\[\\];'\",._\\-\\s]){0,1000}$";
	public static final String GENERAL_REG_EXP_MESSAGE_LEN_1000 = " length must be between 0 to 1000 characters. Alphanumeric characters and the following special characters are:!@#$%^&()=+{}[];'\",._-  allowed";

	/*
	 * Service class expression. Follow java class name standard
	 */
	public static final String SERVICE_CLASS_NAME_REG_EXP = "^([a-zA-Z_$][a-zA-Z0-9_()$]*\\.)*[a-zA-Z_$][a-zA-Z0-9_()$]*$";
	public static final String SERVICE_CLASS_NAME_REG_EXP_MESSAGE = " Class name should follow java standard";

	/*
	 * Regular expression for PROV_CATEG_REG_EXP. Explanation: length must be
	 * between 0 to 10 characters. Only numeric values are allowed
	 */
	public static final String PROV_CATEG_REG_EXP = "^([0-9]){0,10}$";
	public static final String PROV_CATEG_REG_EXP_MESSAGE = " Provision category length must be between 0 to 10 characters. Only numeric values are allowed";

	/*
	 * Regular expression for DATA_CATEG_REG_EXP. Explanation: length must be
	 * between 0 to 10 characters. Only numeric values are allowed
	 */
	public static final String DATA_CATEG_REG_EXP = "^([0-9]){0,10}$";
	public static final String DATA_CATEG_REG_EXP_MESSAGE = " Data collection category length must be between 0 to 10 characters. Only numeric values are allowed";

	/*
	 * Regular expression for PROV_METHOD_REG_EXP. Explanation: length must be
	 * between 0 to 10 characters. Only numeric values are allowed
	 */
	public static final String PROV_METHOD_REG_EXP = "^([0-9]){0,10}$";
	public static final String PROV_METHOD_REG_EXP_MESSAGE = " Provision method length must be between 0 to 10 characters. Only numeric values are allowedy";

	public static final String URL_REG_EXP = "^(http://|https://)?(www.)?([a-zA-Z0-9\\-]+)[.a-zA-Z0-9]*?([a-zA-Z0-9=\\-.()&:/?]+)?$";
	public static final String URL_REG_EXP_MESSAGE = " Must follow URL structure";

	/*
	 * Regular expression for CMD_SERVICE_REG_EXP. Explanation: length must be
	 * between 5 to 20 characters. Only numeric values are allowed
	 */
	public static final String CMD_SERVICE_REG_EXP = "^([0-9]){5,20}$";
	public static final String CMD_SERVICE_REG_EXP_MESSAGE = " Service code must be between 5 to 20 characters.  Only numeric values are allowed";

	/*
	 * Regular expression for Service Code. Explanation: length must be between 1 to
	 * 50 characters. Must contain alphanumeric values and white space
	 */
	public static final String SCODE_SEARCH_REG_EXP = "^([A-Za-z0-9\\-_.*()\\s]){1,50}$";
	public static final String SCODE_SEARCH_REG_EXP_MESSAGE = " Service code length must be between 4 to 50 characters. Must contain alphanumeric values and white space";

	/*
	 * Regular expression for DOC TYPE. Explanation: type length must be 3
	 * characters. Must contain numeric values only
	 */
	public static final String SERVICE_INSTANCE_ID_REG_EXP = "^([0-9]){2,20}$";
	public static final String SERVICE_INSTANCE_ID_REG_EXP_MESSAGE = " Service Instance ID length must be between 2 to 20 characters. Must contain numeric values only";

	public static final String ORCH_TYPE = " Orch type";
	public static final String ENTRY_TYPE = " Entry type";
	public static final String DESCRIPTION = " Description";
	public static final String COMPUTE_ZONE = " Compute Zone";
	public static final String INVOCATION_MODE = " Invocation mode";
	public static final String PROCESSING_CELL = " Processing Cell";
	public static final String SERVICE_VERSION = " Service version";
	public static final String DEPLOYMENT_VERSION = " Deployment version";
	public static final String PARENT_LOGICAL_SERVICE = " Parent logical service";
}