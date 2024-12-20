package com.backend.allreva.rent.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent.query.application.RentQueryService;
import com.backend.allreva.rent.query.application.response.DepositAccountResponse;
import com.backend.allreva.rent.query.application.response.RentAdminDetailResponse;
import com.backend.allreva.rent.query.application.response.RentAdminSummaryResponse;
import com.backend.allreva.rent.query.application.response.RentDetailResponse;
import com.backend.allreva.rent.query.application.response.RentSummaryResponse;
import com.backend.allreva.survey.query.application.response.SortType;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rents")
public class RentViewController implements RentViewControllerSwagger {

    private final RentQueryService rentQueryService;

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
            @PathVariable final Long id
    ) {
        return Response.onSuccess(rentQueryService.getRentDetailById(id));
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
