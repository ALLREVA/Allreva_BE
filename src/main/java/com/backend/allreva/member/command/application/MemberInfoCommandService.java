package com.backend.allreva.member.command.application;

import com.backend.allreva.common.event.Events;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.member.command.application.request.MemberRegisterRequest;
import com.backend.allreva.member.command.application.request.RefundAccountRequest;
import com.backend.allreva.member.command.domain.AddedMemberEvent;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.command.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberInfoCommandService {

    private final MemberRepository memberRepository;

    /**
     * 회원 정보 등록
     */
    public Member registerMember(
            final MemberRegisterRequest memberRegisterRequest,
            final Image image
    ) {
        Member member = memberRegisterRequest.toEntity(image);
        return memberRepository.save(member);
    }

    /**
     * 회원 정보 수정
     */
    public Member updateMemberInfo(
            final MemberRegisterRequest memberRegisterRequest,
            final Member member,
            final Image image
    ) {
        member.setMemberInfo(
                memberRegisterRequest.nickname(),
                memberRegisterRequest.introduce(),
                image.getUrl()
        );
        return memberRepository.save(member);
    }

    /**
     * 환불 계좌 등록
     */
    public Member registerRefundAccount(
            final RefundAccountRequest refundAccountRequest,
            final Member member
    ) {
        member.setRefundAccount(
                refundAccountRequest.bank(),
                refundAccountRequest.number()
        );
        return memberRepository.save(member);
    }

    /**
     * 환불 계좌 soft delete
     */
    public Member deleteRefundAccount(
            final Member member
    ) {
        member.setDefaultRefundAccount();
        return memberRepository.save(member);
    }
}
