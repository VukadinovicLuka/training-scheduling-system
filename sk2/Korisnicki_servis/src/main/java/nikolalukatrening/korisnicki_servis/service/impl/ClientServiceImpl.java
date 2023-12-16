package nikolalukatrening.korisnicki_servis.service.impl;

import nikolalukatrening.korisnicki_servis.repository.ClientRepository;
import nikolalukatrening.korisnicki_servis.repository.ManagerRepository;
import nikolalukatrening.korisnicki_servis.service.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

}
