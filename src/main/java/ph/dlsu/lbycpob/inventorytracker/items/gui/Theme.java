package ph.dlsu.lbycpob.inventorytracker.items.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Shared visual language for the whole GUI: colors, fonts, and small factory
 * methods so every panel looks consistent without repeating styling code.
 */
public final class Theme {
    private Theme() {
    }

    // Brand palette
    public static final Color NAVY = new Color(0x10, 0x2A, 0x2E);
    public static final Color NAVY_LIGHT = new Color(0x16, 0x3B, 0x40);
    public static final Color GREEN_PRIMARY = new Color(0x0E, 0x7C, 0x4A);
    public static final Color GREEN_DARK = new Color(0x0A, 0x5C, 0x38);
    public static final Color RED_EMERGENCY = new Color(0xD3, 0x2F, 0x2F);
    public static final Color RED_DARK = new Color(0xB0, 0x22, 0x22);
    public static final Color AMBER_WARNING = new Color(0xF5, 0x9E, 0x0B);
    public static final Color BG_LIGHT = new Color(0xF4, 0xF6, 0xF6);
    public static final Color CARD_WHITE = Color.WHITE;
    public static final Color TEXT_DARK = new Color(0x1F, 0x29, 0x2B);
    public static final Color TEXT_MUTED = new Color(0x66, 0x73, 0x75);
    public static final Color BORDER = new Color(0xDD, 0xE3, 0xE3);

    public static final Color STOCK_GREEN = new Color(0x2E, 0x7D, 0x32);
    public static final Color STOCK_YELLOW = new Color(0xB2, 0x8B, 0x00);
    public static final Color STOCK_RED = new Color(0xC6, 0x28, 0x28);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO_NUMBER = new Font("Segoe UI", Font.BOLD, 20);

    /** Installs sensible global defaults (fonts, selection colors) once at startup. */
    public static void installGlobalDefaults() {
        UIManager.put("ToolTip.background", NAVY);
        UIManager.put("ToolTip.foreground", Color.WHITE);
        UIManager.put("OptionPane.messageFont", FONT_BODY);
        UIManager.put("OptionPane.buttonFont", FONT_BODY_BOLD);
    }

    public static JButton primaryButton(String text) {
        JButton b = baseButton(text);
        b.setBackground(GREEN_PRIMARY);
        b.setForeground(Color.WHITE);
        return b;
    }

    public static JButton dangerButton(String text) {
        JButton b = baseButton(text);
        b.setBackground(RED_EMERGENCY);
        b.setForeground(Color.WHITE);
        return b;
    }

    public static JButton secondaryButton(String text) {
        JButton b = baseButton(text);
        b.setBackground(Color.WHITE);
        b.setForeground(GREEN_DARK);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GREEN_PRIMARY, 1, true),
                new EmptyBorder(8, 16, 8, 16)));
        return b;
    }

    private static JButton baseButton(String text) {
        JButton b = new JButton(text);
        b.setFont(FONT_BODY_BOLD);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(9, 18, 9, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setBorderPainted(false);
        return b;
    }

    public static JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEADER);
        l.setForeground(TEXT_DARK);
        return l;
    }

    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(CARD_WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(16, 18, 16, 18)));
        return p;
    }

    public static Color stockColor(String statusName) {
        return switch (statusName) {
            case "GREEN" -> STOCK_GREEN;
            case "YELLOW" -> STOCK_YELLOW;
            case "RED" -> STOCK_RED;
            default -> TEXT_MUTED;
        };
    }
}
