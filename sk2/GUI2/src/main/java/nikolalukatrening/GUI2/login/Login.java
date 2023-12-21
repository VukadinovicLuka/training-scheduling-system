package nikolalukatrening.GUI2.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import nikolalukatrening.GUI2.client.ClientCreateDto;
import nikolalukatrening.GUI2.client.ClientDto;
import nikolalukatrening.GUI2.client.TokenRequestDto;
import nikolalukatrening.GUI2.client.TokenResponseDto;
import nikolalukatrening.GUI2.interfaces.AdminInterface;
import nikolalukatrening.GUI2.interfaces.ClientInterface;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;


import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter

public class Login extends JFrame {

    private JPanel jPanel1;
    private JButton jButton1, jButton2;
    private JLabel jLabel1, jLabel2, jLabel3, jLabel4;
    private JPasswordField jPasswordField1;
    private JTextField jTextField1;

    private RestTemplate LogInServiceRestTemplate;
    public Login() {
        initComponents();
    }

    private void initComponents() {
        LogInServiceRestTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        messageConverters.add(converter);
        LogInServiceRestTemplate.setMessageConverters(messageConverters);


        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("LOGIN");
        setPreferredSize(new Dimension(400, 500));

        jPanel1 = new JPanel(new GridBagLayout());
        jPanel1.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        jLabel1 = new JLabel("LOGIN");
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 36));
        jLabel1.setForeground(new Color(0, 102, 102));

        jLabel2 = new JLabel("Email");
        jLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jTextField1 = new JTextField(20);
        jTextField1.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jLabel3 = new JLabel("Password");
        jLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jPasswordField1 = new JPasswordField(20);
        jPasswordField1.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jButton1 = new JButton("Login");
        jButton1.setBackground(new Color(0, 102, 102));
        jButton1.setForeground(Color.WHITE);
        jButton1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jButton1.addActionListener(evt-> {
            try {
                jButton1ActionPerformed();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        jLabel4 = new JLabel("I don't have an account");
        jLabel4.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jButton2 = new JButton("Sign Up");
        jButton2.setForeground(new Color(255, 51, 51));
        jButton2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jButton2.addActionListener(evt -> jButton2ActionPerformed());

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        jPanel1.add(jLabel1, gbc);
        jPanel1.add(jLabel2, gbc);
        jPanel1.add(jTextField1, gbc);
        jPanel1.add(jLabel3, gbc);
        jPanel1.add(jPasswordField1, gbc);
        jPanel1.add(jButton1, gbc);

        gbc.fill = GridBagConstraints.NONE;
        jPanel1.add(jLabel4, gbc);
        jPanel1.add(jButton2, gbc);

        add(jPanel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void jButton2ActionPerformed() {
        // Kreiranje instance SignUp prozora
        SignUp signUpFrame = new SignUp();
        // Postavljanje SignUp prozora da bude vidljiv
        signUpFrame.setVisible(true);
        // Zatvaranje trenutnog (Login) prozora
        this.dispose();
    }

    private RequestEntity<TokenRequestDto> TokenRequestDtoRequestEntity() throws JsonProcessingException {
        String username = jTextField1.getText();
        String password = String.valueOf(jPasswordField1.getPassword());
        System.out.println("123" + username + " " + password);
        TokenRequestDto tokenRequestDto = new TokenRequestDto(username, password);

        // Kreirajte header-e za zahtev
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestEntity<TokenRequestDto> requestEntity = RequestEntity.post(URI.create("http://localhost:8080/api/client/login")).headers(headers).body(tokenRequestDto);

        return requestEntity;
    }

    private void jButton1ActionPerformed() throws JsonProcessingException {
//        if(jTextField1.getText().equals("admin")){
//            AdminInterface adminFrame = new AdminInterface();
//            adminFrame.setVisible(true);
//            this.dispose();
//        } else if(jTextField1.getText().equals("client")){
//            ClientInterface clientInterface = new ClientInterface();
//            clientInterface.setVisible(true);
//            this.dispose();
//        } else if(jTextField1.getText().equals("manager")){
//            //ManagerInterface managerFrame = new ManagerInterface();
//            //managerFrame.setVisible(true);
//            this.dispose();
//        } else {
//            this.dispose();
//        }

        RequestEntity<TokenRequestDto> requestEntity = TokenRequestDtoRequestEntity();


        try {
            // Pošaljite zahtev koristeći RestTemplate
            ResponseEntity<TokenResponseDto> response = LogInServiceRestTemplate.exchange(
                    requestEntity,
                    TokenResponseDto.class
            );

            // Obrada odgovora
            String token = response.getBody().getToken();
            if (response.getStatusCode() == HttpStatus.OK) {
                JOptionPane.showMessageDialog(this, "Uspešna prijava!", "Status", JOptionPane.INFORMATION_MESSAGE);
                ClientInterface clientInterface = new ClientInterface();
                clientInterface.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Prijava nije uspešna.", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HttpClientErrorException e) {
            JOptionPane.showMessageDialog(this, "Greška: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }


    }
}
