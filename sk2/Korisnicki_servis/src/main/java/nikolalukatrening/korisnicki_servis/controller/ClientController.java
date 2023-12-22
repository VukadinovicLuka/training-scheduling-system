package nikolalukatrening.korisnicki_servis.controller;

import jakarta.validation.Valid;
import nikolalukatrening.korisnicki_servis.dto.*;
import nikolalukatrening.korisnicki_servis.model.Client;
import nikolalukatrening.korisnicki_servis.repository.ClientRepository;
import nikolalukatrening.korisnicki_servis.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client")

public class ClientController {

    private ClientService clientService;

    private ClientRepository clientRepository;

    public ClientController(ClientService clientService,ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/activate/token")
    public ResponseEntity<String> activateClient(@RequestParam String token) {
        Optional<Client> clientOptional = clientRepository.findByActivationToken(token);

        if (!clientOptional.isPresent()) {
            return new ResponseEntity<>("Token nije važeći ili je istekao.", HttpStatus.BAD_REQUEST);
        }

        Client client = clientOptional.get();
        client.setIsActivated(true);
        client.setActivationToken(null);
        clientRepository.save(client);
        return new ResponseEntity<>("Nalog je uspešno aktiviran.", HttpStatus.OK);
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

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(clientService.login(tokenRequestDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        return clientOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/login/token")
    public ResponseEntity<ClaimResponseDto> getClaimsFromToken(@RequestBody TokenResponseDto tokenResponseDto) {
        return new ResponseEntity<>(clientService.getClaim(tokenResponseDto), HttpStatus.OK);
    }
}
