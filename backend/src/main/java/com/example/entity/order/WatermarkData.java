package com.example.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class WatermarkData {
    Integer id;
    byte[] backgroundImage;
    byte[] modifiedImage;
    String json;
}