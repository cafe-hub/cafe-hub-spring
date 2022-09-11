package com.cafehub.cafehubspring.util;

import com.cafehub.cafehubspring.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInjector implements ApplicationRunner {

    private final CafeService cafeService;

    /**
     * csv 데이터 읽기
     */
    public List<List<String>> readCsvData(String filePath) {

        List<List<String>> data = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                data.add(Arrays.asList(values));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return data;
    }

    /**
     * csv 데이터와 사진 주입
     */
    public void csvDataAndImageInjection(String filePath, String folderPath) {

        List<List<String>> data = readCsvData(filePath);

        for (List cafe : data) {
            String cafeName = cafe.get(0).toString();
            String location = cafe.get(1).toString();
            List<String> openingHours = new ArrayList<>();
            for (int openingHoursColumn = 2; openingHoursColumn < 9; openingHoursColumn++) {
                if (cafe.get(openingHoursColumn).toString().isBlank()) {
                    openingHours.add(null);
                } else {
                    openingHours.add(cafe.get(openingHoursColumn).toString());
                }
            }
            String plugStatus = cafe.get(9).toString();

            Long cafeId = cafeService.save(cafeName, location, plugStatus);

            if (cafeId != null) {
                cafeService.saveOpeningHours(cafeId,
                        openingHours.get(0),
                        openingHours.get(1),
                        openingHours.get(2),
                        openingHours.get(3),
                        openingHours.get(4),
                        openingHours.get(5),
                        openingHours.get(6));
            }

            for (int imgCnt = 1; imgCnt <= Integer.parseInt(cafe.get(10).toString()); imgCnt++) {
                File img = new File(folderPath + cafeName + "_" + imgCnt + ".png");
                if (img.exists()) {
                    cafeService.injectPhoto(cafeId, img);
                }
            }
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        String csvDataPath = "./src/main/resources/cafe-data.csv";
        String imgFolderPath = "./src/main/resources/cafe-img/";
        File csvDataFile = new File(csvDataPath);
        if (csvDataFile.exists()) {
            csvDataAndImageInjection(csvDataPath, imgFolderPath);
        }
    }
}
