package com.cafehub.cafehubspring.service;

import com.cafehub.cafehubspring.domain.Cafe;
import com.cafehub.cafehubspring.domain.Photo;
import com.cafehub.cafehubspring.dto.CafeSaveRequestDto;
import com.cafehub.cafehubspring.exception.http.InternalServerErrorException;
import com.cafehub.cafehubspring.repository.CafeRepository;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CafeService {

    private CafeRepository cafeRepository;
    private PhotoService photoService;

    @Value("${kakao.apiKey}")
    private String apiKey;

    /**
     * 카페 단건 조회
     */
    public Cafe findById(Long cafeId) {

        return cafeRepository.findById(cafeId).get();
    }

    /**
     * 카페 여러 건 조회
     */
    public List<Cafe> findCafes(Float topLeftLongitude, Float topLeftLatitude, Float bottomRightLongitude, Float bottomRightLatitude) {

        List<Cafe> findCafes = cafeRepository.findCafes(topLeftLatitude, bottomRightLatitude, topLeftLongitude, bottomRightLongitude);

        return findCafes;
    }

    /**
     * 카페 저장
     */
    @Transactional
    public Long save(String cafeName, String location, Float latitude, Float longitude, String plugStatus) {

        log.info("IN PROGRESS | Cafe 저장 At " + LocalDateTime.now() +
                " | 카페 이름 = " + cafeName);

        try {
            Cafe cafe = Cafe.builder()
                    .cafeName(cafeName)
                    .location(location)
                    .latitude(latitude)
                    .longitude(longitude)
                    .plugStatus(plugStatus)
                    .build();

            cafeRepository.save(cafe);
            log.info("COMPLETE | Cafe 저장 At " + LocalDateTime.now() + " | 카페 이름 = " + cafe.getCafeName()
                    + " | 카페 아이디 = " + cafe.getId());
            return cafe.getId();
        } catch(Exception e) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public Long savePhoto(Long cafeId, Photo photo, MultipartFile file) {

        log.info("IN PROGRESS | Cafe 포토 저장 At " + LocalDateTime.now() +
                " | 카페 이름 = " + findById(cafeId) + " | 파일명 = " + photo.getFileName());
        Cafe cafe = findById(cafeId);
        Photo savedPhoto = photoService.save(cafe.getCafeName(), cafe.getId(), photo.getFileName(), file);
        cafe.addPhoto(savedPhoto);
        cafeRepository.save(cafe);
        log.info("COMPLETE | Cafe 이미지 저장 At " + LocalDateTime.now() + " | 카페 이름 = " + cafe.getCafeName()
                + " | 카페 포토 파일 이름 = " + savedPhoto.getFileName());
        return cafe.getId();
    }

    @Transactional
    public Long saveOpeningHours(Long cafeId, String dayOfTheWeek, String openingHours) {
        log.info("IN PROGRESS | Cafe 영업시간 저장 At " + LocalDateTime.now() +
                " | 카페 이름 = " + findById(cafeId));
        Cafe cafe = cafeRepository.findById(cafeId).get();
        cafe.addOpeningHours(dayOfTheWeek, openingHours);
        cafeRepository.save(cafe);
        log.info("COMPLETE | Cafe 영업시간 저장 At " + LocalDateTime.now() + " | 카페 이름 = " + cafe.getCafeName());
        return cafe.getId();
    }

    /**
     * 카페 업데이트
     */
    @Transactional
    public void updateCafe(Long cafeId, CafeSaveRequestDto cafeSaveRequestDto) {
        log.info("IN PROGRESS | Cafe 업데이트 At " + LocalDateTime.now() +
                " | 카페 이름 = " + findById(cafeId).getCafeName());
        Cafe cafe = cafeRepository.findById(cafeId).get();

        Float[] coordinate = coordinateFromAddress(cafe.getLocation());

        cafe.updateLongitude(coordinate[0]);
        cafe.updateLatitude(coordinate[1]);
        cafe.updateCafeName(cafeSaveRequestDto.getCafeName());
        cafe.updatePlugStatus(cafeSaveRequestDto.getPlugStatus());
        cafe.updateLocation(cafeSaveRequestDto.getLocation());

        log.info("COMPLETE | Cafe 업데이트 At " + LocalDateTime.now() + " | 카페 이름 = " + cafe.getCafeName());
    }

    @Transactional
    public void updateCafeDeletePhoto(Long cafeId, String fileName) {
        log.info("IN PROGRESS | Cafe 포토 삭제 At " + LocalDateTime.now() +
                " | 카페 이름 = " + findById(cafeId).getCafeName());
        Cafe cafe = cafeRepository.findById(cafeId).get();
        // TODO: delete 수정
        photoService.delete(cafe.getCafeName(), fileName);
        log.info("COMPLETE | Cafe 포토 삭제 At " + LocalDateTime.now());
    }

    @Transactional
    public void updateCafeDeleteOpeningHours(Long cafeId, String dayOfTheWeek, String openingHour) {

        Cafe cafe = cafeRepository.findById(cafeId).get();
        Map<String, String> openingHours = cafe.getOpeningHours();
        openingHours.put(dayOfTheWeek, openingHour);
    }

    /**
     * 카페 삭제
     */
    @Transactional
    public void deleteCafe(Long cafeId) {
        log.info("IN PROGRESS | Cafe 삭제 At " + LocalDateTime.now() +
                " | 카페 이름 = " + findById(cafeId));
        cafeRepository.delete(findById(cafeId));
        log.info("COMPLETE | Cafe 삭제 At " + LocalDateTime.now() + " | 카페 아이디 = " + cafeId);
    }

    /**
     * 주소로부터 좌표값 |
     * 카카오 API를 이용해서 주소를 입력하면 좌표값을 반환 받는다. API URL에 주소를 보내면 json 형태의 응답은 반환 받는다. 반환 받은 json에서 x좌표,
     * y좌표, 즉 경도, 위도의 값을 추출하여 반환한다. 이 과정 중 예외 발생시 500(Internal Server Error)을 던진다.
     */
    public Float[] coordinateFromAddress (String roadFullAddress) {
        Float[] coordinate = new Float[2];

        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
        String jsonString;

        try {
            roadFullAddress = URLEncoder.encode(roadFullAddress, StandardCharsets.UTF_8);
            String apiFullUrl = apiUrl + "?query=" + roadFullAddress;
            URL url = new URL(apiFullUrl);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Authorization", apiKey);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)
            );
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            jsonString = builder.toString();
            reader.close();

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("documents");
            JSONObject documentsObject = jsonArray.getJSONObject(0);
            String longitude = documentsObject.getString("x");
            String latitude = documentsObject.getString("y");
            coordinate[0] = Float.parseFloat(longitude);
            coordinate[1] = Float.parseFloat(latitude);
        } catch (Exception e) {
            throw new InternalServerErrorException(e);
        }
        return coordinate;
    }
}
