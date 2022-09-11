package com.cafehub.cafehubspring.domain;

import com.cafehub.cafehubspring.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;

    private String fileName;

    @Column(length = 1000)
    private String url;

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @Builder
    public Photo(String fileName, String url, Cafe cafe) {
        this.fileName = fileName;
        this.url = url;
        this.cafe = cafe;
    }

    public void updateFileName(String fileName) {
        this.fileName = fileName;
    }

    public void updateUrl(String url) {
        this.url = url;
    }
}