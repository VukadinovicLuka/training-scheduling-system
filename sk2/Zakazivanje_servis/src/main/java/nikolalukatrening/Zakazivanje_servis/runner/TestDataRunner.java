package nikolalukatrening.Zakazivanje_servis.runner;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import nikolalukatrening.Zakazivanje_servis.model.Gym;
import nikolalukatrening.Zakazivanje_servis.model.Training;
import nikolalukatrening.Zakazivanje_servis.model.TrainingTypes;
import nikolalukatrening.Zakazivanje_servis.repository.GymRepository;
import nikolalukatrening.Zakazivanje_servis.repository.TrainingRepository;
import nikolalukatrening.Zakazivanje_servis.repository.TrainingTypesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestDataRunner implements CommandLineRunner {

    private TrainingRepository trainingRepository;
    private TrainingTypesRepository trainingTypesRepository;
    private GymRepository gymRepository;

    public TestDataRunner(TrainingRepository trainingRepository, TrainingTypesRepository trainingTypesRepository, GymRepository gymRepository) {
        this.trainingRepository = trainingRepository;
        this.trainingTypesRepository = trainingTypesRepository;
        this.gymRepository = gymRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create the first training instance
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
//
////        LocalDate date1 = dateFormat.parse("02.01.2024");
////        LocalDate date2 = dateFormat.parse("28.12.2023");
//        LocalDate date1 = LocalDate.of(2024, 1, 2);
//        LocalDate date2 = LocalDate.of(2023,12,28);
//        LocalDate date3 = LocalDate.of(2024,1,9);
//        Training training1 = new Training();
//        training1.setStartTime("08:00");
//        training1.setDate(date1); // You should set this to the actual date you want
//        training1.setTrainingType("Yoga");
//        training1.setIsGroupTraining(true);
//        training1.setMaxParticipants(12);
//        // ... Set other properties as needed
//
//        // Save the first training
//        trainingRepository.save(training1);
//
//        // Create the second training instance
//        Training training2 = new Training();
//        training2.setStartTime("10:00");
//        training2.setDate(date2); // You should set this to the actual date you want
//        training2.setTrainingType("Calistenics");
//        training2.setIsGroupTraining(false);
//        training2.setMaxParticipants(12);
//        // ... Set other properties as needed
//
//        // Save the second training
//        trainingRepository.save(training2);
//
//        Training training3 = new Training();
//        training3.setStartTime("12:00");
//        training3.setDate(date1); // You should set this to the actual date you want
//        training3.setTrainingType("Pilates");
//        training3.setIsGroupTraining(true);
//        training3.setMaxParticipants(12);
//
//        trainingRepository.save(training3);
//
//        Training training4 = new Training();
//        training4.setStartTime("22:00");
//        training4.setDate(date3); // You should set this to the actual date you want
//        training4.setTrainingType("Pilates");
//        training4.setIsGroupTraining(true);
//        training4.setMaxParticipants(2);
//        training4.setUserId(1);
//
//        trainingRepository.save(training4);
//
//        Training training5 = new Training();
//        training5.setStartTime("22:00");
//        training5.setDate(date3); // You should set this to the actual date you want
//        training5.setTrainingType("Pilates");
//        training5.setIsGroupTraining(true);
//        training5.setMaxParticipants(4);
//        training5.setUserId(3);
//
//        trainingRepository.save(training5);
//
//        Training training6 = new Training();
//        training6.setStartTime("22:00");
//        training6.setDate(date3); // You should set this to the actual date you want
//        training6.setTrainingType("Pilates");
//        training6.setIsGroupTraining(true);
//        training6.setMaxParticipants(2);
//        training6.setUserId(2);
//
//        trainingRepository.save(training6);

        // ... Create more training instances if needed

        TrainingTypes trainingTypes1 = new TrainingTypes();
        trainingTypes1.setTrainingType("Yoga");
        trainingTypes1.setTrainingSort("Grupno");
        trainingTypes1.setTrainingTypePrice(1000);
        trainingTypesRepository.save(trainingTypes1);

        TrainingTypes trainingTypes2 = new TrainingTypes();
        trainingTypes2.setTrainingType("Pilates");
        trainingTypes2.setTrainingSort("Grupno");
        trainingTypes2.setTrainingTypePrice(1500);
        trainingTypesRepository.save(trainingTypes2);

        TrainingTypes trainingTypes3 = new TrainingTypes();
        trainingTypes3.setTrainingType("Kalistenika");
        trainingTypes3.setTrainingSort("Individualno");
        trainingTypes3.setTrainingTypePrice(2000);

        trainingTypesRepository.save(trainingTypes3);

        TrainingTypes trainingTypes4 = new TrainingTypes();
        trainingTypes4.setTrainingType("Powerlifting");
        trainingTypes4.setTrainingSort("Individualno");
        trainingTypes4.setTrainingTypePrice(2500);

        trainingTypesRepository.save(trainingTypes4);


        // populate gym
        Gym gym1 = new Gym();
        gym1.setName("Gym1");
        gym1.setDescription("Gym1 description");
        gym1.setPersonalTrainerNumbers(4);
        gymRepository.save(gym1);


        Gym gym2 = new Gym();
        gym2.setName("Gym2");
        gym2.setDescription("Gym2 description");
        gym2.setPersonalTrainerNumbers(5);
        gymRepository.save(gym2);

        Gym gym3 = new Gym();
        gym3.setName("Gym3");
        gym3.setDescription("Gym3 description");
        gym3.setPersonalTrainerNumbers(6);
        gymRepository.save(gym3);
    }
}
