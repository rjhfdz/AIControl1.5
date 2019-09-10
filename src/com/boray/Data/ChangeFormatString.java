package com.boray.Data;

public class ChangeFormatString {
	public static String change(String s){
		String temp = "";
		switch (s) {
		case "X轴":
			temp = "X轴<br><br>";
			break;
		case "X轴微调":
			temp = "X轴微调<br><br>";
			break;
		case "Y轴":
			temp = "Y轴<br><br>";
			break;
		case "Y轴微调":
			temp = "Y轴微调<br><br>";
			break;
		case "速度":
			temp = "速度<br><br>";
			break;
		case "调光":
			temp = "调光<br><br>";
			break;
		case "RGBR-1":
			temp = "RGBR-1<br><br>";
			break;
		case "RGBG-1":
			temp = "RGBG-1<br><br>";
			break;
		case "RGBB-1":
			temp = "RGBB-1<br><br>";
			break;
		case "自定义颜色":
			temp = "自定义颜色";
			break;
		case "图案":
			temp = "图案<br><br>";
			break;
		case "颜色盘":
			temp = "颜色盘<br><br>";
			break;
		case "光圈":
			temp = "光圈<br><br>";
			break;
		case "调焦":
			temp = "调焦<br><br>";
			break;
		case "缩放":
			temp = "缩放<br><br>";
			break;
		case "雾化":
			temp = "雾化<br><br>";
			break;
		case "棱镜":
			temp = "棱镜<br><br>";
			break;
		case "灯具复位":
			temp = "灯具复位";
			break;
		case "自定义通道":
			temp = "自定义通道";
			break;
		case "未定义通道":
			temp = "未定义通道";
			break;
		case "备用1":
			temp = "备用1<br><br>";
			break;
		case "备用2":
			temp = "备用2<br><br>";
			break;
		case "未知":
			temp = "未知<br><br>";
			break;
		case "RGBR-2":
			temp = "RGBR-2<br><br>";
			break;
		case "RGBG-2":
			temp = "RGBG-2<br><br>";
			break;
		case "RGBB-2":
			temp = "RGBB-2<br><br>";
			break;
		case "RGBR-3":
			temp = "RGBR-3<br><br>";
			break;
		case "RGBG-3":
			temp = "RGBG-3<br><br>";
			break;
		case "RGBB-3":
			temp = "RGBB-3<br><br>";
			break;
		default:
			temp = s + "<br><br>";
			break;
		}
		StringBuilder builder = new StringBuilder(temp);
		builder.insert(0, "<html>");
		builder.append("</html>");
		return builder.toString();
	}
}
