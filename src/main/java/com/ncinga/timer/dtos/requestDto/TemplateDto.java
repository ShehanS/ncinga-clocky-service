package com.ncinga.timer.dtos.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TemplateDto {
    private boolean inactive;
    private String name;
    private String id;
}
