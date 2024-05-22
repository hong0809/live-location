package com.rotary.livelocation.model.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Double latitude;
    private Double longitude;
    private Long altitude;
    private String nodeId;
    private String NodeName;
    private long timestamp;

}
