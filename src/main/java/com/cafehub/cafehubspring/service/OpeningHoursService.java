package com.cafehub.cafehubspring.service;

import com.cafehub.cafehubspring.domain.OpeningHours;
import com.cafehub.cafehubspring.repository.OpeningHoursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OpeningHoursService {

    private OpeningHoursRepository openingHoursRepository;

    /**
     * OpeningHours 저장
     */
    @Transactional
    public OpeningHours save(String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {

        OpeningHours openingHours = OpeningHours.builder()
                .monday(monday)
                .tuesday(tuesday)
                .wednesday(wednesday)
                .thursday(thursday)
                .friday(friday)
                .saturday(saturday)
                .sunday(sunday)
                .build();

        openingHoursRepository.save(openingHours);

        return openingHours;
    }

    /**
     * OpeningHours 수정
     */
    public void updateOpeningHours(Long openingHoursId, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {

        OpeningHours openingHours = openingHoursRepository.findById(openingHoursId).get();
        openingHours.updateOpeningHours(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        openingHoursRepository.save(openingHours);
    }

    /**
     * OpeningHours 삭제
     */
    public void deleteOpeningHours(Long openingHoursId) {
        OpeningHours openingHours = openingHoursRepository.findById(openingHoursId).get();
        openingHoursRepository.delete(openingHours);
    }

}
