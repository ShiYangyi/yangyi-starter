package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.ParkingSpace;
import com.example.yangyistarter.repository.ParkingSpaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PickingServiceTest {

    ParkingSpaceRepository parkingSpaceRepository = mock(ParkingSpaceRepository.class);
    ParkingSpace parkingSpace = mock(ParkingSpace.class);
    PickingService pickingService = new PickingService(parkingSpaceRepository);
    long receiptId = 1L;

    @Test
    public void should_return_response_when_receipt_id_invalid() {
        when(parkingSpaceRepository.findById(receiptId)).thenReturn(Optional.empty());
        Assertions.assertEquals(10012, pickingService.picking(receiptId).getCode());
        Assertions.assertEquals("receipt is invalid", pickingService.picking(receiptId).getMessage());

    }

    @Test
    public void should_return_response_when_receipt_parking_space_free() {
        when(parkingSpaceRepository.findById(receiptId)).thenReturn(Optional.of(parkingSpace));
        when(parkingSpace.getReceiptId()).thenReturn(1L);
        when(parkingSpace.getIsUsed()).thenReturn(false);
        Assertions.assertEquals(10014, pickingService.picking(receiptId).getCode());
        Assertions.assertEquals("free parking space and invalid request", pickingService.picking(receiptId).getMessage());

    }

    @Test
    public void should_return_response_when_receipt_is_valid() {
        when(parkingSpaceRepository.findById(receiptId)).thenReturn(Optional.of(parkingSpace));
        when(parkingSpace.getReceiptId()).thenReturn(1L);
        when(parkingSpace.getIsUsed()).thenReturn(true);
        Assertions.assertEquals(10013, pickingService.picking(receiptId).getCode());
        Assertions.assertEquals("pick up car successful", pickingService.picking(receiptId).getMessage());

    }
}
