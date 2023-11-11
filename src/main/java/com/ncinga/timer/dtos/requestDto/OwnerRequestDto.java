package com.ncinga.timer.dtos.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerRequestDto {
    private ListInfoDto list_info;
    private List<OwnerDto> owner;
}
