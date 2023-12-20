package nikolalukatrening.GUI2.interfaces;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class AdminInterface extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    private JToolBar toolBar = new JToolBar();
    private JPanel clientsPanel = new JPanel(new BorderLayout());
    private JPanel managersPanel = new JPanel(new BorderLayout());
    private Set<Integer> highlightedRowsClients = new HashSet<>();
    private Set<Integer> highlightedRowsManagers = new HashSet<>();
    private JTable clientsTable;
    private JTable managersTable;

    public AdminInterface() {
        setTitle("Admin Interfejs");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setupToolbar();
        setupClientsPanel();
        setupManagersPanel();
    }

    private void initComponents() {
        add(toolBar, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        cardPanel.add(clientsPanel, "Clients");
        cardPanel.add(managersPanel, "Managers");
    }

    private void setupToolbar() {
        JButton clientsButton = new JButton("Klijenti");
        clientsButton.addActionListener(e -> cardLayout.show(cardPanel, "Clients"));
        toolBar.add(clientsButton);

        JButton managersButton = new JButton("Menadžeri");
        managersButton.addActionListener(e -> cardLayout.show(cardPanel, "Managers"));
        toolBar.add(managersButton);

        JButton searchButton = new JButton("Zabrani");
        searchButton.addActionListener(e -> highlightRow());
        toolBar.add(searchButton);

        JButton resetColorButton = new JButton("Odblokiraj");
        resetColorButton.addActionListener(e -> resetRowColor());
        toolBar.add(resetColorButton);
    }

    private void setupClientsPanel() {
        String[] clientColumns = new String[]{"id", "username", "email", "firstName", "lastName", "dateOfBirth", "reservedTraining"};
        Object[][] clientData = {
                {"1", "klijent1", "klijent1@email.com", "Ime1", "Prezime1", "01-01-1990", "Da"},
                {"2", "klijent2", "klijent2@email.com", "Ime2", "Prezime2", "02-02-1991", "Ne"},
                {"3", "klijent3", "klijent3@email.com", "Ime3", "Prezime3", "03-03-1992", "Da"}
        };
        DefaultTableModel clientsModel = new DefaultTableModel(clientData, clientColumns);
        clientsTable = new JTable(clientsModel);
        clientsPanel.add(new JScrollPane(clientsTable), BorderLayout.CENTER);
    }

    private void setupManagersPanel() {
        String[] managerColumns = new String[]{"id", "username", "email", "firstName", "lastName", "dateOfHiring", "gymName"};
        Object[][] managerData = {
                {"1", "menadzer1", "menadzer1@email.com", "ImeM1", "PrezimeM1", "01-01-2010", "Teretana1"},
                {"2", "menadzer2", "menadzer2@email.com", "ImeM2", "PrezimeM2", "02-02-2011", "Teretana2"},
                {"3", "menadzer3", "menadzer3@email.com", "ImeM3", "PrezimeM3", "03-03-2012", "Teretana3"}
        };
        DefaultTableModel managersModel = new DefaultTableModel(managerData, managerColumns);
        managersTable = new JTable(managersModel);
        managersPanel.add(new JScrollPane(managersTable), BorderLayout.CENTER);
    }

    private void highlightRowByUsername(JTable table, String username, Set<Integer> highlightedRows) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int usernameColumn = -1;
        for (int i = 0; i < model.getColumnCount(); i++) {
            if ("username".equals(model.getColumnName(i))) {
                usernameColumn = i;
                break;
            }
        }
        if (usernameColumn != -1) {
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, usernameColumn).equals(username)) {
                    highlightedRows.add(i);
                }
            }
        }

        table.clearSelection();
        for (Integer row : highlightedRows) {
            table.addRowSelectionInterval(row, row);
        }
        table.setSelectionBackground(Color.RED);
    }

    private void highlightRow() {
        String username = JOptionPane.showInputDialog(this, "Unesite username:");
        if (username != null && !username.trim().isEmpty()) {
            highlightRowByUsername(clientsTable, username, highlightedRowsClients);
            highlightRowByUsername(managersTable, username, highlightedRowsManagers);
        }
    }

    private void resetRowColor() {
        String username = JOptionPane.showInputDialog(this, "Unesite username za resetovanje boje:");
        if (username != null && !username.trim().isEmpty()) {
            resetRowColorByUsername(clientsTable, username, highlightedRowsClients);
            resetRowColorByUsername(managersTable, username, highlightedRowsManagers);
        }
    }

    private void resetRowColorByUsername(JTable table, String username, Set<Integer> highlightedRows) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int usernameColumn = -1;
        boolean isHighlighted = false;

        for (int i = 0; i < model.getColumnCount(); i++) {
            if ("username".equals(model.getColumnName(i))) {
                usernameColumn = i;
                break;
            }
        }

        if (usernameColumn != -1) {
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, usernameColumn).equals(username)) {
                    if (highlightedRows.contains(i)) {
                        highlightedRows.remove(i);
                        isHighlighted = true;
                    }
                    break; // Prekidamo petlju jer smo našli odgovarajući red
                }
            }
        }
        // Osvežavanje prikaza
        table.clearSelection();
        for (Integer row : highlightedRows) {
            table.addRowSelectionInterval(row, row);
        }
        table.setSelectionBackground(Color.RED);

        // Prikazivanje poruke ako korisnik nije bio blokiran
        if (!isHighlighted) {
            JOptionPane.showMessageDialog(this, "Korisnik '" + username + "' nije blokiran.", "Informacija", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
