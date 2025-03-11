package com.backend.allreva.auth.application;

import com.backend.allreva.auth.application.dto.UserInfo;

public interface OAuth2LoginService {

    UserInfo getUserInfo(String authorizationCode, String domainName);
}
