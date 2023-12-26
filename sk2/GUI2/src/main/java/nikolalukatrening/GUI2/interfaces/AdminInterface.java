package nikolalukatrening.GUI2.interfaces;

import nikolalukatrening.GUI2.adminViews.AllNotificationsAccess;
import nikolalukatrening.GUI2.adminViews.AllUsersAccess;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminInterface extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JToolBar toolBar;
    private AllUsersAccess allUsersAccess;
    private AllNotificationsAccess allNotificationsAccess;

    public AdminInterface() {
         // Save the user ID
        setTitle("Admin Interfejs");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        toolBar = new JToolBar();

        // Initialize other views

        allUsersAccess = new AllUsersAccess(this);
        allNotificationsAccess = new AllNotificationsAccess();

        // Add other views to cardPanel
        cardPanel.add(allUsersAccess, "access");
        cardPanel.add(allNotificationsAccess, "notifications");

        // Add buttons to toolbar
        addButtonToToolbar("Uvid u sve korisnike", "access", "schedule.png");
        addButtonToToolbar("Pregled svih notifikacija", "notifications", "edit.png");

        // Add toolBar and cardPanel to frame
        add(toolBar, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        // Set default view
        cardLayout.show(cardPanel, "access");
    }

    private void addButtonToToolbar(String buttonText, String actionCommand, String iconPath) {
        JButton button = new JButton();
        button.setToolTipText(buttonText);
        button.setActionCommand(actionCommand);
        if (!iconPath.isEmpty()) {
            ImageIcon icon = new ImageIcon(iconPath);
            button.setIcon(icon);
        }
        button.addActionListener(this::toolbarButtonClicked);
        toolBar.add(button);
    }

    private void toolbarButtonClicked(ActionEvent e) {
        if ("access".equals(e.getActionCommand()) && allUsersAccess == null) {
            allUsersAccess = new AllUsersAccess(this); // Create the ProfileEditor instance
            // Load the data for the logged-in user
            cardPanel.add(allUsersAccess, "access"); // Add ProfileEditor to the card panel
        }else if ("notifications".equals(e.getActionCommand()) && allUsersAccess == null){
            allNotificationsAccess = new AllNotificationsAccess();
            cardPanel.add(allNotificationsAccess, "notifications");
        }
        cardLayout.show(cardPanel, e.getActionCommand());
    }

}
