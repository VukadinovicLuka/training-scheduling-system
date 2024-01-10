package nikolalukatrening.GUI2.managerViews;

import nikolalukatrening.GUI2.dto.ClientProfileEditorDto;
import nikolalukatrening.GUI2.dto.GymDto;
import nikolalukatrening.GUI2.dto.ManagerDto;
import nikolalukatrening.GUI2.service.impl.RestTemplateServiceImpl;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.List;

public class GymEditor extends JPanel {
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField numberOfPersonalTrainersField;
    private JComboBox<String> cbTrainingType;
    private JTextField trainingTypePriceField;
    private JButton saveButton;

    private RestTemplate managerRestTemplate;
    private RestTemplateServiceImpl restTemplateServiceImpl;
    private ManagerDto manager;

    public GymEditor() {
        this.restTemplateServiceImpl = new RestTemplateServiceImpl();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        nameField = new JTextField(20);
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        numberOfPersonalTrainersField = new JTextField(20);
        cbTrainingType = new JComboBox<>();
        fetchAllTrainingTypes();
        cbTrainingType.addActionListener(e -> {
            // Logika za prikaz cene treninga
            // Ovde biste pozvali REST servis ili neki drugi mehanizam za dobavljanje cene treninga
            String selectedTrainingType = (String) cbTrainingType.getSelectedItem();
            fetchPriceForTrainingType(selectedTrainingType);

        });
        trainingTypePriceField = new JTextField(20);
        saveButton = new JButton("Sačuvaj");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(new JLabel("Ime sale:"), gbc);

        gbc.gridy++;
        add(nameField, gbc);

        gbc.gridy++;
        add(new JLabel("Opis:"), gbc);

        gbc.gridy++;
        add(new JScrollPane(descriptionArea), gbc);

        gbc.gridy++;
        add(new JLabel("Broj personalnih trenera:"), gbc);

        gbc.gridy++;
        add(numberOfPersonalTrainersField, gbc);

        gbc.gridy++;
        add(new JLabel("Tip treninga:"), gbc);

        gbc.gridy++;
        add(cbTrainingType, gbc);

        gbc.gridy++;
        add(new JLabel("Cena treninga:"), gbc);

        gbc.gridy++;
        add(trainingTypePriceField, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            // Logika za snimanje izmena
            // Ovde biste pozvali REST servis ili neki drugi mehanizam za ažuriranje podataka
            GymDto gymDto = new GymDto();
            gymDto.setName(nameField.getText());
            gymDto.setDescription(descriptionArea.getText());
            gymDto.setPersonalTrainerNumbers(Integer.parseInt(numberOfPersonalTrainersField.getText()));

            managerRestTemplate = restTemplateServiceImpl.setupRestTemplate(managerRestTemplate);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
//            RequestEntity<ClientProfileEditorDto> requestEntity = RequestEntity.put(URI.create("http://localhost:8082/api/manager/" + id)).headers(headers).body(clientToUpdate);
//            ResponseEntity<ClientProfileEditorDto> responseEntity = managerRestTemplate.exchange(requestEntity, ClientProfileEditorDto.class);
        });
    }


    public void loadManagerData(Integer id) {

        managerRestTemplate = restTemplateServiceImpl.setupRestTemplate(managerRestTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<ManagerDto> responseForManager = managerRestTemplate.exchange(
                "http://localhost:8080/api/manager/" +id,
                HttpMethod.GET,
                null,
                ManagerDto.class);


        this.manager = responseForManager.getBody(); // klijent koji je ulogovan
        String gymName = manager.getGymName();

        ResponseEntity<GymDto> responseForGym = managerRestTemplate.exchange(
                "http://localhost:8082/api/gym/name/" + gymName,
                HttpMethod.GET,
                null,
                GymDto.class);


        GymDto gym = responseForGym.getBody();
        setGymNameField(gym.getName());
        setDescriptionArea(gym.getDescription());
        setNumberOfPersonalTrainersField(gym.getPersonalTrainerNumbers());



        this.revalidate();
        this.repaint();
    }

    private void fetchAllTrainingTypes(){
        managerRestTemplate = restTemplateServiceImpl.setupRestTemplate(managerRestTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<List<String>> response = managerRestTemplate.exchange(
                "http://localhost:8082/api/trainingType/allTypes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<java.util.List<String>>() {});

        List<String> trainingTypes = response.getBody();


        for (String trainingType : trainingTypes) {
            cbTrainingType.addItem(trainingType);
        }
    }

    private void fetchPriceForTrainingType(String trainingType){
        managerRestTemplate = restTemplateServiceImpl.setupRestTemplate(managerRestTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Integer> response = managerRestTemplate.exchange(
                "http://localhost:8082/api/trainingType/price/" + trainingType,
                HttpMethod.GET,
                null,
                Integer.class);

        Integer price = response.getBody();
        trainingTypePriceField.setText(price.toString());
    }


    public void setGymNameField(String gymName) {
        this.nameField.setText(gymName);
    }

    public void setDescriptionArea(String description) {
        this.descriptionArea.setText(description);
    }

    public void setNumberOfPersonalTrainersField(Integer numberOfPersonalTrainers) {
        this.numberOfPersonalTrainersField.setText(numberOfPersonalTrainers.toString());
    }

}
