package com.ncinga.timer.dtos.requestDto;

import com.ncinga.timer.dtos.EndTimeDto;
import com.ncinga.timer.dtos.RecordedTimeDto;
import com.ncinga.timer.dtos.StartTimeDto;
import com.ncinga.timer.dtos.WorkLogTypeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AddEditWorklogDto {
    public SimpleOwnerDto owner;
    public boolean include_nonoperational_hours;
    public EndTimeDto end_time;
    public String description;
    public double other_charge;
    public RecordedTimeDto recorded_time;
    public double tech_charge;
    public boolean mark_first_response;
    public StartTimeDto start_time;
    public SimpleWorkLogTypeDto worklog_type;
}
