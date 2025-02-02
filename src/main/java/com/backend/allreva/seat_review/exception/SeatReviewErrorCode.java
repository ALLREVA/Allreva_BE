package com.backend.allreva.seat_review.exception;

import com.backend.allreva.common.exception.code.ErrorCode;
import com.backend.allreva.common.exception.code.ErrorCodeInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum SeatReviewErrorCode implements ErrorCodeInterface {
    SEAT_REVIEW_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "SEAT_REVIEW_SAVE_FALIED", "좌석리뷰 저장 실패"),
    SEAT_REVIEW_IMAGE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "SEAT_REVIEW_IMAGE_SAVE_FAILED", "좌석리뷰 이미지 저장 실패"),
    SEAT_REVIEW_NOT_FOUND(HttpStatus.NO_CONTENT.value(), "SEAT_REVEIEW_NOT_FOUND", "해당하는 좌석리뷰가 없습니다."),
    NOT_WRITER(HttpStatus.FORBIDDEN.value(), "NOT_WRITER", "리뷰 작성자가 아니여서 수정 불가"),
    DUPLICATE_LIKE(HttpStatus.FORBIDDEN.value(), "DUPLICATED_LIKE", "이미 좋아요를 누른 리뷰 입니다."),
    NOT_LIKE_MEMBER(HttpStatus.FORBIDDEN.value(), "NOT_LIKE_MEMBER", "좋아요를 누른 멤버가 아닙니다."),
    SEAT_REVIEW_IMAGE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "SEAT_REVIEW_IMAGE_DELETE_FAILED", "좌석 이미지 삭제 실패");
    private final Integer status;
    private final String errorCode;
    private final String message;


    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.of(status, errorCode, message);
    }

}
