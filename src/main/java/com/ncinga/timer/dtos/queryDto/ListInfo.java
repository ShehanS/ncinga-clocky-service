package com.ncinga.timer.dtos.queryDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListInfo {
    private int row_count;
    private int start_index;
    private List<SearchCriteria> search_criteria;
}
