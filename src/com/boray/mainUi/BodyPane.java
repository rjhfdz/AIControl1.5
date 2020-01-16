package com.boray.mainUi;

import java.awt.CardLayout;

import javax.swing.*;

import com.boray.beiFen.UI.BeiFenUI;
import com.boray.changJing.UI.ChangJinUI;
import com.boray.dengKu.UI.DengKuUI;
import com.boray.kongTiao.UI.KongTiaoUI;
import com.boray.main.UI.BorayMainUI;
import com.boray.quanJu.UI.QuanJuUI;
import com.boray.shengKon.UI.ShengKonUI;
import com.boray.shengKonSuCai.UI.ShengKonSuCaiUI;
import com.boray.suCai.UI.SuCaiUI;
import com.boray.xiaoGuoDeng.UI.XiaoGuoDengUI;
import com.boray.zhongKon.UI.ZhongKonUI;

public class BodyPane {
    public void show(JPanel pane) {
        CardLayout cardLayout = new CardLayout();
        MainUi.map.put("titileCard", cardLayout);
        MainUi.map.put("titilePane", pane);
        pane.setLayout(cardLayout);
        JPanel changJinPane = new JPanel();
        JPanel dengKuPane = new JPanel();
        JPanel xiaoGuoPane = new JPanel();
        JPanel shengKonPane = new JPanel();
        JPanel konTiaoPane = new JPanel();
        JPanel zhongKonPane = new JPanel();
        JPanel quanJuPane = new JPanel();
        JPanel beiFenPane = new JPanel();
        JPanel suCaiPane = new JPanel();

        JPanel mainPane = new JPanel();
        JPanel shengKonSuCai = new JPanel();


        new ChangJinUI().show(changJinPane);
        new DengKuUI().show(dengKuPane);
        new XiaoGuoDengUI().show(xiaoGuoPane);
        new ShengKonUI().show(shengKonPane);
        new KongTiaoUI().show(konTiaoPane);
        new ZhongKonUI().show(zhongKonPane);
        new QuanJuUI().show(quanJuPane);
        new BeiFenUI().show(beiFenPane);
        new SuCaiUI().show(suCaiPane);
        new ShengKonSuCaiUI().show(shengKonSuCai);
        new BorayMainUI().show(mainPane);

        pane.add("1", changJinPane);
        pane.add("2", dengKuPane);
        pane.add("3", xiaoGuoPane);
        pane.add("4", shengKonPane);
        pane.add("5", konTiaoPane);
        pane.add("6", zhongKonPane);
        pane.add("7", quanJuPane);
        pane.add("8", beiFenPane);
        pane.add("9", suCaiPane);
        pane.add("10", mainPane);

        pane.add("11", shengKonSuCai);

    }
}
