package nikolalukatrening.GUI2.interfaces;

import nikolalukatrening.GUI2.client.ClientProfileEditorDto;
import nikolalukatrening.GUI2.client.TokenRequestDto;
import nikolalukatrening.GUI2.client.UserDto;
import nikolalukatrening.GUI2.customTable.CustomTable;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.util.*;
import java.util.List;

public class AdminInterface extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    private JToolBar toolBar = new JToolBar();
    private JPanel clientsPanel = new JPanel(new BorderLayout());
    private JPanel managersPanel = new JPanel(new BorderLayout());
    private CustomTable clientsTable;
    private JTable managersTable;

    private RestTemplate adminServiceRestTemplate;
    private RestTemplate activationServiceRestTemplate;


    public AdminInterface() {
        setTitle("Admin Interfejs");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setupToolbar();
        setupClientsPanel();
        setupManagersPanel();
    }

    private void initComponents() {
        add(toolBar, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        cardPanel.add(clientsPanel, "Clients");
        cardPanel.add(managersPanel, "Managers");
    }

    private void setupToolbar() {
        JButton clientsButton = new JButton("Klijenti");
        clientsButton.addActionListener(e -> cardLayout.show(cardPanel, "Clients"));
        toolBar.add(clientsButton);

        JButton managersButton = new JButton("Menadžeri");
        managersButton.addActionListener(e -> cardLayout.show(cardPanel, "Managers"));
        toolBar.add(managersButton);

        JButton searchButton = new JButton("Zabrani");
        searchButton.addActionListener(e -> zabrani());
        toolBar.add(searchButton);

        JButton resetColorButton = new JButton("Odblokiraj");
        resetColorButton.addActionListener(e -> odblokiraj());
        toolBar.add(resetColorButton);
    }

    private void setupClientsPanel() {
        String[] clientColumns = new String[]{"id", "username", "email", "firstName", "lastName", "dateOfBirth", "reservedTraining", "cardNumber", "isActivated", "password", "activationToken", "role"};

        adminServiceRestTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        messageConverters.add(converter);
        adminServiceRestTemplate.setMessageConverters(messageConverters);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<List<ClientProfileEditorDto>> response = adminServiceRestTemplate.exchange(
                "http://localhost:8080/api/client/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ClientProfileEditorDto>>() {});
        List<ClientProfileEditorDto> clients = response.getBody();

        // Create a data model for the table
        DefaultTableModel clientsModel = new DefaultTableModel(clientColumns, 0);

        // Populate the model with client data
        for (ClientProfileEditorDto client : clients) {
            Object[] row = new Object[]{
                    client.getId(),
                    client.getUser().getUsername(),
                    client.getUser().getEmail(),
                    client.getUser().getFirstName(),
                    client.getUser().getLastName(),
                    client.getUser().getDateOfBirth(),
                    client.getReservedTraining(),
                    client.getCardNumber(),
                    client.getIsActivated(),
                    client.getUser().getPassword(),
                    client.getActivationToken(),
                    client.getUser().getRole()
            };
            clientsModel.addRow(row);
//            if (client.getIsActivated() == false) {
//                highlightedRowsClients.add(clientsModel.getRowCount() - 1);
//            }
        }

        // Set the model to the JTable and add it to the JScrollPane
        clientsTable = new CustomTable(clientsModel);
        JScrollPane scrollPane = new JScrollPane(clientsTable);
        clientsPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void setupManagersPanel() {
        String[] managerColumns = new String[]{"id", "username", "email", "firstName", "lastName", "dateOfHiring", "gymName"};
        Object[][] managerData = {
                {"1", "menadzer1", "menadzer1@email.com", "ImeM1", "PrezimeM1", "01-01-2010", "Teretana1"},
                {"2", "menadzer2", "menadzer2@email.com", "ImeM2", "PrezimeM2", "02-02-2011", "Teretana2"},
                {"3", "menadzer3", "menadzer3@email.com", "ImeM3", "PrezimeM3", "03-03-2012", "Teretana3"}
        };
        DefaultTableModel managersModel = new DefaultTableModel(managerData, managerColumns);
        managersTable = new JTable(managersModel);
        managersPanel.add(new JScrollPane(managersTable), BorderLayout.CENTER);
    }

    private void odblokiraj(){
        String username = JOptionPane.showInputDialog(this, "Unesite username:");
        if (username == null) {
            return;
        }
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Morate uneti username!");
            return;
        }


        // izvlacenje reda iz tabele na osnovu username-a
        int row = -1;
        for (int i = 0; i < clientsTable.getRowCount(); i++) {
            if (clientsTable.getValueAt(i, 1).equals(username)) {
                row = i;
                break;
            }
        }
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Ne postoji korisnik sa unetim username-om!");
            return;
        }

        // izvuci mi sve podatke iz tabele na osnovu reda
        String id = clientsTable.getValueAt(row, 0).toString();
        String username1 = clientsTable.getValueAt(row, 1).toString();
        String email = clientsTable.getValueAt(row, 2).toString();
        String firstName = clientsTable.getValueAt(row, 3).toString();
        String lastName = clientsTable.getValueAt(row, 4).toString();
        String dateOfBirth = clientsTable.getValueAt(row, 5).toString();
        String reservedTraining = clientsTable.getValueAt(row, 6).toString();
        String cardNumber = clientsTable.getValueAt(row, 7).toString();
        String isActivated = clientsTable.getValueAt(row, 8).toString();
        String password = clientsTable.getValueAt(row, 9).toString();
        String activationToken = clientsTable.getValueAt(row, 10).toString();
        String role = clientsTable.getValueAt(row, 11).toString();

        UserDto user = new UserDto();
        user.setUsername(username1);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDateOfBirth(dateOfBirth);
        user.setPassword(password);
        user.setRole(role);


        // napravi objekat od tih podataka
        ClientProfileEditorDto client = new ClientProfileEditorDto();
        client.setId(Long.parseLong(id));
        client.setReservedTraining(Integer.valueOf(reservedTraining));
        client.setCardNumber(Integer.valueOf(cardNumber));
        client.setIsActivated(Boolean.valueOf(isActivated));
        client.setActivationToken(activationToken);
        client.setUser(user);







        activationServiceRestTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        messageConverters.add(converter);
        activationServiceRestTemplate.setMessageConverters(messageConverters);


        // Kreirajte header-e za zahtev
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestEntity<ClientProfileEditorDto> requestEntity = RequestEntity.put(URI.create("http://localhost:8080/api/client/activationUpdate")).headers(headers).body(client);


        // Posaljite zahtev
        ResponseEntity<ClientProfileEditorDto> responseEntity = activationServiceRestTemplate.exchange(requestEntity, ClientProfileEditorDto.class);
        Boolean isActivated1 = responseEntity.getBody().getIsActivated(); // ovo je true

        // hocu da isActivated u tabeli bude vrednost isActivated1
        clientsTable.setValueAt(isActivated1, row, 8);
        // refreshujem tabelu
        clientsTable.repaint();
    }

    private void zabrani(){
        String username = JOptionPane.showInputDialog(this, "Unesite username:");
        if (username == null) {
            return;
        }
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Morate uneti username!");
            return;
        }

    }

}
