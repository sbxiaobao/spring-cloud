package com.kafka.common.errors;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public class TimeoutException extends RetriableException {

	private static final long serialVersionUID = 1L;

	public TimeoutException() {
		super();
	}

	public TimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public TimeoutException(String message) {
		super(message);
	}

	public TimeoutException(Throwable cause) {
		super(cause);
	}
}