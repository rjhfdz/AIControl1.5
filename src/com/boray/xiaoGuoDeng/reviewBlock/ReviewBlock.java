package com.boray.xiaoGuoDeng.reviewBlock;

import com.boray.Data.Data;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ReviewBlock {
    public static void review(int model) {
        if (Data.serialPort != null) {
            try {

                //灯具数据
                byte[] b = TimeBlockReviewData.getInfOfLight();
                OutputStream os = Data.serialPort.getOutputStream();
                os.write(b);
                os.flush();

                Thread.sleep(200);

                //分组
                b = TimeBlockReviewData.getGroupOfLights();
                os.write(b);
                os.flush();

                Thread.sleep(200);


                //熄灯+加速度
                b = TimeBlockReviewData.getOffLights()[0];
                os.write(b);
                os.flush();

                Thread.sleep(200);
                b = TimeBlockReviewData.getOffLights()[1];
                os.write(b);
                os.flush();

                //灯库
                Thread.sleep(200);
                b = TimeBlockReviewData.getlibOfLights()[0];
                os.write(b);
                os.flush();
                Thread.sleep(200);
                b = TimeBlockReviewData.getlibOfLights()[1];
                os.write(b);
                os.flush();

                //素材数据区
                Thread.sleep(200);
                Object[] objects = TimeBlockReviewData.getEffectLight4(4096, model, 14);
                for (int i = 0; i < objects.length; i++) {
                    b = (byte[]) objects[i];
                    os.write(b);
                    os.flush();
                    Thread.sleep(200);
                }

                //场景
                Thread.sleep(230);
                objects = TimeBlockReviewData.getEffectLight4(4096, model, 15);
                for (int i = 0; i < objects.length; i++) {
                    b = (byte[]) objects[i];
                    os.write(b);
                    os.flush();
                    Thread.sleep(230);
                }

                //启动预览
                Thread.sleep(200);
                b = TimeBlockReviewData.getStarReview2(model);
                os.write(b);
                os.flush();


                //os.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void saveFile(int model) {
        JFileChooser fileChooser = new JFileChooser();
        if (!Data.saveCtrlFilePath.equals("")) {
            fileChooser.setCurrentDirectory(new File(Data.saveCtrlFilePath));
        }
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setSelectedFile(new File("TEST.DAT"));
        int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Data.file = file;
            Data.saveCtrlFilePath = file.getParent();
            try {
                OutputStream os = new FileOutputStream(file);

                //灯具数据
                byte[] b = TimeBlockReviewData.getInfOfLight();
                os.write(b);

                //分组
                b = TimeBlockReviewData.getGroupOfLights();
                os.write(b);

                //熄灯+加速度
                b = TimeBlockReviewData.getOffLights()[0];
                os.write(b);

                b = TimeBlockReviewData.getOffLights()[1];
                os.write(b);

                //灯库
                b = TimeBlockReviewData.getlibOfLights()[0];
                os.write(b);
                b = TimeBlockReviewData.getlibOfLights()[1];
                os.write(b);

                //素材数据区
                Object[] objects = TimeBlockReviewData.getEffectLight4(4096, model, 14);
                for (int i = 0; i < objects.length; i++) {
                    b = (byte[]) objects[i];
                    os.write(b);
                }

                //场景
                objects = TimeBlockReviewData.getEffectLight4(4096, model, 15);
                for (int i = 0; i < objects.length; i++) {
                    b = (byte[]) objects[i];
                    os.write(b);
                }

                //启动预览
                b = TimeBlockReviewData.getStarReview2(model);
                os.write(b);

                os.flush();
                os.close();

                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "场景预览测试文件成功!", "提示", JOptionPane.PLAIN_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
