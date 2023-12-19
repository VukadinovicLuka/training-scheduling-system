package nikolalukatrening.korisnicki_servis.controller;

import jakarta.validation.Valid;
import nikolalukatrening.korisnicki_servis.dto.ClientCreateDto;
import nikolalukatrening.korisnicki_servis.dto.ClientDto;
import nikolalukatrening.korisnicki_servis.dto.ClientUpdateDto;
import nikolalukatrening.korisnicki_servis.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client")

public class ClientController {

    private ClientService clientService;


    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @PostMapping("/register")
    public ResponseEntity<ClientDto> registerClient(@RequestBody @Valid ClientCreateDto clientCreateDto) {
        // Logika za slanje zahteva ka notifikacionom servisu preko message brokera
        return new ResponseEntity<>(clientService.add(clientCreateDto), HttpStatus.CREATED);
    }

    @PostMapping("/registerMultiple")
    public ResponseEntity<List<ClientDto>> registerMultipleClients(@RequestBody @Valid List<ClientCreateDto> clientCreateDtoList) {
        List<ClientDto> savedClients = clientCreateDtoList.stream()
                .map(clientCreateDto -> clientService.add(clientCreateDto))
                .collect(Collectors.toList());
        return new ResponseEntity<>(savedClients, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ClientDto> updateClient(@RequestBody @Valid ClientUpdateDto clientUpdateDto){
        return new ResponseEntity<>(clientService.update(clientUpdateDto), HttpStatus.OK);
    }
}
