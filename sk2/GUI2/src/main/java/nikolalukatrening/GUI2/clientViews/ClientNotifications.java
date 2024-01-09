package nikolalukatrening.GUI2.clientViews;

import nikolalukatrening.GUI2.dto.ClientProfileEditorDto;
import nikolalukatrening.GUI2.dto.NotificationDto;
import nikolalukatrening.GUI2.service.impl.RestTemplateServiceImpl;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class ClientNotifications extends JPanel {

    private JTable trainingTable;
    private DefaultTableModel tableModel;
    private RestTemplate notificationRestTemplate;
    private RestTemplateServiceImpl restTemplateService;

    public ClientNotifications() {
        restTemplateService = new RestTemplateServiceImpl();
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // Definisanje kolona za tabelu
        String[] columnNames = {"Id","Ime", "Prezime", "Link", "Password","Primalac maila", "Vrsta notifikacije"};

        // Kreiranje modela tabele sa definisanim kolonama
        tableModel = new DefaultTableModel(columnNames, 0);
        trainingTable = new JTable(tableModel);

        // Omogućavanje scroll-a za tabelu
        JScrollPane scrollPane = new JScrollPane(trainingTable);
        trainingTable.setFillsViewportHeight(true);

        // Dodavanje scrollPane-a (sa tabelom) u centralni deo panela
        add(scrollPane, BorderLayout.CENTER);
    }

// TODO: kada se promeni lozinka, ne radi refresh tabele

    public void loadNotifications(Integer id){
        // id je id ulogovanog korisnika
        notificationRestTemplate = restTemplateService.setupRestTemplate(notificationRestTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<List<NotificationDto>> notificationResponse = notificationRestTemplate.exchange(
                "http://localhost:8081/api/notifications/" +id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<NotificationDto>>() {});

        if (notificationResponse.getStatusCode() == HttpStatus.OK) {
            List<NotificationDto> notificationsForClient = notificationResponse.getBody();
            System.out.println("NOTIFIKACIJE: " + notificationsForClient);
            for (NotificationDto notification : notificationsForClient) {
                  tableModel.addRow(new Object[]{notification.getId(), notification.getFirstName(),
                          notification.getLastName(), notification.getLink(), notification.getPassword(), notification.getReceiver(), notification.getType()});
            }
        } else {
            // Obavestite korisnika da notifikacije nisu pronađene
            JOptionPane.showMessageDialog(null, "Notifikacije nisu pronađene.");
        }
    }
}
