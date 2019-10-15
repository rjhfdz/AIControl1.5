package com.boray.suCai.reviewBlock;

import com.boray.Data.Data;

import java.io.OutputStream;

public class TimeBlockReviewActionListener {
	private int sc,group,block,index;
	public TimeBlockReviewActionListener(int sc,int group,int block,int index){
		this.sc = sc;
		this.block = block;
		this.group = group;
		this.index = index;
	}
	public void actionPerformed() {
		if (Data.serialPort != null) {
			try {

				//灯具数据
				byte[] b = TimeBlockReviewData.getInfOfLight();
				OutputStream os = Data.serialPort.getOutputStream();
				os.write(b);
				os.flush();

				Thread.sleep(40);

				//分组
				b = TimeBlockReviewData.getGroupOfLights();
				os.write(b);
				os.flush();
				
				Thread.sleep(40);
				

				//熄灯+加速度
				b = TimeBlockReviewData.getOffLights()[0];
				os.write(b);
				os.flush();
				
				Thread.sleep(40);
				b = TimeBlockReviewData.getOffLights()[1];
				os.write(b);
				os.flush();

				//灯库
				Thread.sleep(40);
				b = TimeBlockReviewData.getlibOfLights()[0];
				os.write(b);
				os.flush();
				Thread.sleep(40);
				b = TimeBlockReviewData.getlibOfLights()[1];
				os.write(b);
				os.flush();
				
				//效果灯时间块数据
				Thread.sleep(40);
				Object[] objects = TimeBlockReviewData.getEffectLight(sc-1,group, block,index);
				for (int i = 0; i < objects.length; i++) {
					b = (byte[])objects[i];
					os.write(b);
					os.flush();
					Thread.sleep(40);
				}
				
				//启动预览
				//Thread.sleep(200);
				b = TimeBlockReviewData.getStarReview(sc, group, block);
				os.write(b);
				os.flush();
				
				
				//os.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
