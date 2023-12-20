package nikolalukatrening.GUI2.login;

import lombok.Getter;
import lombok.Setter;
import nikolalukatrening.GUI2.interfaces.AdminInterface;
import nikolalukatrening.GUI2.interfaces.ClientInterface;


import javax.swing.*;
import java.awt.*;

@Getter
@Setter

public class Login extends JFrame {

    private JPanel jPanel1;
    private JButton jButton1, jButton2;
    private JLabel jLabel1, jLabel2, jLabel3, jLabel4;
    private JPasswordField jPasswordField1;
    private JTextField jTextField1;

    public Login() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("LOGIN");
        setPreferredSize(new Dimension(400, 500));

        jPanel1 = new JPanel(new GridBagLayout());
        jPanel1.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        jLabel1 = new JLabel("LOGIN");
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 36));
        jLabel1.setForeground(new Color(0, 102, 102));

        jLabel2 = new JLabel("Email");
        jLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jTextField1 = new JTextField(20);
        jTextField1.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jLabel3 = new JLabel("Password");
        jLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jPasswordField1 = new JPasswordField(20);
        jPasswordField1.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jButton1 = new JButton("Login");
        jButton1.setBackground(new Color(0, 102, 102));
        jButton1.setForeground(Color.WHITE);
        jButton1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jButton1.addActionListener(evt->jButton1ActionPerformed());

        jLabel4 = new JLabel("I don't have an account");
        jLabel4.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jButton2 = new JButton("Sign Up");
        jButton2.setForeground(new Color(255, 51, 51));
        jButton2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jButton2.addActionListener(evt -> jButton2ActionPerformed());

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        jPanel1.add(jLabel1, gbc);
        jPanel1.add(jLabel2, gbc);
        jPanel1.add(jTextField1, gbc);
        jPanel1.add(jLabel3, gbc);
        jPanel1.add(jPasswordField1, gbc);
        jPanel1.add(jButton1, gbc);

        gbc.fill = GridBagConstraints.NONE;
        jPanel1.add(jLabel4, gbc);
        jPanel1.add(jButton2, gbc);

        add(jPanel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void jButton2ActionPerformed() {
        // Kreiranje instance SignUp prozora
        SignUp signUpFrame = new SignUp();
        // Postavljanje SignUp prozora da bude vidljiv
        signUpFrame.setVisible(true);
        // Zatvaranje trenutnog (Login) prozora
        this.dispose();
    }

    private void jButton1ActionPerformed(){
        if(jTextField1.getText().equals("admin")){
            AdminInterface adminFrame = new AdminInterface();
            adminFrame.setVisible(true);
            this.dispose();
        } else if(jTextField1.getText().equals("client")){
            ClientInterface clientInterface = new ClientInterface();
            clientInterface.setVisible(true);
            this.dispose();
        } else if(jTextField1.getText().equals("manager")){
            //ManagerInterface managerFrame = new ManagerInterface();
            //managerFrame.setVisible(true);
            this.dispose();
        } else {
            this.dispose();
        }
    }
}
