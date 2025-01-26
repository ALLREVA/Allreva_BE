package com.backend.allreva.hall.command.application;

import com.backend.allreva.common.util.CsvUtil;
import com.backend.allreva.hall.command.domain.ConcertHallRepository;
import com.backend.allreva.hall.infra.dto.KopisHallResponse;
import com.backend.allreva.hall.infra.dto.KopisHallResponse.Mt13;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminHallService {
    private final KopisHallService kopisHallService;
    private final ConcertHallRepository concertHallRepository;

    //공연장 정보 받아오기
    @CacheEvict(cacheNames = "concertHallMain", allEntries = true)
    public void fetchConcertHallInfoList() {
        List<String> hallCodes = CsvUtil.readConcertHallCodes();
        hallCodes.parallelStream()
                .forEach(hallCode -> {
                    KopisHallResponse response = kopisHallService.fetchConcertHallInfoList(getFacilityCode(hallCode));
                    List<Mt13> mt13List = response.getDb().getMt13s().getMt13List();

                    for (int i = 0; i < mt13List.size(); i++) {
                        Mt13 mt13 = mt13List.get(i);
                        if (mt13.getMt13id().equals(hallCode)) {
                            concertHallRepository.save(KopisHallResponse.toEntity(response, i));
                        }
                    }

                    log.info("hall detail fetch complete for hall Code: {}", hallCode);
                });
    }

    private String getFacilityCode(final String hallCode) {
        return hallCode.split("-")[0];
    }

}

