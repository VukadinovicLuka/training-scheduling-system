package nikolalukatrening.korisnicki_servis.controller;

import jakarta.validation.Valid;
import nikolalukatrening.korisnicki_servis.dto.*;
import nikolalukatrening.korisnicki_servis.model.Client;
import nikolalukatrening.korisnicki_servis.model.Manager;
import nikolalukatrening.korisnicki_servis.repository.ManagerRepository;
import nikolalukatrening.korisnicki_servis.service.ClientService;
import nikolalukatrening.korisnicki_servis.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    private ManagerService managerService;
    private ManagerRepository managerRepository;


    public ManagerController(ManagerService managerService, ManagerRepository managerRepository) {
        this.managerService = managerService;
        this.managerRepository = managerRepository;
    }

    // klijenti imaju zasebne rute za registraciju
    @PostMapping("/register")
    public ResponseEntity<ManagerDto> registerManager(@RequestBody @Valid ManagerCreateDto managerCreateDto) {
        // Logika za slanje zahteva ka notifikacionom servisu preko message brokera
        return new ResponseEntity<>(managerService.add(managerCreateDto), HttpStatus.CREATED);
    }

    @PostMapping("/registerMultiple")
    public ResponseEntity<List<ManagerDto>> registerMultipleManagers(@RequestBody @Valid List<ManagerCreateDto> managerCreateDtoList) {
        List<ManagerDto> savedManagers = managerCreateDtoList.stream()
                .map(managerCreateDto -> managerService.add(managerCreateDto))
                .collect(Collectors.toList());
        return new ResponseEntity<>(savedManagers, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Manager>> getAllManagers() {
        return new ResponseEntity<>(managerService.getAllManagers(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getManagerById(@PathVariable Long id) {
        Optional<Manager> managerOptional = managerRepository.findById(id);
        return managerOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ManagerDto> updateManagerGymName(@PathVariable Long id, @RequestBody  ManagerDto managerDto){
        return new ResponseEntity<>(managerService.updateManagerById(id, managerDto), HttpStatus.OK);
    }


//    @PutMapping("/{id}")
//    public ResponseEntity<ClientAdminDto> updateClientById(@PathVariable Long id, @RequestBody @Valid ClientAdminDto clientAdminDto){
//        return new ResponseEntity<>(clientService.updateClientById(id, clientAdminDto), HttpStatus.OK);
//    }



}
