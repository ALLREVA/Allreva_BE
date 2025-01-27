package com.backend.allreva.chatting.chat.group.infra;

import com.backend.allreva.chatting.chat.group.query.GroupChatDetailResponse;
import com.backend.allreva.chatting.chat.group.query.GroupChatSummaryResponse;
import com.backend.allreva.chatting.chat.integration.model.value.Participant;
import com.backend.allreva.common.exception.NotFoundException;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.member.command.domain.QMember;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.backend.allreva.chatting.chat.group.command.domain.QGroupChat.groupChat;
import static com.backend.allreva.chatting.chat.group.command.domain.QMemberGroupChat.memberGroupChat;

@RequiredArgsConstructor
@Repository
public class MemberGroupChatDslRepositoryImpl implements MemberGroupChatDslRepository {

    private final JPAQueryFactory queryFactory;

    private final QMember me = new QMember("me");
    private final QMember manager = new QMember("manager");
    private final QMember participant = new QMember("participant");

    @Override
    public GroupChatDetailResponse findGroupChatDetail(
            final Long memberId,
            final Long groupChatId
    ) {
        return queryFactory
                .from(memberGroupChat)
                .join(groupChat).on(groupChat.id.eq(memberGroupChat.groupChatId))
                .join(me).on(me.id.eq(memberId))
                .join(manager).on(manager.id.eq(groupChat.managerId))
                .join(participant).on(participant.id.eq(memberGroupChat.memberId))
                .where(memberGroupChat.groupChatId.eq(groupChatId))
                .transform(GroupBy.groupBy(memberGroupChat.groupChatId)
                        .as(groupChatInfoProjections()))
                .get(groupChatId);
    }

    private ConstructorExpression<GroupChatDetailResponse> groupChatInfoProjections() {
        return Projections.constructor(GroupChatDetailResponse.class,
                groupChat.thumbnail,
                groupChat.title,
                groupChat.description,
                Projections.constructor(Participant.class,
                        me.id,
                        me.memberInfo.nickname,
                        Projections.constructor(Image.class, me.memberInfo.profileImageUrl)
                ),
                Projections.constructor(Participant.class,
                        manager.id,
                        manager.memberInfo.nickname,
                        Projections.constructor(Image.class, manager.memberInfo.profileImageUrl)
                ),
                GroupBy.list(Projections.constructor(Participant.class,
                        participant.id,
                        participant.memberInfo.nickname,
                        participant.memberInfo.profileImageUrl

                ))
        );
    }

    @Override
    public GroupChatSummaryResponse findGroupChatSummary(UUID uuid) {
        GroupChatSummaryResponse response = queryFactory
                .select(
                        Projections.constructor(
                                GroupChatSummaryResponse.class,
                                groupChat.id,
                                groupChat.title,
                                groupChat.thumbnail,
                                groupChat.headcount
                        )
                ).from(groupChat)
                .where(groupChat.uuid.eq(uuid))
                .fetchFirst();

        if (response != null) {
            return response;
        }
        throw new NotFoundException();
    }
}
