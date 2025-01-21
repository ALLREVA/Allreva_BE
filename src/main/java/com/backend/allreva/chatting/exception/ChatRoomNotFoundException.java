package com.backend.allreva.chatting.exception;

import com.backend.allreva.common.exception.CustomException;
import com.backend.allreva.common.exception.code.ErrorCodeInterface;

public class ChatRoomNotFoundException extends CustomException {

  public ChatRoomNotFoundException() {
    super(ChattingErrorCode.CHAT_ROOM_NOT_FOUND);
  }
}
