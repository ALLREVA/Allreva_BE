package com.backend.allreva.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.allreva.auth.application.AuthMember;
import com.backend.allreva.member.command.application.MemberCommandService;
import com.backend.allreva.member.command.application.dto.MemberInfoUpdateRequest;
import com.backend.allreva.member.command.domain.Member;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
public class OAuth2RegisterController {

    private final MemberCommandService memberCommandService;

    @PatchMapping("/register")
    public ResponseEntity<Void> register(
            @AuthMember Member member,
            @RequestBody MemberInfoUpdateRequest memberInfoUpdateRequest) {
        memberCommandService.registerMember(memberInfoUpdateRequest, member);

        return ResponseEntity.noContent().build();
    }
}
