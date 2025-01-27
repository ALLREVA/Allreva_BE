package com.backend.allreva.chatting.exception;

import com.backend.allreva.common.exception.CustomException;

public class CannotDeleteGroupChatException extends CustomException {

  public CannotDeleteGroupChatException() {
    super(ChattingErrorCode.DO_NOT_MEET_CONDITIONS_TO_DELETE);
  }
}
