package com.backend.allreva.rent_join.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.rent_join.command.application.RentJoinCommandService;
import com.backend.allreva.rent_join.command.application.request.RentJoinApplyRequest;
import com.backend.allreva.rent_join.command.application.request.RentJoinIdRequest;
import com.backend.allreva.rent_join.command.application.request.RentJoinUpdateRequest;
import com.backend.allreva.rent_join.query.RentJoinQueryService;
import com.backend.allreva.rent_join.query.response.RentJoinResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/rents")
@RestController
@Validated
public class RentJoinController implements RentJoinControllerSwagger{

    private final RentJoinCommandService rentJoinCommandService;
    private final RentJoinQueryService rentJoinQueryService;

    @PostMapping("/apply")
    public Response<Long> applyRent(
            @RequestBody final RentJoinApplyRequest rentJoinApplyRequest,
            @AuthMember final Member member
    ) {
        Long rentIdResponse = rentJoinCommandService.applyRent(rentJoinApplyRequest, member.getId());
        return Response.onSuccess(rentIdResponse);
    }

    @PatchMapping("/apply")
    public Response<Void> updateRentJoin(
            @RequestBody final RentJoinUpdateRequest rentJoinUpdateRequest,
            @AuthMember final Member member
    ) {
        rentJoinCommandService.updateRentJoin(rentJoinUpdateRequest, member.getId());
        return Response.onSuccess();
    }

    @DeleteMapping("/apply")
    public Response<Void> deleteRentJoin(
            @RequestBody final RentJoinIdRequest rentJoinIdRequest,
            @AuthMember final Member member
    ) {
        rentJoinCommandService.deleteRentJoin(rentJoinIdRequest, member.getId());
        return Response.onSuccess();
    }

    @GetMapping("/join/list")
    public Response<List<RentJoinResponse>> getRentJoin(
            @AuthMember Member member
    ) {
        return Response.onSuccess(rentJoinQueryService.getRentJoin(member.getId()));
    }
}
