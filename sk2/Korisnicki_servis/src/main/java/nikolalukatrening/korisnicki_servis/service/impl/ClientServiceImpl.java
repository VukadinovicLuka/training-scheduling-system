package nikolalukatrening.korisnicki_servis.service.impl;

import nikolalukatrening.korisnicki_servis.dto.ClientCreateDto;
import nikolalukatrening.korisnicki_servis.dto.ClientDto;
import nikolalukatrening.korisnicki_servis.dto.EmailMessageDto;
import nikolalukatrening.korisnicki_servis.helper.MessageHelper;
import nikolalukatrening.korisnicki_servis.mapper.ClientMapper;
import nikolalukatrening.korisnicki_servis.model.Client;
import nikolalukatrening.korisnicki_servis.repository.ClientRepository;
import nikolalukatrening.korisnicki_servis.repository.ManagerRepository;
import nikolalukatrening.korisnicki_servis.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private ClientMapper clientMapper;

    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;
    private String activationDestination;


    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper,
                             JmsTemplate jmsTemplate, MessageHelper messageHelper, @Value("${destination.createActivation}") String activationDestination) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.activationDestination = activationDestination;
    }

    @Override
    public ClientDto add(ClientCreateDto clientCreateDto) {
        Client client = clientMapper.clientCreateDtoToClient(clientCreateDto);
        clientRepository.save(client);
        ClientDto clientDto = clientMapper.clientToClientDto(client);

//        EmailMessageDto emailMessage = new EmailMessageDto(clientDto.getEmail(), "Activation Email", "Please activate your account...");
//        jmsTemplate.convertAndSend(activationDestination, messageHelper.createTextMessage(emailMessage));
        createEmailMessageDto(clientDto);
        // createTextMessage konvertuje objekat u JSON string koristeći ObjectMapper
        return clientDto;
    }


    private void createEmailMessageDto(ClientDto clientDto) {
        Map<String, String> params = new HashMap<>();
        params.put("ime", clientDto.getFirstName());
        params.put("prezime", clientDto.getLastName());
        params.put("link", "http://link.za.aktivaciju.com/aktivacija");
        EmailMessageDto emailMessage = new EmailMessageDto(
                clientDto.getEmail(),
                "Activation Email",
                "Pozdrav %ime% %prezime%, da bi ste se verifikovali idite na sledeci %link%",
                "ACTIVATION",
                params
        );
        jmsTemplate.convertAndSend(activationDestination, messageHelper.createTextMessage(emailMessage));
    }
}
