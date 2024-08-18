package nikolalukatrening.Notifikacioni_servis.listener;

import nikolalukatrening.Notifikacioni_servis.config.dto.ClientProfileEditorDto;
import nikolalukatrening.Notifikacioni_servis.config.dto.EmailMessageDto;
import nikolalukatrening.Notifikacioni_servis.config.dto.TrainingDto;
import nikolalukatrening.Notifikacioni_servis.config.dto.UserDto;
import nikolalukatrening.Notifikacioni_servis.service.impl.RestTemplateServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GroupTrainingReminderService {

    private RestTemplateServiceImpl restTemplateServiceImpl;
    private RestTemplate trainingRestTemplate;

    private JmsTemplate jmsTemplate;
    private String activationDestination;
    private MessageHelper messageHelper;

    public GroupTrainingReminderService(JmsTemplate jmsTemplate, @Value("${destination.createActivation}") String activationDestination, MessageHelper messageHelper) {
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


    @Scheduled(cron = "* 0 * * * *") // At the top of every hour
    public void checkTrainingSessions() {
        // Your database query logic goes here
        List<TrainingDto> trainings = fetchAllTrainings();

        for (TrainingDto training : trainings) {
            if(training.getIsGroupTraining()){
                LocalDate lt = LocalDate.now();
                if (lt.plusDays(1).equals(training.getDate())) {
                    // proveri mi da li je vreme treninga 24 sata od sada
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    String currentTime = sdf.format(cal.getTime());
                    if (currentTime.equals(training.getStartTime())) {
                        if(training.getMaxParticipants() < 3) {
                            createEmailMessage(training);
                            System.out.println("Otkazivanje treninga korisniku sa id: " + training.getUserId());
                            deleteTraining(training.getDate(),training.getStartTime(),training.getUserId());
                        }
                    }
                }
            }
        }

        System.out.println("Checking for training sessions 24 hours from now...");
        // Example: queryDatabaseForTrainingSessions();
    }


    private void deleteTraining(LocalDate date, String startTime, int userId){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<Void> response = trainingRestTemplate.exchange(
                "http://localhost:8082/api/training/deleteTraining?date=" +
                        date.toString() + "&startTime=" + startTime + "&userId=" + userId,
                HttpMethod.DELETE,
                entity,
                Void.class);
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

        updateReservationForClient(client);

        Map<String, String> params = new HashMap<>();
        params.put("ime", client.getUser().getFirstName());
        params.put("prezime", client.getUser().getLastName());
        params.put("clientId", userId.toString());
        EmailMessageDto emailMessage = new EmailMessageDto(
                client.getUser().getEmail(),
                "Reservation Email",
                "Pozdrav," + params.get("ime") + " " + params.get("prezime") +
                        ",zbog nedostatka clanova za sutrasnji grupni trening, trening se otkazuje.",
                "CANCELLATION",
                params
        );
        jmsTemplate.convertAndSend(activationDestination, messageHelper.createTextMessage(emailMessage));
    }

    private void updateReservationForClient(ClientProfileEditorDto client){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        int trening = client.getReservedTraining() - 1;
        UserDto userDto = new UserDto();
        userDto.setPassword(client.getUser().getPassword());
        userDto.setUsername(client.getUser().getUsername());
        userDto.setEmail(client.getUser().getEmail());
        userDto.setFirstName(client.getUser().getFirstName());
        userDto.setLastName(client.getUser().getLastName());
        userDto.setDateOfBirth(client.getUser().getDateOfBirth());
        userDto.setRole(client.getUser().getRole());
        ClientProfileEditorDto clientToUpdate = new ClientProfileEditorDto();
        clientToUpdate.setUser(userDto);
        clientToUpdate.setCardNumber(client.getCardNumber());
        clientToUpdate.setReservedTraining(trening);
        clientToUpdate.setId(Long.valueOf(client.getId()));
        clientToUpdate.setActivationToken(client.getActivationToken());
        clientToUpdate.setIsActivated(client.getIsActivated());

        RequestEntity<ClientProfileEditorDto> requestEntity1 = RequestEntity.put(URI.create("http://localhost:8080/api/client/" + client.getId())).headers(headers).body(clientToUpdate);

        // Posaljite zahtev
        ResponseEntity<ClientProfileEditorDto> responseEntity1 = trainingRestTemplate.exchange(requestEntity1, ClientProfileEditorDto.class);
    }

}
