package com.backend.allreva.hall.query.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "concerts_hall")
@Setting(settingPath = "elasticsearch/mappings/es-settings.json")
@Mapping(mappingPath = "elasticsearch/mappings/concert_hall-mapping.json")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ConcertHallDocument {
    @Id
    private String id;

    @Field(type = FieldType.Boolean, name = "cafe")
    private Boolean cafe;

    @Field(type = FieldType.Boolean, name = "park_barrier")
    private Boolean parkBarrier;

    @Field(type = FieldType.Boolean, name = "rest_barrier")
    private Boolean restBarrier;

    @Field(type = FieldType.Boolean, name = "elev_barrier")
    private Boolean elevBarrier;

    @Field(type = FieldType.Boolean, name = "parking")
    private Boolean parking;

    @Field(type = FieldType.Boolean, name = "restaurant")
    private Boolean restaurant;

    @Field(type = FieldType.Boolean, name = "runw_barrier")
    private Boolean runwBarrier;

    @Field(type = FieldType.Boolean, name = "store")
    private Boolean store;

    @Field(type = FieldType.Text, name = "address", analyzer = "korean_mixed")
    private String address;

    @Field(type = FieldType.Double, name = "latitude")
    private Double latitude;

    @Field(type = FieldType.Double, name = "longitude")
    private Double longitude;

    @Field(type = FieldType.Text , name = "name" , analyzer = "korean_mixed")
    private String name;

    @Field(type = FieldType.Integer, name = "seat_scale")
    private Integer seatScale;

    @Field(type = FieldType.Double, name = "star")
    private Double star;

}
