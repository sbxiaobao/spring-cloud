package com.kafka.common.errors;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public class KafkaException extends RuntimeException {

	private final static long serialVersionUID = 1L;

	public KafkaException(String message, Throwable cause) {
		super(message, cause);
	}

	public KafkaException(String message) {
		super(message);
	}

	public KafkaException(Throwable cause) {
		super(cause);
	}

	public KafkaException() {
		super();
	}
}
