package com.backend.allreva.notification.infra;

import static com.backend.allreva.notification.command.domain.QNotification.notification;

import com.backend.allreva.notification.command.domain.Notification;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationDslRepository {

    private final JPAQueryFactory queryFactory;

    // 알림 최신 순 페이징 조회
    public List<Notification> findNotificationsByRecipientId(
            final Long recipientId,
            final Long lastId,
            final int pageSize
    ) {
        return queryFactory
                .selectFrom(notification)
                .where(
                        notification.recipientId.eq(recipientId),
                        pagingCondition(lastId)
                )
                .orderBy(notification.createdAt.desc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression pagingCondition(Long lastId) {
        return lastId != null ? notification.id.lt(lastId) : null;
    }
}
