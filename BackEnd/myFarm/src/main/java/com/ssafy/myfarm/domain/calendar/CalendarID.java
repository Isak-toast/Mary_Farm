package com.ssafy.myfarm.domain.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarID implements Serializable {
    @Column(name = "plant_id")
    private String id;
    @Column(name = "day")
    private String day;
}
