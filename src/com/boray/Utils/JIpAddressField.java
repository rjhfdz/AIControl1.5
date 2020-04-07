package com.boray.Utils;

import java.awt.*;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.border.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * =====================================================================
 * IP控件,此控件与Windows系统使用效果基本一致。目的是为了提高友好性，使客户更容易接受。
 *
 * @author LEE WANG
 * @Date 2010-08-27
 * =====================================================================
 */
public class JIpAddressField extends JComponent implements SwingConstants {
    private static final long serialVersionUID = 1L;
    private static final Dimension fixedDimension = new Dimension(130, 25);
    private int fixedWidth = 132;
    private int fixedHeight = 25;
    private int maxValue = 255;
    private int minValue = 0;
    private int length = 3;
    private int width = 30;
    private int height = 23;
    private String ipAddress;

    /**
     * *****************************************************************
     * IP地址输入框的分解：txtDigital1、txtDigital2、txtDigital3、txtDigital4
     * *****************************************************************
     */
    private DigitalText txtDigital1 = new DigitalText();
    private DigitalText txtDigital2 = new DigitalText();
    private DigitalText txtDigital3 = new DigitalText();
    private DigitalText txtDigital4 = new DigitalText();
    /**
     * *****************************************************************
     * IP地址输入框分割符，使用三个JTextArea。原因是：JTextArea与DigitalText类似 必免影响外形上的美观
     * *****************************************************************
     */
    private JTextArea txtCompart1 = new JTextArea();
    private JTextArea txtCompart2 = new JTextArea();
    private JTextArea txtCompart3 = new JTextArea();

    /**
     * *****************************************************************
     * 带参数的构造方法： 默认情况下的 宽为：30 这里的宽指每格输入框的宽度 如‘192’所占位 高为: 25
     * *****************************************************************
     */
    public JIpAddressField() {
        initGUI();
    }

    /**
     * *****************************************************************
     * 带参数的构造方法： 默认情况下的 宽为：30 这里的宽指每格输入框的宽度 如‘192’所占位 高为: 25
     *
     * @param width *****************************************************************
     */
    public JIpAddressField(int width) {
        this.width = width;
        this.fixedWidth = width * 4 + 12;
        initGUI();
    }

    /**
     * *****************************************************************
     * 带参数的构造方法： 默认情况下的 宽为：30 这里的宽指每格输入框的宽度 如‘192’所占位 高为: 25
     *
     * @param width
     * @param height *****************************************************************
     */
    public JIpAddressField(int width, int height) {
        this.width = width;
        this.height = height - 2;
        this.fixedWidth = width * 4 + 12;
        this.fixedHeight = height;
        initGUI();
    }

    /**
     * *****************************************************************
     * 设置输入IP值
     *
     * @param newIpAddress *****************************************************************
     */
    public void setIpAddress(String newIpAddress) {
        StringTokenizer strToken = new StringTokenizer(newIpAddress, ".");
        String[] strIPAddress = new String[strToken.countTokens()];
        if (strToken.countTokens() != 4)
            return;
        int k = 0;
        while (strToken.hasMoreTokens()) {
            strIPAddress[k] = strToken.nextToken();
            k++;
        }
        try {
            txtDigital1.setValue(Integer.parseInt(strIPAddress[0]));
            txtDigital2.setValue(Integer.parseInt(strIPAddress[1]));
            txtDigital3.setValue(Integer.parseInt(strIPAddress[2]));
            txtDigital4.setValue(Integer.parseInt(strIPAddress[3]));
            ipAddress = getIpAddress();
        } catch (Exception e) {
            return;
        }
    }

    /**
     * *****************************************************************
     * 设置IP输入值，同setIpAddress()功能一样
     *
     * @param newIpAddress *****************************************************************
     */
    public void setText(String newIpAddress) {
        setIpAddress(newIpAddress);
    }

    /**
     * *****************************************************************
     * 这个方法可以获得IP
     *
     * @return 0.0.0.0
     * *****************************************************************
     */
    public String getIpAddress() {
        try {
            ipAddress = Integer.toString(txtDigital1.getValue()) + "."
                    + Integer.toString(txtDigital2.getValue()) + "."
                    + Integer.toString(txtDigital3.getValue()) + "."
                    + Integer.toString(txtDigital4.getValue());
        } catch (Exception e) {
            ipAddress = "0.0.0.0";
        }
        return ipAddress;
    }

