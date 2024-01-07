package nikolalukatrening.GUI2.clientViews;

import lombok.Getter;
import lombok.Setter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import nikolalukatrening.GUI2.customTable.DateLabelFormatter;
import nikolalukatrening.GUI2.dto.ClientProfileEditorDto;
import nikolalukatrening.GUI2.dto.TrainingDto;
import nikolalukatrening.GUI2.dto.UserDto;
import nikolalukatrening.GUI2.service.impl.RestTemplateServiceImpl;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
public class ScheduleTraining extends JPanel {
        private JLabel lblTrainingType, lblDayOfWeek, lblTrainingOptions, lblTime;
        private JComboBox<String> cbTrainingType, cbTrainingOptions, cbTime;
        private JButton btnBook;
        private Font labelFont = new Font("Arial", Font.BOLD, 16);
        private Font comboFont = new Font("Arial", Font.PLAIN, 16);
        private Font buttonFont = new Font("Arial", Font.BOLD, 16);
        private JDatePicker datePicker;
        private GroupTraining groupTrainingReference;
        private RestTemplate timeRestTemplate;
        private RestTemplateServiceImpl restTemplateServiceImpl;
        private RestTemplate createTrainingTemplate;
        private Integer userId;
        private ClientProfileEditorDto client;
        private ProfileEditor profileEditor;

