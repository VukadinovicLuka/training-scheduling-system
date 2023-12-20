package nikolalukatrening.GUI2.clientViews;


import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

@Getter
@Setter
    public class ScheduleTraining extends JPanel {
        private JLabel lblTrainingType, lblDayOfWeek, lblTrainingOptions, lblTime;
        private JComboBox<String> cbTrainingType, cbDayOfWeek, cbTrainingOptions, cbTime;
        private JButton btnBook;
        private Font labelFont = new Font("Arial", Font.BOLD, 16);
        private Font comboFont = new Font("Arial", Font.PLAIN, 16);
        private Font buttonFont = new Font("Arial", Font.BOLD, 16);
        private GroupTraining groupTrainingReference;
        public ScheduleTraining(GroupTraining groupTrainingReference) {
            this.groupTrainingReference = groupTrainingReference;
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(15, 15, 15, 15); // Dodaje razmak između komponenti

            JLabel lblTitle = new JLabel("Zakazivanje treninga");
            lblTitle.setFont(new Font("Arial", Font.BOLD, 24)); // Postavljanje većeg fonta za naslov
            lblTitle.setHorizontalAlignment(JLabel.CENTER); // Centriranje teksta naslova

            // Postavljanje naslova na panelu koristeći GridBagConstraints
            gbc.gridx = 0; // Početna tačka X
            gbc.gridy = 0; // Početna tačka Y
            gbc.gridwidth = 2; // Naslov se prostire preko dva stupca
            gbc.anchor = GridBagConstraints.CENTER; // Centriranje naslova u prostoru
            add(lblTitle, gbc);

            lblTrainingType = createLabel("Odaberite vrstu treninga:");
            cbTrainingType = createComboBox(new String[]{"Individualno", "Grupno"});
            lblDayOfWeek = createLabel("Odaberite dan u nedelji:");
            cbDayOfWeek = createComboBox(new String[]{"Ponedeljak", "Utorak", "Sreda", "Četvrtak", "Petak", "Subota", "Nedelja"});
            lblTrainingOptions = createLabel("Tip treninga:");
            cbTrainingOptions = createComboBox(new String[]{}); // Opcije će biti dodate dinamički
            lblTime = createLabel("Vreme:");
            cbTime = createComboBox(new String[]{"08:00", "09:00", "10:00", "11:00"});
            btnBook = createButton("Zakaži");

            // Postavljanje komponenti na panelu koristeći GridBagLayout
            gbc.gridwidth = 1;

            // Odabir vrste treninga
            gbc.gridx = 0;
            gbc.gridy = 1;
            add(lblTrainingType, gbc);
            gbc.gridx = 1;
            add(cbTrainingType, gbc);

            // Odabir dana u nedelji
            gbc.gridx = 0;
            gbc.gridy = 2;
            add(lblDayOfWeek, gbc);
            gbc.gridx = 1;
            add(cbDayOfWeek, gbc);

            // Tip treninga
            gbc.gridx = 0;
            gbc.gridy = 3;
            add(lblTrainingOptions, gbc);
            gbc.gridx = 1;
            add(cbTrainingOptions, gbc);

            // Vreme
            gbc.gridx = 0;
            gbc.gridy = 4;
            add(lblTime, gbc);
            gbc.gridx = 1;
            add(cbTime, gbc);

            // Dugme za zakazivanje
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2; // Dugme se prostire preko dva stupca
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(btnBook, gbc);

            // Add action listeners to handle the dynamic changes
            cbTrainingType.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    // Proveravamo da li je događaj za selekciju
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        updateTrainingOptions();
                    }
                }
            });

            // Initialize options
            updateTrainingOptions();
            styleComponents();
        }

        private void updateTrainingOptions() {
            cbTrainingOptions.removeAllItems();
            Object selectedItem = cbTrainingType.getSelectedItem();
            if (selectedItem != null) {
                String selectedType = selectedItem.toString();
                if ("Individualno".equals(selectedType)) {
                    cbTrainingOptions.addItem("Kalistenika");
                    cbTrainingOptions.addItem("Powerlifting");
                } else if ("Grupno".equals(selectedType)) {
                    cbTrainingOptions.addItem("Joga");
                    cbTrainingOptions.addItem("Pilates");
                }
            }
        }

        private JLabel createLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(labelFont);
            return label;
        }

        private JComboBox<String> createComboBox(String[] items) {
            JComboBox<String> comboBox = new JComboBox<>(items);
            comboBox.setFont(comboFont);
            return comboBox;
        }

        private JButton createButton(String text) {
            JButton button = new JButton(text);
            button.setFont(buttonFont);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    bookTraining();
                }
            });
            return button;
        }

    private void bookTraining() {
        // Prikupljanje informacija iz ComboBox-ova i TextField-ova
        String trainingType = cbTrainingType.getSelectedItem().toString();
        String day = cbDayOfWeek.getSelectedItem().toString();
        String trainingOption = cbTrainingOptions.getSelectedItem().toString();
        String time = cbTime.getSelectedItem().toString();
        int numberOfMembers = trainingType.equals("Individualno") ? 1 : (int) (Math.random() * 12 + 1);

        // Dodavanje reda u GroupTraining tabelu
        groupTrainingReference.addTrainingRow(trainingType, day, trainingOption, time, numberOfMembers);
    }
        private void styleComponents() {
            setBackground(Color.WHITE); // Postavite boju pozadine panela na belo
            setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Dodajte ivicu oko panela

            lblTrainingType.setFont(new Font("Arial", Font.BOLD, 16));
            lblDayOfWeek.setFont(new Font("Arial", Font.BOLD, 16));
            lblTrainingOptions.setFont(new Font("Arial", Font.BOLD, 16));
            lblTime.setFont(new Font("Arial", Font.BOLD, 16));

            cbTrainingType.setFont(new Font("Arial", Font.PLAIN, 14));
            cbDayOfWeek.setFont(new Font("Arial", Font.PLAIN, 14));
            cbTrainingOptions.setFont(new Font("Arial", Font.PLAIN, 14));
            cbTime.setFont(new Font("Arial", Font.PLAIN, 14));

            btnBook.setFont(new Font("Arial", Font.BOLD, 14));
            btnBook.setBackground(new Color(0, 123, 255));
            btnBook.setForeground(Color.WHITE);
        }

    }






