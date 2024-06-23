package login;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    public Login() {
        setTitle("chat app ");
        setSize(350, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);


        ImageIcon logoIcon = new ImageIcon("logo.png");
        JLabel logoLabel = new JLabel(logoIcon);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        add(logoLabel, c);
        //tiêu đề
        JLabel titleLabel = new JLabel("Welcome to chat app");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        c.gridy = 1;
        add(titleLabel, c);
        usernameField = new JTextField(20);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(usernameField, c);
        passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        c.gridy = 3;
        add(passwordField, c);


        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(246, 149, 203));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                statusLabel.setText("Đăng nhập thành công ! ");


            }
        });
        c.gridy = 4;
        add(loginButton, c);
        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.BLACK);
        c.gridy = 5;
        add(statusLabel, c);


        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(131, 157, 211));


            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(246, 149, 203));

            }

            ;
        });


    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (authenticate(username, password)) {
            statusLabel.setText("Đăng nhập thành công ");
            launchChapApp(username);

        } else {
            statusLabel.setText("Đăng nhập thất bại. Vui lòng thử lại");
        }

    }

    private boolean authenticate(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();

    }

    private void launchChapApp(String username) {
        SwingUtilities.invokeLater(() -> {
            Client chatclient = new Client(username);
            chatclient.setVisible(true);

            this.dispose();
        });
        loginButton.addActionListener(e -> performLogin());
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);


        });
    }
}



