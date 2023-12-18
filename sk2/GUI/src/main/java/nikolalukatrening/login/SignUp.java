package nikolalukatrening.login;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
public class SignUp extends JFrame {

    private JPanel jPanel1;
    private JButton jButton1, jButton2;
    private JLabel jLabel4, jLabel5, jLabel6, jLabel7, jLabel8,jlabel9,jlabel10,jlabel11;
    private JPasswordField jPasswordField1;
    private JTextField jTextField1, jTextField2,jTextField3,jTextField4,jTextField5 ;

    public SignUp() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sign Up");
        setPreferredSize(new Dimension(400, 700));

        jPanel1 = new JPanel(new GridBagLayout());
        jPanel1.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        jLabel4 = new JLabel("SIGN UP");
        jLabel4.setFont(new Font("Segoe UI", Font.BOLD, 36));
        jLabel4.setForeground(new Color(0, 102, 102));

        jLabel5 = new JLabel("Name:");
        jLabel5.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jTextField1 = new JTextField(20);
        jTextField1.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jlabel9 = new JLabel("Lastname:");
        jlabel9.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jTextField2 = new JTextField(20);
        jTextField2.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jlabel10 = new JLabel("Date of birth:");
        jlabel10.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jTextField3 = new JTextField(20);
        jTextField3.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jLabel6 = new JLabel("Email:");
        jLabel6.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jTextField4 = new JTextField(20);
        jTextField4.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jlabel11 = new JLabel("Username:");
        jlabel11.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jTextField5 = new JTextField(20);
        jTextField5.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jLabel7 = new JLabel("Password");
        jLabel7.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jPasswordField1 = new JPasswordField(20);
        jPasswordField1.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jButton1 = new JButton("Sign Up");
        jButton1.setBackground(new Color(0, 102, 102));
        jButton1.setForeground(Color.WHITE);
        jButton1.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jLabel8 = new JLabel("I've an account");
        jLabel8.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jButton2 = new JButton("Login");
        jButton2.setForeground(new Color(255, 51, 51));
        jButton2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jButton2.addActionListener(evt -> jButton2ActionPerformed());

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        jPanel1.add(jLabel4, gbc);
        jPanel1.add(jLabel5, gbc);
        jPanel1.add(jTextField1, gbc);
        jPanel1.add(jlabel9, gbc);
        jPanel1.add(jTextField2, gbc);
        jPanel1.add(jlabel10, gbc);
        jPanel1.add(jTextField3, gbc);
        jPanel1.add(jLabel6, gbc);
        jPanel1.add(jTextField4, gbc);
        jPanel1.add(jlabel11, gbc);
        jPanel1.add(jTextField5, gbc);
        jPanel1.add(jLabel7, gbc);
        jPanel1.add(jPasswordField1, gbc);
        jPanel1.add(jButton1, gbc);

        gbc.fill = GridBagConstraints.NONE;
        jPanel1.add(jLabel8, gbc);
        jPanel1.add(jButton2, gbc);

        add(jPanel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void jButton2ActionPerformed() {
        // Kreiranje instance Login prozora
        Login loginFrame = new Login();
        // Postavljanje Login prozora da bude vidljiv
        loginFrame.setVisible(true);
        // Zatvaranje trenutnog (SignUp) prozora
        this.dispose();
    }
}
