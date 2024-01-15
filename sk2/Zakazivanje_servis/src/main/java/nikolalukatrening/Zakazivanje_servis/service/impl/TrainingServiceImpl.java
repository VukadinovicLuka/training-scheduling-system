package nikolalukatrening.Zakazivanje_servis.service.impl;

import nikolalukatrening.Zakazivanje_servis.dto.*;
import nikolalukatrening.Zakazivanje_servis.mapper.TrainingMapper;
import nikolalukatrening.Zakazivanje_servis.message.MessageHelper;
import nikolalukatrening.Zakazivanje_servis.model.Training;
import nikolalukatrening.Zakazivanje_servis.repository.TrainingRepository;
import nikolalukatrening.Zakazivanje_servis.service.TrainingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingService {

    private TrainingRepository trainingRepository;

    private TrainingMapper trainingMapper;
    private RestTemplateServiceImpl restTemplateServiceImpl;
    private RestTemplate trainingRestTemplate;
    private RestTemplate managerRestTemplate;

    private JmsTemplate jmsTemplate;
    private String activationDestination;
    private MessageHelper messageHelper;
    private EmailMessageDto emailMessage;
    public TrainingServiceImpl(TrainingRepository trainingRepository,TrainingMapper trainingMapper, JmsTemplate jmsTemplate,
                               @Value("${destination.createActivation}") String activationDestination, MessageHelper messageHelper){
        this.trainingRepository = trainingRepository;
        this.trainingMapper = trainingMapper;
        this.jmsTemplate = jmsTemplate;
        this.activationDestination = activationDestination;
        this.messageHelper = messageHelper;
    }

    @Override
    public Training add(TrainingDto trainingDto) {
        Training training = trainingMapper.trainingDtoToTraining(trainingDto);
        trainingRepository.save(training);
        createEmailMessage(trainingDto, 0);
        createManagerEmailMessage(trainingDto, 0);
        return training;
    }

    @Override
    public Training update(TrainingDto trainingDto) {

        // zelim da nadjem trening koji ima isti datum, vreme i maxParticipants - 1 u odnosu na trainingDto
        Training training = trainingRepository.findByDateAndStartTimeAndMaxParticipants(trainingDto.getDate(),trainingDto.getStartTime(),trainingDto.getMaxParticipants()-1).orElseThrow(()->new RuntimeException());

        training.setTrainingType(trainingDto.getTrainingType());
        training.setIsGroupTraining(trainingDto.getIsGroupTraining());
        training.setDate(trainingDto.getDate());
        training.setStartTime(trainingDto.getStartTime());
        training.setGymId(trainingDto.getGymId());
        training.setUserId(trainingDto.getUserId());
        training.setMaxParticipants(trainingDto.getMaxParticipants());
        training.setIsAvailable(trainingDto.getIsAvailable());
        return trainingRepository.save(training);
    }

    @Override
    public Training updateReserve(TrainingDto trainingDto) {
        Training training = trainingRepository.findByDateAndStartTimeAndUserId(trainingDto.getDate(),trainingDto.getStartTime(), Long.valueOf(trainingDto.getUserId())).orElseThrow(()->new RuntimeException());

        training.setTrainingType(trainingDto.getTrainingType());
        training.setIsGroupTraining(trainingDto.getIsGroupTraining());
        training.setDate(trainingDto.getDate());
        training.setStartTime(trainingDto.getStartTime());
        training.setGymId(trainingDto.getGymId());
        training.setUserId(trainingDto.getUserId());
        training.setMaxParticipants(trainingDto.getMaxParticipants());
        training.setIsAvailable(trainingDto.getIsAvailable());
        // email
//        createEmailMessage(trainingDto, 1);
//        createManagerEmailMessage(trainingDto, 1);

        return trainingRepository.save(training);
    }

    @Override
    public Training updateReserveEmail(TrainingDto trainingDto) {
        Training training = trainingRepository.findByDateAndStartTimeAndUserId(trainingDto.getDate(),trainingDto.getStartTime(), Long.valueOf(trainingDto.getUserId())).orElseThrow(()->new RuntimeException());

        training.setTrainingType(trainingDto.getTrainingType());
        training.setIsGroupTraining(trainingDto.getIsGroupTraining());
        training.setDate(trainingDto.getDate());
        training.setStartTime(trainingDto.getStartTime());
        training.setGymId(trainingDto.getGymId());
        training.setUserId(trainingDto.getUserId());
        training.setMaxParticipants(trainingDto.getMaxParticipants());
        training.setIsAvailable(trainingDto.getIsAvailable());
        // email
        createEmailMessage(trainingDto, 1);
        createManagerEmailMessage(trainingDto, 1);

        return trainingRepository.save(training);
    }


    @Override
    public boolean deleteTraining(LocalDate date, String startTime, Long userId) {
        Optional<Training> trainingOptional = trainingRepository.findByDateAndStartTimeAndUserId(date, startTime, userId);
        if (trainingOptional.isPresent()) {
            trainingRepository.delete(trainingOptional.get());
            // email
            createEmailMessage(trainingMapper.trainingToTrainingDto(trainingOptional.get()), 1);
            createManagerEmailMessage(trainingMapper.trainingToTrainingDto(trainingOptional.get()), 1);
            return true; // Successfully deleted
        } else {
            return false; // Training not found
        }
    }


    private void createManagerEmailMessage(TrainingDto trainingDto, int flag){
        String gymName = getGymName(trainingDto.getGymId());
        managerRestTemplate = restTemplateServiceImpl.setupRestTemplate(managerRestTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<ManagerDto> responseForClient = managerRestTemplate.exchange(
                "http://localhost:8080/api/manager/gymName/" +gymName,
                HttpMethod.GET,
                entity,
                ManagerDto.class);
        ManagerDto manager = responseForClient.getBody();

        Map<String, String> params = new HashMap<>();
        params.put("ime", manager.getUser().getFirstName());
        params.put("prezime", manager.getUser().getLastName());
        params.put("managerId", manager.getId().toString());
        if (flag == 0){
            emailMessage = new EmailMessageDto(
                    manager.getUser().getEmail(),
                    "Manager Reservation Email",
                    "Pozdrav, " + params.get("ime") + " " + params.get("prezime") +
                            " Zakazan je trening u vasoj sali: " + trainingDto.getDate() + " u " + trainingDto.getStartTime() + "h",
                    "MANAGER_RESERVATION",
                    params
            );
        }else{
            emailMessage = new EmailMessageDto(
                    manager.getUser().getEmail(),
                    "Manager Reservation Email",
                    "Pozdrav, " + params.get("ime") + " " + params.get("prezime") +
                            " Zakazan trening u vasoj sali je otkazan: " + trainingDto.getDate() + " u " + trainingDto.getStartTime() + "h",
                    "MANAGER_CANCELATION",
                    params
            );
        }

        jmsTemplate.convertAndSend(activationDestination, messageHelper.createTextMessage(emailMessage));
        System.out.println("Email sent to manager" +  manager.getUser().getFirstName());
    }

    private void createEmailMessage(TrainingDto trainingDto, int flag) {

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
        if (flag == 0){
            emailMessage = new EmailMessageDto(
                    client.getUser().getEmail(),
                    "Reservation Email",
                    "Pozdrav, " + params.get("ime") + " " + params.get("prezime") +
                    " Vas trening je uspesno zakazan za " + trainingDto.getDate() + " u " + trainingDto.getStartTime() + "h",
                    "RESERVATION",
                    params
            );
        }else{
            emailMessage = new EmailMessageDto(
                    client.getUser().getEmail(),
                    "Cancellation Email",
                    "Pozdrav, " + params.get("ime") + " " + params.get("prezime") +
                            " Vas trening je uspesno otkazan za " + trainingDto.getDate() + " u " + trainingDto.getStartTime() + "h",
                    "CANCELATION",
                    params
            );
        }
        jmsTemplate.convertAndSend(activationDestination, messageHelper.createTextMessage(emailMessage));
    }

    private String getGymName(Integer gymId) {
        restTemplateServiceImpl = new RestTemplateServiceImpl();
        trainingRestTemplate = restTemplateServiceImpl.setupRestTemplate(trainingRestTemplate);
        String gymName = null;
        try {
            ResponseEntity<Map<String, String>> response = trainingRestTemplate.exchange(
                    "http://localhost:8082/api/gym/" + gymId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, String>>() {});

            // Check if response is good
            if (response.getStatusCode() == HttpStatus.OK) {
                gymName = response.getBody().get("name");
                System.out.println("Retrieved gym name: " + gymName);
            } else {
                System.out.println("Error retrieving gym name!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gymName;
    }

    @Override
    public Optional<String> getTrainingTypeByDateAndStartTime(LocalDate date, String startTime) {
        return trainingRepository.findByDateAndStartTime(date, startTime)
                .map(Training::getTrainingType);
    }
}
