package ru.yan0kom.innfl.dto;

import lombok.Value;
import ru.yan0kom.innfl.model.Person;

import java.time.LocalDate;

@Value
public class PersonInDto {
    String firstName;
    String lastName;
    String patronymic;
    LocalDate dateOfBirth;

    public Person toEntity() {
        return new Person(null, firstName, lastName, patronymic, dateOfBirth, "None", null);
    }
}
