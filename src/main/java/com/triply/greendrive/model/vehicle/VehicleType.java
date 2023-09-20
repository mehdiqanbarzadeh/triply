package com.triply.greendrive.model.vehicle;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VehicleType {

    VAN(180,0),
    TAXI(200,0),
    BUS(250,0),
    CRANE(350,0),
    FORKLIFT(150,0),
    TRACTOR(300,0),
    DUMP_TRUCK(400,0),
    CEMENT_MIXER(350,0),
    MOTORCYCLE(80,0),
    AMBULANCE(220,0);


    private final int diffusion;
    private final int mileageThreshold;

    @JsonValue
    public int toDiffusion() {
        return diffusion;
    }
}
