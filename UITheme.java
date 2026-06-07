import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Centralized UI theme constants and styling utilities for the hotel reservation
 * system. Provides consistent colors, fonts, and helper methods to style Swing
 * components throughout the application.
 */
public class UITheme {

    /** Dark navy blue — primary brand color. */
    public static final Color PRIMARY = new Color(26, 43, 76);

    /** Gold — accent/secondary color. */
    public static final Color SECONDARY = new Color(212, 175, 55);

    /** Light gray — page background. */
    public static final Color BACKGROUND = new Color(245, 245, 245);

    /** Pure white. */
    public static final Color WHITE = Color.WHITE;

    /** Red — danger/error indicators. */
    public static final Color DANGER = new Color(192, 57, 43);

    /** Green — success/available indicators. */
    public static final Color SUCCESS = new Color(39, 174, 96);

    /** Near-black — dark text color. */
    public static final Color TEXT_DARK = new Color(30, 30, 30);

    /** Lighter navy — sidebar hover state. */
    public static final Color SIDEBAR_HOVER = new Color(45, 65, 105);

    /** Light blue-gray — alternating table row background. */
    public static final Color TABLE_ROW_ALT = new Color(235, 240, 255);

    /** Font for page titles (22pt bold). */
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);

    /** Font for subtitles and table headers (14pt bold). */
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 14);

    /** Font for labels and form fields (13pt plain). */
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);

    /** Font for buttons (13pt bold). */
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);

    /** Font for table cell content (13pt plain). */
    public static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 13);

    /** Font for secondary/small text (11pt plain). */
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    /**
     * Applies consistent styling to a JButton including hover effect via
     * MouseListener.
     *
     * @param btn the button to style
     * @param bg  the background color
     * @param fg  the foreground (text) color
     */
    public static void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(FONT_BUTTON);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
    }

    /**
     * Applies consistent styling to a JTable including alternating row colors,
     * custom header appearance, and selection colors.
     *
     * @param table the table to style
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setRowHeight(32);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(SECONDARY);
        table.setSelectionForeground(WHITE);
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(FONT_SUBTITLE);
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean selected, boolean focused, int row, int col) {
                super.getTableCellRendererComponent(t, val, selected, focused, row, col);
                if (!selected) {
                    if (row % 2 == 0) {
                        setBackground(WHITE);
                    } else {
                        setBackground(TABLE_ROW_ALT);
                    }
                }
                return this;
            }
        });
    }

    /**
     * Applies consistent styling to a JTextField.
     *
     * @param field the text field to style
     */
    public static void styleTextField(JTextField field) {
        field.setFont(FONT_LABEL);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        field.setBackground(WHITE);
    }

    /**
     * Applies consistent styling to a JPasswordField.
     *
     * @param field the password field to style
     */
    public static void stylePasswordField(JPasswordField field) {
        field.setFont(FONT_LABEL);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        field.setBackground(WHITE);
    }
}
