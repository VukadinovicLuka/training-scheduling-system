package nikolalukatrening.korisnicki_servis.controller;

import jakarta.validation.Valid;
import nikolalukatrening.korisnicki_servis.dto.ClientCreateDto;
import nikolalukatrening.korisnicki_servis.dto.ClientDto;
import nikolalukatrening.korisnicki_servis.dto.ManagerCreateDto;
import nikolalukatrening.korisnicki_servis.dto.ManagerDto;
import nikolalukatrening.korisnicki_servis.service.ClientService;
import nikolalukatrening.korisnicki_servis.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    private ManagerService managerService;


    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
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
}
