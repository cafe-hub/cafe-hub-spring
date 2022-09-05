package com.cafehub.cafehubspring.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OpeningHours {

    @Id
    @GeneratedValue
    @Column(name="opening_hours_id")
    private Long id;

    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;

    @OneToOne(mappedBy = "openingHours")
    private Cafe cafe;

    @Builder
    public OpeningHours(String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {

        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public void updateOpeningHours(String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {

        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }
}
