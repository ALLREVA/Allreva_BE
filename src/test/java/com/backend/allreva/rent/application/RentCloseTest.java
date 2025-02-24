package com.backend.allreva.rent.application;

import static com.backend.allreva.rent.fixture.RentFixture.createRentFixture;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.backend.allreva.rent.command.application.RentCommandService;
import com.backend.allreva.rent.command.application.request.RentIdRequest;
import com.backend.allreva.rent.command.domain.RentRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class RentCloseTest {

    @InjectMocks
    private RentCommandService rentCommandService;
    @Mock
    private RentRepository rentRepository;

    @Test
    void 차량_대절_폼_마감을_성공한다() {
        // given
        var memberId = 1L;
        var rentFormIdRequest = new RentIdRequest(1L);
        var rent = createRentFixture(memberId, 1L);
        given(rentRepository.findById(anyLong())).willReturn(Optional.of(rent));

        // when
        rentCommandService.closeRent(rentFormIdRequest, memberId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(rent.getMemberId()).isEqualTo(memberId);
            softly.assertThat(rent.isClosed()).isTrue();
        });
    }
}
