package com.boray.Utils;

import javax.swing.*;
import java.awt.*;

public class IconJDialog extends JDialog {


    public IconJDialog() {
        super();
        setIconImage(new ImageIcon(Util.class.getResource("/icon/boray.png")).getImage());
        repaint();
    }

    public IconJDialog(Frame owner, boolean modal) {
        super(owner, modal);
        setIconImage(new ImageIcon(Util.class.getResource("/icon/boray.png")).getImage());
        repaint();
    }

    public IconJDialog(Dialog owner) {
        super(owner, false);
    }

    public IconJDialog(Dialog owner, boolean modal) {
        super(owner, "", modal);
    }
}
