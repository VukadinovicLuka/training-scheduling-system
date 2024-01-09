package nikolalukatrening.Zakazivanje_servis.controller;

import jakarta.validation.Valid;
import nikolalukatrening.Zakazivanje_servis.dto.TrainingDto;
import nikolalukatrening.Zakazivanje_servis.model.Training;
import nikolalukatrening.Zakazivanje_servis.repository.TrainingRepository;
import nikolalukatrening.Zakazivanje_servis.service.TrainingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/training")
public class TrainingController {

    private TrainingService trainingService;
    private TrainingRepository trainingRepository;

    public TrainingController(TrainingService trainingService, TrainingRepository trainingRepository) {
        this.trainingService = trainingService;
        this.trainingRepository = trainingRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Training>> getAllTrainings() {
        return ResponseEntity.ok(trainingRepository.findAll());
    }

    @GetMapping("/start-times")
    public ResponseEntity<List<String>> getStartTimesByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        System.out.println(date);
        List<Training> trainings = trainingRepository.findAllByDate(date);
        System.out.println(trainings);
        List<String> startTimes = trainings.stream()
                .map(Training::getStartTime)
                .collect(Collectors.toList());
        return ResponseEntity.ok(startTimes);
    }

    @PostMapping("/createTraining")
    public ResponseEntity<Training> registerClient(@RequestBody @Valid TrainingDto trainingDto) {
        // Logika za slanje zahteva ka notifikacionom servisu preko message brokera
        return new ResponseEntity<>(trainingService.add(trainingDto), HttpStatus.CREATED);
    }

    @PutMapping("/updateTraining")
    public ResponseEntity<Training> updateTraining(@RequestBody @Valid TrainingDto trainingDto) {
        return new ResponseEntity<>(trainingService.update(trainingDto), HttpStatus.OK);
    }

    @PutMapping("/updateTrainingReserve")
    public ResponseEntity<Training> updateTrainingReserve(@RequestBody @Valid TrainingDto trainingDto) {
        return new ResponseEntity<>(trainingService.updateReserve(trainingDto), HttpStatus.OK);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Training>> getTrainingsByUserId(@PathVariable Long userId) {
        List<Training> trainings = trainingRepository.findAllByUserId(userId);
        if (trainings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(trainings);
    }

    @DeleteMapping("/deleteTraining")
    public ResponseEntity<Void> deleteTraining(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("startTime") String startTime,
            @RequestParam("userId") Long userId) {

        Optional<Training> trainingOptional = trainingRepository.findByDateAndStartTimeAndUserId(date, startTime, userId);
        if (trainingOptional.isPresent()) {
            trainingRepository.delete(trainingOptional.get());
            return ResponseEntity.ok().build(); // Successfully deleted
        } else {
            return ResponseEntity.notFound().build(); // Training not found
        }
    }

}
