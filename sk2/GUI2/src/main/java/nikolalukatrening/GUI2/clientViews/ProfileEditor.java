package nikolalukatrening.GUI2.clientViews;

import lombok.Getter;
import lombok.Setter;
import nikolalukatrening.GUI2.client.ClientProfileEditorDto;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ProfileEditor extends JPanel {

    private JTextField usernameField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField passwordField;
    private JTextField dateOfBirthField;
    private JTextField cardNumberField;
    private JTextField reservedTrainingsField;
    private JTextField emailField;
    private RestTemplate ProfileEditorServiceRestTemplate;
    private Integer id;
    public ProfileEditor() {
        usernameField = new JTextField(15);
        lastNameField = new JTextField(15);
        firstNameField = new JTextField(15);
        passwordField = new JTextField(15);
        dateOfBirthField = new JTextField(15);
        cardNumberField = new JTextField(15);
        reservedTrainingsField = new JTextField(15);
        emailField = new JTextField(15);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Naslov
        JLabel lblTitle = new JLabel("Izmena Profila");
        lblTitle.setFont(new Font("Serif", Font.BOLD, 24));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitle, gbc);

        addLabelAndTextField("Korisničko ime:", 1, usernameField);
        addLabelAndTextField("Prezime:", 2, lastNameField);
        addLabelAndTextField("Ime:", 3, firstNameField);
        addLabelAndTextField("Lozinka:", 4, passwordField);
        addLabelAndTextField("Datum rodjenja:", 5,dateOfBirthField);
        addLabelAndTextField("Broj kartice:", 6, cardNumberField);
        addLabelAndTextField("Rezervisani treninzi:", 7,(reservedTrainingsField));
        addLabelAndTextField("Email:", 8, emailField);

        cardNumberField.setEditable(false);
        cardNumberField.setEnabled(false);
        reservedTrainingsField.setEditable(false);
        reservedTrainingsField.setEnabled(false);

        // Dugme za potvrdu izmena
        JButton btnConfirm = new JButton("Potvrdi izmene");
        btnConfirm.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnConfirm.setBackground(new Color(0, 150, 136));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFocusPainted(false);
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnConfirm, gbc);
    }



    public void loadProfileData(Integer id) {
        ProfileEditorServiceRestTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        messageConverters.add(converter);
        ProfileEditorServiceRestTemplate.setMessageConverters(messageConverters);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<ClientProfileEditorDto> responseForClient = ProfileEditorServiceRestTemplate.exchange(
                "http://localhost:8080/api/client/" +id,
                HttpMethod.GET,
                entity,
                ClientProfileEditorDto.class);
        setCardNumberField(responseForClient.getBody().getCardNumber());
        setEmailField(responseForClient.getBody().getUser().getEmail());
        setDateOfBirthField(responseForClient.getBody().getUser().getDateOfBirth());
        setPasswordField(responseForClient.getBody().getUser().getPassword());
        setFirstNameField(responseForClient.getBody().getUser().getFirstName());
        setLastNameField(responseForClient.getBody().getUser().getLastName());
        setUsernameField(responseForClient.getBody().getUser().getUsername());
        setReservedTrainingsField(responseForClient.getBody().getReservedTraining());
    }


    private void addLabelAndTextField(String labelText, int gridy, JTextField textField) {
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(5, 0, 5, 10);
        add(label, gbc);

        textField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        gbc.gridx = 1;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(textField, gbc);
    }


    public void setUsernameField(String username) {
        usernameField.setText(username);
    }

    public void setLastNameField(String lastName) {
        lastNameField.setText(lastName);
    }

    public void setFirstNameField(String firstName) {
        firstNameField.setText(firstName);
    }

    public void setPasswordField(String password) {
        passwordField.setText(password);
    }

    public void setDateOfBirthField(String dateOfBirth) {
        dateOfBirthField.setText(dateOfBirth);
    }

    public void setCardNumberField(Integer cardNumber) {
        cardNumberField.setText(String.valueOf(cardNumber));
    }

    public void setReservedTrainingsField(Integer reservedTrainings) {
        reservedTrainingsField.setText(String.valueOf(reservedTrainings));
    }

    public void setEmailField(String email) {
        emailField.setText(email);
    }
}
