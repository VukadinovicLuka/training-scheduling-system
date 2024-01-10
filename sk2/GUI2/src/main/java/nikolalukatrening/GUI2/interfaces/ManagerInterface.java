package nikolalukatrening.GUI2.interfaces;

import nikolalukatrening.GUI2.adminViews.AllNotificationsAccess;
import nikolalukatrening.GUI2.adminViews.AllUsersAccess;
import nikolalukatrening.GUI2.clientViews.ClientNotifications;
import nikolalukatrening.GUI2.managerViews.GymEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ManagerInterface extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JToolBar toolBar;
    private GymEditor gymEditorView;
    private Integer userId;

    public ManagerInterface(Integer userId) {
        // Save the user ID
        this.userId = userId;
        setTitle("Manager Interfejs");
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

        gymEditorView = new GymEditor();
        gymEditorView.loadManagerData(userId);

        // Add other views to cardPanel
        cardPanel.add(gymEditorView, "gymEdit");

        // Add buttons to toolbar
        addButtonToToolbar("Uređivanje podataka o fiskulturnoj sali", "gymEdit", "edit.png");
        addButtonToToolbar("Poslate notifikacije menadzeru", "notifications", "rate.png");
        // Add toolBar and cardPanel to frame
        add(toolBar, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        // Set default view
        cardLayout.show(cardPanel, "gymEdit");
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
        if ("gymEdit".equals(e.getActionCommand())) {
            gymEditorView = new GymEditor();
            gymEditorView.loadManagerData(userId);
            cardPanel.add(gymEditorView, "gymEdit");
        } else if ("notifications".equals(e.getActionCommand())){
            // TODO: Add notifications view
//            clientNotificationsView = new ClientNotifications();
//            clientNotificationsView.loadNotifications(this.userId);
//            cardPanel.add(clientNotificationsView, "notifications");
        }
        cardLayout.show(cardPanel, e.getActionCommand());
    }

}
