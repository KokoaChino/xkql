package com.example.entity.other;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class WatermarkData {
    Integer id;
    String username;
    byte[] backgroundImage;
    byte[] previewImage;
    String json;
}