    /**
     * *****************************************************************
     * 为了方面以前的习惯，我又加上这个方法，其实就是调用了一下getIpAddress()
     *
     * @return *****************************************************************
     */
    public String getText() {
        return getIpAddress();
    }

    /**
     * *****************************************************************
     * 设置IP输入框的大小
     *
     * @param fixedWidth
     * @param fixedHeight *****************************************************************
     */
    public void setFixedSize(int fixedWidth, int fixedHeight) {
        this.setSize(fixedWidth * 4 + 12, fixedHeight);
        this.width = (this.getWidth() - 9) / 4;
        this.height = this.getHeight() - 2;
        Dimension digitalSize = new Dimension(this.width, this.height);
        Dimension compartSize = new Dimension(3, this.height);
        txtDigital1.setPreferredSize(digitalSize);
        txtCompart1.setPreferredSize(compartSize);
        txtDigital2.setPreferredSize(digitalSize);
        txtCompart2.setPreferredSize(compartSize);
        txtDigital3.setPreferredSize(digitalSize);
        txtCompart3.setPreferredSize(compartSize);
        txtDigital4.setPreferredSize(digitalSize);
    }

    /**
     * *****************************************************************
     * 设置IP输入框的可用状态
     * *****************************************************************
     */
    public void setEnabled(boolean bool) {
        txtDigital1.setEnabled(bool);
        txtDigital2.setEnabled(bool);
        txtDigital3.setEnabled(bool);
        txtDigital4.setEnabled(bool);
        if (bool) {
            txtCompart1.setEditable(false);
            txtCompart2.setEditable(false);
            txtCompart3.setEditable(false);
        } else {
            txtCompart1.setEnabled(false);
            txtCompart2.setEnabled(false);
            txtCompart3.setEnabled(false);
        }
    }

    public void setBounds(int l, int t, int w, int h) {
        super.setBounds(l, t, fixedWidth, fixedHeight);
        return;
    }

    /**
     * *****************************************************************
     * 初始化GUI
     * *****************************************************************
     */
    private void initGUI() {
        final FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.CENTER);
        flowLayout.setHgap(0);
        flowLayout.setVgap(0);
        this.setLayout(flowLayout);

        this.setSize(fixedWidth, fixedHeight);
        this.setPreferredSize(fixedDimension);
        this.setOpaque(false);
        this.width = (this.getWidth() - 9) / 4;
        this.height = this.getHeight() - 2;

        /*
         * *****************************************************************
         * 初始化分割组件
         * *****************************************************************
         */
        Font font = new Font("", Font.PLAIN, 16);
        String compart = ".";
        Dimension compartSize = new Dimension(3, height);
        CompoundBorder compartBorder = new CompoundBorder(null, null);

        txtCompart1.setFont(font);
        txtCompart1.setText(compart);
        txtCompart1.setPreferredSize(compartSize);
        txtCompart1.setBorder(compartBorder);

        txtCompart2.setFont(font);
        txtCompart2.setText(compart);
        txtCompart2.setPreferredSize(compartSize);
        txtCompart2.setBorder(compartBorder);

