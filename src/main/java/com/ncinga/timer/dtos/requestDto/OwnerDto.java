package com.ncinga.timer.dtos.requestDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OwnerDto {
    private String email_id;
    private boolean is_technician;
    private Object sms_mail;
    private String mobile;
    private String last_name;
    private String user_scope;
    private Object sms_mail_id;
    private String cost_per_hour;
    private SiteDto site;
    private String phone;
    private String employee_id;
    private String name;
    private String id;
    private String photo_url;
    private boolean is_vip_user;
    private DepartmentDto department;
    private String first_name;
    private String job_title;
}
