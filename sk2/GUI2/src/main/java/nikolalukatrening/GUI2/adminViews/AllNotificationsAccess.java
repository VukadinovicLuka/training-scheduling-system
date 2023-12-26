package nikolalukatrening.GUI2.adminViews;

import nikolalukatrening.GUI2.customTable.CustomTable;
import nikolalukatrening.GUI2.dto.NotificationDto;
import nikolalukatrening.GUI2.service.impl.AdminServiceImpl;
import nikolalukatrening.GUI2.service.impl.RestTemplateServiceImpl;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AllNotificationsAccess extends JPanel {

    private DefaultTableModel tableModel;
    private JTable notificationTable;
    private RestTemplate restTemplate;
    private final String ALL_NOTIFICATIONS_URL = "http://localhost:8081/api/notifications/all";
    private final String NOTIFICATIONS_BY_TYPE_URL = "http://localhost:8081/api/notifications/type?notificationType=";
    private final String NOTIFICATIONS_BY_EMAIL_URL = "http://localhost:8081/api/notifications/receiver?receiver=";

    public AllNotificationsAccess() {
        setLayout(new BorderLayout());
        initializeComponents();
        setUpAllPanel(); // Fetch and display all notifications by default
    }

    private void initializeComponents() {
        // Table initialization
        String[] columnNames = {"Id", "Ime", "Prezime", "Link", "Primalac maila", "Vrsta notifikacije"};
        tableModel = new DefaultTableModel(columnNames, 0);
        notificationTable = new JTable(tableModel);
        notificationTable.setFillsViewportHeight(true);

        // Toolbar initialization
        JToolBar toolBar = new JToolBar();
        JButton allButton = new JButton("Sve");
        JButton typeButton = new JButton("Tip");
        JButton emailButton = new JButton("Email");

        allButton.addActionListener(e -> setUpAllPanel());
        typeButton.addActionListener(e -> setUpTypePanel());
        emailButton.addActionListener(e->setUpEmailPanel());

        toolBar.add(allButton);
        toolBar.add(typeButton);
        toolBar.add(emailButton);

        // Add components to panel
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(notificationTable), BorderLayout.CENTER);
    }

    private void setUpAllPanel() {
        fetchData(ALL_NOTIFICATIONS_URL);
    }

    private void setUpTypePanel() {
        String type = JOptionPane.showInputDialog(this, "Unesite tip koji zelite da pretrazite:");
        if (type != null && !type.trim().isEmpty()) {
            fetchData(NOTIFICATIONS_BY_TYPE_URL + type);
        }
    }

    private void setUpEmailPanel(){
        String email = JOptionPane.showInputDialog(this, "Unesite email koji zelite da pretrazite:");
        if (email != null && !email.trim().isEmpty()) {
            fetchData(NOTIFICATIONS_BY_EMAIL_URL + email);
        }
    }

    private void fetchData(String url) {
        restTemplate = new RestTemplate(); // Replace with your setupRestTemplate method if needed
        ResponseEntity<List<NotificationDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<NotificationDto>>() {}
        );

        List<NotificationDto> notifications = response.getBody();
        updateTableModel(notifications);
    }

    private void updateTableModel(List<NotificationDto> notifications) {
        clearTableModel();
        if (notifications != null) {
            for (NotificationDto notification : notifications) {
                Object[] row = new Object[]{
                        notification.getId(),
                        notification.getFirstName(),
                        notification.getLastName(),
                        notification.getLink(),
                        notification.getReceiver(),
                        notification.getType(),
                };
                tableModel.addRow(row);
            }
        }
        // Notify the table that its model has changed
        notificationTable.setModel(tableModel);
    }

    private void clearTableModel() {
        tableModel.setRowCount(0);
    }
}
