package nikolalukatrening.Zakazivanje_servis.controller;


import nikolalukatrening.Zakazivanje_servis.model.Gym;
import nikolalukatrening.Zakazivanje_servis.model.Training;
import nikolalukatrening.Zakazivanje_servis.model.TrainingTypes;
import nikolalukatrening.Zakazivanje_servis.repository.GymRepository;
import nikolalukatrening.Zakazivanje_servis.repository.TrainingTypesRepository;
import nikolalukatrening.Zakazivanje_servis.service.GymService;
import nikolalukatrening.Zakazivanje_servis.service.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
    public ResponseEntity<Map<String, String>> getGymNameById(@PathVariable Long gymId) {
        Gym gym = gymRepository.findById(gymId).get();
        String gymName = gym.getName();
        Map<String, String> response = new HashMap<>();
        response.put("name", gymName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/name/{gymName}")
    public ResponseEntity<Gym> getGymByGymName(@PathVariable String gymName) {
        Optional<Gym> gym =  gymRepository.findByName(gymName);
        return ResponseEntity.ok(gym.get());
    }

    // na osnovu gymName zelim da pronadjem gym i update-ujem mu description i broj personalnih trenera
    @PutMapping("/update/{gymName}")
    public ResponseEntity<Gym> updateGym(@PathVariable String gymName, @RequestBody Gym gym) {
        Optional<Gym> gymToUpdate = gymRepository.findByName(gymName);
        gymToUpdate.get().setName(gym.getName());
        gymToUpdate.get().setDescription(gym.getDescription());
        gymToUpdate.get().setPersonalTrainerNumbers(gym.getPersonalTrainerNumbers());
        gymRepository.save(gymToUpdate.get());
        return ResponseEntity.ok(gymToUpdate.get());
    }

}

