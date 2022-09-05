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
    @GeneratedValue
    @Column(name = "photo_id")
    private String id;

    private String fileName;

    private String url;

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    public void setCafe(Cafe cafe) {
        this.cafe = cafe;
    }

    @Builder
    public Photo(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }

    public void updateFileName(String fileName) {
        this.fileName = fileName;
    }

    public void updateUrl(String url) {
        this.url = url;
    }
}