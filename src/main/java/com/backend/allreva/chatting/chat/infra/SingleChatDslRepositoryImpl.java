package com.backend.allreva.chatting.chat.infra;

import com.backend.allreva.chatting.Participant;
import com.backend.allreva.chatting.chat.query.RoomInfoDetail;
import com.backend.allreva.common.exception.NotFoundException;
import com.backend.allreva.common.model.Image;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.backend.allreva.chatting.member_chatting.member_chat.command.domain.QMemberChat.memberChat;
import static com.backend.allreva.member.command.domain.QMember.member;

@RequiredArgsConstructor
@Repository
public class SingleChatDslRepositoryImpl implements SingleChatDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public RoomInfoDetail findSingleChatInfo(
            final Long memberId,
            final Long singleChatId
    ) {
        RoomInfoDetail roomInfoDetail = queryFactory
                .select(
                        Projections.constructor(RoomInfoDetail.class,
                                memberChat.otherMember.otherMemberThumbnail,
                                memberChat.otherMember.otherMemberNickname,
                                Projections.list(
                                        Projections.constructor(Participant.class,
                                                member.id,
                                                member.memberInfo.nickname,
                                                Projections.constructor(Image.class, member.memberInfo.profileImageUrl)
                                        )
                                )
                        )
                )
                .from(memberChat)
                .join(member).on(member.id.eq(memberChat.memberId)
                        .or(memberChat.otherMember.otherMemberId.eq(memberChat.otherMember.otherMemberId)))
                .where(memberChat.chatId.eq(singleChatId)
                        .and(memberChat.memberId.eq(memberId)))
                .fetchFirst();

        if (roomInfoDetail == null) {
            throw new NotFoundException();
        }
        return roomInfoDetail;
    }
}
