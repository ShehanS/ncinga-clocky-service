package com.ncinga.timer.dtos.requestDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ncinga.timer.dtos.queryDto.SearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDTO {
    public static final Logger logger = LoggerFactory.getLogger(TaskDTO.class);

    private List<ResponseStatus> responseStatus;
    private ListInfo listInfo;
    private List<Task> tasks;

    @Data
    public static class ResponseStatus {
        private int statusCode;
        private String status;
    }

    @Data
    public static class ListInfo {
        private int rowCount;
        private List<SearchCriteria> searchCriteria;

        public ListInfo(List<SearchCriteria> searchCriteria, int rowCount) {
            this.searchCriteria = searchCriteria;
            this.rowCount = rowCount;
        }

        public ListInfo() {

        }
    }


    @Data
    public static class Task {
        private String marked_group;
        private int index;
        private int percentage_completion;
        private String display_id;
        private String percentageCompletion;
        private String estimated_effort_hours;
        private String email_before;
        private String description;
        private String title;
        private MarkedTechnician marked_technician;
        private boolean overdue;
        private String additional_cost;
        private ActualEndTime actual_end_time;
        private String id;
        private ActualStartTime actual_start_time;
        private Owner owner;
        private String module;
        private Priority priority;
        private CreatedBy created_by;
        private ScheduledEndTime scheduled_end_time;
        private String estimated_effort_minutes;
        private String estimated_effort;
        private CreatedDate created_date;
        private String estimated_effort_days;
        private TaskType task_type;
        private ScheduledStartTime scheduled_start_time;
        private Status status;
        private Project project;
        private String associated_entity;
        private Site site;
        private boolean deleted;
    }

    @Data
    public static class MarkedTechnician {
        private String email_id;
        private boolean is_technician;
        private String smsMail;
        private String phone;
        private String name;
        private String mobile;
        private String id;
        private String photoUrl;
        private boolean isVipUser;
        private Object department;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ActualEndTime {
        private String displayValue;
        private String value;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ActualStartTime {
        private String displayValue;
        private String value;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Owner {
        private String email_id;
        private boolean is_technician;
        private String smsMail;
        private String phone;
        private String name;
        private String mobile;
        private String id;
        private String photoUrl;
        private boolean isVipUser;
        private Object department;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Priority {
        private String color;
        private String name;
        private String id;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreatedBy {
        private String email_id;
        private boolean is_technician;
        private String smsMail;
        private String phone;
        private String name;
        private String mobile;
        private String id;
        private String photoUrl;
        private boolean isVipUser;
        private Object department;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ScheduledEndTime {
        private String displayValue;
        private String value;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreatedDate {
        private String displayValue;
        private String value;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaskType {
        private String color;
        private String name;
        private String id;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ScheduledStartTime {
        private String displayValue;
        private String value;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status {
        private boolean inProgress;
        private String internalName;
        private boolean stopTimer;
        private String color;
        private String name;
        private String id;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Project {
        private String id;
        private Object display_id;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Site {
        private String name;
        private String location;
        private boolean deleted;
        private String id;
        private boolean is_default;
    }
}
