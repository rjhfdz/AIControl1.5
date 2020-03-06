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
	public static List DengKuList = new ArrayList(20);//灯库各个通道值
	public static List DengKuChannelCountList = new ArrayList(20);//灯库通道数
	public static List DengKuVersionList = new ArrayList(20);//灯库版本号
	public static List dengKuBlackOutAndSpeedList = new ArrayList(20);//灯库对应的熄灯和速度通道
	public static List GroupOfLightList = new ArrayList(30);//灯具组别
	
	public static Map YaoMaiMap = new HashMap<>();//摇麦数据
	public static Map DaoHeCaiMap = new HashMap<>();//声控倒喝彩数据
	public static Map ShengKonModelSet = new HashMap<>();//声控效果灯模式设置参数
	public static Map ShengKonModelDmxMap = new HashMap<>();//声控效果灯模式叠加DMX数据
	public static Map ShengKonHuanJingModelMap = new HashMap<>();//声控环境灯模式数据
	public static Object[][] ShengKonShiXuSetObjects = new Object[16][20];//声控时序设置数据
	/*
	 * 16表示16个模式，30表示30组，20表示20个时间块
	 */
	public static Object[][][] ShengKonEditObjects = new Object[16][30][20];//声控效果灯模式编辑效果数据
	/*
	 * 24表示24个模式，30表示30组，20表示20个时间块
	 */
	public static Object[][][] XiaoGuoDengObjects = new Object[24][30][20];//效果灯编程各个时间快对应的数据
	
	/*
	 * 素材数据
	 * 20表示20个灯库，30表示30个素材
	 */
	public static Object[][] SuCaiObjects = new Object[20][30];
	//新建素材的顺序
	public static List<String> AddSuCaiOrder = new ArrayList<>();
	//4个雾机模式数据
	public static Map wuJiMap = new HashMap<>();
	//串口
	public static SerialPort serialPort = null;
	//输入流
	public static InputStream is = null;
	//监听线程
	public static Thread thread = null;
	//波特率
	public static int baud;
	//com口
	public static String comKou;

	//UDP连接
	public static DatagramSocket socket;

	public static InetSocketAddress address;

	//端口
	public static int port = 8089;

	public static boolean deviceShow = false;
	
	//记录选择场景模式
	public static int changJingModel = 0;
	
	//场景的模式切换
	public static boolean changJin_change = false;
	public static String current_box = "";
	
	//声控的模式切换
	public static boolean shengKon_change = false;
	public static String shengKon_current_box = "";
	
	//记录场景编程或场景测试
	public static boolean isTest = false;
	
	//记录通道数
	public static List dengKuName = new ArrayList();
	//public static Map dengkuDataMap = new HashMap();
	
	//复制保存的数据
	public static Object[] CopyObj = null;
	
	//记录灯库各个通道定义
	//public static List dengKuMapList = new ArrayList();
	
	//记录灯具slider值
	/*public static List[] sliderList = new ArrayList[32];
	static{
		for (int i = 0; i < 32; i++) {
			sliderList[i] = new ArrayList();
		}
	}*/
	//记录场景灯具勾选值
	public static boolean[] checkList = new boolean[512];
	
	//记录声控灯具勾选值
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
	
	//记录灯库导入路径
	public static String importPath = "";
	
	//记录灯库导出路径
	public static String exportPath = "";
	
	//记录场景另存为路径
	//public static String otherSavePath = "";
	
	//记录生成控制器文件路径
	public static String saveCtrlFilePath = "";
	
	//记录合并数据路径
	//public static String dataMergePath = "";
	
	public static String projectFilePath = "";

	//云端添加工程
	public static String yunProjectFilePath = "";
	
	//灯具变更
	public static boolean dengJu_change = false;
	
	//灯库变更
	public static boolean dengKu_change = false;
	
	//记录生成控制器文件
	public static File file = null;
	//缓存的控制器文件
	public static File tempFile = null;
	//预览更改的变量
	public static List packetNumList = new ArrayList();
	public static List valueList = new ArrayList();
	public static Map reviewMap = new HashMap();
	
	//素材管理的变量
	public static Map suCaiMap = new HashMap();
	public static Map suCaiNameMap = new HashMap();
	//云端素材
	public static Map yunSuCaiMap = new HashMap();
	//声控素材
	public static Map shengKonSuCaiMap = new HashMap();
	public static Map shengKonSuCaiNameMap = new HashMap();
	//声控素材数据 30表示30个灯组，30表示30个素材
	public static Object[][] ShengKonSuCai = new Object[30][30];

	public static String ipPort;
	public static String ip;
	public static String downloadIp;

	public static Map<String,String> userLogin = new HashMap<>();//记录用户登录的信息
	public static Boolean RememberPassword = false;

	public static ProjectFile tempProjectFile;//临时工程

	public static FileOrFolder tempWebFolder;//临时文件夹
	public static ProjectFile tempWebFile;//临时文件
	public static Timer tempFileAutoSaveTimer;//自动保存定时器
    public static File tempEditWebFile;
    public static File tempLocalFile;//本地临时文件 用于复制 粘贴

    public static Object[] dataWrite;//写入数据
	public static int sendDataCount = 0;//重发数据次数
	public static Timer againSendDataTimer;//重发数据定时器
	public static int sendDataSum;//记录发包
}
