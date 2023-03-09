package com.furniture.backend.payload;

import lombok.Data;

import java.time.Instant;

@Data
public abstract class DateAuditPayload {

	private Instant createdAt;

	private Instant updatedAt;
}
