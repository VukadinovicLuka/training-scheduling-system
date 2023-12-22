package nikolalukatrening.GUI2.interfaces;

import nikolalukatrening.GUI2.clientViews.GroupTraining;
import nikolalukatrening.GUI2.clientViews.ProfileEditor;
import nikolalukatrening.GUI2.clientViews.RateCoachOrGym;
import nikolalukatrening.GUI2.clientViews.ScheduleTraining;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ClientInterface extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JToolBar toolBar;
    private ProfileEditor profileEditorView; // This will hold the ProfileEditor instance
    private Integer userId; // This will hold the user ID for the logged-in user

    public ClientInterface(Integer userId) {
        this.userId = userId; // Save the user ID
        setTitle("Korisnički Interfejs");
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
        GroupTraining groupTrainingView = new GroupTraining();
        ScheduleTraining scheduleTrainingView = new ScheduleTraining(groupTrainingView);
        RateCoachOrGym rateCoachOrGymView = new RateCoachOrGym();

        // Add other views to cardPanel
        cardPanel.add(scheduleTrainingView, "schedule");
        cardPanel.add(groupTrainingView, "group");
        cardPanel.add(rateCoachOrGymView, "rate");

        // Add buttons to toolbar
        addButtonToToolbar("Zakazivanje treninga", "schedule", "schedule.png");
        addButtonToToolbar("Pregled i izmena ličnih podataka", "profile", "edit.png");
        addButtonToToolbar("Prijavljivanje na grupne treninge", "group", "group.png");
        addButtonToToolbar("Ocena trenera ili teretane", "rate", "rate.png");

        // Add toolBar and cardPanel to frame
        add(toolBar, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        // Set default view
        cardLayout.show(cardPanel, "schedule");
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
        if ("profile".equals(e.getActionCommand()) && profileEditorView == null) {
            profileEditorView = new ProfileEditor(); // Create the ProfileEditor instance
            profileEditorView.loadProfileData(this.userId); // Load the data for the logged-in user
            cardPanel.add(profileEditorView, "profile"); // Add ProfileEditor to the card panel
        }
        cardLayout.show(cardPanel, e.getActionCommand());
    }
}
