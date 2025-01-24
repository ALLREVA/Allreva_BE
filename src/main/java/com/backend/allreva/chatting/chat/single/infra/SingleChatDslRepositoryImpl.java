package com.backend.allreva.chatting.chat.single.infra;

import com.backend.allreva.chatting.chat.integration.model.Participant;
import com.backend.allreva.chatting.chat.single.query.SingleChatDetailResponse;
import com.backend.allreva.common.model.Image;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.backend.allreva.chatting.member_chatting.member_chat.command.domain.QMemberChat.memberChat;

@RequiredArgsConstructor
@Repository
public class SingleChatDslRepositoryImpl implements SingleChatDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public SingleChatDetailResponse findSingleChatInfo(
            final Long memberId,
            final String memberNickname,
            final String memberProfileUrl,
            final Long singleChatId
    ) {
        return queryFactory
                .from(memberChat)
                .where(memberChat.chatId.eq(singleChatId)
                        .and(memberChat.memberId.eq(memberId))
                )
                .transform(GroupBy.groupBy(memberChat.chatId)
                        .as(singleChatInfoProjections(memberId, memberNickname, memberProfileUrl)))
                .get(singleChatId);
    }

    private ConstructorExpression<SingleChatDetailResponse> singleChatInfoProjections(
            final Long memberId,
            final String memberNickname,
            final String memberProfileUrl
    ) {
        return Projections.constructor(SingleChatDetailResponse.class,
                memberChat.otherMember.otherMemberThumbnail,
                memberChat.otherMember.otherMemberNickname,

                Projections.constructor(Participant.class,
                        Expressions.constant(memberId),
                        Expressions.constant(memberNickname),
                        Projections.constructor(Image.class,
                                Expressions.constant(memberProfileUrl)
                        )
                ),

                Projections.constructor(Participant.class,
                        memberChat.otherMember.otherMemberId,
                        memberChat.otherMember.otherMemberNickname,
                        memberChat.otherMember.otherMemberThumbnail
                )
        );
    }
}
