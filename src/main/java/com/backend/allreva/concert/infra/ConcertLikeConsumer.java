package com.backend.allreva.concert.infra;

import com.backend.allreva.concert.exception.exception.EventConsumingException;
import com.backend.allreva.concert.exception.exception.SearchResultNotFoundException;
import com.backend.allreva.concert.command.domain.ConcertLikeEvent;
import com.backend.allreva.concert.query.application.domain.ConcertSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConcertLikeConsumer {
    private final ConcertSearchRepository concertSearchRepository;

    @Transactional
    @KafkaListener(
            topics = "concertLike-event",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleConcertListEvent(
            @Payload ConcertLikeEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ){
        try {
            log.info("이벤트 수신 - partition: {}, offset: {}, event: {}",
                    partition, offset, event);

            concertSearchRepository
                    .findByConcertCode(event.getEventId())
                    .ifPresentOrElse(
                            concertDocument -> {
                                log.info("event.getIncreaseCount : {}", event.getIncreaseCount());
                                concertDocument.updateViewCount((long) event.getIncreaseCount());
                                concertSearchRepository.save(concertDocument);
                            },
                            () -> {
                                throw new SearchResultNotFoundException();
                            }
                    );

            log.info("Elasticsearch 업데이트 완료 - eventId: {}", event.getEventId());
        } catch (Exception e) {
            throw new EventConsumingException();
        }
    }
}
