package com.backend.allreva.chatting.chat.group_chat.command.application;

import com.backend.allreva.chatting.exception.GroupChatNotFoundException;
import com.backend.allreva.chatting.chat.group_chat.command.application.request.AddGroupChatRequest;
import com.backend.allreva.chatting.chat.group_chat.command.application.request.UpdateGroupChatRequest;
import com.backend.allreva.chatting.chat.group_chat.command.domain.AddedGroupChatEvent;
import com.backend.allreva.chatting.chat.group_chat.command.domain.DeletedGroupChatEvent;
import com.backend.allreva.chatting.chat.group_chat.command.domain.GroupChat;
import com.backend.allreva.chatting.chat.group_chat.command.domain.GroupChatRepository;
import com.backend.allreva.common.application.S3ImageService;
import com.backend.allreva.common.event.Events;
import com.backend.allreva.common.model.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional
@Service
public class GroupChatCommandService {

    private final GroupChatRepository groupChatRepository;
    private final S3ImageService s3ImageService;

    public Long add(
            final AddGroupChatRequest request,
            final MultipartFile imageFile,
            final Long memberId
    ) {
        Image uploadedImage = s3ImageService.upload(imageFile);

        GroupChat groupChat = GroupChat.builder()
                .title(request.title())
                .description(request.description())
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

    public void update(
            final UpdateGroupChatRequest request,
            final MultipartFile imageFile,
            final Long memberId
    ) {
        Image uploadedImage = s3ImageService.upload(imageFile);
        GroupChat groupChat = groupChatRepository.findById(request.groupChatId())
                .orElseThrow(GroupChatNotFoundException::new);

        groupChat.validateManager(memberId);
        groupChat.updateInfo(
                memberId,
                request.title(),
                request.description(),
                uploadedImage
        );
    }

    public void delete(final Long groupChatId, final Long memberId) {
        GroupChat groupChat = groupChatRepository.findById(groupChatId)
                .orElseThrow(GroupChatNotFoundException::new);

        groupChat.validateForDelete(memberId);
        groupChatRepository.deleteById(groupChatId);
        Events.raise(new DeletedGroupChatEvent(groupChatId, memberId));
    }


}
