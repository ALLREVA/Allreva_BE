package com.backend.allreva.chatting.member_chatting.member_chat.command;

import com.backend.allreva.chatting.chat.single_chat.domain.SingleChat;
import com.backend.allreva.chatting.chat.single_chat.domain.SingleChatRepository;
import com.backend.allreva.chatting.exception.InvalidWriterException;
import com.backend.allreva.chatting.member_chatting.member_chat.command.domain.MemberChatRepository;
import com.backend.allreva.chatting.member_chatting.member_chat.command.domain.MemberChat;
import com.backend.allreva.chatting.member_chatting.member_chat.command.domain.OtherMember;
import com.backend.allreva.member.query.application.MemberDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberChatCommandService {

    private final SingleChatRepository singleChatRepository;
    private final MemberChatRepository memberChatrepository;
    private final MemberDetailRepository memberDetailRepository;

    @Transactional
    public Long startSingleChatting(
            final Long memberId,
            final Long otherMemberId
    ) {
        SingleChat generatedSingleChat = singleChatRepository.save(new SingleChat());

        OtherMember otherMember = memberDetailRepository.findMemberSummary(otherMemberId);
        MemberChat memberChat = new MemberChat(
                memberId,
                generatedSingleChat.getId(),
                otherMember
        );
        memberChatrepository.save(memberChat);

        startOtherSingleChatting(generatedSingleChat.getId(), memberId, otherMemberId);

        return generatedSingleChat.getId();
    }

    private void startOtherSingleChatting(
            final Long chatId,
            final Long memberId,
            final Long otherMemberId
    ) {
        OtherMember myInformation = memberDetailRepository.findMemberSummary(memberId);
        MemberChat memberChat = new MemberChat(
                otherMemberId,
                chatId,
                myInformation
        );
        memberChatrepository.save(memberChat);
    }

    @Transactional
    public void leaveSingleChat(
            final Long memberId,
            final Long singleChatId
    ) {
        // 회원과 채팅방 id 가 전부 동일해야 삭제됨
        boolean exists = memberChatrepository.existsById(singleChatId);
        if (exists) {
            memberChatrepository
                    .deleteMemberChatByChatIdAndMemberId(singleChatId, memberId);
        }
        throw new InvalidWriterException();
    }
}
