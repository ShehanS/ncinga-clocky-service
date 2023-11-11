package com.ncinga.timer.dtos.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class SiteDto {
    private String id;
    private boolean deleted;
    private String name;
    private boolean is_default;
}
