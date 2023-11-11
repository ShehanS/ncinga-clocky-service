package com.ncinga.timer.dtos.responseDto;

import com.ncinga.timer.dtos.requestDto.OwnerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerResponseDto {
    private List<OwnerDto> owner;
}
