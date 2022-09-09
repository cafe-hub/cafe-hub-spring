package com.cafehub.cafehubspring.service;

import com.cafehub.cafehubspring.domain.Cafe;
import com.cafehub.cafehubspring.domain.OpeningHours;
import com.cafehub.cafehubspring.domain.Photo;
import com.cafehub.cafehubspring.exception.http.InternalServerErrorException;
import com.cafehub.cafehubspring.exception.http.NotAcceptableException;
import com.cafehub.cafehubspring.exception.http.NotFoundException;
import com.cafehub.cafehubspring.repository.CafeRepository;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.lang.Integer.parseInt;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;
    private final PhotoService photoService;
    private final OpeningHoursService openingHoursService;

    @Value("${kakao.apiKey}")
    private String apiKey;

    /**
     * Cafe 단건 조회 |
     * 카페를 식별자를 통해 조회합니다.
     */
    public Cafe findOneById(Long cafeId) {
        log.info("START | Cafe 단건 조회 AT " + LocalDateTime.now() + " | " + cafeId);

        Optional<Cafe> foundCafe = cafeRepository.findById(cafeId);
        if (foundCafe.isEmpty()) {
            throw new NotFoundException("카페 정보를 찾을 수 없습니다.");
        }

        log.info("COMPLETE | Cafe 단건 조회 AT " + LocalDateTime.now() + " | " + foundCafe.get().getCafeName());

        return foundCafe.get();
    }

    /**
     * Cafe 여러 건 조회 |
     * 왼쪽 상단의 위도, 경도와 오른쪽 하단의 위도, 경도를 통해서 위도, 경도의 좌표값 안에 위치한 카페들을 조회한다.
     */
    public List<Cafe> findManyByCoordinates(Float topLeftLongitude,
                                            Float topLeftLatitude,
                                            Float bottomRightLongitude,
                                            Float bottomRightLatitude) {

        return cafeRepository.findCafesByCoordinates(
                topLeftLatitude,
                bottomRightLatitude,
                topLeftLongitude,
                bottomRightLongitude
        );
    }

    /**
     * Cafe 전체 조회 |
     * 현재 디비에 있는 모든 카페 데이터들을 조회한다.
     */
    public List<Cafe> findAll() {

        return cafeRepository.findAll();
    }

    /**
     * Cafe 저장 |
     * 카페를 저장합니다. 저장 중 디비에서 에러가 발생하면 500(Internal Server Error)을 던진다.
     */
    @Transactional
    public Long save(String cafeName, String location, String plugStatus) {

        log.info("IN PROGRESS | Cafe 저장 At " + LocalDateTime.now() + " | " + cafeName);

        Double[] coordinate = coordinateFromAddress(location);

        Double longitude = coordinate[0];
        Double latitude = coordinate[1];

        Cafe cafe = Cafe.builder()
                .cafeName(cafeName)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .plugStatus(plugStatus)
                .build();

        try {
            cafeRepository.save(cafe);
        } catch(Exception e) {
            throw new InternalServerErrorException("Cafe save 중 에러 발생", e);
        }

        log.info("COMPLETE | Cafe 저장 At " + LocalDateTime.now() + " | " + cafe.toString());
        return cafe.getId();
    }

    /**
     * Cafe 사진 저장 |
     * 카페 사진을 저장한다. 식별자를 통한 카페 조회 중 에러가 발생하면 404(Not Found)을 던진다. 저장할 파일이 존재 한다면 파일 이름을 부여하여 차례대로
     * 저장한다.
     */
    @Transactional
    public Long savePhoto(Long cafeId, MultipartFile photo) {

        log.info("IN PROGRESS | Cafe 포토 저장 At " + LocalDateTime.now() + " | " + cafeId);
        Cafe foundCafe = findOneById(cafeId);

        int index = fileNumbering(foundCafe.getPhotos());
        String fileName = foundCafe.getCafeName() + "_" + index +
                photo.getContentType().replace("image/", ".");
        Photo savedPhoto =
                photoService.save(foundCafe, fileName, photo);
        foundCafe.addPhoto(savedPhoto);

        log.info("COMPLETE | Cafe 이미지 저장 At " + LocalDateTime.now() + " | " + foundCafe.getPhotos().size());

        return foundCafe.getId();
    }

    /**
     * 파일 번호 부여 |
     * 현재 매핑되어 있는 사진들의 파일 이름을 확인하여 가장 마지막으로 부여한 번호에 1을 더하여 리턴합니다. 이 번호는 다음 파일 번호를 부여하는데 사용됩니다.
     */
    private int fileNumbering(List<Photo> photos) {

        log.info("IN PROGRESS | 파일 번호 부여 At " + LocalDateTime.now() + " | " + photos.size());
        int index = 1;

        if (photos.isEmpty()) {
            return index;
        } else {
            for (Photo photo : photos) {
                String[] fileName = photo.getFileName().split("[_.]");
                int fileNameIndex = parseInt(fileName[1]);
                if (fileNameIndex > index) {
                    index = fileNameIndex;
                }
            }
            index += 1;
        }

        log.info("COMPLETE | 파일 번호 부여 At " + LocalDateTime.now() + " | " + index);
        return index;
    }

    /**
     * Cafe 운영시간 저장 |
     * 카페 식별자를 통해 관련된 카페 정보를 조회하여 운영시간을 저장한다.
     */
    @Transactional
    public Long saveOpeningHours(Long cafeId,
                                 String monday,
                                 String tuesday,
                                 String wednesday,
                                 String thursday,
                                 String friday,
                                 String saturday,
                                 String sunday) {

        log.info("IN PROGRESS | Cafe 영업시간 저장 At " + LocalDateTime.now() + " | " + cafeId);
        Cafe foundCafe = findOneById(cafeId);

        OpeningHours openingHours =
                openingHoursService.save(foundCafe,
                        monday,
                        tuesday,
                        wednesday,
                        thursday,
                        friday,
                        saturday,
                        sunday);

        foundCafe.updateOpeningHours(openingHours);
        log.info("COMPLETE | Cafe 영업시간 저장 At " + LocalDateTime.now() + " | " + foundCafe.getId());
        return foundCafe.getId();
    }

    /**
     * Cafe 수정 |
     * 카페 식별자를 통해 관련된 카페 정보를 조회하여 카페 정보를 수정한다.
     */
    @Transactional
    public Long updateCafe(Long cafeId, String cafeName, String location, String plugStatus) {
        log.info("IN PROGRESS | Cafe 업데이트 At " + LocalDateTime.now() + " | " + cafeId);
        Cafe foundCafe = findOneById(cafeId);

        Double[] coordinate = coordinateFromAddress(location);

        Double longitude = coordinate[0];
        Double latitude = coordinate[1];

        foundCafe.updateLongitude(longitude);
        foundCafe.updateLatitude(latitude);
        foundCafe.updateCafeName(cafeName);
        foundCafe.updatePlugStatus(plugStatus);
        foundCafe.updateLocation(location);

        log.info("COMPLETE | Cafe 업데이트 At " + LocalDateTime.now() + " | " + foundCafe.getId());
        return foundCafe.getId();
    }

    /**
     * Cafe 사진 삭제 |
     * 카페 식별자를 통해 관련된 카페 정보를 조회하여 카페 사진을 삭제한다.
     */
    @Transactional
    public void updateCafeDeletePhoto(Long cafeId, String fileName) {

        log.info("IN PROGRESS | Cafe 사진 삭제 At " + LocalDateTime.now() + " | " + cafeId);
        Cafe foundCafe = findOneById(cafeId);

        photoService.delete(foundCafe, fileName);
        log.info("COMPLETE | Cafe 사진 삭제 At " + LocalDateTime.now());
    }

    /**
     * Cafe 운영시간 수정 |
     * 카페 식별자를 통해 관련된 카페 정보를 조회하여 관련된 카페 운영시간을 수정한다.
     */
    @Transactional
    public void updateCafeOpeningHours(Long cafeId,
                                       String monday,
                                       String tuesday,
                                       String wednesday,
                                       String thursday,
                                       String friday,
                                       String saturday,
                                       String sunday) {

        log.info("IN PROGRESS | Cafe 운영시간 수정 At " + LocalDateTime.now() + " | " + cafeId);

        Cafe foundCafe = findOneById(cafeId);

        OpeningHours openingHours = foundCafe.getOpeningHours();
        openingHoursService.update(openingHours.getId(),
                monday,
                tuesday,
                wednesday,
                thursday,
                friday,
                saturday,
                sunday);
        log.info("COMPLETE | Cafe 운영시간 수정 At " + LocalDateTime.now() + " | " + cafeId);
    }

    /**
     * Cafe 운영시간 삭제 |
     * 카페 식별자를 통해 관련된 카페 정보를 조회하여 관련된 카페 운영시간을 삭제한다.
     */
    @Transactional
    public void updateCafeDeleteOpeningHours(Long cafeId) {

        log.info("IN PROGRESS | Cafe 운영시간 수정 삭제 " + LocalDateTime.now() + " | " + cafeId);
        Cafe foundCafe = findOneById(cafeId);

        Long operatingHoursId = foundCafe.getOpeningHours().getId();
        openingHoursService.delete(operatingHoursId);
        log.info("COMPLETE | Cafe 운영시간 수정 삭제 " + LocalDateTime.now() + " | " + cafeId);
    }

    /**
     * Cafe 삭제 |
     * 카페 식별자를 통해 관련된 카페 정보를 조회하여 관련된 카페 운영시간, 사진, 정보를 삭제한다.
     */
    @Transactional
    public void deleteCafe(Long cafeId) {
        log.info("IN PROGRESS | Cafe 삭제 At " + LocalDateTime.now() + " | " + cafeId);

        Cafe foundCafe = findOneById(cafeId);

        updateCafeDeleteOpeningHours(foundCafe.getId());

        foundCafe.getPhotos().forEach(photo -> {
            updateCafeDeletePhoto(photo.getCafe().getId(), photo.getFileName());
        });

        try {
            cafeRepository.delete(foundCafe);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cafe 삭제 중 에러 발생", e);
        }

        log.info("COMPLETE | Cafe 삭제 At " + LocalDateTime.now() + " | " + cafeId);
    }

    /**
     * 주소로부터 좌표값 |
     * 카카오 API를 이용해서 주소를 입력하면 좌표값을 반환 받는다. API URL에 주소를 보내면 json 형태의 응답은 반환 받는다. 반환 받은 json에서 x좌표,
     * y좌표, 즉 경도, 위도의 값을 추출하여 반환한다. 이 과정 중 예외 발생시 500(Internal Server Error)을 던진다. 만약 잘못된 주소 입력으로 인해
     * 반환 값을 받지 못하게 된다면 406(Not Acceptable)을 던진다.
     */
    public Double[] coordinateFromAddress (String roadFullAddress) {
        log.info("IN PROGRESS | 주소로부터 좌표값 At " + LocalDateTime.now() + " | " + roadFullAddress);
        Double[] coordinate = new Double[2];

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
            if (jsonArray.getJSONObject(0) == null) {
                throw new NotAcceptableException("잘못된 주소입니다");
            }
            JSONObject documentsObject = jsonArray.getJSONObject(0);
            String longitude = documentsObject.getString("x");
            String latitude = documentsObject.getString("y");
            DecimalFormat sevenDecimalFormat = new DecimalFormat("#.#######");

            coordinate[0] = Double.parseDouble(
                    sevenDecimalFormat.format(
                            Double.parseDouble(longitude)
                    )
            );
            coordinate[1] = Double.parseDouble(
                    sevenDecimalFormat.format(
                            Double.parseDouble(latitude)
                    )
            );
        } catch (Exception e) {
            throw new InternalServerErrorException(e);
        }
        log.info("COMPLETE | 주소로부터 좌표값 At " + LocalDateTime.now() + " | " + coordinate.toString());
        return coordinate;
    }
}