        List<String> timeSlots = new ArrayList<>();
        public ScheduleTraining(GroupTraining groupTrainingReference, Integer userId) {
            this.userId = userId;
            this.restTemplateServiceImpl = new RestTemplateServiceImpl();
            this.groupTrainingReference = groupTrainingReference;
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(15, 15, 15, 15); // Dodaje razmak između komponenti

            JLabel lblTitle = new JLabel("Zakazivanje treninga");
            lblTitle.setFont(new Font("Arial", Font.BOLD, 24)); // Postavljanje većeg fonta za naslov
            lblTitle.setHorizontalAlignment(JLabel.CENTER); // Centriranje teksta naslova

            // Postavljanje naslova na panelu koristeći GridBagConstraints
            gbc.gridx = 0; // Početna tačka X
            gbc.gridy = 0; // Početna tačka Y
            gbc.gridwidth = 2; // Naslov se prostire preko dva stupca
            gbc.anchor = GridBagConstraints.CENTER; // Centriranje naslova u prostoru
            add(lblTitle, gbc);

            lblTrainingType = createLabel("Odaberite vrstu treninga:");
            cbTrainingType = createComboBox(new String[]{"Individualno", "Grupno"});
            lblDayOfWeek = createLabel("Odaberite datum:");
            UtilDateModel model = new UtilDateModel();
            Properties p = new Properties();
            p.put("text.today", "Today");
            p.put("text.month", "Month");
            p.put("text.year", "Year");
            JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
            datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
            datePicker.addActionListener(e -> {
                Date selectedDate = (Date) datePicker.getModel().getValue();
                if (selectedDate != null) {
                    // Convert Date to LocalDate or the format required by your backend
                    LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    // Now fetch the unavailable times for this date
                    fetchUnavailableTimes(localDate);
                }
            });
//            cbDayOfWeek = createComboBox(new String[]{"Ponedeljak", "Utorak", "Sreda", "Četvrtak", "Petak", "Subota", "Nedelja"});
            lblTrainingOptions = createLabel("Tip treninga:");
            cbTrainingOptions = createComboBox(new String[]{}); // Opcije će biti dodate dinamički
            lblTime = createLabel("Vreme:");

            for (int hour = 8; hour < 24; hour++) {
                String start = String.format("%02d:00", hour);
                String end = String.format("%02d:00", (hour + 1) % 24);
                timeSlots.add(start + "-" + end);
            }

            cbTime = createComboBox(timeSlots.toArray(new String[0]));
            btnBook = createButton("Zakaži");
            btnBook.addActionListener(e-> zakazivanje());

            // Postavljanje komponenti na panelu koristeći GridBagLayout
            gbc.gridwidth = 1;

            // Odabir vrste treninga
            gbc.gridx = 0;
            gbc.gridy = 1;
            add(lblTrainingType, gbc);
            gbc.gridx = 1;
            add(cbTrainingType, gbc);

            // Odabir dana u nedelji
            gbc.gridx = 0;
            gbc.gridy = 2;
            add(lblDayOfWeek, gbc);
            gbc.gridx = 1;
            add((Component) datePicker, gbc);

            // Tip treninga
            gbc.gridx = 0;
            gbc.gridy = 3;
            add(lblTrainingOptions, gbc);
            gbc.gridx = 1;
            add(cbTrainingOptions, gbc);

            // Vreme
            gbc.gridx = 0;
            gbc.gridy = 4;
            add(lblTime, gbc);
            gbc.gridx = 1;
            add(cbTime, gbc);

            // Dugme za zakazivanje
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2; // Dugme se prostire preko dva stupca
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(btnBook, gbc);

            // Add action listeners to handle the dynamic changes
            cbTrainingType.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    // Proveravamo da li je događaj za selekciju
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        updateTrainingOptions();
                    }
                }
            });
            // Initialize options
            updateTrainingOptions();
            styleComponents();
        }

    private void zakazivanje() {
        if (cbTrainingType.getSelectedItem() == null || datePicker.getModel().getValue() == null || cbTrainingOptions.getSelectedItem() == null || cbTime.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Morate odabrati sve podatke!");
        }

        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setTrainingType((String) cbTrainingOptions.getSelectedItem());
        if (cbTrainingType.getSelectedItem().equals("Grupno")){
            System.out.println("usao");
            trainingDto.setIsGroupTraining(true);
            trainingDto.setMaxParticipants(12);
        }
        if(cbTrainingType.getSelectedItem().equals("Individualno")) {
            trainingDto.setIsGroupTraining(false);
            trainingDto.setMaxParticipants(1);
        }
        Date date = (Date) datePicker.getModel().getValue();

        // Convert Date to LocalDate
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Now you can use localDate in your DTO

        trainingDto.setDate(localDate);
        trainingDto.setGymId(null);

        trainingDto.setUserId(userId);

        String time = (String) cbTime.getSelectedItem();
        String[] prvo = time.split("-");

        trainingDto.setStartTime(prvo[0]);

        createTrainingTemplate = restTemplateServiceImpl.setupRestTemplate(createTrainingTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        RequestEntity<TrainingDto> requestEntity = RequestEntity.post(URI.create("http://localhost:8082/api/training/createTraining")).headers(headers).body(trainingDto);
        ResponseEntity<TrainingDto> responseEntity = createTrainingTemplate.exchange(requestEntity, TrainingDto.class);
        fetchUnavailableTimes(localDate);

        ResponseEntity<ClientProfileEditorDto> responseForClient = createTrainingTemplate.exchange(
                "http://localhost:8080/api/client/" +userId,
                HttpMethod.GET,
                entity,
                ClientProfileEditorDto.class);
        int trening = responseForClient.getBody().getReservedTraining() + 1;
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
        ResponseEntity<ClientProfileEditorDto> responseEntity1 = createTrainingTemplate.exchange(requestEntity1, ClientProfileEditorDto.class);

        profileEditor = new ProfileEditor();
        profileEditor.loadProfileData(userId);
    }
    private void fetchUnavailableTimes(LocalDate date) {
        // This is just an example, you need to replace it with your actual REST call logic
        String url = "http://localhost:8082/api/training/start-times?date=" + date;
        // Assuming you have a RestTemplate instance named restTemplate
        timeRestTemplate = restTemplateServiceImpl.setupRestTemplate(timeRestTemplate);
        ResponseEntity<List<String>> response = timeRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});

        List<String> unavailableTimes = response.getBody();

        // Now update the ComboBox
        updateComboBox(unavailableTimes);
    }

    private void updateComboBox(List<String> unavailableTimes) {
        // First, clear the ComboBox
        cbTime.removeAllItems();
        // Add all times, but skip the ones that are unavailable
        for (String timeSlot : timeSlots) {
            String[] part = timeSlot.split("-");
            if (!unavailableTimes.contains(part[0].trim())) {
                cbTime.addItem(timeSlot);
            }
        }
    }

        private void updateTrainingOptions() {
            cbTrainingOptions.removeAllItems();
            Object selectedItem = cbTrainingType.getSelectedItem();
            if (selectedItem != null) {
                String selectedType = selectedItem.toString();
                if ("Individualno".equals(selectedType)) {
                    cbTrainingOptions.addItem("Kalistenika");
                    cbTrainingOptions.addItem("Powerlifting");
                } else if ("Grupno".equals(selectedType)) {
                    cbTrainingOptions.addItem("Joga");
                    cbTrainingOptions.addItem("Pilates");
                }
            }
        }

        private JLabel createLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(labelFont);
            return label;
        }

        private JComboBox<String> createComboBox(String[] items) {
            JComboBox<String> comboBox = new JComboBox<>(items);
            comboBox.setFont(comboFont);
            return comboBox;
        }

        private JButton createButton(String text) {
            JButton button = new JButton(text);
            button.setFont(buttonFont);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    bookTraining();
                }
            });
            return button;
        }

    private void bookTraining() {
        // Prikupljanje informacija iz ComboBox-ova i TextField-ova
        String trainingType = cbTrainingType.getSelectedItem().toString();
        Date selectedDate = (Date) datePicker.getModel().getValue();
        String trainingOption = cbTrainingOptions.getSelectedItem().toString();
        String time = cbTime.getSelectedItem().toString();
        int numberOfMembers = trainingType.equals("Individualno") ? 1 : (int) (Math.random() * 12 + 1);

        // Dodavanje reda u GroupTraining tabelu
//        groupTrainingReference.addTrainingRow(trainingType, day, trainingOption, time, numberOfMembers);
    }
        private void styleComponents() {
            setBackground(Color.WHITE); // Postavite boju pozadine panela na belo
            setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Dodajte ivicu oko panela

            lblTrainingType.setFont(new Font("Arial", Font.BOLD, 16));
            lblDayOfWeek.setFont(new Font("Arial", Font.BOLD, 16));
            lblTrainingOptions.setFont(new Font("Arial", Font.BOLD, 16));
            lblTime.setFont(new Font("Arial", Font.BOLD, 16));

            cbTrainingType.setFont(new Font("Arial", Font.PLAIN, 14));
//            cbDayOfWeek.setFont(new Font("Arial", Font.PLAIN, 14));
            cbTrainingOptions.setFont(new Font("Arial", Font.PLAIN, 14));
            cbTime.setFont(new Font("Arial", Font.PLAIN, 14));

            btnBook.setFont(new Font("Arial", Font.BOLD, 14));
            btnBook.setBackground(new Color(0, 123, 255));
            btnBook.setForeground(Color.WHITE);
        }
}






