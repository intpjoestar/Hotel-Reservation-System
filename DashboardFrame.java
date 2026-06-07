import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Main dashboard window for the Hotel Reservation System.
 * Provides a sidebar navigation with separate panels for Dashboard,
 * Rooms, Guests, Bookings, and User management (admin only).
 * Uses {@link CardLayout} to switch between panels.
 */
public class DashboardFrame extends JFrame {

    private HotelSystem system;
    private DatabaseManager db;
    private User currentUser;

    private JPanel sidebar;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private static final String DASHBOARD_CARD = "dashboard";
    private static final String ROOMS_CARD = "rooms";
    private static final String GUESTS_CARD = "guests";
    private static final String BOOKINGS_CARD = "bookings";
    private static final String USERS_CARD = "users";

    private JButton dashboardBtn;
    private JButton roomsBtn;
    private JButton guestsBtn;
    private JButton bookingsBtn;
    private JButton usersBtn;
    private JButton logoutBtn;
    private JLabel userInfoLabel;

    private JLabel totalRoomsLabel;
    private JLabel availableRoomsLabel;
    private JLabel totalBookingsLabel;
    private JLabel activeBookingsLabel;

    private DefaultTableModel roomTableModel;
    private JTable roomTable;
    private JTextField roomNumberField;
    private JComboBox<String> roomTypeCombo;
    private JTextField roomPriceField;

    private DefaultTableModel guestTableModel;
    private JTable guestTable;
    private JTextField guestNameField;
    private JTextField guestNationalIdField;
    private JTextField guestPhoneField;

    private DefaultTableModel bookingTableModel;
    private JTable bookingTable;
    private JComboBox<String> bookingGuestCombo;
    private JComboBox<String> bookingRoomCombo;
    private JSpinner checkInSpinner;
    private JSpinner checkOutSpinner;

    private DefaultTableModel userTableModel;
    private JTable userTable;
    private JTextField newUsernameField;
    private JPasswordField newPasswordField;
    private JComboBox<String> userRoleCombo;

    /**
     * Constructs and displays the dashboard window.
     *
     * @param system      the core HotelSystem
     * @param db          the DatabaseManager for database operations
     * @param currentUser the currently authenticated user
     */
    public DashboardFrame(HotelSystem system, DatabaseManager db, User currentUser) {
        this.system = system;
        this.db = db;
        this.currentUser = currentUser;
        buildUI();
        setVisible(true);
    }

    /**
     * Assembles the complete dashboard UI including sidebar and content panels.
     */
    private void buildUI() {
        setTitle("Hotel California - Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BACKGROUND);

        createSidebar();
        createContentPanel();

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        setActiveButton(dashboardBtn);
        updateDashboardStats();
    }

    // ---- SIDEBAR ----

    /**
     * Builds the left sidebar with brand info, navigation buttons, and logout.
     */
    private void createSidebar() {
        sidebar = new JPanel();
        sidebar.setBackground(UITheme.PRIMARY);
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JPanel brandPanel = new JPanel();
        brandPanel.setBackground(UITheme.PRIMARY);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 20, 20));

        JLabel brandLabel = new JLabel("HOTEL CALIFORNIA");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brandLabel.setForeground(UITheme.SECONDARY);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brandSub = new JLabel("Reservation System");
        brandSub.setFont(UITheme.FONT_SMALL);
        brandSub.setForeground(Color.LIGHT_GRAY);
        brandSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandPanel.add(brandLabel);
        brandPanel.add(Box.createVerticalStrut(2));
        brandPanel.add(brandSub);

        userInfoLabel = new JLabel();
        userInfoLabel.setFont(UITheme.FONT_SMALL);
        userInfoLabel.setForeground(UITheme.SECONDARY);
        userInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userInfoLabel.setText(currentUser.getUsername() + " [" + currentUser.getRole() + "]");
        brandPanel.add(Box.createVerticalStrut(8));
        brandPanel.add(userInfoLabel);

        sidebar.add(brandPanel);
        sidebar.add(Box.createVerticalStrut(10));

