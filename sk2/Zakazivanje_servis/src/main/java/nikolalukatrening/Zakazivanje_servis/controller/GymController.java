package nikolalukatrening.Zakazivanje_servis.controller;


import nikolalukatrening.Zakazivanje_servis.model.Gym;
import nikolalukatrening.Zakazivanje_servis.model.TrainingTypes;
import nikolalukatrening.Zakazivanje_servis.repository.GymRepository;
import nikolalukatrening.Zakazivanje_servis.repository.TrainingTypesRepository;
import nikolalukatrening.Zakazivanje_servis.service.GymService;
import nikolalukatrening.Zakazivanje_servis.service.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gym")
public class GymController {

    private GymService gymService;
    private GymRepository gymRepository;

    public GymController(GymService gymService, GymRepository gymRepository) {
        this.gymService = gymService;
        this.gymRepository = gymRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<Set<String>> getAllGyms() {
        Set<String> trainingTypes = gymRepository.findAll()
                .stream()
                .map(Gym::getName) // Assuming you have a getName method
                .collect(Collectors.toSet());
        return ResponseEntity.ok(trainingTypes);
    }


    // saljem gymId zelim da dobijem gymName
    @GetMapping("/{gymId}")
    public ResponseEntity<String> getGymNameById(@PathVariable Long gymId) {
        Gym gym = gymRepository.findById(gymId).get();
        String gymName = gym.getName();
        System.out.println("gymName: " + gymName);
        return ResponseEntity.ok(gymName);
    }

}

