import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

/**
 * Interfaccia grafica moderna per il sistema Ado-Transfert
 * Implementa una GUI completa con design professionale
 */
public class AppGUI extends JFrame {
    private static final String SERVER_HOST = System.getenv().getOrDefault("RAILWAY_PUBLIC_DOMAIN", "localhost");
    private static final int SERVER_PORT = System.getenv().getOrDefault("PORT", 1099);
    private static final String SERVICE_NAME = "AdoTransfertService";
    
    private InterfaceTransfer serverStub;
    private String currentUserID = null;
    private String currentUserType = null;
    private String currentUserName = null;
    
    // Componenti principali
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel statusLabel;
    private JLabel balanceLabel;
    
    // Colori del tema
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204);
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color ERROR_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new AppGUI().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Errore nell'avvio dell'applicazione: " + e.getMessage(), 
                    "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    public AppGUI() {
        initializeGUI();
        connectToServer();
    }
    
    private void initializeGUI() {
        setTitle("Ado-Transfert - Sistema di Gestione Transazioni");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Layout principale
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.WHITE);
        
        // Pannello superiore con status
        JPanel topPanel = createTopPanel();
        
        // Crea le diverse schermate
        createLoginScreen();
        createRegistrationScreen();
        createClientDashboard();
        createAdminDashboard();
        
        // Layout finale
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Mostra la schermata di login inizialmente
        cardLayout.show(mainPanel, "LOGIN");
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("🏦 Ado-Transfert");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        statusLabel = new JLabel("Non connesso");
        statusLabel.setForeground(Color.WHITE);
        
        balanceLabel = new JLabel("");
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(statusLabel, BorderLayout.CENTER);
        panel.add(balanceLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void createLoginScreen() {
        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(new EmptyBorder(50, 100, 50, 100));
        
        // Pannello centrale per il form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Titolo
        JLabel titleLabel = new JLabel("Accedi al tuo Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centerPanel.add(titleLabel, gbc);
        
        // Campo Email
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(emailField, gbc);
        
        // Campo Password
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(passwordField, gbc);
        
        // Pulsanti
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton loginButton = createStyledButton("Accedi", PRIMARY_COLOR, Color.WHITE);
        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            if (!email.isEmpty() && !password.isEmpty()) {
                performLogin(email, password);
            } else {
                showMessage("Inserisci email e password", ERROR_COLOR);
            }
        });
        
        JButton registerButton = createStyledButton("Registrati", Color.GRAY, Color.WHITE);
        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));
        
        JButton aboutButton = createStyledButton("Informazioni App", WARNING_COLOR, Color.WHITE);
        aboutButton.addActionListener(e -> showAboutDialog());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(aboutButton);
        centerPanel.add(buttonPanel, gbc);
        
        loginPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(loginPanel, "LOGIN");
    }
    
    private void createRegistrationScreen() {
        JPanel registerPanel = new JPanel(new BorderLayout());
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        
        // Pannello centrale con scroll
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Titolo
        JLabel titleLabel = new JLabel("Crea il tuo Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centerPanel.add(titleLabel, gbc);
        
        // Campi del form
        String[] labels = {"Nome:", "Cognome:", "User ID:", "Telefono:", "Email:", "Password:"};
        final JTextField[] fields = new JTextField[labels.length - 1]; // -1 perché password è separato
        
        JPasswordField passwordField = null;
        for (int i = 0; i < labels.length; i++) {
            gbc.gridwidth = 1; gbc.gridy = i + 1; gbc.gridx = 0; gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.NONE;
            centerPanel.add(new JLabel(labels[i]), gbc);
            
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            if (i == 5) { // Password field
                passwordField = new JPasswordField(20);
                passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
                centerPanel.add(passwordField, gbc);
            } else {
                fields[i] = new JTextField(20);
                fields[i].setFont(new Font("Arial", Font.PLAIN, 14));
                centerPanel.add(fields[i], gbc);
            }
        }
        final JPasswordField finalPasswordField = passwordField;
        
        // Pulsanti
        gbc.gridx = 0; gbc.gridy = labels.length + 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton registerButton = createStyledButton("Registrati", SUCCESS_COLOR, Color.WHITE);
        registerButton.addActionListener(e -> {
            String nome = fields[0].getText().trim();
            String cognome = fields[1].getText().trim();
            String userID = fields[2].getText().trim();
            String telefono = fields[3].getText().trim();
            String email = fields[4].getText().trim();
            String password = new String(finalPasswordField.getPassword());
            
            if (validateRegistration(nome, cognome, userID, telefono, email, password)) {
                performRegistration(nome, cognome, userID, telefono, email, password);
            }
        });
        
        JButton backButton = createStyledButton("Torna al Menu", Color.GRAY, Color.WHITE);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        centerPanel.add(buttonPanel, gbc);
        
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        registerPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(registerPanel, "REGISTER");
    }
    
    private void createClientDashboard() {
        JPanel clientPanel = new JPanel(new BorderLayout());
        clientPanel.setBackground(Color.WHITE);
        
        // Sidebar con menu
        JPanel sidebar = createClientSidebar();
        
        // Area principale con card layout
        CardLayout contentLayout = new CardLayout();
        JPanel contentPanel = new JPanel(contentLayout);
        contentPanel.setBackground(Color.WHITE);
        
        // Crea le diverse sezioni del dashboard
        contentPanel.add(createTransactionPanel(contentLayout, contentPanel), "TRANSACTIONS");
        contentPanel.add(createProfilePanel(contentLayout, contentPanel), "PROFILE");
        contentPanel.add(createMessagesPanel(contentLayout, contentPanel), "MESSAGES");
        contentPanel.add(createAddressPanel(contentLayout, contentPanel), "ADDRESS");
        contentPanel.add(createHistoryPanel(contentLayout, contentPanel), "HISTORY");
        
        clientPanel.add(sidebar, BorderLayout.WEST);
        clientPanel.add(contentPanel, BorderLayout.CENTER);
        
        mainPanel.add(clientPanel, "CLIENT");
    }
    
    private JPanel createClientSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(SECONDARY_COLOR);
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        
        JLabel welcomeLabel = new JLabel("Benvenuto!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(PRIMARY_COLOR);
        sidebar.add(welcomeLabel);
        sidebar.add(Box.createVerticalStrut(20));
        
        String[] menuItems = {"💰 Transazioni", "👤 Profilo", "📧 Messaggi", "🏠 Indirizzo", "📊 Storico"};
        String[] cardNames = {"TRANSACTIONS", "PROFILE", "MESSAGES", "ADDRESS", "HISTORY"};
        
        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = new JButton(menuItems[i]);
            menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            menuButton.setPreferredSize(new Dimension(180, 40));
            menuButton.setMaximumSize(new Dimension(180, 40));
            menuButton.setBackground(Color.WHITE);
            menuButton.setForeground(PRIMARY_COLOR);
            menuButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            menuButton.setFont(new Font("Arial", Font.PLAIN, 12));
            
            final String cardName = cardNames[i];
            menuButton.addActionListener(e -> {
                CardLayout layout = (CardLayout) ((JPanel) mainPanel.getComponent(0)).getLayout();
                layout.show(mainPanel, "CLIENT");
                CardLayout contentLayout = (CardLayout) ((JPanel) ((JPanel) mainPanel.getComponent(0)).getComponent(1)).getLayout();
                contentLayout.show((JPanel) ((JPanel) mainPanel.getComponent(0)).getComponent(1), cardName);
            });
            
            sidebar.add(menuButton);
            sidebar.add(Box.createVerticalStrut(10));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        JButton logoutButton = new JButton("🚪 Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setPreferredSize(new Dimension(180, 40));
        logoutButton.setMaximumSize(new Dimension(180, 40));
        logoutButton.setBackground(ERROR_COLOR);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorder(null);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.addActionListener(e -> performLogout());
        
        sidebar.add(logoutButton);
        
        return sidebar;
    }
    
    private JPanel createTransactionPanel(CardLayout layout, JPanel parent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Titolo
        JLabel titleLabel = new JLabel("💰 Gestione Transazioni");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Pannello per i pulsanti delle operazioni
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton depositButton = createStyledButton("💳 Versamento", SUCCESS_COLOR, Color.WHITE);
        depositButton.addActionListener(e -> showTransactionDialog("VERSAMENTO"));
        
        JButton withdrawalButton = createStyledButton("💸 Prelievo", WARNING_COLOR, Color.WHITE);
        withdrawalButton.addActionListener(e -> showTransactionDialog("PRELIEVO"));
        
        JButton transferButton = createStyledButton("🔄 Trasferimento", PRIMARY_COLOR, Color.WHITE);
        transferButton.addActionListener(e -> showTransferDialog());
        
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawalButton);
        buttonPanel.add(transferButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createProfilePanel(CardLayout layout, JPanel parent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("👤 Gestione Profilo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JButton viewProfileButton = createStyledButton("Visualizza Profilo", PRIMARY_COLOR, Color.WHITE);
        viewProfileButton.addActionListener(e -> showProfileDetails());
        
        JButton editProfileButton = createStyledButton("Modifica Profilo", SUCCESS_COLOR, Color.WHITE);
        editProfileButton.addActionListener(e -> showEditProfileDialog());
        
        JButton accountDetailsButton = createStyledButton("Dettagli Conto", WARNING_COLOR, Color.WHITE);
        accountDetailsButton.addActionListener(e -> showAccountDetails());
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(viewProfileButton, gbc);
        
        gbc.gridy = 1;
        contentPanel.add(editProfileButton, gbc);
        
        gbc.gridy = 2;
        contentPanel.add(accountDetailsButton, gbc);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createMessagesPanel(CardLayout layout, JPanel parent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("📧 Sistema Messaggi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton viewMessagesButton = createStyledButton("Visualizza Messaggi", PRIMARY_COLOR, Color.WHITE);
        viewMessagesButton.addActionListener(e -> showMessages());
        
        JButton createMessageButton = createStyledButton("Nuovo Messaggio", SUCCESS_COLOR, Color.WHITE);
        createMessageButton.addActionListener(e -> showCreateMessageDialog());
        
        JButton deleteMessageButton = createStyledButton("Elimina Messaggio", ERROR_COLOR, Color.WHITE);
        deleteMessageButton.addActionListener(e -> showDeleteMessageDialog());
        
        buttonPanel.add(viewMessagesButton);
        buttonPanel.add(createMessageButton);
        buttonPanel.add(deleteMessageButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAddressPanel(CardLayout layout, JPanel parent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("🏠 Gestione Indirizzo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton createAddressButton = createStyledButton("Crea Indirizzo", SUCCESS_COLOR, Color.WHITE);
        createAddressButton.addActionListener(e -> showAddressDialog("CREATE"));
        
        JButton editAddressButton = createStyledButton("Modifica Indirizzo", WARNING_COLOR, Color.WHITE);
        editAddressButton.addActionListener(e -> showAddressDialog("EDIT"));
        
        buttonPanel.add(createAddressButton);
        buttonPanel.add(editAddressButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createHistoryPanel(CardLayout layout, JPanel parent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("📊 Storico Transazioni");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JButton viewHistoryButton = createStyledButton("Visualizza Storico", PRIMARY_COLOR, Color.WHITE);
        viewHistoryButton.addActionListener(e -> showTransactionHistory());
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(viewHistoryButton, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void createAdminDashboard() {
        JPanel adminPanel = new JPanel(new BorderLayout());
        adminPanel.setBackground(Color.WHITE);
        
        // Sidebar admin
        JPanel sidebar = createAdminSidebar();
        
        // Area principale
        CardLayout contentLayout = new CardLayout();
        JPanel contentPanel = new JPanel(contentLayout);
        contentPanel.setBackground(Color.WHITE);
        
        contentPanel.add(createUserManagementPanel(), "USER_MANAGEMENT");
        contentPanel.add(createAdminMessagesPanel(), "ADMIN_MESSAGES");
        
        adminPanel.add(sidebar, BorderLayout.WEST);
        adminPanel.add(contentPanel, BorderLayout.CENTER);
        
        mainPanel.add(adminPanel, "ADMIN");
    }
    
    private JPanel createAdminSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(SECONDARY_COLOR);
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("👑 Pannello Admin");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);
        sidebar.add(titleLabel);
        sidebar.add(Box.createVerticalStrut(20));
        
        String[] menuItems = {"👥 Gestione Utenti", "📧 Messaggi"};
        String[] cardNames = {"USER_MANAGEMENT", "ADMIN_MESSAGES"};
        
        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = new JButton(menuItems[i]);
            menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            menuButton.setPreferredSize(new Dimension(180, 40));
            menuButton.setMaximumSize(new Dimension(180, 40));
            menuButton.setBackground(Color.WHITE);
            menuButton.setForeground(PRIMARY_COLOR);
            menuButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            menuButton.setFont(new Font("Arial", Font.PLAIN, 12));
            
            final String cardName = cardNames[i];
            menuButton.addActionListener(e -> {
                CardLayout layout = (CardLayout) ((JPanel) mainPanel.getComponent(0)).getLayout();
                layout.show(mainPanel, "ADMIN");
                CardLayout contentLayout = (CardLayout) ((JPanel) ((JPanel) mainPanel.getComponent(0)).getComponent(1)).getLayout();
                contentLayout.show((JPanel) ((JPanel) mainPanel.getComponent(0)).getComponent(1), cardName);
            });
            
            sidebar.add(menuButton);
            sidebar.add(Box.createVerticalStrut(10));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        JButton logoutButton = new JButton("🚪 Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setPreferredSize(new Dimension(180, 40));
        logoutButton.setMaximumSize(new Dimension(180, 40));
        logoutButton.setBackground(ERROR_COLOR);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorder(null);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.addActionListener(e -> performLogout());
        
        sidebar.add(logoutButton);
        
        return sidebar;
    }
    
    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("👥 Gestione Utenti");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton viewUsersButton = createStyledButton("Visualizza Utenti", PRIMARY_COLOR, Color.WHITE);
        viewUsersButton.addActionListener(e -> showAllUsers());
        
        JButton approveUserButton = createStyledButton("Approva Utente", SUCCESS_COLOR, Color.WHITE);
        approveUserButton.addActionListener(e -> showApproveUserDialog());
        
        JButton blockUserButton = createStyledButton("Blocca Utente", WARNING_COLOR, Color.WHITE);
        blockUserButton.addActionListener(e -> showBlockUserDialog());
        
        JButton deleteUserButton = createStyledButton("Elimina Utente", ERROR_COLOR, Color.WHITE);
        deleteUserButton.addActionListener(e -> showDeleteUserDialog());
        
        buttonPanel.add(viewUsersButton);
        buttonPanel.add(approveUserButton);
        buttonPanel.add(blockUserButton);
        buttonPanel.add(deleteUserButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAdminMessagesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("📧 Gestione Messaggi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton viewMessagesButton = createStyledButton("Visualizza Messaggi", PRIMARY_COLOR, Color.WHITE);
        viewMessagesButton.addActionListener(e -> showMessages());
        
        JButton createMessageButton = createStyledButton("Nuovo Messaggio", SUCCESS_COLOR, Color.WHITE);
        createMessageButton.addActionListener(e -> showCreateMessageDialog());
        
        buttonPanel.add(viewMessagesButton);
        buttonPanel.add(createMessageButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // === METODI DI SUPPORTO ===
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 40));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effetto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void showMessage(String message, Color color) {
        SwingUtilities.invokeLater(() -> {
            JLabel messageLabel = new JLabel(message);
            messageLabel.setForeground(color);
            messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            JOptionPane.showMessageDialog(this, messageLabel, "Informazione", 
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private void connectToServer() {
        SwingUtilities.invokeLater(() -> {
            try {
                Registry registry = LocateRegistry.getRegistry(SERVER_HOST, SERVER_PORT);
                serverStub = (InterfaceTransfer) registry.lookup(SERVICE_NAME);
                statusLabel.setText("✅ Connesso al server");
                statusLabel.setForeground(SUCCESS_COLOR);
                
                String testResult = serverStub.testConnection();
                if (testResult.startsWith("SUCCESS")) {
                    System.out.println("Connessione al database verificata");
                }
            } catch (Exception e) {
                statusLabel.setText("❌ Errore connessione");
                statusLabel.setForeground(ERROR_COLOR);
                JOptionPane.showMessageDialog(this, 
                    "Errore di connessione al server: " + e.getMessage(), 
                    "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private boolean validateRegistration(String nome, String cognome, String userID, 
                                      String telefono, String email, String password) {
        if (nome.isEmpty() || cognome.isEmpty() || userID.isEmpty() || 
            telefono.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showMessage("Tutti i campi sono obbligatori", ERROR_COLOR);
            return false;
        }
        
        if (password.length() < 8) {
            showMessage("La password deve essere di almeno 8 caratteri", ERROR_COLOR);
            return false;
        }
        
        if (!email.contains("@")) {
            showMessage("Email non valida", ERROR_COLOR);
            return false;
        }
        
        return true;
    }
    
    private void performLogin(String email, String password) {
        SwingUtilities.invokeLater(() -> {
            try {
                String result = serverStub.login(email, password);
                
                if (result.startsWith("SUCCESS:")) {
                    // Estrai informazioni dall'utente
                    currentUserID = email; // Temporaneo, dovrebbe essere userID dal server
                    currentUserType = "cliente"; // Temporaneo, dovrebbe essere determinato dal server
                    currentUserName = email; // Temporaneo
                    
                    statusLabel.setText("👤 " + currentUserName);
                    statusLabel.setForeground(Color.WHITE);
                    
                    showMessage("Login effettuato con successo!", SUCCESS_COLOR);
                    
                    // Determina il tipo di dashboard da mostrare
                    if ("admin".equalsIgnoreCase(currentUserType)) {
                        cardLayout.show(mainPanel, "ADMIN");
                    } else {
                        cardLayout.show(mainPanel, "CLIENT");
                    }
                    
                    // Aggiorna il saldo
                    updateBalance();
                    
                } else {
                    showMessage(result.replace("ERROR: ", ""), ERROR_COLOR);
                }
            } catch (Exception e) {
                showMessage("Errore durante il login: " + e.getMessage(), ERROR_COLOR);
            }
        });
    }
    
    private void performRegistration(String nome, String cognome, String userID, 
                                   String telefono, String email, String password) {
        SwingUtilities.invokeLater(() -> {
            try {
                String result = serverStub.creaUtente(nome, cognome, userID, telefono, email, password);
                
                if (result.startsWith("SUCCESS:")) {
                    showMessage("Registrazione completata! In attesa di approvazione.", SUCCESS_COLOR);
                    cardLayout.show(mainPanel, "LOGIN");
                } else {
                    showMessage(result.replace("ERROR: ", ""), ERROR_COLOR);
                }
            } catch (Exception e) {
                showMessage("Errore durante la registrazione: " + e.getMessage(), ERROR_COLOR);
            }
        });
    }
    
    private void performLogout() {
        SwingUtilities.invokeLater(() -> {
            try {
                if (currentUserID != null) {
                    serverStub.logout(currentUserID);
                }
                
                currentUserID = null;
                currentUserType = null;
                currentUserName = null;
                
                statusLabel.setText("Non connesso");
                statusLabel.setForeground(Color.WHITE);
                balanceLabel.setText("");
                
                showMessage("Logout effettuato con successo!", SUCCESS_COLOR);
                cardLayout.show(mainPanel, "LOGIN");
                
            } catch (Exception e) {
                showMessage("Errore durante il logout: " + e.getMessage(), ERROR_COLOR);
            }
        });
    }
    
    private void updateBalance() {
        SwingUtilities.invokeLater(() -> {
            try {
                String result = serverStub.visualizzaDettagliConto(currentUserID);
                if (result.startsWith("SUCCESS:")) {
                    // Estrai il saldo dal risultato
                    String[] lines = result.split("\n");
                    for (String line : lines) {
                        if (line.contains("Saldo attuale:")) {
                            String balance = line.substring(line.indexOf("Saldo attuale:") + 15);
                            balanceLabel.setText("💰 " + balance.trim());
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Errore nell'aggiornamento del saldo: " + e.getMessage());
            }
        });
    }
    
    // === METODI PER DIALOGHI E AZIONI ===
    
    private void showTransactionDialog(String type) {
        JDialog dialog = new JDialog(this, type.equals("VERSAMENTO") ? "Versamento" : "Prelievo", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel label = new JLabel("Importo:");
        JTextField amountField = new JTextField(15);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(amountField, gbc);
        
        JButton confirmButton = createStyledButton("Conferma", SUCCESS_COLOR, Color.WHITE);
        JButton cancelButton = createStyledButton("Annulla", ERROR_COLOR, Color.WHITE);
        
        confirmButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    showMessage("L'importo deve essere positivo", ERROR_COLOR);
                    return;
                }
                
                String result;
                if (type.equals("VERSAMENTO")) {
                    result = serverStub.faiVersamento(currentUserID, amount);
                } else {
                    result = serverStub.faiPrelievo(currentUserID, amount);
                }
                
                if (result.startsWith("SUCCESS:")) {
                    showMessage("Operazione completata con successo!", SUCCESS_COLOR);
                    updateBalance();
                } else {
                    showMessage(result.replace("ERROR: ", ""), ERROR_COLOR);
                }
                
                dialog.dispose();
            } catch (NumberFormatException ex) {
                showMessage("Inserisci un importo valido", ERROR_COLOR);
            } catch (Exception ex) {
                showMessage("Errore: " + ex.getMessage(), ERROR_COLOR);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void showTransferDialog() {
        JDialog dialog = new JDialog(this, "Trasferimento", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel phoneLabel = new JLabel("Telefono destinatario:");
        JTextField phoneField = new JTextField(15);
        JLabel amountLabel = new JLabel("Importo:");
        JTextField amountField = new JTextField(15);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(phoneLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(amountLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(amountField, gbc);
        
        JButton confirmButton = createStyledButton("Conferma", SUCCESS_COLOR, Color.WHITE);
        JButton cancelButton = createStyledButton("Annulla", ERROR_COLOR, Color.WHITE);
        
        confirmButton.addActionListener(e -> {
            try {
                String phone = phoneField.getText().trim();
                double amount = Double.parseDouble(amountField.getText());
                
                if (phone.isEmpty()) {
                    showMessage("Inserisci il telefono del destinatario", ERROR_COLOR);
                    return;
                }
                
                if (amount <= 0) {
                    showMessage("L'importo deve essere positivo", ERROR_COLOR);
                    return;
                }
                
                String result = serverStub.faiTrasferimento(currentUserID, phone, amount);
                
                if (result.startsWith("SUCCESS:")) {
                    showMessage("Trasferimento completato con successo!", SUCCESS_COLOR);
                    updateBalance();
                } else {
                    showMessage(result.replace("ERROR: ", ""), ERROR_COLOR);
                }
                
                dialog.dispose();
            } catch (NumberFormatException ex) {
                showMessage("Inserisci un importo valido", ERROR_COLOR);
            } catch (Exception ex) {
                showMessage("Errore: " + ex.getMessage(), ERROR_COLOR);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    // Metodi stub per le altre funzionalità
    private void showProfileDetails() {
        try {
            String result = serverStub.visualizzaProfilo(currentUserID);
            showMessage(result.replace("SUCCESS: ", ""), PRIMARY_COLOR);
        } catch (Exception e) {
            showMessage("Errore: " + e.getMessage(), ERROR_COLOR);
        }
    }
    
    private void showEditProfileDialog() {
        showMessage("Funzionalità in sviluppo", WARNING_COLOR);
    }
    
    private void showAccountDetails() {
        try {
            String result = serverStub.visualizzaDettagliConto(currentUserID);
            showMessage(result.replace("SUCCESS: ", ""), PRIMARY_COLOR);
        } catch (Exception e) {
            showMessage("Errore: " + e.getMessage(), ERROR_COLOR);
        }
    }
    
    private void showMessages() {
        try {
            String result = serverStub.visualizzaMessaggi(currentUserID);
            showMessage(result.replace("SUCCESS: ", ""), PRIMARY_COLOR);
        } catch (Exception e) {
            showMessage("Errore: " + e.getMessage(), ERROR_COLOR);
        }
    }
    
    private void showCreateMessageDialog() {
        showMessage("Funzionalità in sviluppo", WARNING_COLOR);
    }
    
    private void showDeleteMessageDialog() {
        showMessage("Funzionalità in sviluppo", WARNING_COLOR);
    }
    
    private void showAddressDialog(String type) {
        showMessage("Funzionalità in sviluppo", WARNING_COLOR);
    }
    
    private void showTransactionHistory() {
        try {
            String result = serverStub.visualizzaStoricoTransazioni(currentUserID);
            showMessage(result.replace("SUCCESS: ", ""), PRIMARY_COLOR);
        } catch (Exception e) {
            showMessage("Errore: " + e.getMessage(), ERROR_COLOR);
        }
    }
    
    // Metodi admin
    private void showAllUsers() {
        try {
            String result = serverStub.visualizzaUtenti(currentUserID);
            showMessage(result.replace("SUCCESS: ", ""), PRIMARY_COLOR);
        } catch (Exception e) {
            showMessage("Errore: " + e.getMessage(), ERROR_COLOR);
        }
    }
    
    private void showApproveUserDialog() {
        showMessage("Funzionalità in sviluppo", WARNING_COLOR);
    }
    
    private void showBlockUserDialog() {
        showMessage("Funzionalità in sviluppo", WARNING_COLOR);
    }
    
    private void showDeleteUserDialog() {
        showMessage("Funzionalità in sviluppo", WARNING_COLOR);
    }
    
    private void showAboutDialog() {
        JDialog aboutDialog = new JDialog(this, "Informazioni sull'Applicazione", true);
        aboutDialog.setSize(500, 400);
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.setLayout(new BorderLayout());
        
        // Pannello principale con scroll
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header con logo e titolo
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("🏦 Ado-Transfert");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel subtitleLabel = new JLabel("Sistema di Gestione Transazioni");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        
        // Contenuto delle informazioni
        JTextArea infoText = new JTextArea();
        infoText.setEditable(false);
        infoText.setFont(new Font("Arial", Font.PLAIN, 14));
        infoText.setBackground(Color.WHITE);
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        
        String aboutText = 
            "📋 DESCRIZIONE\n\n" +
            "Ado-Transfert è un sistema completo di gestione transazioni finanziarie \n" +
            "sviluppato con tecnologia Java RMI (Remote Method Invocation) e \n" +
            "interfaccia grafica moderna.\n\n" +
            "🚀 CARATTERISTICHE PRINCIPALI\n\n" +
            "• Sistema di autenticazione sicuro con hash delle password\n" +
            "• Gestione completa degli utenti (registrazione, approvazione, amministrazione)\n" +
            "• Transazioni finanziarie: versamenti, prelievi, trasferimenti\n" +
            "• Sistema di messaggistica integrato\n" +
            "• Gestione indirizzi utente\n" +
            "• Storico completo delle transazioni\n" +
            "• Interfaccia grafica moderna e intuitiva\n" +
            "• Database MySQL per persistenza dati\n" +
            "• Architettura client-server con RMI\n\n" +
            "👥 TIPI DI UTENTE\n\n" +
            "🔹 Cliente: può effettuare transazioni, gestire il proprio profilo\n" +
            "🔹 Amministratore: gestisce utenti, approva registrazioni, supervisiona il sistema\n\n" +
            "🛠️ TECNOLOGIE UTILIZZATE\n\n" +
            "• Java Swing/AWT per l'interfaccia grafica\n" +
            "• Java RMI per la comunicazione client-server\n" +
            "• MySQL per il database\n" +
            "• JDBC per la connessione al database\n\n" +
            "📞 SUPPORTO\n\n" +
            "Per assistenza tecnica o segnalazioni, contatta il team di sviluppo.\n\n" +
            "Versione: 1.0.0\n" +
            "© 2024 Ado-Transfert Team";
        
        infoText.setText(aboutText);
        infoText.setCaretPosition(0); // Inizia dall'inizio
        
        JScrollPane scrollPane = new JScrollPane(infoText);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Pulsante di chiusura
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton closeButton = createStyledButton("Chiudi", PRIMARY_COLOR, Color.WHITE);
        closeButton.addActionListener(e -> aboutDialog.dispose());
        buttonPanel.add(closeButton);
        
        // Assemblaggio del dialog
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        aboutDialog.add(mainPanel, BorderLayout.CENTER);
        aboutDialog.setVisible(true);
    }
}