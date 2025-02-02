package com.backend.allreva.chatting.chat.integration.infra;

import com.backend.allreva.chatting.chat.integration.model.ChatParticipantDoc;
import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ChatParticipantCustomRepositoryImpl implements ChatParticipantCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Long findLastReadMessageNumber(
            final Long memberId,
            final Long chatId,
            final ChatType chatType
    ) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(memberId));

        Criteria ByChatIdAndType = Criteria.where("chatId").is(chatId)
                .and("chatType").is(chatType);

        query.addCriteria(
                Criteria.where("chatSummaries")
                        .elemMatch(ByChatIdAndType)
        );
        query.fields().include("chatSummaries.$.lastReadMessageNumber");
        ChatParticipantDoc result = mongoTemplate
                .findOne(query, ChatParticipantDoc.class);

        if (result != null && result.getChatSummaries() != null) {
            return result.getChatSummaries()
                    .first()
                    .getLastReadMessageNumber();
        }
        return 0L;
    }
}
