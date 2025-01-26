package com.backend.allreva.chatting.chat.single.infra;

import com.backend.allreva.chatting.chat.integration.model.value.Participant;
import com.backend.allreva.chatting.chat.single.command.domain.OtherMember;
import com.backend.allreva.chatting.chat.single.query.SingleChatDetailResponse;
import com.backend.allreva.common.exception.NotFoundException;
import com.backend.allreva.common.model.Image;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.backend.allreva.chatting.chat.single.command.domain.QMemberSingleChat.memberSingleChat;


@RequiredArgsConstructor
@Repository
public class SingleChatDslRepositoryImpl implements SingleChatDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public OtherMember findOtherMemberInfo(
            final Long memberId,
            final Long singleChatId
    ) {
        OtherMember otherMember = queryFactory
                .select(
                        Projections.constructor(OtherMember.class, memberSingleChat.otherMember)
                )
                .from(memberSingleChat)
                .where(memberSingleChat.singleChatId.eq(singleChatId)
                        .and(memberSingleChat.memberId.eq(memberId))
                )
                .fetchFirst();
        if (otherMember != null) {
            return otherMember;
        }
        throw new NotFoundException();
    }

    @Override
    public SingleChatDetailResponse findSingleChatInfo(
            final Long memberId,
            final String memberNickname,
            final String memberProfileUrl,
            final Long singleChatId
    ) {
        return queryFactory
                .from(memberSingleChat)
                .where(memberSingleChat.singleChatId.eq(singleChatId)
                        .and(memberSingleChat.memberId.eq(memberId))
                )
                .transform(GroupBy.groupBy(memberSingleChat.singleChatId)
                        .as(singleChatInfoProjections(memberId, memberNickname, memberProfileUrl)))
                .get(singleChatId);
    }

    private ConstructorExpression<SingleChatDetailResponse> singleChatInfoProjections(
            final Long memberId,
            final String memberNickname,
            final String memberProfileUrl
    ) {
        return Projections.constructor(SingleChatDetailResponse.class,
                memberSingleChat.otherMember.thumbnail,
                memberSingleChat.otherMember.nickname,

                Projections.constructor(Participant.class,
                        Expressions.constant(memberId),
                        Expressions.constant(memberNickname),
                        Projections.constructor(Image.class,
                                Expressions.constant(memberProfileUrl)
                        )
                ),

                Projections.constructor(Participant.class,
                        memberSingleChat.otherMember.id,
                        memberSingleChat.otherMember.nickname,
                        memberSingleChat.otherMember.thumbnail
                )
        );
    }
}
