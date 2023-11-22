package com.opentext.bn.solutiondesigner.vo.serviceregistry;

import javax.validation.constraints.Pattern;

import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DesignerInfo {

	@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_100, message = SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_100)
	private String designerDefinition;

	private String designerImage;
	private String legacyIcon;
	private String embeddedProperties;

	public String getDesignerDefinition() {
		return designerDefinition;
	}

	public String getDesignerImage() {
		return designerImage;
	}

	public String getEmbeddedProperties() {
		return embeddedProperties;
	}

	public String getLegacyIcon() {
		return legacyIcon;
	}

	public void setDesignerDefinition(String designerDefinition) {
		this.designerDefinition = designerDefinition;
	}

	public void setDesignerImage(String designerImage) {
		this.designerImage = designerImage;
	}

	public void setEmbeddedProperties(String embeddedProperties) {
		this.embeddedProperties = embeddedProperties;
	}

	public void setLegacyIcon(String legacyIcon) {
		this.legacyIcon = legacyIcon;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
