package nikolalukatrening.GUI2.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import nikolalukatrening.GUI2.client.*;
import nikolalukatrening.GUI2.clientViews.ProfileEditor;
import nikolalukatrening.GUI2.interfaces.AdminInterface;
import nikolalukatrening.GUI2.interfaces.ClientInterface;
import org.springframework.context.annotation.Profile;
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

        jLabel2 = new JLabel("Username");
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

    private RequestEntity<TokenResponseDto> GetClaimsFromTokenRequestEntity(String token) throws JsonProcessingException {

        TokenResponseDto tokenResponseDto = new TokenResponseDto(token);

        // Kreirajte header-e za zahtev
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestEntity<TokenResponseDto> requestEntity = RequestEntity.post(URI.create("http://localhost:8080/api/client/login/token")).headers(headers).body(tokenResponseDto);

        return requestEntity;
    }

    private void jButton1ActionPerformed() throws JsonProcessingException {

        RequestEntity<TokenRequestDto> requestTokenEntity = TokenRequestDtoRequestEntity();

        try {
            // Pošaljite zahtev koristeći RestTemplate
            ResponseEntity<TokenResponseDto> responseForToken = LogInServiceRestTemplate.exchange(
                    requestTokenEntity,
                    TokenResponseDto.class
            );

            // Obrada odgovora
            String token = responseForToken.getBody().getToken();
            System.out.println("Token:" + token);
            RequestEntity<TokenResponseDto> requestClaimsEntity = GetClaimsFromTokenRequestEntity(token);
            ResponseEntity<ClaimsResponseDto> responseForClaims = LogInServiceRestTemplate.exchange(
                    requestClaimsEntity,
                    ClaimsResponseDto.class
            );


            if (responseForClaims.getStatusCode() == HttpStatus.OK) {
//                JOptionPane.showMessageDialog(this, "Uspešna prijava!", "Status", JOptionPane.INFORMATION_MESSAGE);
                ClaimsResponseDto claims = responseForClaims.getBody();

                if(claims.getRole().equals("ROLE_ADMIN")){
                    AdminInterface adminFrame = new AdminInterface();
                    adminFrame.setVisible(true);
                    this.dispose();
                } else if(claims.getRole().equals("ROLE_CLIENT")){

                    Integer userId = claims.getId();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
                    ResponseEntity<ClientProfileEditorDto> responseForClient = LogInServiceRestTemplate.exchange(
                            "http://localhost:8080/api/client/" +userId,
                            HttpMethod.GET,
                            entity,
                            ClientProfileEditorDto.class);

                    if (!responseForClient.getBody().getIsActivated()){
                        JOptionPane.showMessageDialog(this, "Nalog nije aktiviran!", "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                     ClientInterface clientInterface = new ClientInterface(userId);
                     clientInterface.setVisible(true);
                     this.dispose();
                } else if(claims.getRole().equals("ROLE_MANAGER")){
//                    ManagerInterface managerFrame = new ManagerInterface();
//                    managerFrame.setVisible(true);
//                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "GRESKA KOD ROLE", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "GRESKA KOD PARSOVANJA TOKENA U CLAIMOVE", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HttpClientErrorException e) {
            JOptionPane.showMessageDialog(this, "Greška: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
}
