package com.siddarthmishra.springboot.dto;

import java.io.Serializable;

public record ProducerMessageDTO(String key, String message) implements Serializable {
	private static final long serialVersionUID = 1844910486310002039L;
}
