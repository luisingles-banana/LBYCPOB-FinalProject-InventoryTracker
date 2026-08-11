package ph.dlsu.lbycpob.inventorytracker.items.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URI;

/**
 * Attempts to place a call using the OS's registered "tel:" handler (this
 * works out of the box on many phones/tablets and some desktops with a
 * softphone installed). Most plain desktop machines have no such handler, so
 * this always falls back to copying the number to the clipboard and telling
 * the user to dial it manually — it never silently fails.
 */
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
