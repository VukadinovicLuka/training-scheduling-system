package nikolalukatrening.GUI2.clientViews;

import lombok.Getter;
import lombok.Setter;
import nikolalukatrening.GUI2.dto.ClientProfileEditorDto;
import nikolalukatrening.GUI2.dto.UserDto;
import nikolalukatrening.GUI2.service.impl.RestTemplateServiceImpl;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ProfileEditor extends JPanel {

    private JTextField usernameField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JPasswordField passwordField;
    private JTextField dateOfBirthField;
    private JTextField cardNumberField;
    private JTextField reservedTrainingsField;
    private JTextField emailField;
    private RestTemplate ProfileEditorServiceRestTemplate;
    private RestTemplate clientEditorRestTemplate;
    private RestTemplate updatePasswordClientRestTemplate;
    private RestTemplateServiceImpl restTemplateServiceImpl;
    private Integer id;
    private ClientProfileEditorDto client;
    public ProfileEditor() {
        this.restTemplateServiceImpl = new RestTemplateServiceImpl();
        usernameField = new JTextField(15);
        lastNameField = new JTextField(15);
        firstNameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        passwordField.setEchoChar('*');
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
        JButton changePasswordButton = new JButton("Promeni lozinku");
        changePasswordButton.addActionListener(e -> changePassword()); // Metod za promenu lozinke
        gbc.gridx = 2; // Postavite dugme pored polja za tekst lozinke
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(changePasswordButton, gbc);
        addLabelAndTextField("Datum rodjenja:", 5,dateOfBirthField);
        addLabelAndTextField("Broj kartice:", 6, cardNumberField);
        addLabelAndTextField("Rezervisani treninzi:", 7,(reservedTrainingsField));
        addLabelAndTextField("Email:", 8, emailField);

        cardNumberField.setEditable(false);
        cardNumberField.setEnabled(false);
        reservedTrainingsField.setEditable(false);
        reservedTrainingsField.setEnabled(false);
        passwordField.setEnabled(false);
        passwordField.setEditable(false);

        // Dugme za potvrdu izmena
        JButton btnConfirm = new JButton("Potvrdi izmene");
        btnConfirm.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnConfirm.setBackground(new Color(0, 150, 136));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFocusPainted(false);
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirm.addActionListener(e -> confirmChanges());
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnConfirm, gbc);
    }



    public void loadProfileData(Integer id) {
        this.id = id;

        ProfileEditorServiceRestTemplate = restTemplateServiceImpl.setupRestTemplate(ProfileEditorServiceRestTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<ClientProfileEditorDto> responseForClient = ProfileEditorServiceRestTemplate.exchange(
                "http://localhost:8080/api/client/" +id,
                HttpMethod.GET,
                entity,
                ClientProfileEditorDto.class);
        this.client = responseForClient.getBody(); // klijent koji je ulogovan
        client.setActivationToken(responseForClient.getBody().getActivationToken());
        System.out.println("client ATIVACIONI TOKEN: " + client.getActivationToken());
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

    private void confirmChanges() {
        // sakupljanje podataka iz polja
        UserDto userDto = new UserDto();
        userDto.setUsername(usernameField.getText());
        userDto.setPassword(passwordField.getText());
        userDto.setEmail(emailField.getText());
        userDto.setFirstName(firstNameField.getText());
        userDto.setLastName(lastNameField.getText());
        userDto.setDateOfBirth(dateOfBirthField.getText());
        userDto.setRole(client.getUser().getRole());
        ClientProfileEditorDto clientToUpdate = new ClientProfileEditorDto();
        clientToUpdate.setUser(userDto);
        clientToUpdate.setCardNumber(Integer.parseInt(cardNumberField.getText()));
        clientToUpdate.setReservedTraining(Integer.parseInt(reservedTrainingsField.getText()));
        clientToUpdate.setId(Long.valueOf(id));
        System.out.println("Aktivacioni token: " + client.getActivationToken());
        clientToUpdate.setActivationToken(client.getActivationToken());
        clientToUpdate.setIsActivated(client.getIsActivated());

        clientEditorRestTemplate = restTemplateServiceImpl.setupRestTemplate(clientEditorRestTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestEntity<ClientProfileEditorDto> requestEntity = RequestEntity.put(URI.create("http://localhost:8080/api/client/" + id)).headers(headers).body(clientToUpdate);




        // Posaljite zahtev
        ResponseEntity<ClientProfileEditorDto> responseEntity = clientEditorRestTemplate.exchange(requestEntity, ClientProfileEditorDto.class);

        // Proverite odgovor servera
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JOptionPane.showMessageDialog(null, "Uspesno ste izmenili profil!");
            loadProfileData(id); // id korisnika je dobar
        } else {
            JOptionPane.showMessageDialog(null, "Doslo je do greske pri izmeni profila!");
        }

    }


    private void changePassword() {
        // Prvo tražimo trenutnu lozinku od korisnika
        JPasswordField currentPasswordField = new JPasswordField();
        int action = JOptionPane.showConfirmDialog(this, currentPasswordField, "Unesite trenutnu lozinku:", JOptionPane.OK_CANCEL_OPTION);
        if (action == JOptionPane.OK_OPTION) {
            String currentPassword = new String(currentPasswordField.getPassword());
            // ovde bi valjalo da dodate logiku za proveru da li je uneta trenutna lozinka tačna
            boolean isCurrentPasswordCorrect = checkPassword(currentPassword);
            if (isCurrentPasswordCorrect) {
                // Trenutna lozinka je tačna, sada tražimo novu lozinku
                JPasswordField newPasswordField = new JPasswordField();
                int newPasswordAction = JOptionPane.showConfirmDialog(this, newPasswordField, "Unesite novu lozinku:", JOptionPane.OK_CANCEL_OPTION);
                if (newPasswordAction == JOptionPane.OK_OPTION) {
                    String newPassword = new String(newPasswordField.getPassword());
                    if (!newPassword.isEmpty()) {
                        // ovde logika za postavljanje nove lozinke
                        updatePassword(newPassword);
                    } else {
                        JOptionPane.showMessageDialog(this, "Nova lozinka ne može biti prazna.", "Greška", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Trenutna lozinka nije tačna.", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean checkPassword(String password) {

        if (client.getUser().getPassword().equals(password)){ // uporedjujemo lozinku ulogovanog klijenta sa unetim current passwordom
            return true;
        }
        return false;
    }

    private void updatePassword(String newPassword) {
        // Ovde implementirajte logiku za ažuriranje lozinke
        // Na primer, ažuriranje lozinke u bazi podataka ili poslati zahtev na server da promeni lozinku

        // Prvo kreiramo objekat koji sadrži novu lozinku
        UserDto userDto = new UserDto();
        userDto.setPassword(newPassword);
        userDto.setUsername(client.getUser().getUsername());
        userDto.setEmail(client.getUser().getEmail());
        userDto.setFirstName(client.getUser().getFirstName());
        userDto.setLastName(client.getUser().getLastName());
        userDto.setDateOfBirth(client.getUser().getDateOfBirth());
        userDto.setRole(client.getUser().getRole());
        ClientProfileEditorDto clientToUpdate = new ClientProfileEditorDto();
        clientToUpdate.setUser(userDto);
        clientToUpdate.setCardNumber(client.getCardNumber());
        clientToUpdate.setReservedTraining(client.getReservedTraining());
        clientToUpdate.setId(Long.valueOf(id));
        clientToUpdate.setActivationToken(client.getActivationToken());
        clientToUpdate.setIsActivated(client.getIsActivated());

        // Proverite da li je objekat kreiran
        System.out.println("Klijent koji se salje na server: " + clientToUpdate.toString());

        // Postavite RestTemplate
        updatePasswordClientRestTemplate = restTemplateServiceImpl.setupRestTemplate(updatePasswordClientRestTemplate);

        // Postavite hedere
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestEntity<ClientProfileEditorDto> requestEntity = RequestEntity.put(URI.create("http://localhost:8080/api/client/" + id)).headers(headers).body(clientToUpdate);


        // Posaljite zahtev
        ResponseEntity<ClientProfileEditorDto> responseEntity = updatePasswordClientRestTemplate.exchange(requestEntity, ClientProfileEditorDto.class);

        // Proverite odgovor servera
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JOptionPane.showMessageDialog(null, "Uspesno ste promenili lozinku! Za prikaz lozinke idite na gmail nalog.");
            loadProfileData(id); // id korisnika je dobar

        } else {
            JOptionPane.showMessageDialog(null, "Doslo je do greske pri promeni lozinke!");
        }
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
