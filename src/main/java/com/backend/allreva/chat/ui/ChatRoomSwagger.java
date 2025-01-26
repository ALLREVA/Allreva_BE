package com.backend.allreva.chat.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "채팅방 HTTP API")
public interface ChatRoomSwagger {

    @Operation(
            summary = "기존 채팅방 입장",
            description = "참여중인 채팅방에 입장합니다. 이전 채팅 기록을 불러오며 읽지 않은 메시지를 표시합니다"
    )
    Response<List<ExampleResponse>> enterChatRoom(
            @PathVariable("roomId") final String roomId,
            @RequestParam("lastChatIdForPaging") final String lastChatIdForPaging,
            @AuthMember final Member member
    );


    @Operation(
            summary = "그룹 채팅 만들기",
            description = "채팅방 이름(필수) 및 이미지(선택)를 넣어야함, multipart/form-data 설정 필요"
    )
    Response<String> addGroupChatRoom(
            @RequestPart(value = "request") final AddGroupChatRoomRequest request,
            @RequestPart(value = "images", required = false) final MultipartFile imageFile,
            @AuthMember final Member member
    );

    @Operation(
            summary = "채팅방 목록 조회",
            description = "참여중인 채팅방들 전체 조회"
    )
    Response<List<ChatRoomSummaryResponse>> findChatRoomSummaries(
            @AuthMember final Member member
    );

    @Operation(
            summary = "채팅방 삭제",
            description = "참여중인 채팅방을 삭제합니다"
    )
    Response<Void> deleteChatRoom(
            @PathVariable("roomId") final String roomId,
            @AuthMember final Member member
    );

    @Operation(
            summary = "채팅방 알람 on/off",
            description = "참여중인 채팅방 알람의 상태를 on -> off 혹은 off -> on 으로 바꿉니다 " +
                    "return 값 true = on, false = off"
    )
    Response<Boolean> changeNotificationSetting(
            @PathVariable("roomId") final String roomId,
            @AuthMember final Member member
    );

    @Operation(
            summary = "채팅방 정보(개인 채팅인 경우) 혹은 참여 인원들(단체 채팅인 경우) 확인",
            description = "해당 채팅방의 설명을 확인합니다"
    )
    Response<String> findRoomInfo(
            @PathVariable("roomId") final String roomId,
            @AuthMember final Member member
    );


}
