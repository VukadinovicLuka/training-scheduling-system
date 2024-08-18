package nikolalukatrening.korisnicki_servis.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import nikolalukatrening.korisnicki_servis.dto.*;
import nikolalukatrening.korisnicki_servis.exception.NotFoundException;
import nikolalukatrening.korisnicki_servis.helper.MessageHelper;
import nikolalukatrening.korisnicki_servis.mapper.ClientMapper;
import nikolalukatrening.korisnicki_servis.model.Admin;
import nikolalukatrening.korisnicki_servis.model.Client;
import nikolalukatrening.korisnicki_servis.model.Manager;
import nikolalukatrening.korisnicki_servis.model.User;
import nikolalukatrening.korisnicki_servis.repository.AdminRepository;
import nikolalukatrening.korisnicki_servis.repository.ClientRepository;
import nikolalukatrening.korisnicki_servis.repository.ManagerRepository;
import nikolalukatrening.korisnicki_servis.security.service.TokenService;
import nikolalukatrening.korisnicki_servis.service.ClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private AdminRepository adminRepository;
    private ManagerRepository managerRepository;
    private ClientMapper clientMapper;

    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;
    private String activationDestination;
    private TokenService tokenService;


    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper,
                             JmsTemplate jmsTemplate, MessageHelper messageHelper, @Value("${destination.createActivation}") String activationDestination,
                             TokenService tokenService, AdminRepository adminRepository, ManagerRepository managerRepository) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.activationDestination = activationDestination;
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    public ClientDto add(ClientCreateDto clientCreateDto) {
        Client client = clientMapper.clientCreateDtoToClient(clientCreateDto);
        client.setIsActivated(false);
        client.setActivationToken(UUID.randomUUID().toString());
        clientRepository.save(client);
        ClientDto clientDto = clientMapper.clientToClientDto(client);

        createEmailMessageDto(clientDto,client.getActivationToken());
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
//        Client client = clientRepository
//                .findByUser_UsernameAndUser_Password(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
//                .orElseThrow(() -> new NotFoundException(String.format("User with username: %s and password: %s not found.", tokenRequestDto.getUsername(),
//                                tokenRequestDto.getPassword())));
        Claims claims = Jwts.claims();
        if (clientRepository.findByUserUsername(tokenRequestDto.getUsername()).isPresent()){
            Client client;
            client = clientRepository.findByUserUsername(tokenRequestDto.getUsername()).get();
            //Create token payload
            claims.put("id", client.getId());
            claims.put("role", client.getUser().getRole());
        }else if(adminRepository.findByUserUsername(tokenRequestDto.getUsername()).isPresent()){
            Admin admin;
            admin = adminRepository.findByUserUsername(tokenRequestDto.getUsername()).get();
            claims.put("id", admin.getId());
            claims.put("role", admin.getUser().getRole());
        } else if(managerRepository.findByUserUsername(tokenRequestDto.getUsername()).isPresent()){
            Manager manager;
            manager = managerRepository.findByUserUsername(tokenRequestDto.getUsername()).get();
            claims.put("id", manager.getId());
            claims.put("role", manager.getUser().getRole());
        } else {
            throw new NotFoundException(String.format("User with username: %s and password: %s not found.", tokenRequestDto.getUsername(),
                                tokenRequestDto.getPassword()));

        }

        //Generate token
        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public ClaimResponseDto getClaim(TokenResponseDto tokenResponseDto) {
        Claims claims = tokenService.parseToken(tokenResponseDto.getToken());

        return new ClaimResponseDto(claims.get("id", Integer.class), claims.get("role", String.class));
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public ClientAdminDto updateClientActivation(ClientAdminDto clientAdminDto) {
        Client client = clientRepository.findByUserUsername(clientAdminDto.getUser().getUsername()).orElseThrow(()->new RuntimeException());
        if (client.getIsActivated() == true){
            client.setIsActivated(false);
        }else{
            client.setIsActivated(true);
        }
        clientRepository.save(client);
        return clientMapper.clientToClientAdminDto(client);
    }

    @Override
    public ClientAdminDto updateClientById(Long id, ClientAdminDto clientAdminDto) {
        //  clientRepository.findById(id)
        Client client = clientRepository.findById(id).orElseThrow(()->new RuntimeException());
        client.getUser().setUsername(clientAdminDto.getUser().getUsername());

        // ukoliko je stara sifra ista kao i nova, ne menjaj je, ukoliko je razlicita onda salji mail
        if (client.getUser().getPassword().equals(clientAdminDto.getUser().getPassword())) {
            client.getUser().setPassword(clientAdminDto.getUser().getPassword());
        }else{
            client.getUser().setPassword(clientAdminDto.getUser().getPassword());
            createPasswordEmailMessageDto(clientAdminDto);
        }

        client.getUser().setRole(clientAdminDto.getUser().getRole());
        client.getUser().setFirstName(clientAdminDto.getUser().getFirstName());
        client.getUser().setLastName(clientAdminDto.getUser().getLastName());
        client.getUser().setEmail(clientAdminDto.getUser().getEmail());
        client.setActivationToken(clientAdminDto.getActivationToken());
        client.setIsActivated(clientAdminDto.getIsActivated());
        client.setCardNumber(clientAdminDto.getCardNumber());
        client.setReservedTraining(clientAdminDto.getReservedTraining());
        client.setId(id);

        clientRepository.save(client);
        return clientMapper.clientToClientAdminDto(client);
    }

    private void createEmailMessageDto(ClientDto clientDto,String activationToken) {
        Map<String, String> params = new HashMap<>();
        params.put("ime", clientDto.getFirstName());
        params.put("prezime", clientDto.getLastName());
        params.put("clientId", clientDto.getId().toString());
        params.put("link", "http://localhost:8080/api/swagger-ui/index.html");
        EmailMessageDto emailMessage = new EmailMessageDto(
                clientDto.getEmail(),
                "Activation Email",
                "Pozdrav," + params.get("ime") + " " + params.get("prezime") + ",da biste nastavili verifikaciju idite na link: "
                        + params.get("link") + "\n" + "Uputstvo za verifikaciju: klikom na client/activate/token, izaci ce Vam dugme try it out. Klikente na to" +
                        "dugme i unesete token koji se nalazi u nastavku." + "\n" + "Token: " + activationToken ,
                "ACTIVATION",
                params
        );
        jmsTemplate.convertAndSend(activationDestination, messageHelper.createTextMessage(emailMessage));
    }

    private void createPasswordEmailMessageDto(ClientAdminDto clientAdminDto) {
        Map<String, String> params = new HashMap<>();
        params.put("ime", clientAdminDto.getUser().getFirstName());
        params.put("prezime", clientAdminDto.getUser().getLastName());
        params.put("password", clientAdminDto.getUser().getPassword());
        params.put("clientId", clientAdminDto.getId().toString());
        EmailMessageDto emailMessage = new EmailMessageDto(
                clientAdminDto.getUser().getEmail(),
                "New password Email",
                "Pozdrav," + params.get("ime") + " " + params.get("prezime") + ",vasa nova lozinka je: " + "\n"
                        + params.get("password"),
                "PASSWORD",
                params
        );
        jmsTemplate.convertAndSend(activationDestination, messageHelper.createTextMessage(emailMessage));
    }

}
