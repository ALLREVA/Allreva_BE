package com.backend.allreva.seat_review.command.application.dto;

public record FileData(
        byte[] bytes,
        String filename
) {
}
