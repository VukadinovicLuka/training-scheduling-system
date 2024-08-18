package nikolalukatrening.GUI2.clientViews;

import nikolalukatrening.GUI2.dto.ClientProfileEditorDto;
import nikolalukatrening.GUI2.dto.GymDto;
import nikolalukatrening.GUI2.dto.TrainingDto;
import nikolalukatrening.GUI2.dto.UserDto;
import nikolalukatrening.GUI2.service.impl.RestTemplateServiceImpl;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientsTrainings extends JPanel {

    private JTable trainingTable;
    private DefaultTableModel tableModel;
    private RestTemplate trainingsRestTemplate;
    private RestTemplate priceTemplate;
    private RestTemplate gymNameTemplate;
    private RestTemplate gymTemplate;
    private RestTemplateServiceImpl restTemplateService;
    private ClientProfileEditorDto client;
    public ClientsTrainings(){
        restTemplateService = new RestTemplateServiceImpl();
        setLayout(new BorderLayout());
        initializeUI();
        addCancelArea();
    }

    private void initializeUI() {
        // Definisanje kolona za tabelu
        String[] columnNames = {"Datum","Grupno", "Clanova", "Pocetno vreme", "Tip", "UserId", "Cena", "Sala"};

        // Kreiranje modela tabele sa definisanim kolonama
        tableModel = new DefaultTableModel(columnNames, 0);
        trainingTable = new JTable(tableModel);

        // Omogućavanje scroll-a za tabelu
        JScrollPane scrollPane = new JScrollPane(trainingTable);
        trainingTable.setFillsViewportHeight(true);

        // Dodavanje scrollPane-a (sa tabelom) u centralni deo panela
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addCancelArea() {
        // Create a panel for the button to avoid it stretching across the whole width
        JPanel cancelPanel = new JPanel();
        cancelPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center the button
        JButton cancelButton = new JButton("Otkazi");
        cancelButton.addActionListener(e -> cancelTraining());
        cancelPanel.add(cancelButton);

        // Add the panel above the table, but below the existing components (if any)
        add(cancelPanel, BorderLayout.NORTH);
    }

    private void cancelTraining() {
        int row = trainingTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Molimo vas da prvo selektujete trening koji želite da otkazete.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Optional: Extracting the training id or some identifier from the selected row
        // Long trainingId = (Long) tableModel.getValueAt(row, COLUMN_INDEX_OF_ID); // Replace COLUMN_INDEX_OF_ID with the actual column index

        int confirmed = JOptionPane.showConfirmDialog(this, "Da li ste sigurni da želite da otkazete trening?", "Potvrda otkazivanja", JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION) {
            // Perform the cancellation action
            performCancellation(row); // Replace with the actual cancellation logic
        }
    }

    private void performCancellation(int row) {
        String gymName = (String) tableModel.getValueAt(row, 7);
        Boolean isGroupTraining = (Boolean) tableModel.getValueAt(row, 1);
        String trainingType = (String) tableModel.getValueAt(row, 4);
        int participants = (int) tableModel.getValueAt(row,2);
        String vreme = (String) tableModel.getValueAt(row,3);
        LocalDate localDate = (LocalDate) tableModel.getValueAt(row,0);
        //LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int userId = (int) tableModel.getValueAt(row,5);

        if(isGroupTraining){
            if(participants>1) {
                //pozivamo post metode za ostale korisnike koji imaju taj trening
                Set<TrainingDto> treninzi = fetchAllTrainings();
                for (TrainingDto trainingDto : treninzi) {
                    if (trainingDto.getDate().equals(localDate) && trainingDto.getStartTime().equals(vreme)) {
                        trainingDto.setMaxParticipants(trainingDto.getMaxParticipants() - 1);
                        updateParticipantsTraining(trainingDto);
                    }
                }
            }
        }

        //pozivamo DELETE rutu za trening i put metodu za rezervisane treninge
//        activateAvailableTrainingRoute(localDate,vreme,userId);
        TrainingDto tra = new TrainingDto();
        tra.setDate(localDate);
        tra.setStartTime(vreme);
        tra.setMaxParticipants(participants);
        tra.setIsGroupTraining(isGroupTraining);
        tra.setIsAvailable(false);
        tra.setGymId(Math.toIntExact(fetchGymByGymName(gymName).getId()));
        tra.setTrainingType(trainingType);
        tra.setUserId(userId);


        updateParticipantsTrainingEmail(tra);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<ClientProfileEditorDto> responseForClient = trainingsRestTemplate.exchange(
                "http://localhost:8080/api/client/" +userId,
                HttpMethod.GET,
                entity,
                ClientProfileEditorDto.class);

        int trening = responseForClient.getBody().getReservedTraining() - 1;
        this.client = responseForClient.getBody();
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
        clientToUpdate.setId(Long.valueOf(userId));
        clientToUpdate.setActivationToken(client.getActivationToken());
        clientToUpdate.setIsActivated(client.getIsActivated());

        RequestEntity<ClientProfileEditorDto> requestEntity1 = RequestEntity.put(URI.create("http://localhost:8080/api/client/" + userId)).headers(headers).body(clientToUpdate);

        // Posaljite zahtev
        ResponseEntity<ClientProfileEditorDto> responseEntity1 = trainingsRestTemplate.exchange(requestEntity1, ClientProfileEditorDto.class);

        tableModel.removeRow(row);

    }

    private String getGymName(Integer gymId) {
        gymNameTemplate = restTemplateService.setupRestTemplate(gymNameTemplate);
        String gymName = null;
        try {
            ResponseEntity<Map<String, String>> response = gymNameTemplate.exchange(
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

    private GymDto fetchGymByGymName(String gymName){

        GymDto gym = null;
        gymTemplate = restTemplateService.setupRestTemplate(gymTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:8082/api/gym/name/" + gymName;
        try {
            ResponseEntity<GymDto> response = gymTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<GymDto>() {});
            gym = response.getBody();
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return gym;

    }

    private Integer updatePrice(Boolean boolSort, String selectedTrainingType) {
        String selectedTrainingSort = null;
        if (boolSort == false){
            selectedTrainingSort = "Individualno";
        }else{
            selectedTrainingSort = "Grupno";
        }
        return getPriceFromDatabase(selectedTrainingSort, selectedTrainingType);
    }

    private int getPriceFromDatabase(String trainingSort, String trainingType) {
        priceTemplate = restTemplateService.setupRestTemplate(priceTemplate);
        Integer price = 0;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        try {
            ResponseEntity<Integer> response = priceTemplate.exchange(
                    "http://localhost:8082/api/trainingType/price/" + trainingSort + "/" + trainingType,
                    HttpMethod.GET,
                    entity,
                    Integer.class);

            price = response.getBody();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return price;
    }

    public void loadTrainings(Integer id) {
        trainingsRestTemplate = restTemplateService.setupRestTemplate(trainingsRestTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        String url = "http://localhost:8082/api/training/by-user/" + id;

        try {
            ResponseEntity<List<TrainingDto>> trainingResponse = trainingsRestTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TrainingDto>>() {});

            List<TrainingDto> trainingsForClient = trainingResponse.getBody();
            int i = 1;
            if (trainingsForClient != null) {
                for (TrainingDto trainingDto : trainingsForClient) {
                    if(trainingDto.getIsAvailable()) {
                        Integer price = 0;
                        if(i%12==0){
                            price = 0;
                        } else {
                            price = updatePrice(trainingDto.getIsGroupTraining(), trainingDto.getTrainingType());
                        }
                        tableModel.addRow(new Object[]{trainingDto.getDate(), trainingDto.getIsGroupTraining(), trainingDto.getMaxParticipants(),
                                trainingDto.getStartTime(), trainingDto.getTrainingType(),
                                trainingDto.getUserId(),price, getGymName(trainingDto.getGymId())});
                    }
                    i++;
                }
            } else {
                showNoTrainingsMessage();
            }
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                showNoTrainingsMessage();
            } else {
                // Handle other types of exceptions here
                JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage());
            }
        }
    }

    private void showNoTrainingsMessage() {
        // Remove all existing rows
        tableModel.setRowCount(0);

        // Show message in the UI
        JOptionPane.showMessageDialog(null, "You have no scheduled trainings.");
    }


    public void updateParticipantsTraining(TrainingDto trainingDto){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        RequestEntity<TrainingDto> requestEntity = RequestEntity.put(URI.create("http://localhost:8082/api/training/updateTrainingReserve")).headers(headers).body(trainingDto);
        ResponseEntity<TrainingDto> responseEntity = trainingsRestTemplate.exchange(requestEntity, TrainingDto.class);
    }

    public void updateParticipantsTrainingEmail(TrainingDto trainingDto){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        RequestEntity<TrainingDto> requestEntity = RequestEntity.put(URI.create("http://localhost:8082/api/training/updateTrainingReserveEmail")).headers(headers).body(trainingDto);
        ResponseEntity<TrainingDto> responseEntity = trainingsRestTemplate.exchange(requestEntity, TrainingDto.class);
    }

    private Set<TrainingDto> fetchAllTrainings() {
        restTemplateService = new RestTemplateServiceImpl();
        trainingsRestTemplate = restTemplateService.setupRestTemplate(trainingsRestTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<Set<TrainingDto>> responseForClient = trainingsRestTemplate.exchange(
                "http://localhost:8082/api/training/all",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Set<TrainingDto>>() {});
        Set<TrainingDto> training = responseForClient.getBody();
        return training;
    }

    // In your frontend Java code where you handle the cancellation

    public void activateAvailableTrainingRoute(LocalDate date, String startTime, int userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<Void> response = trainingsRestTemplate.exchange(
                "http://localhost:8082/api/training/deleteTraining?date=" +
                        date.toString() + "&startTime=" + startTime + "&userId=" + userId,
                HttpMethod.DELETE,
                entity,
                Void.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Training cancelled successfully.");
        } else {
            System.out.println("Failed to cancel training.");
        }
    }

}
