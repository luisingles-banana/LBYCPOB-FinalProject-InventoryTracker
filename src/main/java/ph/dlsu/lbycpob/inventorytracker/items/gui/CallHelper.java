package ph.dlsu.lbycpob.inventorytracker.items.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URI;

final class CallHelper {
    private CallHelper() {
    }

    static void call(Component parent, String contactName, String number) {
        String digits = number.replaceAll("[^0-9+]", "");
        boolean dialed = false;
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("tel:" + digits));
                dialed = true;
            }
        } catch (Exception ignored) {
            // No "tel:" handler on this machine — fall back below.
        }

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(number), null);

        if (!dialed) {
            JOptionPane.showMessageDialog(parent,
                    "This computer has no phone dialer registered, so " + contactName +
                            "'s number (" + number + ") has been copied to your clipboard instead.\n" +
                            "Please dial it manually from your phone.",
                    "Number Copied", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
