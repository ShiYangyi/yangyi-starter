package com.example.yangyistarter.controller;

import com.example.yangyistarter.service.PickingService;
import com.example.yangyistarter.util.ResponseCode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PickingController {
    PickingService pickingService;
    @PostMapping("/cars")
    public ResponseCode picking(@RequestParam Long receiptId) {
        return pickingService.picking(receiptId);
    }
}
