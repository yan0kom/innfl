package ru.yan0kom.innfl.error;

public class PersonNotFound extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PersonNotFound(Long id) {
        super(String.format("Person with id = %d not found", id));
    }
}
