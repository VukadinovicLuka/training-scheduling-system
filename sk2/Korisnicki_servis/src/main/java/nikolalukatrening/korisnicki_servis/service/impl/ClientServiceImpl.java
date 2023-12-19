package nikolalukatrening.korisnicki_servis.service.impl;

import nikolalukatrening.Notifikacioni_servis.model.EmailMessage;
import nikolalukatrening.korisnicki_servis.dto.ClientCreateDto;
import nikolalukatrening.korisnicki_servis.dto.ClientDto;
import nikolalukatrening.korisnicki_servis.dto.ClientUpdateDto;
import nikolalukatrening.korisnicki_servis.mapper.ClientMapper;
import nikolalukatrening.korisnicki_servis.model.Client;
import nikolalukatrening.korisnicki_servis.repository.ClientRepository;
import nikolalukatrening.korisnicki_servis.repository.ManagerRepository;
import nikolalukatrening.korisnicki_servis.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private ClientMapper clientMapper;

    @Autowired // ovo znaci da ce Spring da ubaci JmsTemplate u ovaj atribut
    private JmsTemplate jmsTemplate;


    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public ClientDto add(ClientCreateDto clientCreateDto) {
        Client client = clientMapper.clientCreateDtoToClient(clientCreateDto);
        clientRepository.save(client);
        ClientDto clientDto = clientMapper.clientToClientDto(client);

        // Kreirajte poruku koja će biti poslata
        //EmailMessage emailMessage = createEmailMessage(clientDto);

        // Pošaljite poruku u queue
        //jmsTemplate.convertAndSend("activationQueue", emailMessage); //

        return clientDto;
    }

    @Override
    public ClientDto update(ClientUpdateDto clientUpdateDto) {
        Client client = clientRepository.findByUserUsername(clientUpdateDto.getOldUsername()).orElseThrow(()->new RuntimeException());
        client = clientMapper.clientUpdateToClient(client,clientUpdateDto);
        client = clientRepository.save(client);
        return clientMapper.clientToClientDto(client);
    }

    private EmailMessage createEmailMessage(ClientDto clientDto) {
        EmailMessage emailMessage = new EmailMessage();
        // Popunite emailMessage sa potrebnim informacijama
        emailMessage.setTo(clientDto.getEmail());
        emailMessage.setSubject("Activation Email");
        emailMessage.setBody("Thank you for registering. Please activate your account using this link: ..."); // Generišite aktivacioni link
        return emailMessage; // ovo je mail koji ce se poslati klientu
    }
}
