package com.backend.allreva.rent.command.application;

import com.backend.allreva.chatting.chat.group.command.application.GroupChatCommandService;
import com.backend.allreva.chatting.chat.group.command.application.request.AddGroupChatRequest;
import com.backend.allreva.common.event.Events;
import com.backend.allreva.common.event.NotificationMessage;
import com.backend.allreva.rent.command.application.request.RentIdRequest;
import com.backend.allreva.rent.command.application.request.RentRegisterRequest;
import com.backend.allreva.rent.command.application.request.RentUpdateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RentCommandFacade {

    private final RentCommandService rentCommandService;
    private final GroupChatCommandService groupChatCommandService;

    public Long registerRent(
            final RentRegisterRequest rentRegisterRequest,
            final Long memberId
    ) {
        // register rent
        Long rentId = rentCommandService.registerRent(rentRegisterRequest, memberId);

        // create group chat
        groupChatCommandService.add(
                new AddGroupChatRequest(
                        rentRegisterRequest.title(),
                        rentRegisterRequest.maxPassenger()
                ),
                rentRegisterRequest.image(),
                memberId
        );

        // push notification
        List<Long> recipientIds = List.of(memberId);
        Events.raise(
                NotificationMessage.NEW_RENT_REGISTERED
                        .toEvent(recipientIds, rentRegisterRequest.title())
        );

        return rentId;
    }

    public void updateRent(
            final RentUpdateRequest rentUpdateRequest,
            final Long memberId
    ) {
        rentCommandService.updateRent(rentUpdateRequest, memberId);
    }

    public void closeRent(
            final RentIdRequest rentIdRequest,
            final Long memberId
    ) {
        rentCommandService.closeRent(rentIdRequest, memberId);
    }

    public void deleteRent(
            final RentIdRequest rentIdRequest,
            final Long memberId
    ) {
        rentCommandService.deleteRent(rentIdRequest, memberId);
    }
}
