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
import java.text.MessageFormat;
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

    public DashboardFrame(HotelSystem system, DatabaseManager db, User currentUser) {
        this.system = system;
        this.db = db;
        this.currentUser = currentUser;
        buildUI();
        UITheme.applyRTL(getContentPane());
        setVisible(true);
    }

    private void buildUI() {
        setTitle(Strings.DASHBOARD_WINDOW_TITLE);
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BACKGROUND);

        createSidebar();
        createContentPanel();

        add(sidebar, BorderLayout.EAST);
        add(contentPanel, BorderLayout.CENTER);

        setActiveButton(dashboardBtn);
        updateDashboardStats();
    }

    private Font getFontLabel() {
        return UITheme.FONT_ARABIC;
    }

    private Font getFontBold() {
        return UITheme.FONT_ARABIC_BOLD;
    }

    private Font getFontTitle() {
        return UITheme.FONT_ARABIC_TITLE;
    }

    private void createSidebar() {
        sidebar = new JPanel();
        sidebar.setBackground(UITheme.PRIMARY);
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JPanel brandPanel = new JPanel();
        brandPanel.setBackground(UITheme.PRIMARY);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 20, 20));

        JLabel brandLabel = new JLabel(Strings.BRAND_TITLE);
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brandLabel.setForeground(UITheme.SECONDARY);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brandSub = new JLabel(Strings.BRAND_SUBTITLE);
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

        dashboardBtn = createSidebarButton(Strings.NAV_DASHBOARD, true);
        roomsBtn = createSidebarButton(Strings.NAV_ROOMS, false);
        guestsBtn = createSidebarButton(Strings.NAV_GUESTS, false);
        bookingsBtn = createSidebarButton(Strings.NAV_BOOKINGS, false);

        sidebar.add(dashboardBtn);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(roomsBtn);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(guestsBtn);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(bookingsBtn);

        if ("ADMIN".equals(currentUser.getRole())) {
            sidebar.add(Box.createVerticalStrut(5));
            usersBtn = createSidebarButton(Strings.NAV_USERS, false);
            sidebar.add(usersBtn);
        }

        sidebar.add(Box.createVerticalGlue());

        JPanel logoutPanel = new JPanel();
        logoutPanel.setBackground(UITheme.PRIMARY);
        logoutPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        logoutPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoutBtn = new JButton(Strings.NAV_LOGOUT);
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

    private JButton createSidebarButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(getFontBold());
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

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel(Strings.DASHBOARD_TITLE);
        title.setFont(getFontTitle());
        title.setForeground(UITheme.TEXT_DARK);

        JPanel cardsGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        cardsGrid.setBackground(UITheme.BACKGROUND);
        cardsGrid.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        totalRoomsLabel = new JLabel("0", SwingConstants.CENTER);
        availableRoomsLabel = new JLabel("0", SwingConstants.CENTER);
        totalBookingsLabel = new JLabel("0", SwingConstants.CENTER);
        activeBookingsLabel = new JLabel("0", SwingConstants.CENTER);

        cardsGrid.add(createStatCard(Strings.DASHBOARD_TOTAL_ROOMS, totalRoomsLabel, UITheme.PRIMARY));
        cardsGrid.add(createStatCard(Strings.DASHBOARD_AVAILABLE_ROOMS, availableRoomsLabel, UITheme.SUCCESS));
        cardsGrid.add(createStatCard(Strings.DASHBOARD_TOTAL_BOOKINGS, totalBookingsLabel, UITheme.SECONDARY));
        cardsGrid.add(createStatCard(Strings.DASHBOARD_ACTIVE_BOOKINGS, activeBookingsLabel, UITheme.DANGER));

        panel.add(title, BorderLayout.NORTH);
        panel.add(cardsGrid, BorderLayout.CENTER);

        return panel;
    }

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
        label.setFont(getFontLabel());
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

    private JPanel createRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel(Strings.ROOMS_TITLE);
        title.setFont(getFontTitle());
        title.setForeground(UITheme.TEXT_DARK);

        String[] roomCols = {
            Strings.ROOMS_COL_ID,
            Strings.ROOMS_COL_NUMBER,
            Strings.ROOMS_COL_TYPE,
            Strings.ROOMS_COL_PRICE,
            Strings.ROOMS_COL_STATUS
        };
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

        JLabel roomNumLabel = new JLabel(Strings.ROOMS_LABEL_NUMBER);
        roomNumLabel.setFont(getFontLabel());
        roomNumberField = new JTextField(10);
        UITheme.styleTextField(roomNumberField);

        JLabel roomTypeLabel = new JLabel(Strings.ROOMS_LABEL_TYPE);
        roomTypeLabel.setFont(getFontLabel());
        roomTypeCombo = new JComboBox<String>();
        roomTypeCombo.addItem(Strings.ROOMS_TYPE_SINGLE);
        roomTypeCombo.addItem(Strings.ROOMS_TYPE_DOUBLE);
        roomTypeCombo.setFont(getFontLabel());

        JLabel roomPriceLabel = new JLabel(Strings.ROOMS_LABEL_PRICE);
        roomPriceLabel.setFont(getFontLabel());
        roomPriceField = new JTextField(8);
        UITheme.styleTextField(roomPriceField);

        JButton addRoomBtn = new JButton(Strings.ROOMS_ADD);
        UITheme.styleButton(addRoomBtn, UITheme.SUCCESS, UITheme.WHITE);

        JButton deleteRoomBtn = new JButton(Strings.ROOMS_DELETE);
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

    private void handleAddRoom() {
        String roomNum = roomNumberField.getText().trim();
        String type = (String) roomTypeCombo.getSelectedItem();
        String priceStr = roomPriceField.getText().trim();

        if (roomNum.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, Strings.ROOMS_FILL_FIELDS,
                Strings.DIALOG_VALIDATION_ERROR, JOptionPane.WARNING_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, Strings.ROOMS_PRICE_NUMBER,
                Strings.DIALOG_VALIDATION_ERROR, JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Room r : system.getRooms()) {
            if (r.getRoomNumber().equals(roomNum)) {
                JOptionPane.showMessageDialog(this, Strings.ROOMS_DUPLICATE_NUMBER,
                    Strings.DIALOG_DUPLICATE_ERROR, JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        int id = system.getRooms().size() + 1;
        Room room = new Room(id, roomNum, type, price);
        system.addRoom(room);

        try {
            db.saveRoom(room);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                MessageFormat.format(Strings.DIALOG_DB_ERROR, ex.getMessage()),
                Strings.DIALOG_ERROR, JOptionPane.ERROR_MESSAGE);
        }

        loadRoomData();
        roomNumberField.setText("");
        roomPriceField.setText("");
    }

    private void handleDeleteRoom() {
        int row = roomTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, Strings.ROOMS_NO_SELECTION,
                Strings.DIALOG_NO_SELECTION, JOptionPane.WARNING_MESSAGE);
            return;
        }
        int roomId = (int) roomTableModel.getValueAt(row, 0);
        for (Booking booking : system.getBookings()) {
            if (booking.getRoom().getId() == roomId) {
                JOptionPane.showMessageDialog(this, Strings.ROOMS_HAS_BOOKINGS,
                    Strings.DIALOG_WARNING, JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            MessageFormat.format(Strings.ROOMS_CONFIRM_DELETE, roomId),
            Strings.DIALOG_CONFIRM, JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            system.removeRoom(roomId);
            try {
                db.removeRoomFromDB(roomId);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    MessageFormat.format(Strings.DIALOG_DB_ERROR, ex.getMessage()),
                    Strings.DIALOG_ERROR, JOptionPane.ERROR_MESSAGE);
            }
            loadRoomData();
        }
    }

    private void loadRoomData() {
        roomTableModel.setRowCount(0);
        for (Room room : system.getRooms()) {
            String type = room.getType();
            if ("Single".equals(type)) {
                type = Strings.ROOMS_TYPE_SINGLE;
            } else if ("Double".equals(type)) {
                type = Strings.ROOMS_TYPE_DOUBLE;
            }
            String status = room.getStatus();
            if ("AVAILABLE".equals(status)) {
                status = Strings.ROOMS_STATUS_AVAILABLE;
            } else if ("OCCUPIED".equals(status)) {
                status = Strings.ROOMS_STATUS_OCCUPIED;
            }
            Object[] rowData = {
                room.getId(),
                room.getRoomNumber(),
                type,
                String.format("%.2f", room.getPrice()),
                status
            };
            roomTableModel.addRow(rowData);
        }
        updateDashboardStats();
    }

    private JPanel createGuestsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel(Strings.GUESTS_TITLE);
        title.setFont(getFontTitle());
        title.setForeground(UITheme.TEXT_DARK);

        String[] guestCols = {
            Strings.GUESTS_COL_ID,
            Strings.GUESTS_COL_NAME,
            Strings.GUESTS_COL_NATIONAL_ID,
            Strings.GUESTS_COL_PHONE
        };
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

        JLabel nameLabel = new JLabel(Strings.GUESTS_LABEL_NAME);
        nameLabel.setFont(getFontLabel());
        guestNameField = new JTextField(12);
        UITheme.styleTextField(guestNameField);

        JLabel nidLabel = new JLabel(Strings.GUESTS_LABEL_NATIONAL_ID);
        nidLabel.setFont(getFontLabel());
        guestNationalIdField = new JTextField(10);
        UITheme.styleTextField(guestNationalIdField);

        JLabel phoneLabel = new JLabel(Strings.GUESTS_LABEL_PHONE);
        phoneLabel.setFont(getFontLabel());
        guestPhoneField = new JTextField(10);
        UITheme.styleTextField(guestPhoneField);

        JButton addGuestBtn = new JButton(Strings.GUESTS_ADD);
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

    private void handleAddGuest() {
        String name = guestNameField.getText().trim();
        String nid = guestNationalIdField.getText().trim();
        String phone = guestPhoneField.getText().trim();

        if (name.isEmpty() || nid.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, Strings.GUESTS_FILL_FIELDS,
                Strings.DIALOG_VALIDATION_ERROR, JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!nid.matches("\\d{6}")) {
            JOptionPane.showMessageDialog(this, Strings.GUESTS_NID_INVALID,
                Strings.DIALOG_VALIDATION_ERROR, JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, Strings.GUESTS_PHONE_INVALID,
                Strings.DIALOG_VALIDATION_ERROR, JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Guest g : system.getGuests()) {
            if (g.getNationalId().equals(nid)) {
                JOptionPane.showMessageDialog(this, Strings.GUESTS_DUPLICATE_NID,
                    Strings.DIALOG_DUPLICATE_ERROR, JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        int id = system.getGuests().size() + 1;
        Guest guest = new Guest(id, name, nid, phone);
        system.addGuest(guest);

        try {
            db.saveGuest(guest);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                MessageFormat.format(Strings.DIALOG_DB_ERROR, ex.getMessage()),
                Strings.DIALOG_ERROR, JOptionPane.ERROR_MESSAGE);
        }

        loadGuestData();
        guestNameField.setText("");
        guestNationalIdField.setText("");
        guestPhoneField.setText("");
    }

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

    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel(Strings.BOOKINGS_TITLE);
        title.setFont(getFontTitle());
        title.setForeground(UITheme.TEXT_DARK);

        String[] bookingCols = {
            Strings.BOOKINGS_COL_ID,
            Strings.BOOKINGS_COL_GUEST,
            Strings.BOOKINGS_COL_ROOM,
            Strings.BOOKINGS_COL_CHECK_IN,
            Strings.BOOKINGS_COL_CHECK_OUT,
            Strings.BOOKINGS_COL_STATUS
        };
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

        JLabel guestLabel = new JLabel(Strings.BOOKINGS_LABEL_GUEST);
        guestLabel.setFont(getFontLabel());
        bookingGuestCombo = new JComboBox<String>();
        bookingGuestCombo.setFont(getFontLabel());
        bookingGuestCombo.setPreferredSize(new Dimension(150, 30));

        JLabel roomLabel = new JLabel(Strings.BOOKINGS_LABEL_ROOM);
        roomLabel.setFont(getFontLabel());
        bookingRoomCombo = new JComboBox<String>();
        bookingRoomCombo.setFont(getFontLabel());
        bookingRoomCombo.setPreferredSize(new Dimension(120, 30));

        JLabel checkInLabel = new JLabel(Strings.BOOKINGS_LABEL_CHECK_IN);
        checkInLabel.setFont(getFontLabel());
        checkInSpinner = new JSpinner(new SpinnerDateModel());
        checkInSpinner.setEditor(new JSpinner.DateEditor(checkInSpinner, "yyyy-MM-dd"));
        checkInSpinner.setPreferredSize(new Dimension(130, 28));

        JLabel checkOutLabel = new JLabel(Strings.BOOKINGS_LABEL_CHECK_OUT);
        checkOutLabel.setFont(getFontLabel());
        checkOutSpinner = new JSpinner(new SpinnerDateModel());
        checkOutSpinner.setEditor(new JSpinner.DateEditor(checkOutSpinner, "yyyy-MM-dd"));
        checkOutSpinner.setPreferredSize(new Dimension(130, 28));

        JButton createBookingBtn = new JButton(Strings.BOOKINGS_CREATE);
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

        JButton checkInBtn = new JButton(Strings.BOOKINGS_CHECK_IN);
        UITheme.styleButton(checkInBtn, UITheme.SUCCESS, UITheme.WHITE);

        JButton cancelBookingBtn = new JButton(Strings.BOOKINGS_CANCEL);
        UITheme.styleButton(cancelBookingBtn, UITheme.DANGER, UITheme.WHITE);

        JButton checkOutBtn = new JButton(Strings.BOOKINGS_CHECK_OUT);
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

    private void handleCreateBooking() {
        if (bookingGuestCombo.getItemCount() == 0 || bookingRoomCombo.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, Strings.BOOKINGS_NO_GUESTS_ROOMS,
                Strings.DIALOG_WARNING, JOptionPane.WARNING_MESSAGE);
            return;
        }

        String guestSel = (String) bookingGuestCombo.getSelectedItem();
        String roomSel = (String) bookingRoomCombo.getSelectedItem();

        Date checkInDate = (Date) checkInSpinner.getValue();
        Date checkOutDate = (Date) checkOutSpinner.getValue();
        LocalDate checkIn = new java.sql.Date(checkInDate.getTime()).toLocalDate();
        LocalDate checkOut = new java.sql.Date(checkOutDate.getTime()).toLocalDate();

        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            JOptionPane.showMessageDialog(this, Strings.BOOKINGS_INVALID_DATE,
                Strings.DIALOG_VALIDATION_ERROR, JOptionPane.WARNING_MESSAGE);
            return;
        }

        int guestId = Integer.parseInt(guestSel.split(" - ")[0]);
        int roomId = Integer.parseInt(roomSel.split(" - ")[0]);

        Guest foundGuest = findGuestById(system.getGuests(), guestId);
        Room foundRoom = findRoomById(system.getRooms(), roomId);

        if (foundGuest == null || foundRoom == null) {
            JOptionPane.showMessageDialog(this, Strings.BOOKINGS_ERROR,
                Strings.DIALOG_ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Booking booking = system.addBooking(foundGuest, foundRoom, checkIn, checkOut);
            db.saveBooking(booking);
            JOptionPane.showMessageDialog(this,
                MessageFormat.format(Strings.BOOKINGS_SUCCESS, booking.getId()),
                Strings.DIALOG_SUCCESS, JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                MessageFormat.format(Strings.DIALOG_DB_ERROR, ex.getMessage()),
                Strings.DIALOG_ERROR, JOptionPane.ERROR_MESSAGE);
        }

        loadBookingData();
        refreshBookingDropdowns();
        checkInSpinner.setValue(new Date());
        checkOutSpinner.setValue(new Date());
        updateDashboardStats();
    }

    private void handleCancelBooking() {
        int row = bookingTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, Strings.BOOKINGS_NO_SELECTION,
                Strings.DIALOG_NO_SELECTION, JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookingId = (int) bookingTableModel.getValueAt(row, 0);
        String status = (String) bookingTableModel.getValueAt(row, 5);

        if (!"ACTIVE".equals(status)) {
            JOptionPane.showMessageDialog(this, Strings.BOOKINGS_NOT_ACTIVE,
                Strings.DIALOG_INVALID_ACTION, JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            MessageFormat.format(Strings.BOOKINGS_CONFIRM_CANCEL, bookingId),
            Strings.DIALOG_CONFIRM, JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                system.cancelBooking(bookingId);
                db.cancelBookingInDB(bookingId);
                Booking booking = findBookingById(system.getBookings(), bookingId);
                if (booking != null) {
                    db.updateRoomStatus(booking.getRoom());
                }
                JOptionPane.showMessageDialog(this, Strings.BOOKINGS_CANCELLED,
                    Strings.DIALOG_SUCCESS, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    MessageFormat.format(Strings.DIALOG_DB_ERROR, ex.getMessage()),
                    Strings.DIALOG_ERROR, JOptionPane.ERROR_MESSAGE);
            }
            loadBookingData();
            refreshBookingDropdowns();
            updateDashboardStats();
        }
    }

    private void handleCheckIn() {
        int row = bookingTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, Strings.BOOKINGS_SELECT_FOR_CHECK_IN,
                Strings.DIALOG_NO_SELECTION, JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookingId = (int) bookingTableModel.getValueAt(row, 0);
        String status = (String) bookingTableModel.getValueAt(row, 5);

        if (!"ACTIVE".equals(status)) {
            JOptionPane.showMessageDialog(this,
                MessageFormat.format(Strings.BOOKINGS_CHECK_IN_EMPTY, status),
                Strings.DIALOG_INVALID_ACTION, JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            MessageFormat.format(Strings.BOOKINGS_CHECK_IN_CONFIRM, bookingId),
            Strings.DIALOG_CONFIRM, JOptionPane.YES_NO_OPTION);

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
                    MessageFormat.format(Strings.BOOKINGS_CHECK_IN_SUCCESS, bookingId),
                    Strings.DIALOG_SUCCESS, JOptionPane.INFORMATION_MESSAGE);

                loadBookingData();
                refreshBookingDropdowns();
                updateDashboardStats();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    MessageFormat.format(Strings.DIALOG_DB_ERROR, ex.getMessage()),
                    Strings.DIALOG_ERROR, JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void handleCheckOut() {
        int row = bookingTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, Strings.BOOKINGS_CHECK_OUT_EMPTY,
                Strings.DIALOG_NO_SELECTION, JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookingId = (int) bookingTableModel.getValueAt(row, 0);
        String status = (String) bookingTableModel.getValueAt(row, 5);

        if (!"CHECKED_IN".equals(status)) {
            JOptionPane.showMessageDialog(this,
                MessageFormat.format(Strings.BOOKINGS_CHECK_OUT_ONLY_CHECKED_IN, status),
                Strings.DIALOG_INVALID_ACTION, JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            MessageFormat.format(Strings.BOOKINGS_CONFIRM_CHECK_OUT, bookingId),
            Strings.DIALOG_CONFIRM, JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                system.checkOut(bookingId);
                db.checkOutInDB(bookingId);
                Booking booking = findBookingById(system.getBookings(), bookingId);
                if (booking != null) {
                    db.updateRoomStatus(booking.getRoom());
                }
                JOptionPane.showMessageDialog(this, Strings.BOOKINGS_CHECK_OUT_SUCCESS,
                    Strings.DIALOG_SUCCESS, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    MessageFormat.format(Strings.DIALOG_DB_ERROR, ex.getMessage()),
                    Strings.DIALOG_ERROR, JOptionPane.ERROR_MESSAGE);
            }
            loadBookingData();
            refreshBookingDropdowns();
            updateDashboardStats();
        }
    }

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

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel(Strings.USERS_TITLE);
        title.setFont(getFontTitle());
        title.setForeground(UITheme.TEXT_DARK);

        String[] userCols = {
            Strings.USERS_COL_ID,
            Strings.USERS_COL_USERNAME,
            Strings.USERS_COL_ROLE
        };
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

        JLabel userLabel = new JLabel(Strings.USERS_LABEL_USERNAME);
        userLabel.setFont(getFontLabel());
        newUsernameField = new JTextField(10);
        UITheme.styleTextField(newUsernameField);

        JLabel passLabel = new JLabel(Strings.USERS_LABEL_PASSWORD);
        passLabel.setFont(getFontLabel());
        newPasswordField = new JPasswordField(10);
        UITheme.stylePasswordField(newPasswordField);

        JLabel roleLabel = new JLabel(Strings.USERS_LABEL_ROLE);
        roleLabel.setFont(getFontLabel());
        userRoleCombo = new JComboBox<String>();
        userRoleCombo.addItem(Strings.USERS_ROLE_RECEPTIONIST);
        userRoleCombo.addItem(Strings.USERS_ROLE_ADMIN);
        userRoleCombo.setFont(getFontLabel());

        JButton addUserBtn = new JButton(Strings.USERS_ADD);
        UITheme.styleButton(addUserBtn, UITheme.SUCCESS, UITheme.WHITE);

        JButton deleteUserBtn = new JButton(Strings.USERS_DELETE);
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

    private void handleAddUser() {
        String username = newUsernameField.getText().trim();
        String password = new String(newPasswordField.getPassword()).trim();
        String role = (String) userRoleCombo.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, Strings.USERS_FILL_FIELDS,
                Strings.DIALOG_VALIDATION_ERROR, JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (User u : system.getUsers()) {
            if (u.getUsername().equals(username)) {
                JOptionPane.showMessageDialog(this, Strings.USERS_DUPLICATE_USERNAME,
                    Strings.DIALOG_DUPLICATE_ERROR, JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        int id = system.getUsers().size() + 1;
        User newUser;
        if (Strings.USERS_ROLE_ADMIN.equals(role)) {
            newUser = new Admin(id, username, password);
        } else {
            newUser = new Receptionist(id, username, password);
        }
        system.addUser(newUser);

        try {
            db.saveUser(newUser);
            JOptionPane.showMessageDialog(this, Strings.USERS_ADD_SUCCESS,
                Strings.DIALOG_SUCCESS, JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                MessageFormat.format(Strings.DIALOG_DB_ERROR, ex.getMessage()),
                Strings.DIALOG_ERROR, JOptionPane.ERROR_MESSAGE);
        }

        loadUserData();
        newUsernameField.setText("");
        newPasswordField.setText("");
    }

    private void handleDeleteUser() {
        int row = userTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, Strings.USERS_NO_SELECTION,
                Strings.DIALOG_NO_SELECTION, JOptionPane.WARNING_MESSAGE);
            return;
        }
        int userId = (int) userTableModel.getValueAt(row, 0);

        if (userId == currentUser.getId()) {
            JOptionPane.showMessageDialog(this, Strings.USERS_SELF_DELETE,
                Strings.DIALOG_ERROR, JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            MessageFormat.format(Strings.USERS_CONFIRM_DELETE, userId),
            Strings.DIALOG_CONFIRM, JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            system.removeUser(userId);
            try {
                db.removeUserFromDB(userId);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    MessageFormat.format(Strings.DIALOG_DB_ERROR, ex.getMessage()),
                    Strings.DIALOG_ERROR, JOptionPane.ERROR_MESSAGE);
            }
            loadUserData();
        }
    }

    private void loadUserData() {
        if (userTableModel == null) {
            return;
        }
        userTableModel.setRowCount(0);
        for (User user : system.getUsers()) {
            String role = user.getRole();
            if ("ADMIN".equals(role)) {
                role = Strings.USERS_ROLE_ADMIN;
            } else if ("RECEPTIONIST".equals(role)) {
                role = Strings.USERS_ROLE_RECEPTIONIST;
            }
            Object[] rowData = {
                user.getId(),
                user.getUsername(),
                role
            };
            userTableModel.addRow(rowData);
        }
    }

    private void handleLogout() {
        system.logout();
        dispose();
        new LoginFrame(system, db);
    }

    private Guest findGuestById(java.util.ArrayList<Guest> list, int id) {
        for (Guest g : list) {
            if (g.getId() == id) {
                return g;
            }
        }
        return null;
    }

    private Room findRoomById(java.util.ArrayList<Room> list, int id) {
        for (Room r : list) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    private Booking findBookingById(java.util.ArrayList<Booking> list, int id) {
        for (Booking b : list) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }
}
