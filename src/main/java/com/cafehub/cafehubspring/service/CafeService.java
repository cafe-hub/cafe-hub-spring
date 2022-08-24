package com.cafehub.cafehubspring.service;

import com.cafehub.cafehubspring.exception.http.InternalServerErrorException;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class CafeService {

    @Value("${apiKey}")
    private String apiKey;

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
