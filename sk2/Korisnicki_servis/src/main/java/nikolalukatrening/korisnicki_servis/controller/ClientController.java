package nikolalukatrening.korisnicki_servis.controller;

import jakarta.validation.Valid;
import nikolalukatrening.korisnicki_servis.dto.ClientCreateDto;
import nikolalukatrening.korisnicki_servis.dto.ClientDto;
import nikolalukatrening.korisnicki_servis.model.Client;
import nikolalukatrening.korisnicki_servis.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")

public class ClientController {

    private ClientService clientService;


    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    // klijenti imaju zasebne rute za registraciju
    @PostMapping("/register")
    public ResponseEntity<ClientDto> registerClient(@RequestBody @Valid ClientCreateDto clientCreateDto) {

        // Logika za slanje zahteva ka notifikacionom servisu preko message brokera
        return new ResponseEntity<>(clientService.add(clientCreateDto), HttpStatus.CREATED);
    }




}
