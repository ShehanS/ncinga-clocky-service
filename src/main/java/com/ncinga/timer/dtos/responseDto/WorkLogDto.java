package com.ncinga.timer.dtos.responseDto;

import com.ncinga.timer.dtos.EndTimeDto;
import com.ncinga.timer.dtos.RecordedTimeDto;
import com.ncinga.timer.dtos.StartTimeDto;
import com.ncinga.timer.dtos.WorkLogTypeDto;
import com.ncinga.timer.dtos.requestDto.OwnerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class WorkLogDto {
    public OwnerDto owner;
    public boolean include_nonoperational_hours;
    public EndTimeDto end_time;
    public String description;
    public String other_charge;
    public String total_charge;
    public CreatedDateDto created_by;
    public RecordedTimeDto recorded_time;
    public TimeSpentDto time_spent;
    public String tech_charge;
    public StartTimeDto start_time;
    public WorkLogTypeDto worklog_type;
    public String id;
}
