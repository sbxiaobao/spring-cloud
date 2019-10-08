package com.kafka.common.errors;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public class ApiException extends KafkaException {

	private static final long serialVersionUID = 1L;

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiException(String message) {
		super(message);
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

	public ApiException() {
		super();
	}

	/* avoid the expensive and useless stack trace for api exceptions */
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}