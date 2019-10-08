package com.kafka.common.errors;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public class RetriableException extends ApiException {

	private static final long serialVersionUID = 1L;

	public RetriableException(String message, Throwable cause) {
		super(message, cause);
	}

	public RetriableException(String message) {
		super(message);
	}

	public RetriableException(Throwable cause) {
		super(cause);
	}

	public RetriableException() {
	}
}