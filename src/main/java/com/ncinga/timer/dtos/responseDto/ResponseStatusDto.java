package com.ncinga.timer.dtos.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStatusDto {
    private int status_code;
    private String status;
}
