package com.backend.allreva.chatting.chat.query.model.chat_room;

import com.backend.allreva.chatting.chat.query.model.document.MemberChatRoomDoc;
import com.backend.allreva.chatting.chat.query.model.document.MemberChatRoomRepository;
import com.backend.allreva.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PreviewMessageUpdater {

    private final MemberChatRoomRepository memberChatRoomRepository;

    @Transactional
    public void update(
            final Long memberId,
            final Long roomId,
            final RoomType roomType,
            final long previewMessageNumber,
            final String previewText,
            final LocalDateTime sentAt
    ) {
        MemberChatRoomDoc document = memberChatRoomRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        document.updatePreviewMessage(
                roomId,
                roomType,
                previewMessageNumber,
                previewText,
                sentAt
        );
        memberChatRoomRepository.save(document);
    }
}
