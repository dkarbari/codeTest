package com.opentext.bn.solutiondesigner.vo.serviceregistry;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ProvisioningInfo {

	private String assetInfo;
	private String parameterSchema;
	private String bulkUploadTemplate;

	@Max(Integer.MAX_VALUE)
	private Integer provisioningCategory;

	@Max(Integer.MAX_VALUE)
	private int dataCollectionCategory;

	private boolean tgaProvisioning;

	@Max(Integer.MAX_VALUE)
	private int tgaProvisioningMethod;

	@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_1000, message = "Prov. Resource:"
			+ SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_1000)
	private String tgaProvisioningResource;

	@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_100, message = "Cartridge API:"
			+ SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_100)
	private String cartridgeApiVersion;

	@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_100, message = "Cartridge Version:"
			+ SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_100)
	private String cartridgeVersion;

	@Pattern(regexp = SolutionDesignerConstant.URL_REG_EXP, message = "Cartridge Validate URL:"
			+ SolutionDesignerConstant.URL_REG_EXP_MESSAGE)
	private String cartridgeValidateURL;

	@Pattern(regexp = SolutionDesignerConstant.URL_REG_EXP, message = "Cartridge Provisioning URL:"
			+ SolutionDesignerConstant.URL_REG_EXP_MESSAGE)
	private String cartridgeProvisioningURL;

	@Pattern(regexp = SolutionDesignerConstant.URL_REG_EXP, message = "Cartridge Parameter Schema:"
			+ SolutionDesignerConstant.URL_REG_EXP_MESSAGE)
	private String parameterSchemaUri;

	@Pattern(regexp = SolutionDesignerConstant.PROV_CATEG_REG_EXP, message = SolutionDesignerConstant.PROV_CATEG_REG_EXP_MESSAGE)
	private String serviceMetaInfoId;

	@Pattern(regexp = SolutionDesignerConstant.URL_REG_EXP, message = "External Provisioning URL:"
			+ SolutionDesignerConstant.URL_REG_EXP_MESSAGE)
	private String externalProvisioningURL;

	public String getAssetInfo() {
		return assetInfo;
	}

	public String getBulkUploadTemplate() {
		return bulkUploadTemplate;
	}

	public String getCartridgeApiVersion() {
		return cartridgeApiVersion;
	}

	public String getCartridgeProvisioningURL() {
		return cartridgeProvisioningURL;
	}

	public String getCartridgeValidateURL() {
		return cartridgeValidateURL;
	}

	public String getCartridgeVersion() {
		return cartridgeVersion;
	}

	public int getDataCollectionCategory() {
		return dataCollectionCategory;
	}

	public String getExternalProvisioningURL() {
		return externalProvisioningURL;
	}

	public String getParameterSchema() {
		return parameterSchema;
	}

	public String getParameterSchemaUri() {
		return parameterSchemaUri;
	}

	public int getProvisioningCategory() {
		return provisioningCategory;
	}

	public String getServiceMetaInfoId() {
		return serviceMetaInfoId;
	}

	public int getTgaProvisioningMethod() {
		return tgaProvisioningMethod;
	}

	public String getTgaProvisioningResource() {
		return tgaProvisioningResource;
	}

	public boolean isTgaProvisioning() {
		return tgaProvisioning;
	}

	public void setAssetInfo(String assetInfo) {
		this.assetInfo = assetInfo;
	}

	public void setBulkUploadTemplate(String bulkUploadTemplate) {
		this.bulkUploadTemplate = bulkUploadTemplate;
	}

	public void setCartridgeApiVersion(String cartridgeApiVersion) {
		this.cartridgeApiVersion = cartridgeApiVersion;
	}

	public void setCartridgeProvisioningURL(String cartridgeProvisioningURL) {
		this.cartridgeProvisioningURL = cartridgeProvisioningURL;
	}

	public void setCartridgeValidateURL(String cartridgeValidateURL) {
		this.cartridgeValidateURL = cartridgeValidateURL;
	}

	public void setCartridgeVersion(String cartridgeVersion) {
		this.cartridgeVersion = cartridgeVersion;
	}

	public void setDataCollectionCategory(int dataCollectionCategory) {
		this.dataCollectionCategory = dataCollectionCategory;
	}

	public void setExternalProvisioningURL(String externalProvisioningURL) {
		this.externalProvisioningURL = externalProvisioningURL;
	}

	public void setParameterSchema(String parameterSchema) {
		this.parameterSchema = parameterSchema;
	}

	public void setParameterSchemaUri(String parameterSchemaUri) {
		this.parameterSchemaUri = parameterSchemaUri;
	}

	public void setProvisioningCategory(int provisioningCategory) {
		this.provisioningCategory = provisioningCategory;
	}

	public void setServiceMetaInfoId(String serviceMetaInfoId) {
		this.serviceMetaInfoId = serviceMetaInfoId;
	}

	public void setTgaProvisioning(boolean tgaProvisioning) {
		this.tgaProvisioning = tgaProvisioning;
	}

	public void setTgaProvisioningMethod(int tgaProvisioningMethod) {
		this.tgaProvisioningMethod = tgaProvisioningMethod;
	}

	public void setTgaProvisioningResource(String tgaProvisioningResource) {
		this.tgaProvisioningResource = tgaProvisioningResource;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
