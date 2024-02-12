package com.ncinga.timer.dtos.queryDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private String logical_operator;
    private String field;
    private String condition;
    private String value;

    public void setLogicalOperator(String and) {
    }
}
