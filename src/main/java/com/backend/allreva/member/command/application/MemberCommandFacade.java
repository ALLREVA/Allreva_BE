package com.backend.allreva.member.command.application;

import com.backend.allreva.common.application.S3ImageService;
import com.backend.allreva.common.event.Events;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.member.command.application.request.MemberRegisterRequest;
import com.backend.allreva.member.command.application.request.RefundAccountRequest;
import com.backend.allreva.member.command.domain.AddedMemberEvent;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class MemberCommandFacade {

    private final MemberInfoCommandService memberInfoCommandService;
    private final MemberArtistCommandService memberArtistCommandService;
    private final S3ImageService s3ImageService;

    @Transactional
    public void registerMember(
            final MemberRegisterRequest memberRegisterRequest,
            final MultipartFile image
    ) {
        Image uploadedImage = s3ImageService.upload(image);
        Member registeredMember = memberInfoCommandService.registerMember(memberRegisterRequest, uploadedImage);

        AddedMemberEvent addedEvent = new AddedMemberEvent(registeredMember.getId());
        Events.raise(addedEvent);

        memberArtistCommandService.updateMemberArtist(memberRegisterRequest.memberArtistRequests(), registeredMember);
    }

    @Transactional
    public void updateMemberInfo(
            final MemberRegisterRequest memberRegisterRequest,
            final Member member,
            final MultipartFile image
    ) {
        Image uploadedImage = s3ImageService.upload(image);
        memberInfoCommandService.updateMemberInfo(memberRegisterRequest, member, uploadedImage);
        memberArtistCommandService.updateMemberArtist(memberRegisterRequest.memberArtistRequests(), member);
    }

    @Transactional
    public void registerRefundAccount(
            final RefundAccountRequest refundAccountRequest,
            final Member member
    ) {
        memberInfoCommandService.registerRefundAccount(refundAccountRequest, member);
    }

    @Transactional
    public void deleteRefundAccount(
            final Member member
    ) {
        memberInfoCommandService.deleteRefundAccount(member);
    }
}
