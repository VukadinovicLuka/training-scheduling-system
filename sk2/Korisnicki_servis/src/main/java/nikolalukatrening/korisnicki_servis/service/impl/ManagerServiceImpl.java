package nikolalukatrening.korisnicki_servis.service.impl;

import nikolalukatrening.korisnicki_servis.repository.ManagerRepository;
import nikolalukatrening.korisnicki_servis.service.ManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ManagerServiceImpl implements ManagerService {

    private ManagerRepository managerRepository;

    public ManagerServiceImpl(ManagerRepository managerRepository){
        this.managerRepository = managerRepository;
    }

}
