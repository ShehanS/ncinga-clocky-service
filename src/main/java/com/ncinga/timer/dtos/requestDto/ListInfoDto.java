package com.ncinga.timer.dtos.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListInfoDto {
    private boolean has_more_rows;
    private int start_index;
    private int page;
    private int row_count;
}
