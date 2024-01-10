package nikolalukatrening.Zakazivanje_servis.controller;

import nikolalukatrening.Zakazivanje_servis.model.TrainingTypes;
import nikolalukatrening.Zakazivanje_servis.repository.TrainingTypesRepository;
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
@RequestMapping("/trainingType")
public class TrainingTypeController {

    private TrainingService trainingService;
    private TrainingTypesRepository trainingTypesRepository;

    public TrainingTypeController(TrainingService trainingService, TrainingTypesRepository trainingTypesRepository) {
        this.trainingService = trainingService;
        this.trainingTypesRepository = trainingTypesRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<Set<String>> getAllTrainingTypes() {
        Set<String> trainingTypes = trainingTypesRepository.findAll()
                .stream()
                .map(TrainingTypes::getTrainingSort) // Assuming you have a getName method
                .collect(Collectors.toSet());
        return ResponseEntity.ok(trainingTypes);
    }
    @GetMapping("/allTypes")
    public ResponseEntity<List<String>> getAllTypes() {
        List<String> trainingTypes = trainingTypesRepository.findAll()
                .stream()
                .map(TrainingTypes::getTrainingType) // Assuming you have a getName method
                .collect(Collectors.toList());
        return ResponseEntity.ok(trainingTypes);
    }
    
    @GetMapping("/by-category/{category}")
    public ResponseEntity<Set<String>> getTrainingTypesByCategory(@PathVariable String category) {
        Set<String> trainingTypes = trainingTypesRepository.findByTrainingSort(category)
                .stream()
                .map(TrainingTypes::getTrainingType)
                .collect(Collectors.toSet());
        System.out.println(trainingTypes);
        return ResponseEntity.ok(trainingTypes);
    }

    // zelim da na osnovu trainingSort i trainingType dobijem price
    @GetMapping("/price/{trainingSort}/{trainingType}")
    public ResponseEntity<Integer> getPriceByTrainingSortAndTrainingyTType(@PathVariable String trainingSort, @PathVariable String trainingType) {
        TrainingTypes trainingTypes = trainingTypesRepository.findByTrainingSortAndTrainingType(trainingSort, trainingType);
        Integer price = trainingTypes.getTrainingTypePrice();
        return ResponseEntity.ok(price);
    }

    // zelim da na osnovu trainingType dobijem price
    @GetMapping("/price/{trainingType}")
    public ResponseEntity<Integer> getPriceByTrainingyTType(@PathVariable String trainingType) {
        TrainingTypes trainingTypes = trainingTypesRepository.findByTrainingType(trainingType);
        Integer price = trainingTypes.getTrainingTypePrice();
        return ResponseEntity.ok(price);
    }
}
