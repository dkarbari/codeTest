package com.opentext.bn.solutiondesigner.util;

import com.opentext.bn.solutiondesigner.vo.RequestVO;
import com.opentext.bn.solutiondesigner.vo.ResponseVO;

public abstract class CustomRunnable implements Runnable {
	private RequestVO requestVO = null;
	private ResponseVO responseVO = null;

	public RequestVO getRequestVO() {
		return requestVO;
	}

	public ResponseVO getResponseVO() {
		return responseVO;
	}

	public void setRequestVO(final RequestVO requestVO) {
		this.requestVO = requestVO;
	}

	public void setResponseVO(final ResponseVO responseVO) {
		this.responseVO = responseVO;
	}

	public void run() {
		run();
	}
}