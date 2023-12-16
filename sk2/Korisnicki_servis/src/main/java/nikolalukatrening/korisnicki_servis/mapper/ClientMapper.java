package nikolalukatrening.korisnicki_servis.mapper;

import nikolalukatrening.korisnicki_servis.dto.ClientCreateDto;
import nikolalukatrening.korisnicki_servis.dto.ClientDto;
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



    public Client clientCreateDtoToClient(ClientCreateDto clientCreateDto) {
        User user = new User();
        user.setUsername(clientCreateDto.getUsername());
        user.setPassword(clientCreateDto.getPassword());
        user.setEmail(clientCreateDto.getEmail());
        user.setFirstName(clientCreateDto.getFirstName());
        user.setLastName(clientCreateDto.getLastName());
        user.setDateOfBirth(clientCreateDto.getDateOfBirth());

        Client client = new Client();
        client.setUser(user);
        client.setCardNumber(clientCreateDto.getCardNumber());
        client.setReservedTraining(clientCreateDto.getReservedTraining());

        return client;
    }
}
