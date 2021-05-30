package ru.yan0kom.innfl.config;

import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import ru.yan0kom.innfl.service.FnsInnFioDrClient;

@Configuration
public class FnsInnFioDrClientConfig {
    @Value("${fns-inn-fio-dr.url}")
    private String fnsInnFioDrUrl;

    @Bean
    public Jaxb2Marshaller marshaller() {
        val marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("ru.yan0kom.fnsinnflfiodr");
        return marshaller;
    }

    @Bean
    public FnsInnFioDrClient fnsInnFioDrClient(Jaxb2Marshaller marshaller) {
        val client = new FnsInnFioDrClient();
        client.setDefaultUri(fnsInnFioDrUrl);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
