package com.backend.allreva.chatting.message.infra.counter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageCounterService {

    private final MongoOperations mongoOperations;

    public long getSingleMessageNumber(final Long chatId) {
        Query query = new Query(Criteria.where("_id").is(chatId));
        Update update = new Update().inc("number", 1);
        FindAndModifyOptions options = new FindAndModifyOptions()
                .returnNew(true)
                .upsert(true);

        SingleMessageCounter singleMessageCounter = mongoOperations
                .findAndModify(query, update, options, SingleMessageCounter.class);
        return singleMessageCounter.getNumber();
    }

    public long getGroupMessageNumber(final Long groupChatId) {
        Query query = new Query(Criteria.where("_id").is(groupChatId));
        Update update = new Update().inc("number", 1);
        FindAndModifyOptions options = new FindAndModifyOptions()
                .returnNew(true)
                .upsert(true);

        GroupMessageCounter groupMessageCounter = mongoOperations
                .findAndModify(query, update, options, GroupMessageCounter.class);
        return groupMessageCounter.getNumber();
    }
}
