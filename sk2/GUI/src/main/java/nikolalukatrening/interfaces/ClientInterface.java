package nikolalukatrening.interfaces;

import nikolalukatrening.clientViews.GroupTraining;
import nikolalukatrening.clientViews.ProfileEditor;
import nikolalukatrening.clientViews.RateCoachOrGym;
import nikolalukatrening.clientViews.ScheduleTraining;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ClientInterface extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JToolBar toolBar;

    public ClientInterface() {
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

        // Kreirajte instance view-a
        ProfileEditor profileEditorView = new ProfileEditor();
        GroupTraining groupTrainingView = new GroupTraining();
        ScheduleTraining scheduleTrainingView = new ScheduleTraining(groupTrainingView);
        RateCoachOrGym rateCoachOrGymView = new RateCoachOrGym();

        // Dodajte view-ove u cardPanel
        cardPanel.add(scheduleTrainingView, "schedule");
        cardPanel.add(profileEditorView, "profile");
        cardPanel.add(groupTrainingView, "group");
        cardPanel.add(rateCoachOrGymView, "rate");

        // Dodajte dugmiće u toolbar
        addButtonToToolbar("Zakazivanje treninga", "schedule", "D:\\\\LukaFakultet\\\\Komponente\\\\sk2-teamNikolaLuka\\\\sk2\\\\GUI\\\\src\\\\main\\\\resources\\\\schedule.png");
        addButtonToToolbar("Pregled i izmena ličnih podataka", "profile", "D:\\\\LukaFakultet\\\\Komponente\\\\sk2-teamNikolaLuka\\\\sk2\\\\GUI\\\\src\\\\main\\\\resources\\\\izmena.jpeg");
        addButtonToToolbar("Prijavljivanje na grupne treninge", "group", "D:\\\\LukaFakultet\\\\Komponente\\\\sk2-teamNikolaLuka\\\\sk2\\\\GUI\\\\src\\\\main\\\\resources\\\\grupa.jpg");
        addButtonToToolbar("Ocena trenera ili teretane", "rate", "D:\\\\LukaFakultet\\\\Komponente\\\\sk2-teamNikolaLuka\\\\sk2\\\\GUI\\\\src\\\\main\\\\resources\\\\ocena.png");

        // Dodajte toolBar i cardPanel u frame
        add(toolBar, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        // Postavite defaultni view
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
        cardLayout.show(cardPanel, e.getActionCommand());
    }

}

