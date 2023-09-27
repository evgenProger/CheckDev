package ru.checkdev.mock.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity(name = "interview")
@Table(name = "interview")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Builder(builderMethodName = "of")
@AllArgsConstructor
@NoArgsConstructor
public class Interview {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null")
    private int id;

    @NotNull(message = "Title must be non null")
    private int typeInterview;

    @NotNull(message = "Title must be non null")
    private int submitterId;

    @NotBlank(message = "Title must be not empty")
    private String title;

    @NotBlank(message = "Title must be not empty")
    private String description;

    @NotBlank(message = "Title must be not empty")
    private String contactBy;

    @NotBlank(message = "Title must be not empty")
    private String approximateDate;
}
