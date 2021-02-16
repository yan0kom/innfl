package ru.yan0kom.innfl.service;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import ru.yan0kom.innfl.dao.PersonDao;
import ru.yan0kom.innfl.dto.CheckInnQueryMsg;
import ru.yan0kom.innfl.dto.QueryInnMsg;

@Slf4j
@Component
public class JmsListeners {
    @Autowired
    private FnsInnFioDrClient fnsInnFioDrClient;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private JmsTemplate jmsTemplateWithDeliveryDelay;

    @JmsListener(destination = "inn-queries")
    @SendTo("inn-query-checks")
    public CheckInnQueryMsg receiveQeuryInnMsg(QueryInnMsg msg) {
        log.debug("Received {}", msg);

        val qrOpt = fnsInnFioDrClient
                .queryInn(msg.getFirstName(), msg.getLastName(), msg.getPatronymic(), msg.getPatronymic());
        if (qrOpt.isPresent() && qrOpt.get().getКодОбр() == null) {
            val personOpt = personDao.findById(msg.getPersonId());
            if (personOpt.isPresent()) {
                val person = personOpt.get();
                person.setInn(null);
                person.setInnState("Queried");
                personDao.save(person);
                return new CheckInnQueryMsg(msg.getPersonId(), qrOpt.get().getИдЗапросФ());
            }
        }
        return null;
    }

    @JmsListener(destination = "inn-query-checks")
    public void receiveQeuryInnMsg(CheckInnQueryMsg msg) {
        log.debug("Received {}", msg);
        val grOpt = fnsInnFioDrClient.getInn(msg.getFnsQueryId());
        val personOpt = personDao.findById(msg.getPersonId());
        grOpt.ifPresentOrElse((gr) -> {
            switch (gr.getКодОбр() == null ? "null" : gr.getКодОбр()) {
                case "01":
                    personOpt.ifPresent((person) -> {
                        person.setInnState("NotFound");
                        personDao.save(person);
                    });
                    break;
                case "52":
                    jmsTemplateWithDeliveryDelay.convertAndSend("inn-query-checks", msg);
                    break;
                case "83":
                    log.info("КодОбр: 83 - Отсутствует запрос с идентификатором {}", msg.getFnsQueryId());
                    personOpt.ifPresent((person) -> {
                        person.setInnState("None");
                        personDao.save(person);
                    });
                    break;
                case "99":
                    log.info("КодОбр: 99 - Системная ошибка");
                    personOpt.ifPresent((person) -> {
                        person.setInnState("None");
                        personDao.save(person);
                    });
                    break;
                case "null":
                    personOpt.ifPresent((person) -> {
                        person.setInnState("Fetched");
                        person.setInn(gr.getИННФЛ());
                        personDao.save(person);
                    });
                    break;
                default:
                    log.warn("Unexpected value of КодОбр: {}", gr.getКодОбр());
            }
        }, () -> {
            personOpt.ifPresent((person) -> {
                person.setInnState("None");
                personDao.save(person);
            });
        });
    }

}
