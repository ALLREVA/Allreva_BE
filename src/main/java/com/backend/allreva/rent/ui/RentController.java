package com.backend.allreva.rent.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.rent.command.application.RentCommandFacade;
import com.backend.allreva.rent.command.application.request.RentIdRequest;
import com.backend.allreva.rent.command.application.request.RentRegisterRequest;
import com.backend.allreva.rent.command.application.request.RentUpdateRequest;
import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent.query.application.RentQueryService;
import com.backend.allreva.rent.query.application.response.*;
import com.backend.allreva.survey.query.application.response.SortType;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rents")
public class RentController implements RentControllerSwagger {

    private final RentCommandFacade rentCommandFacade;
    private final RentQueryService rentQueryService;


    @PostMapping
    public Response<Long> createRent(
            @RequestBody final RentRegisterRequest rentRegisterRequest,
            @RequestBody(required = false) final Image image,
            @AuthMember final Member member
    ) {
        Long rentIdResponse = rentCommandFacade.registerRent(rentRegisterRequest, image, member.getId());
        return Response.onSuccess(rentIdResponse);
    }

    @PatchMapping
    public Response<Void> updateRent(
            @RequestBody final RentUpdateRequest rentUpdateRequest,
            @RequestBody final Image image,
            @AuthMember final Member member
    ) {
        rentCommandFacade.updateRent(rentUpdateRequest, image, member.getId());
        return Response.onSuccess();
    }

    @PatchMapping("/close")
    public Response<Void> closeRent(
            @RequestBody final RentIdRequest rentIdRequest,
            @AuthMember final Member member
    ) {
        rentCommandFacade.closeRent(rentIdRequest, member.getId());
        return Response.onSuccess();
    }

    @DeleteMapping
    public Response<Void> deleteRent(
            @RequestBody final RentIdRequest rentIdRequest,
            @AuthMember final Member member
    ) {
        rentCommandFacade.deleteRent(rentIdRequest, member.getId());
        return Response.onSuccess();
    }

    @GetMapping("/main")
    public Response<List<RentSummaryResponse>> getRentMainSummaries(){
        return Response.onSuccess(rentQueryService.getRentMainSummaries());
    }

    @GetMapping("/list")
    public Response<List<RentSummaryResponse>> getRentSummaries(
            @RequestParam(name = "region", required = false) final Region region,
            @RequestParam(name = "sort", defaultValue = "LATEST") final SortType sortType,
            @RequestParam(name = "lastId", required = false) final Long lastId,
            @RequestParam(name = "lastEndDate", required = false) final LocalDate lastEndDate,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(10) final int pageSize
    ) {
        return Response.onSuccess(rentQueryService.getRentSummaries(region, sortType, lastEndDate, lastId, pageSize));
    }

    @GetMapping("/{id}")
    public Response<RentDetailResponse> getRentDetailById(
            @PathVariable final Long id,
            @AuthMember final Member member
    ) {
        return Response.onSuccess(rentQueryService.getRentDetailById(id, member));
    }

    @GetMapping("/{id}/deposit-account")
    public Response<DepositAccountResponse> getDepositAccountById(
            @PathVariable final Long id
    ) {
        return Response.onSuccess(rentQueryService.getDepositAccountById(id));
    }

    @GetMapping("/register/list")
    public Response<List<RentAdminSummaryResponse>> getRentAdminSummaries(
            @AuthMember Member member
    ) {
        return Response.onSuccess(rentQueryService.getRentAdminSummariesByMemberId(member.getId()));
    }

    @GetMapping("/{id}/register")
    public Response<RentAdminDetailResponse> getRentAdminDetail(
            @PathVariable("id") final Long rentId,
            @RequestParam final LocalDate boardingDate,
            @AuthMember Member member
    ) {
        return Response.onSuccess(rentQueryService.getRentAdminDetail(member.getId(), boardingDate, rentId));
    }
}
