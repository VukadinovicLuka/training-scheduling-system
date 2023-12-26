package nikolalukatrening.Zakazivanje_servis.runner;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import nikolalukatrening.Zakazivanje_servis.model.Training;
import nikolalukatrening.Zakazivanje_servis.repository.TrainingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestDataRunner implements CommandLineRunner {

    private TrainingRepository trainingRepository;

    public TestDataRunner(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create the first training instance
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

//        LocalDate date1 = dateFormat.parse("02.01.2024");
//        LocalDate date2 = dateFormat.parse("28.12.2023");
        LocalDate date1 = LocalDate.of(2024, 1, 2);
        LocalDate date2 = LocalDate.of(2023,12,28);
        Training training1 = new Training();
        training1.setStartTime("08:00");
        training1.setDate(date1); // You should set this to the actual date you want
        training1.setTrainingType("Yoga");
        training1.setIsGroupTraining(true);
        training1.setMaxParticipants(12);
        // ... Set other properties as needed

        // Save the first training
        trainingRepository.save(training1);

        // Create the second training instance
        Training training2 = new Training();
        training2.setStartTime("10:00");
        training2.setDate(date2); // You should set this to the actual date you want
        training2.setTrainingType("Calistenics");
        training2.setIsGroupTraining(false);
        training2.setMaxParticipants(12);
        // ... Set other properties as needed

        // Save the second training
        trainingRepository.save(training2);

        Training training3 = new Training();
        training3.setStartTime("12:00");
        training3.setDate(date1); // You should set this to the actual date you want
        training3.setTrainingType("Pilates");
        training3.setIsGroupTraining(true);
        training3.setMaxParticipants(12);

        trainingRepository.save(training3);

        // ... Create more training instances if needed
    }
}
