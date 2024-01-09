package nikolalukatrening.GUI2.managerViews;

import javax.swing.*;
import java.awt.*;

public class GymEditor extends JPanel {
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField numberOfPersonalTrainersField;
    private JTextField trainingTypeField;
    private JTextField trainingTypePriceField;
    private JButton saveButton;

    public GymEditor() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        nameField = new JTextField(20);
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        numberOfPersonalTrainersField = new JTextField(20);
        trainingTypeField = new JTextField(20);
        trainingTypePriceField = new JTextField(20);
        saveButton = new JButton("Sačuvaj");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(new JLabel("Ime sale:"), gbc);

        gbc.gridy++;
        add(nameField, gbc);

        gbc.gridy++;
        add(new JLabel("Opis:"), gbc);

        gbc.gridy++;
        add(new JScrollPane(descriptionArea), gbc);

        gbc.gridy++;
        add(new JLabel("Broj personalnih trenera:"), gbc);

        gbc.gridy++;
        add(numberOfPersonalTrainersField, gbc);

        gbc.gridy++;
        add(new JLabel("Tip treninga:"), gbc);

        gbc.gridy++;
        add(trainingTypeField, gbc);

        gbc.gridy++;
        add(new JLabel("Cena treninga:"), gbc);

        gbc.gridy++;
        add(trainingTypePriceField, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            // Logika za snimanje izmena
            // Ovde biste pozvali REST servis ili neki drugi mehanizam za ažuriranje podataka
        });
    }
}
