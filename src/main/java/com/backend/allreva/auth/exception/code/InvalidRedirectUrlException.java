package com.backend.allreva.auth.exception.code;

import com.backend.allreva.common.exception.CustomException;

public class InvalidRedirectUrlException extends CustomException {

    public InvalidRedirectUrlException() {
        super(OAuth2ErrorCode.INVALID_REDIRECT_URI);
    }
}
