package ru.yan0kom.innfl.service;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.Nullable;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.w3c.dom.Element;
import ru.yan0kom.fnsinnflfiodr.GetINNFLFIODRResponse;
import ru.yan0kom.fnsinnflfiodr.ObjectFactory;
import ru.yan0kom.fnsinnflfiodr.QueryINNFLFIODRResponse;
import ru.yan0kom.fnsinnflfiodr.get.rs.GetResponseДокумент;
import ru.yan0kom.fnsinnflfiodr.query.rs.QueryResponseДокумент;

import javax.xml.bind.JAXBContext;
import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigInteger;
import java.util.Optional;

@Slf4j
public class FnsInnFioDrClient extends WebServiceGatewaySupport {
    private final static String FORMAT_VERSION = "4.01";
    private ObjectFactory baseObjFactory = new ru.yan0kom.fnsinnflfiodr.ObjectFactory();

    public Optional<QueryResponseДокумент> queryInn(String firstName, String lastName, @Nullable String patronymic, String dateOfBirth) {
        val ofQuery = new ru.yan0kom.fnsinnflfiodr.query.rq.ObjectFactory();

        val params = ofQuery.createQueryRequestДокументСвФЛ();
        params.setИмя(firstName);
        params.setФамилия(lastName);
        if (patronymic != null) {
            params.setОтчество(patronymic);
        } else {
            params.setПризОтч("1");
        }
        params.setДатаРожд(dateOfBirth);

        val request = ofQuery.createQueryRequestДокумент();
        request.setВерсФорм(FORMAT_VERSION);
        request.setСвФЛ(params);

        val appData = baseObjFactory.createAppDataType();
        appData.getAnies().add(jaxbToDomElement(request));

        val msgData = baseObjFactory.createMessageDataType();
        msgData.setAppData(appData);

        val query = baseObjFactory.createQueryINNFLFIODR();
        query.setMessageData(msgData);

        try {
            val response = (QueryINNFLFIODRResponse) getWebServiceTemplate().marshalSendAndReceive(query);
            if (response.getMessageData().getAppData().getAnies().size() == 1) {
                return Optional.of(
                        domElementToJaxb(response.getMessageData().getAppData().getAnies().get(0), QueryResponseДокумент.class));
            }
        } catch (Exception e) {
            log.error("Error sending SOAP request", e);
        }
        return Optional.empty();
    }

    public Optional<GetResponseДокумент> getInn(BigInteger fqid) {
        val ofGet = new ru.yan0kom.fnsinnflfiodr.get.rq.ObjectFactory();

        val params = ofGet.createGetRequestДокумент();
        params.setВерсФорм(FORMAT_VERSION);
        params.setИдЗапросФ(fqid);

        val appData = baseObjFactory.createAppDataType();
        appData.getAnies().add(jaxbToDomElement(params));

        val msgData = baseObjFactory.createMessageDataType();
        msgData.setAppData(appData);

        val get = baseObjFactory.createGetINNFLFIODR();
        get.setMessageData(msgData);

        try {
            val response = (GetINNFLFIODRResponse) getWebServiceTemplate().marshalSendAndReceive(get);
            if (response.getMessageData().getAppData().getAnies().size() == 1) {
                return Optional.of(
                        domElementToJaxb(response.getMessageData().getAppData().getAnies().get(0), GetResponseДокумент.class));
            }
        } catch (Exception e) {
            log.error("Error sending SOAP request", e);
        }
        return Optional.empty();
    }

    private Element jaxbToDomElement(Object jaxb) {
        try {
            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            JAXBContext context = JAXBContext.newInstance(jaxb.getClass().getPackage().getName());
            context.createMarshaller().marshal(jaxb, doc);
            return doc.getDocumentElement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T domElementToJaxb(Element element, Class<T> clazz) {        
        try {
            JAXBContext context = JAXBContext.newInstance(clazz.getPackage().getName());
            return (T) context.createUnmarshaller().unmarshal(element);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
