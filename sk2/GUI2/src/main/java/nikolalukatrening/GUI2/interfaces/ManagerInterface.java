package nikolalukatrening.GUI2.interfaces;

import nikolalukatrening.GUI2.adminViews.AllNotificationsAccess;
import nikolalukatrening.GUI2.adminViews.AllUsersAccess;
import nikolalukatrening.GUI2.managerViews.GymEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ManagerInterface extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JToolBar toolBar;
    private GymEditor gymEditorView;

    public ManagerInterface() {
        // Save the user ID
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

        // Add other views to cardPanel
        cardPanel.add(gymEditorView, "gymEdit");

        // Add buttons to toolbar
        addButtonToToolbar("Uređivanje podataka o fiskulturnoj sali", "gymEdit", "edit.png");

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
        if ("gymEdit".equals(e.getActionCommand()) && gymEditorView == null) {
            gymEditorView = new GymEditor();
            cardPanel.add(gymEditorView, "gymEdit");
        }
        cardLayout.show(cardPanel, e.getActionCommand());
    }

}
