package com.ncinga.timer.dtos.responseDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ncinga.timer.dtos.requestDto.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto {
    private int percentage_completion;
    private Object estimated_effort_hours;
    private String email_before;
    private String description;
    private ProjectDto project;
    private String title;
    private Object marked_technician;
    private boolean overdue;
    private Object additional_cost;
    private ActualEndTimeDto actual_end_time;
    private String id;
    private ActualStartTimeDto actual_start_time;
    private OwnerDto owner;
    private String associated_entity;
    private String module;
    private int index;
    private PriorityDto priority;
    private CreatedByDto created_by;
    private ScheduledEndTimeDto scheduled_end_time;
    private Object marked_group;
    private SiteDto site;
    private Object estimated_effort_minutes;
    private boolean deleted;
    private Object estimated_effort;
    private CreatedDateDto created_date;
    private Object estimated_effort_days;
    private Object task_type;
    private ScheduledStartTimeDto scheduled_start_time;
    private StatusDto status;
}
