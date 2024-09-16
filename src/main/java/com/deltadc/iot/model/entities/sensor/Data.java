//package com.deltadc.iot.model.entities.sensor;
//
//import jakarta.persistence.*;
//
//@Embeddable
//public class Data {
//
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "value", column = @Column(name = "temperature_value")),
//            @AttributeOverride(name = "unit", column = @Column(name = "temperature_unit"))
//    })
//    private Measurement temperature;
//
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "value", column = @Column(name = "humidity_value")),
//            @AttributeOverride(name = "unit", column = @Column(name = "humidity_unit"))
//    })
//    private Measurement humidity;
//
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "value", column = @Column(name = "brightness_value")),
//            @AttributeOverride(name = "unit", column = @Column(name = "brightness_unit"))
//    })
//    private Measurement brightness;
//}
