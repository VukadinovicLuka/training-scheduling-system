package nikolalukatrening.korisnicki_servis.service.impl;

import nikolalukatrening.korisnicki_servis.repository.AdminRepository;
import nikolalukatrening.korisnicki_servis.repository.ManagerRepository;
import nikolalukatrening.korisnicki_servis.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//  Implementacija UserService interfejsa.
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

}
