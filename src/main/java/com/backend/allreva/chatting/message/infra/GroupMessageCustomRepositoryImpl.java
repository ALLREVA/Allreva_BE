package com.backend.allreva.chatting.message.infra;

import com.backend.allreva.chatting.message.domain.GroupMessage;
import com.backend.allreva.chatting.message.query.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class GroupMessageCustomRepositoryImpl implements GroupMessageCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<MessageResponse> findMessageResponsesWithinRange(
            final Long groupChatId,
            final long fromNumber,
            final long toNumber
    ) {
        Criteria criteria = Criteria
                .where("id").is(groupChatId)
                .and("messageNumber").gte(fromNumber).lte(toNumber);

        return getMessageResponsesByCriteria(criteria);
    }

    private List<MessageResponse> getMessageResponsesByCriteria(
            final Criteria criteria
    ) {
        Query query = new Query(criteria);
        query.fields()
                .exclude("_id")
                .exclude("id");

        List<GroupMessage> messages = mongoTemplate
                .find(query, GroupMessage.class);

        return messages.stream()
                .map(MessageResponse::from)
                .toList();
    }
}