        dashboardBtn = createSidebarButton("DASHBOARD", true);
        roomsBtn = createSidebarButton("ROOMS", false);
        guestsBtn = createSidebarButton("GUESTS", false);
        bookingsBtn = createSidebarButton("BOOKINGS", false);

        sidebar.add(dashboardBtn);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(roomsBtn);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(guestsBtn);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(bookingsBtn);

        if ("ADMIN".equals(currentUser.getRole())) {
            sidebar.add(Box.createVerticalStrut(5));
            usersBtn = createSidebarButton("USERS", false);
            sidebar.add(usersBtn);
        }

        sidebar.add(Box.createVerticalGlue());

        JPanel logoutPanel = new JPanel();
        logoutPanel.setBackground(UITheme.PRIMARY);
        logoutPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        logoutPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoutBtn = new JButton("LOGOUT");
        logoutBtn.setFont(UITheme.FONT_BUTTON);
        logoutBtn.setForeground(Color.LIGHT_GRAY);
        logoutBtn.setBackground(new Color(40, 60, 100));
        logoutBtn.setOpaque(true);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setBackground(new Color(60, 80, 120));
            }
            public void mouseExited(MouseEvent e) {
                logoutBtn.setBackground(new Color(40, 60, 100));
            }
        });
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });

        logoutPanel.add(logoutBtn);
        sidebar.add(logoutPanel);

        dashboardBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setActiveButton(dashboardBtn);
                cardLayout.show(contentPanel, DASHBOARD_CARD);
                updateDashboardStats();
            }
        });

        roomsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setActiveButton(roomsBtn);
                cardLayout.show(contentPanel, ROOMS_CARD);
                loadRoomData();
            }
        });

        guestsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setActiveButton(guestsBtn);
                cardLayout.show(contentPanel, GUESTS_CARD);
                loadGuestData();
            }
        });

        bookingsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setActiveButton(bookingsBtn);
                cardLayout.show(contentPanel, BOOKINGS_CARD);
                loadBookingData();
                refreshBookingDropdowns();
            }
        });

        if (usersBtn != null) {
            usersBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setActiveButton(usersBtn);
                    cardLayout.show(contentPanel, USERS_CARD);
                    loadUserData();
                }
            });
        }
    }

    /**
     * Creates a styled sidebar navigation button.
     *
     * @param text   the button label
     * @param active whether this button is initially active
     * @return the configured button
     */
    private JButton createSidebarButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(14, 40, 14, 40));
        if (active) {
            btn.setBackground(new Color(40, 60, 100));
            btn.setForeground(UITheme.SECONDARY);
        } else {
            btn.setBackground(UITheme.PRIMARY);
            btn.setForeground(Color.LIGHT_GRAY);
        }
        btn.setMaximumSize(new Dimension(220, 48));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!btn.getForeground().equals(UITheme.SECONDARY)) {
                    btn.setBackground(UITheme.SIDEBAR_HOVER);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!btn.getForeground().equals(UITheme.SECONDARY)) {
                    btn.setBackground(UITheme.PRIMARY);
                }
            }
        });

        return btn;
    }

    /**
     * Highlights the active sidebar button and dims the others.
     *
     * @param active the button to set as active
     */
    private void setActiveButton(JButton active) {
        boolean isAdmin = "ADMIN".equals(currentUser.getRole());
        JButton[] buttons;
        if (isAdmin) {
            buttons = new JButton[]{dashboardBtn, roomsBtn, guestsBtn, bookingsBtn, usersBtn};
        } else {
            buttons = new JButton[]{dashboardBtn, roomsBtn, guestsBtn, bookingsBtn};
        }
        for (JButton b : buttons) {
            if (b != null) {
                if (b == active) {
                    b.setBackground(new Color(40, 60, 100));
                    b.setForeground(UITheme.SECONDARY);
                } else {
                    b.setBackground(UITheme.PRIMARY);
                    b.setForeground(Color.LIGHT_GRAY);
                }
            }
        }
    }

    // ---- CONTENT PANEL ----

    /**
     * Builds the main content area with a CardLayout for switching between panels.
     */
    private void createContentPanel() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UITheme.BACKGROUND);

        contentPanel.add(createDashboardPanel(), DASHBOARD_CARD);
        contentPanel.add(createRoomsPanel(), ROOMS_CARD);
        contentPanel.add(createGuestsPanel(), GUESTS_CARD);
        contentPanel.add(createBookingsPanel(), BOOKINGS_CARD);

        if ("ADMIN".equals(currentUser.getRole())) {
            contentPanel.add(createUsersPanel(), USERS_CARD);
        }
    }

    // ---- DASHBOARD PANEL ----

    /**
     * Creates the Dashboard overview panel with summary statistics.
     *
     * @return the dashboard panel
     */
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Dashboard");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_DARK);

        JPanel cardsGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        cardsGrid.setBackground(UITheme.BACKGROUND);
        cardsGrid.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        totalRoomsLabel = new JLabel("0", SwingConstants.CENTER);
        availableRoomsLabel = new JLabel("0", SwingConstants.CENTER);
        totalBookingsLabel = new JLabel("0", SwingConstants.CENTER);
        activeBookingsLabel = new JLabel("0", SwingConstants.CENTER);

        cardsGrid.add(createStatCard("Total Rooms", totalRoomsLabel, UITheme.PRIMARY));
        cardsGrid.add(createStatCard("Available Rooms", availableRoomsLabel, UITheme.SUCCESS));
        cardsGrid.add(createStatCard("Total Bookings", totalBookingsLabel, UITheme.SECONDARY));
        cardsGrid.add(createStatCard("Active Bookings", activeBookingsLabel, UITheme.DANGER));

        panel.add(title, BorderLayout.NORTH);
        panel.add(cardsGrid, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a single stat card component with an accent bar.
     *
     * @param labelText  the card's label text
     * @param valueLabel the JLabel displaying the numeric value
     * @param accent     the accent color for the left bar
     * @return the stat card panel
     */
    private JPanel createStatCard(String labelText, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UITheme.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JPanel accentBar = new JPanel();
        accentBar.setBackground(accent);
        accentBar.setPreferredSize(new Dimension(6, 0));

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JLabel label = new JLabel(labelText);
        label.setFont(UITheme.FONT_LABEL);
        label.setForeground(Color.GRAY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(accent);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);

        content.add(label, BorderLayout.NORTH);
        content.add(valueLabel, BorderLayout.CENTER);

        card.add(accentBar, BorderLayout.WEST);
        card.add(content, BorderLayout.CENTER);

        return card;
    }

    /**
     * Refreshes the dashboard statistics from the current system data.
     */
    private void updateDashboardStats() {
        int totalRooms = system.getRooms().size();
        int available = 0;
        for (Room room : system.getRooms()) {
            if (room.isAvailable()) {
                available++;
            }
        }

        int totalBookings = system.getBookings().size();
        int active = 0;
        for (Booking booking : system.getBookings()) {
            if ("ACTIVE".equals(booking.getStatus())) {
                active++;
            }
        }

        totalRoomsLabel.setText(String.valueOf(totalRooms));
        availableRoomsLabel.setText(String.valueOf(available));
        totalBookingsLabel.setText(String.valueOf(totalBookings));
        activeBookingsLabel.setText(String.valueOf(active));
    }

    // ---- ROOMS PANEL ----

    /**
     * Creates the Room Management panel with a table and admin form.
     *
     * @return the rooms panel
     */
    private JPanel createRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Room Management");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_DARK);

        String[] roomCols = {"ID", "Room Number", "Type", "Price ($)", "Status"};
        roomTableModel = new DefaultTableModel(roomCols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        roomTable = new JTable(roomTableModel);
        UITheme.styleTable(roomTable);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane roomScroll = new JScrollPane(roomTable);
        roomScroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        formPanel.setBackground(UITheme.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel roomNumLabel = new JLabel("Room Number:");
        roomNumLabel.setFont(UITheme.FONT_LABEL);
        roomNumberField = new JTextField(10);
        UITheme.styleTextField(roomNumberField);

        JLabel roomTypeLabel = new JLabel("Type:");
        roomTypeLabel.setFont(UITheme.FONT_LABEL);
        roomTypeCombo = new JComboBox<String>();
        roomTypeCombo.addItem("Single");
        roomTypeCombo.addItem("Double");
        roomTypeCombo.setFont(UITheme.FONT_LABEL);

        JLabel roomPriceLabel = new JLabel("Price ($):");
        roomPriceLabel.setFont(UITheme.FONT_LABEL);
        roomPriceField = new JTextField(8);
        UITheme.styleTextField(roomPriceField);

        JButton addRoomBtn = new JButton("Add Room");
        UITheme.styleButton(addRoomBtn, UITheme.SUCCESS, UITheme.WHITE);

        JButton deleteRoomBtn = new JButton("Delete Selected");
        UITheme.styleButton(deleteRoomBtn, UITheme.DANGER, UITheme.WHITE);

        formPanel.add(roomNumLabel);
        formPanel.add(roomNumberField);
        formPanel.add(roomTypeLabel);
        formPanel.add(roomTypeCombo);
        formPanel.add(roomPriceLabel);
        formPanel.add(roomPriceField);
        formPanel.add(addRoomBtn);
        formPanel.add(deleteRoomBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UITheme.BACKGROUND);
        topPanel.add(title, BorderLayout.NORTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(roomScroll, BorderLayout.CENTER);

        if ("ADMIN".equals(currentUser.getRole())) {
            panel.add(formPanel, BorderLayout.SOUTH);
        }

        addRoomBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleAddRoom();
            }
        });

        deleteRoomBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleDeleteRoom();
            }
        });

        return panel;
    }

    /**
     * Handles adding a new room via the form.
     * Validates input, creates the room, persists to DB, and refreshes the table.
     */
    private void handleAddRoom() {
        String roomNum = roomNumberField.getText().trim();
        String type = (String) roomTypeCombo.getSelectedItem();
        String priceStr = roomPriceField.getText().trim();

        if (roomNum.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all room fields.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be a number.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = system.getRooms().size() + 1;
        Room room = new Room(id, roomNum, type, price);
        system.addRoom(room);

        try {
            db.saveRoom(room);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }

        loadRoomData();
        roomNumberField.setText("");
        roomPriceField.setText("");
    }

    /**
     * Handles deleting a selected room after confirming no active bookings exist.
     */
    private void handleDeleteRoom() {
        int row = roomTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a room to delete.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int roomId = (int) roomTableModel.getValueAt(row, 0);
        for (Booking booking : system.getBookings()) {
            if (booking.getRoom().getId() == roomId) {
                JOptionPane.showMessageDialog(this,
                    "Cannot delete room: it has existing bookings.\nCancel or complete them first.",
                    "Room Has Bookings", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete room ID " + roomId + "?",
            "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            system.removeRoom(roomId);
            try {
                db.removeRoomFromDB(roomId);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            loadRoomData();
        }
    }

    /**
     * Refreshes the room table from the current system data.
     */
    private void loadRoomData() {
        roomTableModel.setRowCount(0);
        for (Room room : system.getRooms()) {
            Object[] rowData = {
                room.getId(),
                room.getRoomNumber(),
                room.getType(),
                String.format("%.2f", room.getPrice()),
                room.getStatus()
            };
            roomTableModel.addRow(rowData);
        }
        updateDashboardStats();
    }

    // ---- GUESTS PANEL ----

    /**
     * Creates the Guest Management panel with a table and add form.
     *
     * @return the guests panel
     */
    private JPanel createGuestsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Guest Management");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_DARK);

        String[] guestCols = {"ID", "Name", "National ID", "Phone"};
        guestTableModel = new DefaultTableModel(guestCols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        guestTable = new JTable(guestTableModel);
        UITheme.styleTable(guestTable);
        guestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane guestScroll = new JScrollPane(guestTable);
        guestScroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        formPanel.setBackground(UITheme.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(UITheme.FONT_LABEL);
        guestNameField = new JTextField(12);
        UITheme.styleTextField(guestNameField);

        JLabel nidLabel = new JLabel("National ID:");
        nidLabel.setFont(UITheme.FONT_LABEL);
        guestNationalIdField = new JTextField(10);
        UITheme.styleTextField(guestNationalIdField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(UITheme.FONT_LABEL);
        guestPhoneField = new JTextField(10);
        UITheme.styleTextField(guestPhoneField);

        JButton addGuestBtn = new JButton("Add Guest");
        UITheme.styleButton(addGuestBtn, UITheme.SUCCESS, UITheme.WHITE);

        formPanel.add(nameLabel);
        formPanel.add(guestNameField);
        formPanel.add(nidLabel);
        formPanel.add(guestNationalIdField);
        formPanel.add(phoneLabel);
        formPanel.add(guestPhoneField);
        formPanel.add(addGuestBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UITheme.BACKGROUND);
        topPanel.add(title, BorderLayout.NORTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(guestScroll, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);

        addGuestBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleAddGuest();
            }
        });

        return panel;
    }

    /**
     * Handles adding a new guest via the form.
     * Validates input (National ID must be 6 digits, phone 10 digits),
     * creates the guest, persists to DB, and refreshes the table.
     */
    private void handleAddGuest() {
        String name = guestNameField.getText().trim();
        String nid = guestNationalIdField.getText().trim();
        String phone = guestPhoneField.getText().trim();

        if (name.isEmpty() || nid.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all guest fields.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!nid.matches("\\d{6}")) {
            JOptionPane.showMessageDialog(this, "National ID must be exactly 6 digits.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = system.getGuests().size() + 1;
        Guest guest = new Guest(id, name, nid, phone);
        system.addGuest(guest);

        try {
            db.saveGuest(guest);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }

        loadGuestData();
        guestNameField.setText("");
        guestNationalIdField.setText("");
        guestPhoneField.setText("");
    }

    /**
     * Refreshes the guest table from the current system data.
     */
    private void loadGuestData() {
        guestTableModel.setRowCount(0);
        for (Guest guest : system.getGuests()) {
            Object[] rowData = {
                guest.getId(),
                guest.getName(),
                guest.getNationalId(),
                guest.getPhone()
            };
            guestTableModel.addRow(rowData);
        }
    }

    // ---- BOOKINGS PANEL ----

    /**
     * Creates the Booking Management panel with a table, creation form,
     * and action buttons for check-in, cancel, and check-out.
     *
     * @return the bookings panel
     */
    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Booking Management");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_DARK);

        String[] bookingCols = {"ID", "Guest", "Room", "Check-In", "Check-Out", "Status"};
        bookingTableModel = new DefaultTableModel(bookingCols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        bookingTable = new JTable(bookingTableModel);
        UITheme.styleTable(bookingTable);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane bookingScroll = new JScrollPane(bookingTable);
        bookingScroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        formPanel.setBackground(UITheme.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel guestLabel = new JLabel("Guest:");
        guestLabel.setFont(UITheme.FONT_LABEL);
        bookingGuestCombo = new JComboBox<String>();
        bookingGuestCombo.setFont(UITheme.FONT_LABEL);
        bookingGuestCombo.setPreferredSize(new Dimension(150, 30));

        JLabel roomLabel = new JLabel("Room:");
        roomLabel.setFont(UITheme.FONT_LABEL);
        bookingRoomCombo = new JComboBox<String>();
        bookingRoomCombo.setFont(UITheme.FONT_LABEL);
        bookingRoomCombo.setPreferredSize(new Dimension(120, 30));

        JLabel checkInLabel = new JLabel("Check-In:");
        checkInLabel.setFont(UITheme.FONT_LABEL);
        checkInSpinner = new JSpinner(new SpinnerDateModel());
        checkInSpinner.setEditor(new JSpinner.DateEditor(checkInSpinner, "yyyy-MM-dd"));
        checkInSpinner.setPreferredSize(new Dimension(130, 28));

        JLabel checkOutLabel = new JLabel("Check-Out:");
        checkOutLabel.setFont(UITheme.FONT_LABEL);
        checkOutSpinner = new JSpinner(new SpinnerDateModel());
        checkOutSpinner.setEditor(new JSpinner.DateEditor(checkOutSpinner, "yyyy-MM-dd"));
        checkOutSpinner.setPreferredSize(new Dimension(130, 28));

        JButton createBookingBtn = new JButton("Create Booking");
        UITheme.styleButton(createBookingBtn, UITheme.SUCCESS, UITheme.WHITE);

        formPanel.add(guestLabel);
        formPanel.add(bookingGuestCombo);
        formPanel.add(roomLabel);
        formPanel.add(bookingRoomCombo);
        formPanel.add(checkInLabel);
        formPanel.add(checkInSpinner);
        formPanel.add(checkOutLabel);
        formPanel.add(checkOutSpinner);
        formPanel.add(createBookingBtn);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(UITheme.WHITE);

        JButton checkInBtn = new JButton("Check In");
        UITheme.styleButton(checkInBtn, UITheme.SUCCESS, UITheme.WHITE);

        JButton cancelBookingBtn = new JButton("Cancel Booking");
        UITheme.styleButton(cancelBookingBtn, UITheme.DANGER, UITheme.WHITE);

        JButton checkOutBtn = new JButton("Check Out");
        UITheme.styleButton(checkOutBtn, UITheme.PRIMARY, UITheme.WHITE);

        actionPanel.add(checkInBtn);
        actionPanel.add(cancelBookingBtn);
        actionPanel.add(checkOutBtn);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.NORTH);
        southPanel.add(actionPanel, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UITheme.BACKGROUND);
        topPanel.add(title, BorderLayout.NORTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(bookingScroll, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        createBookingBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleCreateBooking();
            }
        });

        checkInBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleCheckIn();
            }
        });

        cancelBookingBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleCancelBooking();
            }
        });

        checkOutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleCheckOut();
            }
        });

        return panel;
    }

    /**
     * Refreshes the guest and room dropdowns for the booking creation form.
     */
    private void refreshBookingDropdowns() {
        bookingGuestCombo.removeAllItems();
        for (Guest guest : system.getGuests()) {
            bookingGuestCombo.addItem(guest.getId() + " - " + guest.getName());
        }

        bookingRoomCombo.removeAllItems();
        for (Room room : system.getRooms()) {
            if (room.isAvailable()) {
                bookingRoomCombo.addItem(room.getId() + " - " + room.getRoomNumber());
            }
        }
    }

    /**
     * Handles creating a new booking from the form.
     * Validates date range, finds guest/room, creates booking, and persists.
     */
    private void handleCreateBooking() {
        if (bookingGuestCombo.getItemCount() == 0 || bookingRoomCombo.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No guests or available rooms. Add them first.",
                "Cannot Book", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String guestSel = (String) bookingGuestCombo.getSelectedItem();
        String roomSel = (String) bookingRoomCombo.getSelectedItem();

        Date checkInDate = (Date) checkInSpinner.getValue();
        Date checkOutDate = (Date) checkOutSpinner.getValue();
        LocalDate checkIn = new java.sql.Date(checkInDate.getTime()).toLocalDate();
        LocalDate checkOut = new java.sql.Date(checkOutDate.getTime()).toLocalDate();

        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            JOptionPane.showMessageDialog(this, "Check-out must be after check-in.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int guestId = Integer.parseInt(guestSel.split(" - ")[0]);
        int roomId = Integer.parseInt(roomSel.split(" - ")[0]);

        Guest foundGuest = findGuestById(system.getGuests(), guestId);
        Room foundRoom = findRoomById(system.getRooms(), roomId);

        if (foundGuest == null || foundRoom == null) {
            JOptionPane.showMessageDialog(this, "Guest or room not found.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Booking booking = system.addBooking(foundGuest, foundRoom, checkIn, checkOut);
            db.saveBooking(booking);
            JOptionPane.showMessageDialog(this, "Booking created successfully! ID: " + booking.getId(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                "Booking Error", JOptionPane.ERROR_MESSAGE);
        }

        loadBookingData();
        refreshBookingDropdowns();
        checkInSpinner.setValue(new Date());
        checkOutSpinner.setValue(new Date());
        updateDashboardStats();
    }

    /**
     * Handles cancelling a selected active booking.
     */
    private void handleCancelBooking() {
        int row = bookingTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookingId = (int) bookingTableModel.getValueAt(row, 0);
        String status = (String) bookingTableModel.getValueAt(row, 5);

        if (!"ACTIVE".equals(status)) {
            JOptionPane.showMessageDialog(this, "Only active bookings can be cancelled.",
                "Invalid Action", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Cancel booking ID " + bookingId + "?",
            "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                system.cancelBooking(bookingId);
                db.cancelBookingInDB(bookingId);
                Booking booking = findBookingById(system.getBookings(), bookingId);
                if (booking != null) {
                    db.updateRoomStatus(booking.getRoom());
                }
                JOptionPane.showMessageDialog(this, "Booking cancelled.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            loadBookingData();
            refreshBookingDropdowns();
            updateDashboardStats();
        }
    }

    /**
     * Handles checking in a guest for a selected active booking.
     */
    private void handleCheckIn() {
        int row = bookingTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking to check in.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookingId = (int) bookingTableModel.getValueAt(row, 0);
        String status = (String) bookingTableModel.getValueAt(row, 5);

        if (!"ACTIVE".equals(status)) {
            JOptionPane.showMessageDialog(this,
                "Only ACTIVE bookings can be checked in.\nCurrent status: " + status,
                "Invalid Action", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Check in booking ID " + bookingId + "?\nThis will mark the guest as CHECKED_IN.",
            "Confirm Check In", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                system.checkIn(bookingId);

                Booking booking = findBookingById(system.getBookings(), bookingId);
                if (booking != null) {
                    booking.setStatus("CHECKED_IN");
                    db.updateRoomStatus(booking.getRoom());
                }

                db.updateBookingStatus(bookingId, "CHECKED_IN");

                JOptionPane.showMessageDialog(this,
                    "Check-in successful!\nBooking " + bookingId + " is now CHECKED_IN.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

                loadBookingData();
                refreshBookingDropdowns();
                updateDashboardStats();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error during check-in: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    /**
     * Handles checking out a guest from a checked-in booking.
     */
    private void handleCheckOut() {
        int row = bookingTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking for checkout.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookingId = (int) bookingTableModel.getValueAt(row, 0);
        String status = (String) bookingTableModel.getValueAt(row, 5);

        if (!"CHECKED_IN".equals(status)) {
            JOptionPane.showMessageDialog(this,
                "Only checked-in bookings can be checked out.\nCurrent status: " + status,
                "Invalid Action", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Check out booking ID " + bookingId + "?",
            "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                system.checkOut(bookingId);
                db.checkOutInDB(bookingId);
                Booking booking = findBookingById(system.getBookings(), bookingId);
                if (booking != null) {
                    db.updateRoomStatus(booking.getRoom());
                }
                JOptionPane.showMessageDialog(this, "Checkout successful.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            loadBookingData();
            refreshBookingDropdowns();
            updateDashboardStats();
        }
    }

    /**
     * Refreshes the booking table from the current system data.
     */
    private void loadBookingData() {
        bookingTableModel.setRowCount(0);
        for (Booking booking : system.getBookings()) {
            Object[] rowData = {
                booking.getId(),
                booking.getGuest().getName(),
                booking.getRoom().getRoomNumber(),
                booking.getCheckIn().toString(),
                booking.getCheckOut().toString(),
                booking.getStatus()
            };
            bookingTableModel.addRow(rowData);
        }
    }

    // ---- USERS PANEL (ADMIN ONLY) ----

    /**
     * Creates the User Management panel (admin only) with a table and add/delete form.
     *
     * @return the users panel
     */
    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("User Management");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_DARK);

        String[] userCols = {"ID", "Username", "Role"};
        userTableModel = new DefaultTableModel(userCols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        userTable = new JTable(userTableModel);
        UITheme.styleTable(userTable);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane userScroll = new JScrollPane(userTable);
        userScroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        formPanel.setBackground(UITheme.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(UITheme.FONT_LABEL);
        newUsernameField = new JTextField(10);
        UITheme.styleTextField(newUsernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(UITheme.FONT_LABEL);
        newPasswordField = new JPasswordField(10);
        UITheme.stylePasswordField(newPasswordField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(UITheme.FONT_LABEL);
        userRoleCombo = new JComboBox<String>();
        userRoleCombo.addItem("RECEPTIONIST");
        userRoleCombo.addItem("ADMIN");
        userRoleCombo.setFont(UITheme.FONT_LABEL);

        JButton addUserBtn = new JButton("Add User");
        UITheme.styleButton(addUserBtn, UITheme.SUCCESS, UITheme.WHITE);

        JButton deleteUserBtn = new JButton("Delete Selected");
        UITheme.styleButton(deleteUserBtn, UITheme.DANGER, UITheme.WHITE);

        formPanel.add(userLabel);
        formPanel.add(newUsernameField);
        formPanel.add(passLabel);
        formPanel.add(newPasswordField);
        formPanel.add(roleLabel);
        formPanel.add(userRoleCombo);
        formPanel.add(addUserBtn);
        formPanel.add(deleteUserBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UITheme.BACKGROUND);
        topPanel.add(title, BorderLayout.NORTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(userScroll, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);

        addUserBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleAddUser();
            }
        });

        deleteUserBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleDeleteUser();
            }
        });

        return panel;
    }

    /**
     * Handles adding a new user via the form.
     * Creates either an Admin or Receptionist based on role selection.
     */
    private void handleAddUser() {
        String username = newUsernameField.getText().trim();
        String password = new String(newPasswordField.getPassword()).trim();
        String role = (String) userRoleCombo.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all user fields.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = system.getUsers().size() + 1;
        User newUser;
        if ("ADMIN".equals(role)) {
            newUser = new Admin(id, username, password);
        } else {
            newUser = new Receptionist(id, username, password);
        }
        system.addUser(newUser);

        try {
            db.saveUser(newUser);
            JOptionPane.showMessageDialog(this, "User added successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }

        loadUserData();
        newUsernameField.setText("");
        newPasswordField.setText("");
    }

    /**
     * Handles deleting a selected user (prevents self-deletion).
     */
    private void handleDeleteUser() {
        int row = userTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int userId = (int) userTableModel.getValueAt(row, 0);

        if (userId == currentUser.getId()) {
            JOptionPane.showMessageDialog(this, "You cannot delete yourself!",
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete user ID " + userId + "?",
            "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            system.removeUser(userId);
            try {
                db.removeUserFromDB(userId);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            loadUserData();
        }
    }

    /**
     * Refreshes the user table from the current system data.
     */
    private void loadUserData() {
        if (userTableModel == null) {
            return;
        }
        userTableModel.setRowCount(0);
        for (User user : system.getUsers()) {
            Object[] rowData = {
                user.getId(),
                user.getUsername(),
                user.getRole()
            };
            userTableModel.addRow(rowData);
        }
    }

    // ---- LOGOUT ----

    /**
     * Logs out the current user and returns to the login screen.
     */
    private void handleLogout() {
        system.logout();
        dispose();
        new LoginFrame(system, db);
    }

    // ---- LOOKUP HELPERS ----

    /**
     * Finds a guest by ID from an ArrayList.
     *
     * @param list the list of guests to search
     * @param id   the guest ID to find
     * @return the matching Guest, or {@code null} if not found
     */
    private Guest findGuestById(java.util.ArrayList<Guest> list, int id) {
        for (Guest g : list) {
            if (g.getId() == id) {
                return g;
            }
        }
        return null;
    }

    /**
     * Finds a room by ID from an ArrayList.
     *
     * @param list the list of rooms to search
     * @param id   the room ID to find
     * @return the matching Room, or {@code null} if not found
     */
    private Room findRoomById(java.util.ArrayList<Room> list, int id) {
        for (Room r : list) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    /**
     * Finds a booking by ID from an ArrayList.
     *
     * @param list the list of bookings to search
     * @param id   the booking ID to find
     * @return the matching Booking, or {@code null} if not found
     */
    private Booking findBookingById(java.util.ArrayList<Booking> list, int id) {
        for (Booking b : list) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }
}
