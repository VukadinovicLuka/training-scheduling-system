package nikolalukatrening.Notifikacioni_servis.listener;

import nikolalukatrening.Notifikacioni_servis.dto.ClientProfileEditorDto;
import nikolalukatrening.Notifikacioni_servis.dto.EmailMessageDto;
import nikolalukatrening.Notifikacioni_servis.dto.TrainingDto;
import nikolalukatrening.Notifikacioni_servis.service.impl.RestTemplateServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TrainingReminderService {

    private RestTemplateServiceImpl restTemplateServiceImpl;
    private RestTemplate trainingRestTemplate;

    private JmsTemplate jmsTemplate;
    private String activationDestination;
    private MessageHelper messageHelper;

    public TrainingReminderService(JmsTemplate jmsTemplate,
                                   MessageHelper messageHelper, @Value("${destination.createActivation}") String activationDestination){
        this.jmsTemplate = jmsTemplate;
        this.activationDestination = activationDestination;
        this.messageHelper = messageHelper;
    }

    private List<TrainingDto> fetchAllTrainings() {
        System.out.println("usao u fetch all trainings");
        restTemplateServiceImpl = new RestTemplateServiceImpl();
        trainingRestTemplate = restTemplateServiceImpl.setupRestTemplate(trainingRestTemplate);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<List<TrainingDto>> responseForClient = trainingRestTemplate.exchange(
                "http://localhost:8082/api/training/all",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<TrainingDto>>() {});
        List<TrainingDto> training = responseForClient.getBody();
        return training;
    }


    @Scheduled(cron = "0 * * * * *") // At the top of every hour
    public void checkTrainingSessions() {
        // Your database query logic goes here
        List<TrainingDto> trainings = fetchAllTrainings();

        // get date and start time from trainings
        // if date and start time is 24 hours from now, send email to user
        for (TrainingDto training : trainings) {
            // treba da mi proveris da li je training 24 sata od sada
            if (training.getDate() == null || training.getStartTime() == null) {
                continue;
            }
            // proveri mi danasnji datum + 1 dan i da li je to isto kao datum iz traininga


            LocalDate lt = LocalDate.now();
            if (lt.plusDays(1).equals(training.getDate())) {
                // proveri mi da li je vreme treninga 24 sata od sada
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String currentTime = sdf.format(cal.getTime());
                if (currentTime.equals(training.getStartTime())) {
                    // posalji mejl
                    createEmailMessage(training);
                    System.out.println("Sending email to user with id: " + training.getUserId());
                }
            }

        }

        System.out.println("Checking for training sessions 24 hours from now...");
        // Example: queryDatabaseForTrainingSessions();
    }



    private void createEmailMessage(TrainingDto trainingDto) {

        restTemplateServiceImpl = new RestTemplateServiceImpl();
        trainingRestTemplate = restTemplateServiceImpl.setupRestTemplate(trainingRestTemplate);
        Integer userId = trainingDto.getUserId();
        // na osnovu userId treba da odradim get za usera i da uzmem njegovo ime i prezime
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<ClientProfileEditorDto> responseForClient = trainingRestTemplate.exchange(
                "http://localhost:8080/api/client/" +userId,
                HttpMethod.GET,
                entity,
                ClientProfileEditorDto.class);
        ClientProfileEditorDto client = responseForClient.getBody();




        Map<String, String> params = new HashMap<>();
        params.put("ime", client.getUser().getFirstName());
        params.put("prezime", client.getUser().getLastName());
        params.put("clientId", userId.toString());
        EmailMessageDto emailMessage = new EmailMessageDto(
                client.getUser().getEmail(),
                "Reservation Email",
                "Pozdrav," + params.get("ime") + " " + params.get("prezime") +
                        "Podsetnik za trening datuma " + trainingDto.getDate() + " u " + trainingDto.getStartTime() + "h",
                "REMINDER",
                params
        );
        jmsTemplate.convertAndSend(activationDestination, messageHelper.createTextMessage(emailMessage));
    }
}
