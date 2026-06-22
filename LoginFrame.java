import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame extends JFrame {

    private HotelSystem system;
    private DatabaseManager db;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginFrame(HotelSystem system, DatabaseManager db) {
        this.system = system;
        this.db = db;
        buildUI();
        UITheme.applyRTL(getContentPane());
        setVisible(true);
    }

    private void buildUI() {
        setTitle(Strings.LOGIN_WINDOW_TITLE);
        setSize(500, 480);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.PRIMARY);
        setLayout(new BorderLayout());

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(UITheme.PRIMARY);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JPanel card = new JPanel();
        card.setBackground(UITheme.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        JLabel titleLabel = new JLabel(Strings.LOGIN_TITLE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(UITheme.SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLabel = new JLabel(Strings.LOGIN_SUBTITLE);
        subLabel.setFont(UITheme.FONT_SMALL);
        subLabel.setForeground(Color.GRAY);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel(Strings.LOGIN_USERNAME);
        userLabel.setFont(UITheme.FONT_LABEL);
        userLabel.setForeground(UITheme.TEXT_DARK);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        UITheme.styleTextField(usernameField);

        JLabel passLabel = new JLabel(Strings.LOGIN_PASSWORD);
        passLabel.setFont(UITheme.FONT_LABEL);
        passLabel.setForeground(UITheme.TEXT_DARK);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        UITheme.stylePasswordField(passwordField);

        JButton loginBtn = new JButton(Strings.LOGIN_BUTTON);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginBtn.setBackground(UITheme.PRIMARY);
        loginBtn.setForeground(UITheme.SECONDARY);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        messageLabel = new JLabel(" ");
        messageLabel.setFont(UITheme.FONT_SMALL);
        messageLabel.setForeground(UITheme.DANGER);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(subLabel);
        card.add(Box.createVerticalStrut(24));
        card.add(userLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(14));
        card.add(passLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(10));
        card.add(messageLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(loginBtn);

        outerPanel.add(card, BorderLayout.CENTER);
        add(outerPanel, BorderLayout.CENTER);

        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        passwordField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText(Strings.LOGIN_ERROR_EMPTY);
            return;
        }

        try {
            User user = system.login(username, password);
            new DashboardFrame(system, db, user);
            dispose();
        } catch (InvalidLoginException ex) {
            messageLabel.setText(ex.getMessage());
            passwordField.setText("");
        }
    }
}
