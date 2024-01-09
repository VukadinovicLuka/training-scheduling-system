package nikolalukatrening.Zakazivanje_servis.service.impl;

import nikolalukatrening.Zakazivanje_servis.dto.ClientProfileEditorDto;
import nikolalukatrening.Zakazivanje_servis.dto.EmailMessageDto;
import nikolalukatrening.Zakazivanje_servis.dto.TrainingDto;
import nikolalukatrening.Zakazivanje_servis.mapper.TrainingMapper;
import nikolalukatrening.Zakazivanje_servis.message.MessageHelper;
import nikolalukatrening.Zakazivanje_servis.model.Training;
import nikolalukatrening.Zakazivanje_servis.repository.TrainingRepository;
import nikolalukatrening.Zakazivanje_servis.service.TrainingService;
import org.hibernate.id.AbstractPostInsertGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingService {

    private TrainingRepository trainingRepository;

    private TrainingMapper trainingMapper;
    private RestTemplateServiceImpl restTemplateServiceImpl;
    private RestTemplate trainingRestTemplate;

    private JmsTemplate jmsTemplate;
    private String activationDestination;
    private MessageHelper messageHelper;
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
        createEmailMessage(trainingDto);
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
        training.setGym(null);
        training.setUserId(trainingDto.getUserId());
        training.setMaxParticipants(trainingDto.getMaxParticipants());
        return trainingRepository.save(training);
    }

    @Override
    public Training updateReserve(TrainingDto trainingDto) {
        Training training = trainingRepository.findByDateAndStartTimeAndUserId(trainingDto.getDate(),trainingDto.getStartTime(), Long.valueOf(trainingDto.getUserId())).orElseThrow(()->new RuntimeException());

        training.setTrainingType(trainingDto.getTrainingType());
        training.setIsGroupTraining(trainingDto.getIsGroupTraining());
        training.setDate(trainingDto.getDate());
        training.setStartTime(trainingDto.getStartTime());
        training.setGym(null);
        training.setUserId(trainingDto.getUserId());
        training.setMaxParticipants(trainingDto.getMaxParticipants());
        return trainingRepository.save(training);
    }

//    @Override
//    public ClientDto update(ClientUpdateDto clientUpdateDto) {
//        Client client = clientRepository.findByUserUsername(clientUpdateDto.getOldUsername()).orElseThrow(()->new RuntimeException());
//        client = clientMapper.clientUpdateToClient(client,clientUpdateDto);
//        client = clientRepository.save(client);
//        return clientMapper.clientToClientDto(client);
//    }

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
                "Vas trening je uspesno zakazan za " + trainingDto.getDate() + " u " + trainingDto.getStartTime() + "h",
                "RESERVATION",
                params
        );
        jmsTemplate.convertAndSend(activationDestination, messageHelper.createTextMessage(emailMessage));
    }
}
