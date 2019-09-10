package com.boray.xiaoGuoDeng.reviewBlock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;

import com.boray.Data.Data;

public class TimeBlockStopReviewActionListener implements ActionListener{
	private int sc,group,block;
	public TimeBlockStopReviewActionListener(int sc,int group,int block){
		this.sc = sc;
		this.block = block;
		this.group = group;
	}
	public void actionPerformed(ActionEvent e) {
		if (Data.serialPort != null) {
			try {
				//Õ£÷π‘§¿¿
				byte[] b = TimeBlockReviewData.getStopReview(sc, group, block);
				OutputStream os = Data.serialPort.getOutputStream();
				os.write(b);
				os.flush();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
