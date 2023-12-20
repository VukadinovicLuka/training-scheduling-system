package nikolalukatrening.GUI2.clientViews;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GroupTraining extends JPanel {

    private JTable trainingTable;
    private DefaultTableModel tableModel;

    public GroupTraining() {
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // Definisanje kolona za tabelu
        String[] columnNames = {"Id","Vrsta treninga", "Dan", "Tip treninga", "Vreme","Broj clanova"};

        // Kreiranje modela tabele sa definisanim kolonama
        tableModel = new DefaultTableModel(columnNames, 0);
        trainingTable = new JTable(tableModel);

        // Omogućavanje scroll-a za tabelu
        JScrollPane scrollPane = new JScrollPane(trainingTable);
        trainingTable.setFillsViewportHeight(true);

        // Dodavanje scrollPane-a (sa tabelom) u centralni deo panela
        add(scrollPane, BorderLayout.CENTER);

        // Početni set podataka za tabelu (ako je potrebno)
        addDummyData();
    }

    private void addDummyData() {
        // Ovde možete dodati početne podatke u tabelu
        // Na primer:
        tableModel.addRow(new Object[]{"1","Grupno", "Ponedeljak", "Joga", "08:00",5});
        tableModel.addRow(new Object[]{"2","Individualno", "Utorak", "Powerlifting", "10:00",1});
    }

    public void addTrainingRow(String trainingType, String day, String trainingOption, String time, int numberOfMembers) {
        int nextId = tableModel.getRowCount() + 1;
        tableModel.addRow(new Object[]{nextId, trainingType, day, trainingOption, time, numberOfMembers});
    }
}

