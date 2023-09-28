package ru.checkdev.mock.domain;

import lombok.*;
import javax.persistence.*;

@Entity(name = "wisher")
@Table(name = "wisher")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Builder(builderMethodName = "of")
@AllArgsConstructor
@NoArgsConstructor
public class Wisher {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    private Interview interview;

    private int userId;

    private String contactBy;

    private boolean approve;
}
