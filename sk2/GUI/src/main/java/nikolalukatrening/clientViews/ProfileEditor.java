package nikolalukatrening.clientViews;

import javax.swing.*;
import java.awt.*;


public class ProfileEditor extends JPanel {

    public ProfileEditor() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Naslov
        JLabel lblTitle = new JLabel("Izmena Profila");
        lblTitle.setFont(new Font("Serif", Font.BOLD, 24));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitle, gbc);

        // Username
        addLabelAndTextField("Korisničko ime:", 1, "username");
        // Lastname
        addLabelAndTextField("Prezime:", 2, "lastname");
        // Firstname
        addLabelAndTextField("Ime:", 3, "firstname");
        // Password
        addLabelAndTextField("Lozinka:", 4, "password");
        // Date of Birth
        addLabelAndTextField("Datum rođenja:", 5, "dateofbirth");
        // Card Number
        addLabelAndTextField("Broj kartice:", 6, "cardnumber");
        // Reserved Trainings
        addLabelAndTextField("Rezervisani treninzi:", 7, "reservedtrainings");
        // Email
        addLabelAndTextField("E-mail:", 8, "email");

        // Dugme za potvrdu izmena
        JButton btnConfirm = new JButton("Potvrdi izmene");
        btnConfirm.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnConfirm.setBackground(new Color(0, 150, 136));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFocusPainted(false);
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnConfirm, gbc);
    }

    private void addLabelAndTextField(String labelText, int gridy, String fieldName) {
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(5, 0, 5, 10);
        add(label, gbc);

        JTextField textField = new JTextField(15);
        textField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        gbc.gridx = 1;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(textField, gbc);
    }

}
