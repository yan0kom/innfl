package ru.yan0kom.innfl.dto;

import lombok.*;
import ru.yan0kom.innfl.model.Person;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class QueryInnMsg {
    private Long personId;
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate dateOfBirth;

    public QueryInnMsg(Person person) {
        personId = person.getId();
        firstName = person.getFirstName();
        lastName = person.getLastName();
        patronymic = person.getPatronymic();
        dateOfBirth = person.getDateOfBirth();
    }
}
