package com.backend.allreva.member.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.application.MemberCommandFacade;
import com.backend.allreva.member.command.application.request.MemberRegisterRequest;
import com.backend.allreva.member.command.application.request.RefundAccountRequest;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.query.application.MemberQueryService;
import com.backend.allreva.member.query.application.response.MemberDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
public class MemberController implements MemberControllerSwagger {

    private final MemberCommandFacade memberCommandFacade;
    private final MemberQueryService memberQueryService;

    @GetMapping
    public Response<MemberDetailResponse> getMemberDetail(
            @AuthMember final Member member
    ) {
        return Response.onSuccess(memberQueryService.getById(member.getId()));
    }

    @GetMapping("/check-nickname")
    public Response<Boolean> isDuplicatedNickname(
            @RequestParam final String nickname
    ) {
        return Response.onSuccess(memberQueryService.isDuplicatedNickname(nickname).isDuplicated());
    }

    @PostMapping("/register")
    public Response<Void> registerMember(
            @RequestBody final MemberRegisterRequest memberRegisterRequest
    ) {
        memberCommandFacade.registerMember(memberRegisterRequest);
        return Response.onSuccess();
    }

    @PatchMapping("/info")
    public Response<Void> updateMemberInfo(
            @AuthMember final Member member,
            @RequestBody final MemberRegisterRequest memberRegisterRequest
    ) {
        memberCommandFacade.updateMemberInfo(memberRegisterRequest, member);
        return Response.onSuccess();
    }

    @PostMapping("/refund-account")
    public Response<Void> registerRefundAccount(
            @AuthMember final Member member,
            @RequestBody final RefundAccountRequest refundAccountRequest
    ) {
        memberCommandFacade.registerRefundAccount(refundAccountRequest, member);
        return Response.onSuccess();
    }

    @DeleteMapping("/refund-account")
    public Response<Void> deleteRefundAccount(
            @AuthMember final Member member
    ) {
        memberCommandFacade.deleteRefundAccount(member);
        return Response.onSuccess();
    }
}
