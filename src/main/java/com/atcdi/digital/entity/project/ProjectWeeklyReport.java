package com.atcdi.digital.entity.project;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectWeeklyReport {
    int reportId;
    int projectId;
    LocalDate startDate;
    LocalDate endDate;
    String report;
}