        txtCompart3.setFont(font);
        txtCompart3.setText(compart);
        txtCompart3.setPreferredSize(compartSize);
        txtCompart3.setBorder(compartBorder);
        /*
         * *****************************************************************
         * 初始化输入框
         * *****************************************************************
         */
        Border digitaBorder = BorderFactory.createEmptyBorder();
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                this_componentResized(e);
            }
        });
        txtDigital1.setBorder(digitaBorder);
        txtDigital1.setHorizontalAlignment(SwingConstants.CENTER);
        txtDigital1.setMaxLength(length);
        txtDigital1.setMaxValue(maxValue);
        txtDigital1.setMinValue(minValue);
        txtDigital1.setPreferredSize(new Dimension(width, height));
        txtDigital1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                txtDigital1_keyReleased(e);
            }

            public void keyPressed(KeyEvent e) {
                txtDigital1_keyPressed(e);
            }
        });

        txtDigital2.setBorder(digitaBorder);
        txtDigital2.setHorizontalAlignment(SwingConstants.CENTER);
        txtDigital2.setMaxLength(length);
        txtDigital2.setMaxValue(maxValue);
        txtDigital2.setMinValue(minValue);
        txtDigital2.setPreferredSize(new Dimension(width, height));
        txtDigital2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                txtDigital2_keyReleased(e);
            }

            public void keyPressed(KeyEvent e) {
                txtDigital2_keyPressed(e);
            }
        });

        txtDigital3.setBorder(digitaBorder);
        txtDigital3.setHorizontalAlignment(SwingConstants.CENTER);
        txtDigital3.setMaxLength(length);
        txtDigital3.setMaxValue(maxValue);
        txtDigital3.setPreferredSize(new Dimension(width, height));
        txtDigital3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                txtDigital3_keyReleased(e);
            }

            public void keyPressed(KeyEvent e) {
                txtDigital3_keyPressed(e);
            }
        });

        txtDigital4.setBorder(digitaBorder);
        txtDigital4.setHorizontalAlignment(SwingConstants.CENTER);
        txtDigital4.setMaxLength(length);
        txtDigital4.setMaxValue(maxValue);
        txtDigital4.setPreferredSize(new Dimension(width, height));
        txtDigital4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                txtDigital4_keyReleased(e);
            }

            public void keyPressed(KeyEvent e) {
                txtDigital4_keyPressed(e);
            }
        });

        this.add(txtDigital1);
        this.add(txtCompart1);
        this.add(txtDigital2);
        this.add(txtCompart2);
        this.add(txtDigital3);
        this.add(txtCompart3);
        this.add(txtDigital4);

        txtCompart1.setEditable(false);
        txtCompart2.setEditable(false);
        txtCompart3.setEditable(false);

        Border mainBorder = BorderFactory.createLineBorder(new Color(106, 106,
                106));
        this.setBorder(mainBorder);
    }

    private void this_componentResized(ComponentEvent e) {
        this.setSize(fixedWidth, fixedHeight);
        validate();
    }

    private void txtDigital1_keyReleased(KeyEvent e) {
        switch (isChangeTextFocus(txtDigital1, e)) {
            case 1:
                txtDigital2.requestFocus();
                txtDigital2.selectAll();
                break;
            case 2:
                txtDigital1.requestFocus(true);
                break;
            case 4:
                txtDigital2.requestFocus(true);
            default:
                break;
        }
    }

    private void txtDigital1_keyPressed(KeyEvent e) {
        isChangeTextLenght(txtDigital1, e);
    }

    private void txtDigital2_keyReleased(KeyEvent e) {
        switch (isChangeTextFocus(txtDigital2, e)) {
            case 1:
                txtDigital3.requestFocus();
                txtDigital3.selectAll();
                break;
            case 2:
                txtDigital1.requestFocus(true);
                break;
            case 3:
                txtDigital1.requestFocus(true);
                break;
            case 4:
                txtDigital3.requestFocus(true);
                break;
            default:
                break;
        }
    }

    private void txtDigital2_keyPressed(KeyEvent e) {
        isChangeTextLenght(txtDigital2, e);
    }

    private void txtDigital3_keyReleased(KeyEvent e) {
        switch (isChangeTextFocus(txtDigital3, e)) {
            case 1:
                txtDigital4.requestFocus();
                txtDigital4.selectAll();
                break;
            case 2:
                txtDigital2.requestFocus(true);
                break;
            case 3:
                txtDigital2.requestFocus(true);
                break;
            case 4:
                txtDigital4.requestFocus(true);
                break;
            default:
                break;
        }
    }

    private void txtDigital3_keyPressed(KeyEvent e) {
        isChangeTextLenght(txtDigital3, e);
    }

    private void txtDigital4_keyReleased(KeyEvent e) {
        switch (isChangeTextFocus(txtDigital4, e)) {
            case 1:
                // txtDigital4.nextFocus(); 我觉得这里暂时就不需在跳这样它的效果和Windows一样，用户更习惯
                break;
            case 2:
                txtDigital3.grabFocus();
                break;
            case 3:
                txtDigital3.requestFocus(true);
                break;
            default:
                break;
        }
    }

    private void txtDigital4_keyPressed(KeyEvent e) {
        isChangeTextLenght(txtDigital4, e);
    }

    private int isChangeTextFocus(DigitalText text, KeyEvent e) {
        int length = text.getText().length();
        String selectText = text.getSelectedText();
        if (length == 0 && e.getKeyCode() == 8) {// 8代表退格
            if (text.getSelectionStart() == 0)
                return 2;
            else
                return 0;
        }
        if (e.getKeyCode() == 37) {// 37代表向左
            if (selectText == null && text.getSelectionStart() == 0)
                return 3;
            else
                return 0;
        }
        if (e.getKeyCode() == 39) {// 39代表向右
            if (selectText == null && text.getSelectionEnd() == length)
                return 4;
            else
                return 0;
        }
        if (selectText == null && length >= text.getMaxLength()) {
            return 1;
        }
        if (selectText == null && length > 0 && (e.getKeyCode() == 46 || e.getKeyCode() == 110)) {// 110,46代表'.'
            return 1;
        }
        return 0;
    }

    private int isChangeTextLenght(DigitalText text, KeyEvent e) {
        try {
            String info = text.getText().trim();
            if (info.length() == 2 && text.getSelectedText() == null) {
                int value = Integer.parseInt(info + e.getKeyChar());
                if (value > maxValue) {
                    text.setText(maxValue + "");
                    return 5;
                }
            }
        } catch (Exception ex) {
            return 5;
        }
        return 0;
    }

    private class DigitalText extends JTextField implements Serializable {

        /**
         * 用来输入正整数的单行文本框 可以控制输入的最大长度，最大值和最小值并在显示时可以选择是否在数字前面补零以达到最大长度
         */
        private static final long serialVersionUID = 1L;
        /**
         * 数据成员变量，分别代表当前值，最大值，最小值和最大长度,分别赋默认值
         */
        private int value = 0;
        private int maxValue = 99;
        private int minValue = 0;
        private int maxLength = 2;
        /**
         * 成员变量，代表输入框是否需要在最前面补零，true表示需要
         */
        private boolean enableAddZero = false;

        // 构造函数
        public DigitalText() {
            try {
                initGUI();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void initGUI() throws Exception {
            setDocument(new DigitalTextDocument());
            setHorizontalAlignment(JTextField.RIGHT);

            setMaxLength(maxLength = 2);
            setMaxValue(maxValue = 99);
            setMinValue(minValue = 0);

            addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    this_keyReleased(e);
                }
            });
            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    // this_focusLost(e); //失去焦点时不做任何事，这样比较符合Windows的习惯
                }

                public void focusGained(FocusEvent e) {
                    // this_focusGained(e); //得到焦点时不做任何事，这样比较符合Windows的习惯
                }
            });
        }

        public void setValue(int newValue) {
            if ((newValue <= maxValue) && (newValue >= minValue))
                value = newValue;
            setText(Integer.toString(value));
            adjustForZero();
            revalidate();
        }

        // 获取value
        public int getValue() {
            return Integer.parseInt(this.getText());
        }

        // 设置最大值
        public void setMaxValue(int maxValue) {
            setMinMaxValue(minValue, maxValue);
        }

        // 获取最大值
        public int getMaxValue() {
            return maxValue;
        }

        // 设置最小值
        public void setMinValue(int minValue) {
            setMinMaxValue(minValue, maxValue);
        }

        // 获取最小值
        public int getMinValue() {
            return minValue;
        }

        public void setMinMaxValue(int minValue, int maxValue) {
            int tempMinValue = minValue;
            int tempMaxValue = maxValue;

            if (tempMinValue > tempMaxValue)
                return;

            if (tempMaxValue < 0
                    || tempMinValue > (int) Math.pow(10, maxLength) - 1)
                return;

            if (tempMinValue < 0)
                tempMinValue = 0;

            if (tempMaxValue > (int) Math.pow(10, maxLength) - 1)
                tempMaxValue = (int) Math.pow(10, maxLength) - 1;

            this.maxValue = tempMaxValue;
            this.minValue = tempMinValue;

            if (value < minValue)
                setValue(minValue);

            if (value > maxValue)
                setValue(maxValue);

            ((DigitalTextDocument) getDocument()).setMinValue(minValue);
            ((DigitalTextDocument) getDocument()).setMaxValue(maxValue);
        }

        public void setMaxLength(int newMaxLength) {
            if (newMaxLength < 1)
                return;

            if (newMaxLength >= maxLength) {
                this.maxLength = newMaxLength;
                ((DigitalTextDocument) getDocument()).setMaxLength(maxLength);
            } else {
                if (minValue > (int) Math.pow(10, newMaxLength) - 1) {
                    setMinMaxValue(0, (int) Math.pow(10, newMaxLength) - 1);
                } else if (maxValue > (int) Math.pow(10, newMaxLength) - 1) {
                    setMinMaxValue(minValue,
                            (int) Math.pow(10, newMaxLength) - 1);
                }
                maxLength = newMaxLength;
                ((DigitalTextDocument) getDocument()).setMaxLength(maxLength);
            }
        }

        public int getMaxLength() {
            return maxLength;
        }

        private void this_focusLost(FocusEvent e) {
            commit();
        }

        void writeObject(ObjectOutputStream oos) throws IOException {
            oos.defaultWriteObject();
        }

        void readObject(ObjectInputStream ois) throws ClassNotFoundException,
                IOException {
            ois.defaultReadObject();
        }

        public void adjustForZero() {
            if (enableAddZero) {
                String tempStr = Integer.toString(getValue());
                int addZeroNumber = maxLength - tempStr.length();
                for (int i = 0; i < addZeroNumber; i++)
                    tempStr = "0" + tempStr;
                setText(tempStr);
                revalidate();
            } else {
                int tempValue = Integer.parseInt(getText().trim());
                setText(Integer.toString(tempValue));
                revalidate();
            }
        }

        public void setEnableAddZero(boolean newEnableAddZero) {
            enableAddZero = newEnableAddZero;
            adjustForZero();
        }

        public boolean isEnableAddZero() {
            return enableAddZero;
        }

        void this_keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
                commit();
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                setValue(value);
            }
        }

        private void commit() {
            try {
                setValue(Integer.parseInt(getText()));
            } catch (Exception ex) {
                setValue(value);
                return;
            }
        }

        void this_focusGained(FocusEvent e) {
            this.selectAll();
        }
    }

    private class DigitalTextDocument extends PlainDocument {
        /***********************************************************************
         * Class： DigitalTextDocument Extends： PlainDocument
         **********************************************************************/
        private static final long serialVersionUID = 1L;
        private int maxLength;
        private int maxValue;
        private int minValue;

        // 不帯参数的构造函数
        public DigitalTextDocument() {
            super();
            maxLength = 2;
            minValue = 0;
            maxValue = 99;
        }

        // 构造函数，
        public DigitalTextDocument(int maxLength) {
            super();
            this.maxLength = maxLength;
            minValue = 0;
            maxValue = (int) Math.pow(10, maxLength) - 1;
        }

        // 构造函数，
        public DigitalTextDocument(int maxLength, int min, int max) {
            super();
            this.maxLength = maxLength;
            minValue = Math.max(min, 0);
            maxValue = Math.min(max, (int) Math.pow(10, maxLength) - 1);
        }

        // 设定最大长度
        public void setMaxLength(int length) {
            maxLength = length;
        }

        // 获取最大长度
        public int getMaxLength() {
            return maxLength;
        }

        // 设定最大值
        public void setMaxValue(int max) {
            maxValue = max;
        }

        // 获取最大值
        public int getMaxValue() {
            return maxLength;
        }

        // 设定最小值
        public void setMinValue(int min) {
            minValue = min;
        }

        // 获取最小值
        public int getMinValue() {
            return minValue;
        }

        // 重载父类的insertString函数
        public void insertString(int offset, String str, AttributeSet a)
                throws BadLocationException {
            int valueAfterInsert = 0;
            String strBeforeInsert = getText(0, getLength());
            String strAfterInsert = strBeforeInsert.substring(0, offset) + str
                    + strBeforeInsert.substring(offset);

            // 首先保证插入该字符串后，是整数；如果不是，则不进行插入操作
            try {
                valueAfterInsert = Integer.parseInt(strAfterInsert);
            } catch (NumberFormatException e) {

                return;
            }

            // 如果插入字符创str后，文档超长，则插入失败
            if (strAfterInsert.length() > maxLength)
                return;
                // 如果插入后数据超出范围，插入失败
            else if (valueAfterInsert > maxValue)
                return;
            else if ((strAfterInsert.length() == maxLength)
                    && (valueAfterInsert < minValue))
                return;
            else {
                super.insertString(offset, str, a);
                return;
            }
        }
    }
}

