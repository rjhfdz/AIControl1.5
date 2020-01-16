package com.boray.Utils;

import javax.swing.*;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;

public class JPressButton extends JToggleButton {
    private Color selectBackground;

    public JPressButton(){}

    /**
     * 其它构造方法请自己添加
     */
    public JPressButton(String text) {
        super(text, null, false);
    }

    @Override
    public void updateUI() {
        setUI(new PressButtonUI());
    }

    public Color getSelectBackground() {
        return selectBackground;
    }

    public void setSelectBackground(Color selectBackground) {
        this.selectBackground = selectBackground;
        updateUI();
    }

    class PressButtonUI extends MetalToggleButtonUI {
        @Override
        public void installDefaults(AbstractButton b) {
            super.installDefaults(b);
            JPressButton pressButton = (JPressButton) b;
            selectColor = pressButton.getSelectBackground();
        }
    }
}
