package com.yl.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *异常信息
 * */
public class ErrorException extends Exception{

	private static final long serialVersionUID = 5010675577024932123L;
	private Logger logger = LoggerFactory.getLogger(ErrorException.class);

	public ErrorException(String message) {
		super(message);
		logger.error(" error msg:{}", message);
	}

	public ErrorException() {
		super();
	}

	public ErrorException(Throwable throwable) {
		super(throwable);
		log(null, throwable);
	}

	public ErrorException(String msg, Throwable throwable) {
		super(throwable);
		log(msg, throwable);
	}

	private void log(String msg, Throwable throwable) {
		if (throwable != null) {
			StackTraceElement element = throwable.getStackTrace()[0];
			String exceptionInfo = null;
			if (element == null)
				return;
			exceptionInfo = "class=" + element.getClassName() + ",method=" + element.getMethodName() + ",file="
					+ element.getFileName() + ",line=" + element.getLineNumber();
			logger.error("error msg:{},exception msg:{}", msg, exceptionInfo);
		}
		printStackTrace();
	}
}
