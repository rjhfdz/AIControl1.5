package bezier;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;

import com.boray.Utils.IconJDialog;
import com.boray.mainUi.MainUi;

public class BezierDialog
{
	public void show(int type,int sled,JComboBox[] box88){
		int select = sled;
		JFrame frame = (JFrame)MainUi.map.get("frame");
		IconJDialog dialog = new IconJDialog(frame,true);
		int width = 264;
		int height = 360;
		dialog.setTitle("自定义图形");
		dialog.setSize(width,height);
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
		flowLayout.setHgap(0);
		dialog.setLayout(flowLayout);
		
		BezierPanel bezier = new BezierPanel();
		bezier.setBorder(new LineBorder(Color.black));
		bezier.setPreferredSize(new Dimension(254, 257));
		
		if (type == 0) {
			bezier.setBg(0);
			if (select == 0) {
				bezier.setShape(1);
			} else if (select == 1) {
				bezier.setShape(0);
			} else if (select > 1 && select < 48) {
				bezier.setShape(select);
			} else if (select >= 48) {
				String[] s = (String[])Data.map.get(""+select);
				if (s != null) {
					bezier.setShape(s);
				} else {
					bezier.setShape(0);
				}
			}
		} else {
			bezier.setBg(1);
			if(select <= 10) {
				JOptionPane.showMessageDialog(frame, "此类型为系统保留渐变类型，不允许查看及修改!", "提示", JOptionPane.WARNING_MESSAGE);
				return;
			} else if (select > 10 && select < 52) {
				bezier.setShape_color(select-11);
			} else if (select > 51) {
				String[] s = (String[])Data.map.get("color"+select);
				if (s != null) {
					bezier.setShape(s);
				} else {
					bezier.setShape(0);
				}
			}
		}
		
		
		
		dialog.add(bezier);
		JPanel panel = new JPanel();
		FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
		flowLayout2.setVgap(0);
		flowLayout2.setHgap(0);
		panel.setLayout(flowLayout2);
		//panel.setBorder(new LineBorder(Color.black));
		panel.setPreferredSize(new Dimension(254,60));
		JPanel p1 = new JPanel();
		//p1.setBorder(new LineBorder(Color.black));
		p1.setPreferredSize(new Dimension(182,60));
		FlowLayout flowLayout3 = new FlowLayout(FlowLayout.LEFT);
		flowLayout3.setVgap(-2);
		flowLayout3.setHgap(0);
		p1.setLayout(flowLayout3);
		
		JButton button = new JButton("放大");
		JButton button2 = new JButton("缩小");
		JButton button3 = new JButton("重置");
		JButton button4 = new JButton("旋转");
		
		SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 359, 1); 
		JSpinner spinner = new JSpinner(model);
		spinner.setPreferredSize(new Dimension(68,30));
		
		bezierListener listener = new bezierListener(bezier,sled,box88,spinner);
		
		button.addActionListener(listener);
		button2.addActionListener(listener);
		button3.addActionListener(listener);
		button4.addActionListener(listener);
		
		button.setFocusable(false);
		button2.setFocusable(false);
		button3.setFocusable(false);
		button4.setFocusable(false);
		button.setPreferredSize(new Dimension(60,32));
		button2.setPreferredSize(new Dimension(60,32));
		button3.setPreferredSize(new Dimension(60,32));
		button4.setPreferredSize(new Dimension(60,32));
		
		p1.add(button);
		p1.add(button2);
		p1.add(button3);
		p1.add(button4);
		p1.add(spinner);
		panel.add(p1);
		
		JPanel p2 = new JPanel();
		//p2.setBorder(new LineBorder(Color.black));
		p2.setPreferredSize(new Dimension(70,60));
		p2.setLayout(flowLayout3);
		
		JButton button5 = new JButton("保存");
		button5.addActionListener(listener);
		button5.setFocusable(false);
		button5.setPreferredSize(new Dimension(66,60));
		p2.add(button5);
		panel.add(p2);
		dialog.add(panel);
		dialog.setLocation(frame.getLocation().x+frame.getSize().width/2-width/2,frame.getLocation().y+frame.getSize().height/2-height/2);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dialog.setVisible(true);
	}
}
