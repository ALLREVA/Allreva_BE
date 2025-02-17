package com.backend.allreva.rent.integration;

import static com.backend.allreva.rent.fixture.RentRegisterRequestFixture.createRentRegisterRequestFixture;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.backend.allreva.rent.command.application.RentCommandService;
import com.backend.allreva.rent.command.domain.Rent;
import com.backend.allreva.rent.command.domain.RentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class RentRegisterTest {

    @InjectMocks
    private RentCommandService rentCommandService;
    @Mock
    private RentRepository rentRepository;

    @Test
    void 차량_대절_폼_개설을_성공한다() {
        // given
        var memberId = 1L;
        var rentFormRequest = createRentRegisterRequestFixture();
        given(rentRepository.save(any(Rent.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        rentCommandService.registerRent(rentFormRequest, memberId);

        // then
        var capturedRent = getArgumentCaptorValue();
        assertSoftly(softly -> {
            softly.assertThat(capturedRent.getMemberId()).isEqualTo(memberId);
            softly.assertThat(capturedRent.getDetailInfo().getTitle()).isEqualTo(rentFormRequest.title());
        });
    }

    private Rent getArgumentCaptorValue() {
        var rentFormCaptor = ArgumentCaptor.forClass(Rent.class);
        verify(rentRepository).save(rentFormCaptor.capture());
        return rentFormCaptor.getValue();
    }
}
