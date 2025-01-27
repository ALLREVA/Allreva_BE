package com.backend.allreva.chatting.message.infra;

import com.backend.allreva.chatting.message.domain.SingleMessage;
import com.backend.allreva.chatting.message.query.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class SingleMessageCustomRepositoryImpl implements SingleMessageCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<MessageResponse> findMessageResponsesWithinRange(
            final Long singleChatId,
            final long fromNumber,
            final long toNumber
    ) {
        Criteria criteria = Criteria
                .where("singleChatId").is(singleChatId)
                .and("messageNumber").gte(fromNumber).lte(toNumber);

        return getMessageResponsesByCriteria(criteria);
    }

    private List<MessageResponse> getMessageResponsesByCriteria(
            final Criteria criteria
    ) {
        Query query = new Query(criteria);
        query.fields()
                .exclude("_id")
                .exclude("singleChatId");

        List<SingleMessage> messages = mongoTemplate
                .find(query, SingleMessage.class);

        return messages.stream()
                .map(MessageResponse::from)
                .toList();
    }
}
