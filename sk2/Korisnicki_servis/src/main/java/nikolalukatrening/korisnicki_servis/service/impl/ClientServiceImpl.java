package nikolalukatrening.korisnicki_servis.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import nikolalukatrening.korisnicki_servis.dto.*;
import nikolalukatrening.korisnicki_servis.exception.NotFoundException;
import nikolalukatrening.korisnicki_servis.helper.MessageHelper;
import nikolalukatrening.korisnicki_servis.mapper.ClientMapper;
import nikolalukatrening.korisnicki_servis.model.Client;
import nikolalukatrening.korisnicki_servis.model.User;
import nikolalukatrening.korisnicki_servis.repository.ClientRepository;
import nikolalukatrening.korisnicki_servis.security.service.TokenService;
import nikolalukatrening.korisnicki_servis.service.ClientService;
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
    private TokenService tokenService;


    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper,
                             JmsTemplate jmsTemplate, MessageHelper messageHelper, @Value("${destination.createActivation}") String activationDestination, TokenService tokenService) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.activationDestination = activationDestination;
        this.tokenService = tokenService;
    }

    @Override
    public ClientDto add(ClientCreateDto clientCreateDto) {
        Client client = clientMapper.clientCreateDtoToClient(clientCreateDto);
        ClientDto clientDto = clientMapper.clientToClientDto(client);
        createEmailMessageDto(clientDto);
        clientRepository.save(client);

//        EmailMessageDto emailMessage = new EmailMessageDto(clientDto.getEmail(), "Activation Email", "Please activate your account...");
//        jmsTemplate.convertAndSend(activationDestination, messageHelper.createTextMessage(emailMessage));
        // createTextMessage konvertuje objekat u JSON string koristeći ObjectMapper
        return clientDto;
    }

    @Override
    public ClientDto update(ClientUpdateDto clientUpdateDto) {
        Client client = clientRepository.findByUserUsername(clientUpdateDto.getOldUsername()).orElseThrow(()->new RuntimeException());
        client = clientMapper.clientUpdateToClient(client,clientUpdateDto);
        client = clientRepository.save(client);
        return clientMapper.clientToClientDto(client);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        //Try to find active user for specified credentials
        Client client = clientRepository
                .findByUser_UsernameAndUser_Password(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String.format("User with username: %s and password: %s not found.", tokenRequestDto.getUsername(),
                                tokenRequestDto.getPassword())));
        //Create token payload
        Claims claims = Jwts.claims();
        claims.put("id", client.getId());
        claims.put("role", client.getUser().getRole());
        //Generate token
        return new TokenResponseDto(tokenService.generate(claims));
    }

    private void createEmailMessageDto(ClientDto clientDto) {
        Map<String, String> params = new HashMap<>();
        params.put("ime", clientDto.getFirstName());
        params.put("prezime", clientDto.getLastName());
        params.put("link", "http://link.za.aktivaciju.com/aktivacija");
        EmailMessageDto emailMessage = new EmailMessageDto(
                clientDto.getEmail(),
                "Activation Email",
                "Pozdrav," + params.get("ime") + " " + params.get("prezime") + ",da biste nastavili verifikaciju idite na link:"
                 + params.get("link"),
                "ACTIVATION",
                params
        );
//        jmsTemplate.convertAndSend(activationDestination, messageHelper.createTextMessage(emailMessage));
    }

}
