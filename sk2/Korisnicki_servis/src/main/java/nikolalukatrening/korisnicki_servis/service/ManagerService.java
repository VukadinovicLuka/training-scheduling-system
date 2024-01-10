package nikolalukatrening.korisnicki_servis.service;

import nikolalukatrening.korisnicki_servis.dto.ClientAdminDto;
import nikolalukatrening.korisnicki_servis.dto.ManagerCreateDto;
import nikolalukatrening.korisnicki_servis.dto.ManagerDto;
import nikolalukatrening.korisnicki_servis.model.Client;
import nikolalukatrening.korisnicki_servis.model.Manager;

import java.util.List;

public interface ManagerService {
    
    ManagerDto add(ManagerCreateDto managerCreateDto);
    List<Manager> getAllManagers();
    ManagerDto updateManagerById(Long id, ManagerDto managerDto);
    
}
