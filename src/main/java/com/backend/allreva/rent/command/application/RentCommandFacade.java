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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class RentCommandFacade {

    private final RentCommandService rentCommandService;
    private final S3ImageService s3ImageService;

    private final GroupChatCommandService groupChatCommandService;

    public Long registerRent(
            final RentRegisterRequest rentRegisterRequest,
            final MultipartFile image,
            final Long memberId
    ) {
        Image uploadedImage = s3ImageService.upload(image);
        Long rentId = rentCommandService.registerRent(rentRegisterRequest, uploadedImage, memberId);

        groupChatCommandService.add(
                new AddGroupChatRequest(
                        rentRegisterRequest.title(),
                        rentRegisterRequest.information(),
                        rentRegisterRequest.maxPassenger()
                ),
                uploadedImage,
                memberId
        );

        return rentId;
    }

    public void updateRent(
            final RentUpdateRequest rentUpdateRequest,
            final MultipartFile image,
            final Long memberId
    ) {
        Image uploadedImage = s3ImageService.upload(image);
        rentCommandService.updateRent(rentUpdateRequest, uploadedImage, memberId);
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
