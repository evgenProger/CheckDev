package ru.job4j.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDTO {

    private int id;

    private int typeInterview;

    private int submitterId;

    private String title;

    private String description;

    private String contactBy;

    private String approximateDate;

    private String createDate;
}
