-- при запуске сбрасываем статус незавершенных запросов ИНН, их можно будет запросить повторно
update person set inn_state = 'None' where inn_state in ('Queued', 'Queried');
