package com.backend.allreva.hall.command;

import com.backend.allreva.hall.command.domain.ConcertHall;
import com.backend.allreva.hall.command.domain.ConcertHallRepository;
import com.backend.allreva.hall.exception.ConcertHallNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConcertHallService {
    private final ConcertHallRepository concertHallRepository;

    public ConcertHall updateConcertHallStar(
            final String hallId,
            final int starDelta,
            final int countDelta
    ) {
        ConcertHall concertHall = concertHallRepository.findByIdWithLock(hallId).orElseThrow(
                ConcertHallNotFoundException::new
        );

        concertHall.updateStar(starDelta, countDelta);

        return concertHallRepository.save(concertHall);
    }
}
