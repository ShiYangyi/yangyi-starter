package com.example.yangyistarter.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class ParkingSpaceTest {
    @Test
    public void should_return_id_2_when_set_id_2() {

        ParkingSpace parkingSpace = ParkingSpace.builder().isUsed(false).receiptId(BigInteger.valueOf(2L)).parkingLotName("parking_lot").build();
        parkingSpace.setId(BigInteger.valueOf(2L));
        Assertions.assertEquals(BigInteger.valueOf(2L), parkingSpace.getId());
    }

    @Test
    public void should_return_parking_lot_name_parking_lot_when_set_parking_lot_name_parking_lot() {

        ParkingSpace parkingSpace = ParkingSpace.builder().id(BigInteger.valueOf(2L)).isUsed(false).receiptId(BigInteger.valueOf(2L)).build();
        parkingSpace.setParkingLotName("parking_lot");
        Assertions.assertEquals("parking_lot", parkingSpace.getParkingLotName());
    }

    @Test
    public void should_return_2_when_get_receipt_id_2() {
        ParkingSpace parkingSpace = ParkingSpace.builder().id(BigInteger.valueOf(2L)).isUsed(false).receiptId(BigInteger.valueOf(2L)).parkingLotName("parking_lot").build();
        Assertions.assertEquals(BigInteger.valueOf(2L), parkingSpace.getReceiptId());
    }

    //内部类的测试写法
    @Test
    public void should_return_string_when_call_to_string() {
        ParkingSpace.ParkingSpaceBuilder parkingSpaceBuilder = new ParkingSpace.ParkingSpaceBuilder();
        parkingSpaceBuilder.id(BigInteger.valueOf(2L));
        parkingSpaceBuilder.parkingLotName("parking_lot");
        parkingSpaceBuilder.isUsed(false);
        parkingSpaceBuilder.receiptId(BigInteger.valueOf(2L));
        Assertions.assertEquals("ParkingSpace.ParkingSpaceBuilder(id=2, isUsed=false, receiptId=2, parkingLotName=parking_lot)", parkingSpaceBuilder.toString());
    }

}
