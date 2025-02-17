package com.backend.allreva.chatting.chat.group.command.application;

import com.backend.allreva.chatting.chat.group.command.application.request.AddGroupChatRequest;
import com.backend.allreva.chatting.chat.group.command.application.request.UpdateGroupChatRequest;
import com.backend.allreva.chatting.chat.group.command.domain.GroupChat;
import com.backend.allreva.chatting.chat.group.command.domain.GroupChatRepository;
import com.backend.allreva.chatting.chat.group.command.domain.event.AddedGroupChatEvent;
import com.backend.allreva.chatting.exception.GroupChatNotFoundException;
import com.backend.allreva.common.application.S3ImageService;
import com.backend.allreva.common.event.Events;
import com.backend.allreva.common.exception.NotFoundException;
import com.backend.allreva.common.model.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GroupChatCommandService {

    private final GroupChatRepository groupChatRepository;
    private final S3ImageService s3ImageService;

    @Transactional
    public Long add(
            final AddGroupChatRequest request,
            final Image uploadedImage,
            final Long memberId
    ) {
        GroupChat groupChat = GroupChat.builder()
                .title(request.title())
                .managerId(memberId)
                .thumbnail(uploadedImage)
                .capacity(request.capacity())
                .build();

        groupChatRepository.save(groupChat);
        AddedGroupChatEvent addedEvent = new AddedGroupChatEvent(
                groupChat.getId(),
                memberId
        );
        Events.raise(addedEvent);
        return groupChat.getId();
    }

    @Transactional
    public void update(
            final UpdateGroupChatRequest request,
            final Image image,
            final Long memberId
    ) {
        GroupChat groupChat = groupChatRepository.findById(request.groupChatId())
                .orElseThrow(GroupChatNotFoundException::new);

        groupChat.validateManager(memberId);
        groupChat.updateInfo(
                memberId,
                request.title(),
                request.description(),
                image
        );
    }

    @Transactional
    public Long join(
            final String uuid,
            final Long memberId
    ) {
        GroupChat groupChat = groupChatRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(NotFoundException::new);
        groupChat.addHeadcount(memberId);

        return groupChat.getId();
    }

    @Transactional
    public void leave(
            final Long groupChatId,
            final Long memberId
    ) {
        GroupChat groupChat = groupChatRepository.findById(groupChatId)
                .orElseThrow(NotFoundException::new);
        groupChat.subtractHeadcount(memberId);
    }

    public void delete(final Long groupChatId, final Long memberId) {
        GroupChat groupChat = groupChatRepository.findById(groupChatId)
                .orElseThrow(GroupChatNotFoundException::new);

        s3ImageService.delete(groupChat.getThumbnail().getUrl());

        groupChat.validateForDelete(memberId);
        groupChatRepository.deleteById(groupChatId);
    }


}
