package com.ssafy.maryfarmplantservice.domain.plant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CuratingPlant implements Serializable {
    @Id
    @Column(name = "curating_plant_id")
    private String name;
}
