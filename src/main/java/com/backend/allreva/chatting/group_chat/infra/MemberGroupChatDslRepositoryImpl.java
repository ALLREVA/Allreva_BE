package com.backend.allreva.chatting.group_chat.infra;

import com.backend.allreva.chatting.group_chat.query.GroupChatInfoResponse;
import com.backend.allreva.chatting.member_chatting.Participant;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.backend.allreva.chatting.group_chat.command.domain.QGroupChat.groupChat;
import static com.backend.allreva.chatting.member_chatting.member_groupchat.command.domain.QMemberGroupChat.memberGroupChat;
import static com.backend.allreva.member.command.domain.QMember.member;

@RequiredArgsConstructor
@Repository
public class MemberGroupChatDslRepositoryImpl implements MemberGroupChatDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public GroupChatInfoResponse findGroupChatInfo(
            final Long memberId,
            final Long groupChatId
    ) {
        return queryFactory
                .from(memberGroupChat)
                .join(groupChat).on(groupChat.id.eq(memberGroupChat.groupChatId))
                .join(member).on(member.id.eq(memberGroupChat.memberId))
                .where(
                        memberGroupChat.memberId.eq(memberId)
                                .and(memberGroupChat.groupChatId.eq(groupChatId))
                )
                .transform(GroupBy.groupBy(memberGroupChat.groupChatId)
                        .as(groupChatInfoProjections()))
                .get(groupChatId);
    }

    private ConstructorExpression<GroupChatInfoResponse> groupChatInfoProjections() {
        return Projections.constructor(GroupChatInfoResponse.class,
                groupChat.thumbnail,
                groupChat.title,
                groupChat.description,
                GroupBy.list(Projections.constructor(Participant.class,
                        member.id,
                        member.memberInfo.nickname,
                        member.memberInfo.profileImageUrl

                ))
        );
    }
}
