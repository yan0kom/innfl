package ru.yan0kom.innfl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yan0kom.innfl.dto.PersonInDto;
import ru.yan0kom.innfl.model.Person;
import ru.yan0kom.innfl.service.InnflService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InnflController {
    @Autowired
    private InnflService innflService;

    @PostMapping("person")
    public ResponseEntity<Person> createPerson(@RequestBody PersonInDto personInDto) {
        return new ResponseEntity<>(innflService.createPerson(personInDto), HttpStatus.CREATED);
    }

    @GetMapping("person/{id}")
    public Person getPerson(@PathVariable Long id) {
        return innflService.getPerson(id);
    }

    @GetMapping("persons")
    public List<Person> listPersons() {
        return innflService.getPersonList();
    }

    @PutMapping("person/{id}")
    public Person updatePerson(@PathVariable Long id, @RequestBody PersonInDto personInDto) {
        return innflService.updatePerson(id, personInDto);
    }

    @DeleteMapping("person/{id}")
    public Long deletePerson(@PathVariable Long id) {
        innflService.deletePerson(id);
        return id;
    }

    @PostMapping("person/{id}/queryInn")
    public ResponseEntity<Person> queryInn(@PathVariable Long id) {
        return new ResponseEntity<>(innflService.queryInn(id), HttpStatus.ACCEPTED);
    }
}
