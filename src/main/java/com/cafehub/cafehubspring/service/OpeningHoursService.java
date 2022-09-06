package com.cafehub.cafehubspring.service;

import com.cafehub.cafehubspring.domain.Cafe;
import com.cafehub.cafehubspring.domain.OpeningHours;
import com.cafehub.cafehubspring.exception.http.InternalServerErrorException;
import com.cafehub.cafehubspring.repository.OpeningHoursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OpeningHoursService {

    private final OpeningHoursRepository openingHoursRepository;

    /**
     * OpeningHours 저장 |
     * 영업시간을 저장한다. 저장 중 디비에서 에러가 발생하면 500(Internal Server Error)을 던진다.
     */
    public OpeningHours save(Cafe cafe,
                             String monday,
                             String tuesday,
                             String wednesday,
                             String thursday,
                             String friday,
                             String saturday,
                             String sunday) {

        log.info("IN PROGRESS | OpeningHours 저장 At " + LocalDateTime.now());

        OpeningHours openingHours = OpeningHours.builder()
                .cafe(cafe)
                .monday(monday)
                .tuesday(tuesday)
                .wednesday(wednesday)
                .thursday(thursday)
                .friday(friday)
                .saturday(saturday)
                .sunday(sunday)
                .build();
        try {
            openingHoursRepository.save(openingHours);
        } catch (Exception e) {
            throw new InternalServerErrorException("OpeningHours 저장 중 에러 발생", e);
        }

        log.info("COMPLETE | OpeningHours 저장 At " + LocalDateTime.now() + " | " + openingHours.toString());
        return openingHours;
    }

    /**
     * OpeningHours 수정
     * 영업시간을 수정하고 저장한다. 저장 중 디비에서 에러가 발생하면 500(Internal Server Error)을 던진다.
     */
    public void update(Long openingHoursId,
                                   String monday,
                                   String tuesday,
                                   String wednesday,
                                   String thursday,
                                   String friday,
                                   String saturday,
                                   String sunday) {

        log.info("IN PROGRESS | OpeningHours 수정 At " + LocalDateTime.now() + " | " + openingHoursId.toString());
        OpeningHours openingHours = openingHoursRepository.findById(openingHoursId).get();
        openingHours.updateOpeningHours(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        try {
            openingHoursRepository.save(openingHours);
        } catch (Exception e) {
            throw new InternalServerErrorException("OpeningHours 수정 중 에러 발생", e);
        }
        log.info("COMPLETE | OpeningHours 수정 At " + LocalDateTime.now() + " | " + openingHours.toString());
    }

    /**
     * OpeningHours 삭제
     * 영업시간을 삭제한다. 삭제 중 디비에서 에러가 발생하면 500(Internal Server Error)을 던진다.
     */
    public void delete(Long openingHoursId) {

        log.info("IN PROGRESS | OpeningHours 삭제 At " + LocalDateTime.now() + " | " + openingHoursId.toString());

        OpeningHours openingHours = openingHoursRepository.findById(openingHoursId).get();
        try {
            openingHoursRepository.delete(openingHours);
        } catch (Exception e) {
            throw new InternalServerErrorException("OpeningHours 삭제 중 에러 발생", e);
        }

        log.info("COMPLETE | OpeningHours 삭제 At " + LocalDateTime.now() + " | " + openingHoursId.toString());
    }

}
