package ru.yan0kom.innfl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yan0kom.innfl.model.Person;

@Repository
public interface PersonDao extends JpaRepository<Person, Long> {
}
