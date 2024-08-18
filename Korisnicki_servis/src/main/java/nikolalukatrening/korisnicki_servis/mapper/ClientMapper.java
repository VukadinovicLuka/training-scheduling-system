package nikolalukatrening.korisnicki_servis.mapper;

import nikolalukatrening.korisnicki_servis.dto.*;
import nikolalukatrening.korisnicki_servis.model.Client;
import nikolalukatrening.korisnicki_servis.model.User;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {


    public ClientDto clientToClientDto(Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setUsername(client.getUser().getUsername());
        clientDto.setEmail(client.getUser().getEmail());
        clientDto.setFirstName(client.getUser().getFirstName());
        clientDto.setLastName(client.getUser().getLastName());
        clientDto.setDateOfBirth(client.getUser().getDateOfBirth());
        clientDto.setReservedTraining(client.getReservedTraining());
        return clientDto;
    }

    public ClientAdminDto clientToClientAdminDto(Client client) {
        UserDto user = new UserDto();
        user.setUsername(client.getUser().getUsername());
        user.setEmail(client.getUser().getEmail());
        user.setFirstName(client.getUser().getFirstName());
        user.setLastName(client.getUser().getLastName());
        user.setDateOfBirth(client.getUser().getDateOfBirth());
        user.setRole(client.getUser().getRole());
        user.setPassword(client.getUser().getPassword());


        ClientAdminDto clientDto = new ClientAdminDto();
        clientDto.setId(client.getId());
        clientDto.setReservedTraining(client.getReservedTraining());
        clientDto.setIsActivated(client.getIsActivated());
        clientDto.setCardNumber(client.getCardNumber());
        clientDto.setActivationToken(client.getActivationToken());
        clientDto.setUser(user);

        return clientDto;
    }

    public Client clientCreateDtoToClient(ClientCreateDto clientCreateDto) {
        User user = new User();
        user.setUsername(clientCreateDto.getUsername());
        user.setEmail(clientCreateDto.getEmail());
        user.setFirstName(clientCreateDto.getFirstName());
        user.setLastName(clientCreateDto.getLastName());
        user.setPassword(clientCreateDto.getPassword());
        user.setDateOfBirth(clientCreateDto.getDateOfBirth());
        user.setRole("ROLE_CLIENT");

        Client client = new Client();
        client.setUser(user);
        client.setCardNumber(clientCreateDto.getCardNumber());
        client.setReservedTraining(clientCreateDto.getReservedTraining());

        return client;
    }

    public Client clientUpdateToClient(Client client, ClientUpdateDto clientUpdateDto) {
        User user = new User();
        if(clientUpdateDto.getUsername() != null)
            user.setUsername(clientUpdateDto.getUsername());
        if(clientUpdateDto.getEmail() != null)
            user.setEmail(clientUpdateDto.getEmail());
        user.setFirstName(clientUpdateDto.getFirstName());
        user.setLastName(clientUpdateDto.getLastName());
        client.setUser(user);
        return client;
    }
}
