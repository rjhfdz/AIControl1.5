package com.boray.mainUi;

import java.awt.*;
import java.awt.event.*;
import java.beans.XMLDecoder;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Listener.MainLoginUIListener;
import com.boray.Utils.Socket;
import com.boray.beiFen.Listener.ProjectCreateFileOfCloseFrame;

public class MainUi {
    public static Map map;
    private JFrame frame;

    public static void main(String[] args) {
        new MainUi().show();
    }

    public void show() {
        try {
            //JDialog.setDefaultLookAndFeelDecorated(true);
//			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.setLookAndFeel(ch.randelshofer.quaqua.QuaquaManager.getLookAndFeel());
            UIManager.put("FileChooser.cancelButtonText", "取消");
            UIManager.put("FileChooser.saveButtonText", "保存");
            UIManager.put("FileChooser.openButtonText", "打开");
            UIManager.put("FileChooser.newFolderButtonText", "新建文件夹");
            UIManager.put("OptionPane.yesButtonText", "是");
            UIManager.put("OptionPane.noButtonText", "否");
        } catch (Exception e) {
            e.printStackTrace();
        }
        map = new HashMap();
        frame = new JFrame("X-Series智能灯光控制器系统V1.0");
        map.put("frame", frame);

        int screenWidth = ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
        int frameWidth = 1250;
        int frameHeight = 760;


        frame.setSize(frameWidth, frameHeight);
        frame.setLocation(screenWidth / 2 - frameWidth / 2, screenHeight / 2 - frameHeight / 2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowFocusListener(new WindowFocusListener() {
            public void windowLostFocus(WindowEvent e) {
            }

            public void windowGainedFocus(WindowEvent e) {
                frame.validate();
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
//                File file = new File(new File("").getAbsolutePath() + "\\User.xml");
//                try {
//                    if (!file.exists()) {
//                        file.createNewFile();
//                    }
//                    if (Data.userLogin.keySet().size() > 0) {
//                        FileWriter fileWriter = new FileWriter(file);
//                        fileWriter.write("");
//                        fileWriter.flush();
//                        OutputStream is = new FileOutputStream(file);
//                        XMLEncoder xmlEncoder = new XMLEncoder(is);
//                        xmlEncoder.writeObject(Data.userLogin);
//                        xmlEncoder.flush();
//                        xmlEncoder.close();
//                    }
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
                if (Data.serialPort != null) {
                    try {
                        OutputStream os = Data.serialPort.getOutputStream();
                        os.write(ZhiLingJi.setBackBaut());
                        os.flush();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else if (Data.socket != null) {
                    Socket.UDPSendData(ZhiLingJi.setBackBaut());
                }
                Object[] options = {"否", "是"};
                int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "是否要保存工程？", "警告",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, options, options[1]);
                if (yes == 1) {
                    new ProjectCreateFileOfCloseFrame().save();
                }
                Runtime.getRuntime().halt(1);
            }

            public void windowOpened(WindowEvent e) {

            }
        });

        ImageIcon img = new ImageIcon(getClass().getResource("/icon/界面-1-1250X760副本.png"));
        JLabel label = new JLabel(img);
        frame.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
        label.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        Container cp = frame.getContentPane();
        cp.setLayout(new BorderLayout());
        ((JPanel) cp).setOpaque(false);
//        frame.setUndecorated(true);
        frame.setResizable(false);
//        loadUserXML();
        LoginUI();
//		init();
        frame.setIconImage(new ImageIcon(getClass().getResource("/icon/boray.png")).getImage());
        frame.setVisible(true);
    }

    /**
     * 读取用户以及密码
     */
    private void loadUserXML() {
        File file = new File(new File("").getAbsolutePath() + "\\User.xml");
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                InputStream is = new FileInputStream(file);
                XMLDecoder xmlDecoder = new XMLDecoder(is);
                Data.userLogin = (Map<String, String>) xmlDecoder.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //窗体打开，显示登陆界面
    private void LoginUI() {
        JPanel jPanel2 = new JPanel();
        jPanel2.setPreferredSize(new Dimension(1250, 588));
        JPanel jPanel = new JPanel();
        jPanel.setPreferredSize(new Dimension(1250, 300));
        frame.add(jPanel);
        JPanel pane = new JPanel();
        pane.setPreferredSize(new Dimension(350, 220));
        Font font = new Font("宋体", Font.PLAIN, 20);
        Font font1 = new Font("宋体", Font.PLAIN, 15);
        JLabel UserNameLabel = new JLabel("用户名：");
        JLabel PasswordLabel = new JLabel("密  码：");
        UserNameLabel.setFont(font);
        PasswordLabel.setFont(font);
        pane.add(UserNameLabel);
        JTextField username = new JTextField();
//        JComboBox username = new JComboBox();
//        username.addItem("");
//        for (String key : Data.userLogin.keySet()) {
//            username.addItem(Util.decode(key));
//        }
//        username.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                if (Data.userLogin.containsKey(Util.encode(e.getItem().toString()))) {
//                    JPasswordField password = (JPasswordField) MainUi.map.get("MainPassword");
//                    password.setText(Util.decode(Data.userLogin.get(Util.encode(e.getItem().toString()))));
//                }
//            }
//        });
        username.setEditable(true);
        username.setFont(font1);
        username.setPreferredSize(new Dimension(220, 40));
        MainUi.map.put("MainUsername", username);
        pane.add(username);
        JPasswordField password = new JPasswordField();
        username.setFont(font1);
        password.setPreferredSize(new Dimension(220, 40));
        MainUi.map.put("MainPassword", password);
        pane.add(PasswordLabel);
        pane.add(password);
//        pane.add(new JLabel("                                               "));
//        JCheckBox box = new JCheckBox("是否记住密码");
//        box.setSelected(true);
//        MainUi.map.put("MainRememberPassword", box);
//        pane.add(box);
        JButton clear = new JButton("清除");
        JButton login = new JButton("登录");
        JButton offLine = new JButton("离线");

        clear.setPreferredSize(new Dimension(100, 40));
        login.setPreferredSize(new Dimension(100, 40));
        offLine.setPreferredSize(new Dimension(100, 40));

        MainLoginUIListener loginUIListener = new MainLoginUIListener();
        clear.addActionListener(loginUIListener);
        login.addActionListener(loginUIListener);
        offLine.addActionListener(loginUIListener);

        pane.add(offLine);
        pane.add(clear);
        pane.add(login);
        jPanel2.add(jPanel);
        jPanel2.add(pane);
        frame.add(jPanel2, BorderLayout.CENTER);
    }


    private void init() {
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tabbedPane.setPreferredSize(new Dimension(1020, 100));
        tabbedPane.setFocusable(false);
        //tabbedPane.setUI((TabbedPaneUI)BasicTabbedPaneUI.createUI(tabbedPane));
        JPanel lightPane = new JPanel();
        lightPane.setPreferredSize(new Dimension(1030, 110));
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "-", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        lightPane.setBorder(tb);
        //lightPane.setBorder(new LineBorder(Color.gray));
        new LightBtnPane().show(lightPane);
        JPanel aboutPane = new JPanel();
        new AboutPane().show(aboutPane);
        tabbedPane.add("      灯光编程       ", lightPane);
        tabbedPane.add("       关于          ", aboutPane);

        JPanel bodyPane = new JPanel();
        //bodyPane.setBorder(new LineBorder(Color.gray));
        bodyPane.setPreferredSize(new Dimension(1030, 620));
        new BodyPane().show(bodyPane);
//		JPanel leftPane = new JPanel();
//		new LeftBtnPane().show(leftPane);
//		leftPane.setPreferredSize(new Dimension(80,720));
//		leftPane.setBorder(new LineBorder(Color.gray));
        JPanel main = new JPanel();
        main.setBorder(new LineBorder(Color.gray));
        main.setPreferredSize(new Dimension(1040, 720));
        main.add(lightPane);
        main.add(bodyPane);
//		JPanel panel = new JPanel();
//		panel.add(leftPane);
//		panel.add(main);
//		frame.add(panel);
//		frame.add(leftPane);
        frame.add(main);
//		frame.add(bodyPane);
    }
}
