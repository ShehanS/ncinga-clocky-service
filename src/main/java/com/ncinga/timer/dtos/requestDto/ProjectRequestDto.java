package com.ncinga.timer.dtos.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDto {
    public String email_id;
    public boolean is_technician;
    public Object sms_mail;
    public String mobile;
    public String last_name;
    public String user_scope;
    public Object sms_mail_id;
    public String cost_per_hour;
    public SiteDto site;
    public String phone;
    public String employee_id;
    public String name;
    public String id;
    public String photo_url;
    public boolean is_vip_user;
    public DepartmentDto department;
    public String first_name;
    public String job_title;
}
