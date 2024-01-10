package nikolalukatrening.korisnicki_servis.mapper;

import nikolalukatrening.korisnicki_servis.dto.ClientCreateDto;
import nikolalukatrening.korisnicki_servis.dto.ClientDto;
import nikolalukatrening.korisnicki_servis.dto.ManagerCreateDto;
import nikolalukatrening.korisnicki_servis.dto.ManagerDto;
import nikolalukatrening.korisnicki_servis.model.Client;
import nikolalukatrening.korisnicki_servis.model.Manager;
import nikolalukatrening.korisnicki_servis.model.User;
import org.springframework.stereotype.Component;

@Component
public class ManagerMapper {

    public ManagerDto managerToManagerDto(Manager manager) {

        ManagerDto managerDto = new ManagerDto();
        managerDto.setId(manager.getId());
        managerDto.setUsername(manager.getUser().getUsername());
        managerDto.setEmail(manager.getUser().getEmail());
        managerDto.setFirstName(manager.getUser().getFirstName());
        managerDto.setLastName(manager.getUser().getLastName());
        managerDto.setDateOfBirth(manager.getUser().getDateOfBirth());
        managerDto.setGymName(manager.getGymName());
        managerDto.setDateOfHiring(manager.getDateOfHiring());

        return managerDto;
    }

    public Manager managerCreateDtoToManager(ManagerCreateDto managerCreateDto) {
        User user = new User();
        user.setUsername(managerCreateDto.getUsername());
        user.setPassword(managerCreateDto.getPassword());
        user.setEmail(managerCreateDto.getEmail());
        user.setFirstName(managerCreateDto.getFirstName());
        user.setLastName(managerCreateDto.getLastName());
        user.setDateOfBirth(managerCreateDto.getDateOfBirth());
        user.setRole("ROLE_MANAGER");

        Manager manager = new Manager();
        manager.setUser(user);
        manager.setDateOfHiring(managerCreateDto.getDateOfHiring());
        manager.setGymName(managerCreateDto.getGymName());

        return manager;
    }

}
