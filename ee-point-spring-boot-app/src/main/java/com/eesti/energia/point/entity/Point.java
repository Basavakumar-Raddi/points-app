package com.eesti.energia.point.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;

import com.eesti.energia.point.info.LocationEnum;

import org.pojomatic.annotations.Property;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "point")
@Getter
@Setter
public class Point extends AbstractAuditable{

    @Column(name = "MEASUREMENT_DAY")
    private Long measurementDay;

    @Column(name = "MEASUREMENT_LOCATION")
    @Enumerated(EnumType.STRING)
    private LocationEnum location;

    @Column(name = "MEASUREMENT_VALUE")
    private Double value;

}
