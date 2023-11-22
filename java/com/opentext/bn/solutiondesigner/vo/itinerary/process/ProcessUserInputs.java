package com.opentext.bn.solutiondesigner.vo.itinerary.process;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;

public class ProcessUserInputs {

	@NotNull(message = "SNRF is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.SNRF_REG_EXP, message = SolutionDesignerConstant.SNRF_REG_EXP_MESSAGE)
	private String snrf = null;

	@NotNull(message = "Sender information is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.EDI_ADDRESS_REG_EXP, message = SolutionDesignerConstant.EDI_ADDRESS_ERROR_MESSAGE)
	private String sender = null;

	@NotNull(message = "Doc type is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.DOC_TYPE_REG_EXP, message = SolutionDesignerConstant.DOC_TYPE_REG_EXP_MESSAGE)
	private String docType = null;

	@NotNull(message = "Receiever information is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.EDI_ADDRESS_REG_EXP, message = SolutionDesignerConstant.EDI_ADDRESS_ERROR_MESSAGE)
	private String receiver = null;

	@NotNull(message = "Data type is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.DATA_TYPE_REG_EXP, message = SolutionDesignerConstant.DATA_TYPE_REG_EXP_MESSAGE)
	private String dataType = null;

	@NotNull(message = "Solution ID is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.SID_REG_EXP, message = SolutionDesignerConstant.SID_REG_EXP_MESSAGE)
	private String solutionId = null;

	@NotNull(message = "Payload key is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.PAY_KEY_REG_EXP, message = SolutionDesignerConstant.PAY_KEY_REG_EXP_MESSAGE)
	private String payloadKey = null;

	@NotNull(message = "Payload URI is mandatory")
	@Pattern(regexp = SolutionDesignerConstant.PAY_URI_REG_EXP, message = SolutionDesignerConstant.PAY_URI_REG_EXP_MESSAGE)
	private String payloadUri = null;

	public String getDataType() {
		return dataType;
	}

	public String getDocType() {
		return docType;
	}

	public String getPayloadKey() {
		return payloadKey;
	}

	public String getPayloadUri() {
		return payloadUri;
	}

	public String getReceiver() {
		return receiver;
	}

	public String getSender() {
		return sender;
	}

	public String getSnrf() {
		return snrf;
	}

	public String getSolutionId() {
		return solutionId;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public void setPayloadKey(String payloadKey) {
		this.payloadKey = payloadKey;
	}

	public void setPayloadUri(String payloadUri) {
		this.payloadUri = payloadUri;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setSnrf(String snrf) {
		this.snrf = snrf;
	}

	public void setSolutionId(String solutionId) {
		this.solutionId = solutionId;
	}

}