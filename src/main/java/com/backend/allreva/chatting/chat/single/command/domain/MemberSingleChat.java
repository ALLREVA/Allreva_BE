package com.backend.allreva.chatting.chat.single.command.domain;

import com.backend.allreva.chatting.chat.single.command.domain.value.OtherMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "other_member_id"})
})
@Entity
public class MemberSingleChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long singleChatId;
    private Long memberId;

    @Embedded
    private OtherMember otherMember;


    public MemberSingleChat(
            final Long memberId,
            final Long singleChatId,
            final OtherMember otherMember
    ) {
        this.memberId = memberId;
        this.singleChatId = singleChatId;

        this.otherMember = otherMember;
    }

    public static Set<MemberSingleChat> startFirstChat(
            final Long singleChatId,
            final OtherMember member,
            final OtherMember otherMember
    ) {
        MemberSingleChat chat = new MemberSingleChat(
                member.getId(),
                singleChatId,
                otherMember
        );
        MemberSingleChat otherChat = new MemberSingleChat(
                otherMember.getId(),
                singleChatId,
                member
        );
        return Set.of(chat, otherChat);
    }
}
