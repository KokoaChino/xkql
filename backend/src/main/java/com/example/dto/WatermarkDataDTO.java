package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatermarkDataDTO {
    Integer id;
    byte[] backgroundImage;
    byte[] modifiedImage;
    WatermarkParamsDTO params;
}