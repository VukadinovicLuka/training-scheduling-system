package nikolalukatrening.korisnicki_servis.service;

import nikolalukatrening.korisnicki_servis.dto.ManagerCreateDto;
import nikolalukatrening.korisnicki_servis.dto.ManagerDto;

public interface ManagerService {
    
    ManagerDto add(ManagerCreateDto managerCreateDto);
    
}
