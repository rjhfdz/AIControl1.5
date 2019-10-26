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

                //�ƾ�����
                byte[] b = TimeBlockReviewData.getInfOfLight();
                OutputStream os = Data.serialPort.getOutputStream();
                os.write(b);
                os.flush();

                Thread.sleep(200);

                //����
                b = TimeBlockReviewData.getGroupOfLights();
                os.write(b);
                os.flush();

                Thread.sleep(200);


                //Ϩ��+���ٶ�
                b = TimeBlockReviewData.getOffLights()[0];
                os.write(b);
                os.flush();

                Thread.sleep(200);
                b = TimeBlockReviewData.getOffLights()[1];
                os.write(b);
                os.flush();

                //�ƿ�
                Thread.sleep(200);
                b = TimeBlockReviewData.getlibOfLights()[0];
                os.write(b);
                os.flush();
                Thread.sleep(200);
                b = TimeBlockReviewData.getlibOfLights()[1];
                os.write(b);
                os.flush();

                //�ز�������
                Thread.sleep(200);
                Object[] objects = TimeBlockReviewData.getEffectLight4(4096, model, 14);
                for (int i = 0; i < objects.length; i++) {
                    b = (byte[]) objects[i];
                    os.write(b);
                    os.flush();
                    Thread.sleep(200);
                }

                //����
                Thread.sleep(230);
                objects = TimeBlockReviewData.getEffectLight4(4096, model, 15);
                for (int i = 0; i < objects.length; i++) {
                    b = (byte[]) objects[i];
                    os.write(b);
                    os.flush();
                    Thread.sleep(230);
                }

                //����Ԥ��
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

                //�ƾ�����
                byte[] b = TimeBlockReviewData.getInfOfLight();
                os.write(b);

                //����
                b = TimeBlockReviewData.getGroupOfLights();
                os.write(b);

                //Ϩ��+���ٶ�
                b = TimeBlockReviewData.getOffLights()[0];
                os.write(b);

                b = TimeBlockReviewData.getOffLights()[1];
                os.write(b);

                //�ƿ�
                b = TimeBlockReviewData.getlibOfLights()[0];
                os.write(b);
                b = TimeBlockReviewData.getlibOfLights()[1];
                os.write(b);

                //�ز�������
                Object[] objects = TimeBlockReviewData.getEffectLight4(4096, model, 14);
                for (int i = 0; i < objects.length; i++) {
                    b = (byte[]) objects[i];
                    os.write(b);
                }

                //����
                objects = TimeBlockReviewData.getEffectLight4(4096, model, 15);
                for (int i = 0; i < objects.length; i++) {
                    b = (byte[]) objects[i];
                    os.write(b);
                }

                //����Ԥ��
                b = TimeBlockReviewData.getStarReview2(model);
                os.write(b);

                os.flush();
                os.close();

                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "����Ԥ�������ļ��ɹ�!", "��ʾ", JOptionPane.PLAIN_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
