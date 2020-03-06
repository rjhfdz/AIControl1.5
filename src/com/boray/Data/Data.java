package com.boray.Data;

import com.boray.entity.FileOrFolder;
import com.boray.entity.ProjectFile;

import java.io.File;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.*;

import javax.comm.SerialPort;

public class Data {
	public static List DengKuList = new ArrayList(20);//�ƿ����ͨ��ֵ
	public static List DengKuChannelCountList = new ArrayList(20);//�ƿ�ͨ����
	public static List DengKuVersionList = new ArrayList(20);//�ƿ�汾��
	public static List dengKuBlackOutAndSpeedList = new ArrayList(20);//�ƿ��Ӧ��Ϩ�ƺ��ٶ�ͨ��
	public static List GroupOfLightList = new ArrayList(30);//�ƾ����
	
	public static Map YaoMaiMap = new HashMap<>();//ҡ������
	public static Map DaoHeCaiMap = new HashMap<>();//���ص��Ȳ�����
	public static Map ShengKonModelSet = new HashMap<>();//����Ч����ģʽ���ò���
	public static Map ShengKonModelDmxMap = new HashMap<>();//����Ч����ģʽ����DMX����
	public static Map ShengKonHuanJingModelMap = new HashMap<>();//���ػ�����ģʽ����
	public static Object[][] ShengKonShiXuSetObjects = new Object[16][20];//����ʱ����������
	/*
	 * 16��ʾ16��ģʽ��30��ʾ30�飬20��ʾ20��ʱ���
	 */
	public static Object[][][] ShengKonEditObjects = new Object[16][30][20];//����Ч����ģʽ�༭Ч������
	/*
	 * 24��ʾ24��ģʽ��30��ʾ30�飬20��ʾ20��ʱ���
	 */
	public static Object[][][] XiaoGuoDengObjects = new Object[24][30][20];//Ч���Ʊ�̸���ʱ����Ӧ������
	
	/*
	 * �ز�����
	 * 20��ʾ20���ƿ⣬30��ʾ30���ز�
	 */
	public static Object[][] SuCaiObjects = new Object[20][30];
	//�½��زĵ�˳��
	public static List<String> AddSuCaiOrder = new ArrayList<>();
	//4�����ģʽ����
	public static Map wuJiMap = new HashMap<>();
	//����
	public static SerialPort serialPort = null;
	//������
	public static InputStream is = null;
	//�����߳�
	public static Thread thread = null;
	//������
	public static int baud;
	//com��
	public static String comKou;

	//UDP����
	public static DatagramSocket socket;

	public static InetSocketAddress address;

	//�˿�
	public static int port = 8089;

	public static boolean deviceShow = false;
	
	//��¼ѡ�񳡾�ģʽ
	public static int changJingModel = 0;
	
	//������ģʽ�л�
	public static boolean changJin_change = false;
	public static String current_box = "";
	
	//���ص�ģʽ�л�
	public static boolean shengKon_change = false;
	public static String shengKon_current_box = "";
	
	//��¼������̻򳡾�����
	public static boolean isTest = false;
	
	//��¼ͨ����
	public static List dengKuName = new ArrayList();
	//public static Map dengkuDataMap = new HashMap();
	
	//���Ʊ��������
	public static Object[] CopyObj = null;
	
	//��¼�ƿ����ͨ������
	//public static List dengKuMapList = new ArrayList();
	
	//��¼�ƾ�sliderֵ
	/*public static List[] sliderList = new ArrayList[32];
	static{
		for (int i = 0; i < 32; i++) {
			sliderList[i] = new ArrayList();
		}
	}*/
	//��¼�����ƾ߹�ѡֵ
	public static boolean[] checkList = new boolean[512];
	
	//��¼���صƾ߹�ѡֵ
	public static boolean[] checkList_shengKon = new boolean[512];
	static{
		for (int i = 0; i < checkList.length; i++) {
			checkList[i] = false;
			checkList_shengKon[i] = false;
		}
	}
	
	public static String[] initValue(){
		String[] temp = new String[515];
		temp[0] = "1";
		for (int i = 1; i < temp.length; i++) {
			temp[i] = "0";
		}
		return temp;
	}
	
	public static String[] initValue2(int index){
		String[] temp = new String[515];
		temp[0] = ""+index;
		for (int i = 1; i < temp.length; i++) {
			temp[i] = "0";
		}
		return temp;
	}
	
	public static byte[] DataInit(int p){
		byte[] temp = new byte[2048];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = 0;
		}
		temp[0] = (byte)0xAA;
		temp[1] = (byte)0xBB;
		temp[2] = (byte)0xCC;
		temp[3] = (byte)0xDD;
		temp[4] = (byte)0x00;
		temp[5] = (byte)0x01;
		temp[70] = (byte)p;
		return temp;
	}
	
	//��¼�ƿ⵼��·��
	public static String importPath = "";
	
	//��¼�ƿ⵼��·��
	public static String exportPath = "";
	
	//��¼�������Ϊ·��
	//public static String otherSavePath = "";
	
	//��¼���ɿ������ļ�·��
	public static String saveCtrlFilePath = "";
	
	//��¼�ϲ�����·��
	//public static String dataMergePath = "";
	
	public static String projectFilePath = "";

	//�ƶ���ӹ���
	public static String yunProjectFilePath = "";
	
	//�ƾ߱��
	public static boolean dengJu_change = false;
	
	//�ƿ���
	public static boolean dengKu_change = false;
	
	//��¼���ɿ������ļ�
	public static File file = null;
	//����Ŀ������ļ�
	public static File tempFile = null;
	//Ԥ�����ĵı���
	public static List packetNumList = new ArrayList();
	public static List valueList = new ArrayList();
	public static Map reviewMap = new HashMap();
	
	//�زĹ���ı���
	public static Map suCaiMap = new HashMap();
	public static Map suCaiNameMap = new HashMap();
	//�ƶ��ز�
	public static Map yunSuCaiMap = new HashMap();
	//�����ز�
	public static Map shengKonSuCaiMap = new HashMap();
	public static Map shengKonSuCaiNameMap = new HashMap();
	//�����ز����� 30��ʾ30�����飬30��ʾ30���ز�
	public static Object[][] ShengKonSuCai = new Object[30][30];

	public static String ipPort;
	public static String ip;
	public static String downloadIp;

	public static Map<String,String> userLogin = new HashMap<>();//��¼�û���¼����Ϣ
	public static Boolean RememberPassword = false;

	public static ProjectFile tempProjectFile;//��ʱ����

	public static FileOrFolder tempWebFolder;//��ʱ�ļ���
	public static ProjectFile tempWebFile;//��ʱ�ļ�
	public static Timer tempFileAutoSaveTimer;//�Զ����涨ʱ��
    public static File tempEditWebFile;
    public static File tempLocalFile;//������ʱ�ļ� ���ڸ��� ճ��

    public static Object[] dataWrite;//д������
	public static int sendDataCount = 0;//�ط����ݴ���
	public static Timer againSendDataTimer;//�ط����ݶ�ʱ��
	public static int sendDataSum;//��¼����
}
