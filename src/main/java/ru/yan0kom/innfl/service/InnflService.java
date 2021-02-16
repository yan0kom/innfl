package ru.yan0kom.innfl.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import ru.yan0kom.innfl.dao.PersonDao;
import ru.yan0kom.innfl.dto.PersonInDto;
import ru.yan0kom.innfl.dto.QueryInnMsg;
import ru.yan0kom.innfl.error.PersonNotFound;
import ru.yan0kom.innfl.model.Person;

import java.util.List;

@Service
public class InnflService extends WebServiceGatewaySupport {
    @Autowired
    private PersonDao personDao;
    @Autowired
    private JmsTemplate jmsTemplate;

    public Person queryInn(Long personId) {
        var person = getPerson(personId);
        jmsTemplate.convertAndSend("inn-queries", new QueryInnMsg(person));
        person.setInnState("Queued");
        person = personDao.save(person);
        return person;
    }

    public Person createPerson(PersonInDto personInDto) {
        return personDao.save(personInDto.toEntity());
    }

    public Person getPerson(Long personId) {
        return personDao.findById(personId).orElseThrow(() -> { throw new PersonNotFound(personId); });
    }

    public List<Person> getPersonList() {
        return personDao.findAll();
    }

    public Person updatePerson(Long id, PersonInDto personDto) {
        if (personDao.existsById(id)) {
            val person = personDto.toEntity();
            person.setId(id);
            return personDao.save(person);
        }
        throw new PersonNotFound(id);
    }

    public void deletePerson(Long personId) {
        if (personDao.existsById(personId)) {
            personDao.deleteById(personId);
        } else {
            throw new PersonNotFound(personId);
        }
    }
}
