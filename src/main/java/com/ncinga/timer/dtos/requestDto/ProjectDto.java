package com.ncinga.timer.dtos.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProjectDto {
    private TemplateDto template;
    private int percentage_completion;
    private DisplayIdDto display_id;
    private String description;
    private String title;
    private String actual_hours;
    private ActualEndTimeDto actual_end_time;
    private String id;
    private ActualStartTimeDto actual_start_time;
    private DepartmentDto department;
    private OwnerDto owner;
    private ProjectTypeDto project_type;
    private CreatedTimeDto created_time;
    private String project_code;
    private PriorityDto priority;
    private CreatedByDto created_by;
    private ScheduledEndTimeDto scheduled_end_time;
    private SiteDto site;
    private Object actual_cost;
    private ProjectRequestDto project_requester;
    private String estimated_cost;
    private String estimated_hours;
    private UdfFieldsDto udf_fields;
    private ProjectedEndDto projected_end;
    private StatusDto status;
    private ScheduledStartTimeDto scheduled_start_time;
}
