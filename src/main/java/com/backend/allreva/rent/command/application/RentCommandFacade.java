package com.backend.allreva.rent.command.application;

import com.backend.allreva.chatting.chat.group.command.application.GroupChatCommandService;
import com.backend.allreva.chatting.chat.group.command.application.request.AddGroupChatRequest;
import com.backend.allreva.common.application.S3ImageService;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.rent.command.application.request.RentIdRequest;
import com.backend.allreva.rent.command.application.request.RentRegisterRequest;
import com.backend.allreva.rent.command.application.request.RentUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RentCommandFacade {

    private final RentCommandService rentCommandService;
    private final S3ImageService s3ImageService;

    private final GroupChatCommandService groupChatCommandService;

    public Long registerRent(
            final RentRegisterRequest rentRegisterRequest,
            final Image image,
            final Long memberId
    ) {
        Long rentId = rentCommandService.registerRent(rentRegisterRequest, image, memberId);

        groupChatCommandService.add(
                new AddGroupChatRequest(
                        rentRegisterRequest.title(),
                        rentRegisterRequest.maxPassenger()
                ),
                image,
                memberId
        );

        return rentId;
    }

    public void updateRent(
            final RentUpdateRequest rentUpdateRequest,
            final Image image,
            final Long memberId
    ) {
        rentCommandService.updateRent(rentUpdateRequest, image, memberId);
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
