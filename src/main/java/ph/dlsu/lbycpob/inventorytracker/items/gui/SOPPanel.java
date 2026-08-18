package ph.dlsu.lbycpob.inventorytracker.items.gui;

import ph.dlsu.lbycpob.inventorytracker.items.SOP.SOP;
import ph.dlsu.lbycpob.inventorytracker.items.SOP.SOPRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Browsable library of crisis-specific Standard Operating Procedures: pick a
 * scenario on the left, read its protocol steps and required-supply
 * checklist on the right.
 */
public class SOPPanel extends JPanel {
    private final DefaultListModel<SOP> listModel = new DefaultListModel<>();
    private final JList<SOP> sopList = new JList<>(listModel);
    private final JEditorPane detailPane = new JEditorPane();

    public SOPPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_LIGHT);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        JLabel title = Theme.sectionTitle("SOP & Emergency Protocol Library");
        title.setBorder(new EmptyBorder(0, 0, 16, 0));
        add(title, BorderLayout.NORTH);

        for (SOP sop : SOPRepository.getAllSOPs()) {
            listModel.addElement(sop);
        }
        sopList.setFont(Theme.FONT_BODY_BOLD);
        sopList.setFixedCellHeight(44);
        sopList.setSelectionBackground(Theme.GREEN_PRIMARY);
        sopList.setSelectionForeground(Color.WHITE);
        sopList.setBorder(new EmptyBorder(4, 4, 4, 4));
        sopList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && sopList.getSelectedValue() != null) {
                showDetail(sopList.getSelectedValue());
            }
        });

        JScrollPane listScroll = new JScrollPane(sopList);
        listScroll.setPreferredSize(new Dimension(230, 0));
        listScroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));

        detailPane.setContentType("text/html");
        detailPane.setEditable(false);
        detailPane.setBorder(new EmptyBorder(6, 20, 6, 6));
        JScrollPane detailScroll = new JScrollPane(detailPane);
        detailScroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, detailScroll);
        split.setDividerLocation(230);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);

        if (!listModel.isEmpty()) {
            sopList.setSelectedIndex(0);
        }
    }

    private void showDetail(SOP sop) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family:Segoe UI;padding:8px;'>");
        html.append("<h1 style='color:#0E7C4A;margin-bottom:2px;'>").append(escape(sop.getScenario())).append("</h1>");
        html.append("<p style='color:#667375;'>").append(escape(sop.getDescription())).append("</p>");

        html.append("<h3 style='color:#102A2E;'>Protocol</h3><ol>");
        for (String step : sop.getProtocolSteps()) {
            html.append("<li style='margin-bottom:6px;'>").append(escape(step)).append("</li>");
        }
        html.append("</ol>");

        html.append("<h3 style='color:#102A2E;'>Required Supply Checklist</h3><ul>");
        for (String supply : sop.getRequiredSupplies()) {
            html.append("<li style='margin-bottom:4px;'>\u2610 ").append(escape(supply)).append("</li>");
        }
        html.append("</ul></body></html>");

        detailPane.setText(html.toString());
        detailPane.setCaretPosition(0);
    }

    private String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
