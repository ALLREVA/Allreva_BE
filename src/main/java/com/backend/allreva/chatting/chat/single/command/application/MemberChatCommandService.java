package com.backend.allreva.chatting.chat.single.command.application;

import com.backend.allreva.chatting.chat.single.command.domain.*;
import com.backend.allreva.chatting.exception.InvalidWriterException;
import com.backend.allreva.chatting.chat.single.command.domain.MemberSingleChat;
import com.backend.allreva.member.query.application.MemberDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberChatCommandService {

    private final SingleChatRepository singleChatRepository;
    private final MemberSingleChatRepository memberSingleChatrepository;
    private final MemberDetailRepository memberDetailRepository;

    @Transactional
    public Long startSingleChatting(
            final Long memberId,
            final Long otherMemberId
    ) {
        SingleChat generatedSingleChat = singleChatRepository
                .save(new SingleChat());

        OtherMember otherMember = memberDetailRepository.findMemberSummary(otherMemberId);

        MemberSingleChat memberSingleChat = new MemberSingleChat(
                memberId,
                generatedSingleChat.getId(),
                otherMember
        );
        memberSingleChatrepository.save(memberSingleChat);

        return generatedSingleChat.getId();
    }

    @Transactional
    public void startOtherSingleChatting(
            final Long chatId,
            final Long memberId,
            final Long otherMemberId
    ) {
        OtherMember myInformation = memberDetailRepository.findMemberSummary(memberId);
        MemberSingleChat memberSingleChat = new MemberSingleChat(
                otherMemberId,
                chatId,
                myInformation
        );
        memberSingleChatrepository.save(memberSingleChat);
    }

}
