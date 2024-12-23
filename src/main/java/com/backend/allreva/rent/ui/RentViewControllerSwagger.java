package com.backend.allreva.rent.ui;

import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent.query.application.response.*;
import com.backend.allreva.survey.query.application.response.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "차량 대절 폼 조회 API", description = "차량 대절 폼 조회 API")
public interface RentViewControllerSwagger {

    @Operation(
            summary = "첫 화면 rent API 입니다.",
            description = "첫 화면 rent API 입니다. 현재 날짜에서 가장 가까운 콘서트 순으로 5개 정렬"
    )
    Response<List<RentSummaryResponse>> getRentMainSummaries();

    @Operation(
            summary = "차량 대절 폼 리스트 조회 API",
            description = """
                    차량 대절 폼의 요약된 정보를 리스트로 조회합니다.
                    """)
    Response<List<RentSummaryResponse>> getRentSummaries(
            Region region,
            SortType sortType,
            Long lastId,
            LocalDate lastEndDate,
            @Min(10) int pageSize
    );

    @Operation(
            summary = "차량 대절 폼 상세 조회 API",
            description = """
                    차량 대절 폼을 상세 조회합니다.
                    boardingDate 는 2024.11.30(토) 와 같은 형태로 반환됩니다.
                    endDate는 2024-11-30 과 같은 형태로 반환됩니다.
                    """)
    Response<RentDetailResponse> getRentDetailById(
            Long id
    );

    @Operation(
            summary = "입금 계좌 조회 API",
            description = """
                    입금 계좌를 조회합니다.
                    현재 사용자가 USER 권한보다 아래면 입금 계좌를 조회할 수 없습니다.
                    """)
    Response<DepositAccountResponse> getDepositAccountById(
            Long id
    );

    @Operation(
            summary = "내가 등록한 차 대절 리스트 조회 API",
            description = """
                    사용자가 등록한 차량 대절 폼의 요약된 정보를 리스트로 조회합니다.
                    """)
    Response<List<RentAdminSummaryResponse>> getRentAdminSummaries(
            Member member
    );

    @Operation(
            summary = "내가 등록한 차 대절 상세 조회 API",
            description = """
                    사용자가 등록한 특정 차량 대절 폼의 상세 정보를 조회합니다.
                    
                    조회하고자 하는 차량 대절 rentId는 path variable로, 특정 가용 날짜인 boardingDate는 query parameter로 전달합니다.
                    """)
    Response<RentAdminDetailResponse> getRentAdminDetail(
            Long rentId,
            LocalDate boardingDate,
            Member member
    );
}
