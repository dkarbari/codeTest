package com.opentext.bn.solutiondesigner.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.opentext.bn.solutiondesigner.vo.RequestVO;
import com.opentext.bn.solutiondesigner.vo.ResponseVO;

@Component
@Service
@EnableScheduling
public class CustomScheduler {

	private static CustomScheduler customScheduler = null;

	public static synchronized CustomScheduler getCustomScheduler(final TaskScheduler taskScheduler) {
		if (customScheduler == null) {
			customScheduler = new CustomScheduler(taskScheduler);
		}

		return customScheduler;
	}

	private Map<String, ScheduledFuture<?>> scheduleFuture = null;

	private Map<String, CustomRunnable> customRunnableMap = null;

	final Logger logger = LoggerFactory.getLogger(CustomScheduler.class);

	private TaskScheduler taskScheduler;

	private CustomScheduler(final TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
		this.scheduleFuture = new HashMap<String, ScheduledFuture<?>>();
		this.customRunnableMap = new HashMap<String, CustomRunnable>();
	}

	public void addCustomTask(final String taskType, final CustomRunnable customizeRunnable, final int delay) {
		customRunnableMap.put(taskType, customizeRunnable);
		scheduleFuture.put(taskType, taskScheduler.scheduleAtFixedRate(customizeRunnable, delay));
	}

	public boolean cancelScheduler(final String orchestrationType) {
		return scheduleFuture.get(orchestrationType).cancel(true);
	}

	public RequestVO getRequestVO(final String orchestrationType) {

		if (customRunnableMap.containsKey(orchestrationType)) {
			return customRunnableMap.get(orchestrationType).getRequestVO();
		}

		return null;
	}

	public ResponseVO getResponseVO(final String taskType) {

		if (customRunnableMap.containsKey(taskType)) {
			return customRunnableMap.get(taskType).getResponseVO();
		}

		return null;
	}

	public void modifyCustomTask(final String orchestrationType, final RequestVO requestVO, final int delay) {

		final CustomRunnable customizeRunnable = customRunnableMap.get(orchestrationType);
		customizeRunnable.setRequestVO(requestVO);
		scheduleFuture.put(orchestrationType, taskScheduler.scheduleAtFixedRate(customizeRunnable, delay));
	}

}