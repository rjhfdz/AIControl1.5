package com.boray.dengKu.Entity;

public class BlackOutEntity {
	private String c1,c2,c3,c4;
	private String min1,min2,min3,min4;
	private String max1,max2,max3,max4;
	public BlackOutEntity(){
		c1 = "无";c2 = "无";c3 = "无";c4 = "无";
		min1 = "0";max1 = "0";
		min2 = "0";max2 = "0";
		min3 = "0";max3 = "0";
		min4 = "0";max4 = "0";
	}
	public void setC1(String c1,String min1,String max1){
		this.c1 = c1;
		this.min1 = min1;
		this.max1 = max1;
	}
	public void setC2(String c2,String min2,String max2){
		this.c2 = c2;
		this.min2 = min2;
		this.max2 = max2;
	}
	public void setC3(String c3,String min3,String max3){
		this.c3 = c3;
		this.min3 = min3;
		this.max3 = max3;
	}
	
	public String getC4() {
		return c4;
	}
	public void setC4(String c4) {
		this.c4 = c4;
	}
	public String getMin1() {
		return min1;
	}
	public void setMin1(String min1) {
		this.min1 = min1;
	}
	public String getMin2() {
		return min2;
	}
	public void setMin2(String min2) {
		this.min2 = min2;
	}
	public String getMin3() {
		return min3;
	}
	public void setMin3(String min3) {
		this.min3 = min3;
	}
	public String getMin4() {
		return min4;
	}
	public void setMin4(String min4) {
		this.min4 = min4;
	}
	public String getMax1() {
		return max1;
	}
	public void setMax1(String max1) {
		this.max1 = max1;
	}
	public String getMax2() {
		return max2;
	}
	public void setMax2(String max2) {
		this.max2 = max2;
	}
	public String getMax3() {
		return max3;
	}
	public void setMax3(String max3) {
		this.max3 = max3;
	}
	public String getMax4() {
		return max4;
	}
	public void setMax4(String max4) {
		this.max4 = max4;
	}
	public void setC1(String c1) {
		this.c1 = c1;
	}
	public void setC2(String c2) {
		this.c2 = c2;
	}
	public void setC3(String c3) {
		this.c3 = c3;
	}
	public String[] getC(int i){
		String[] temp = null;
		switch (i) {
		case 0:
			temp =  new String[]{c1,min1,max1};
			break;
		case 1:
			temp =  new String[]{c2,min2,max2};
			break;
		case 2:
			temp =  new String[]{c3,min3,max3};
			break;
		case 3:
			temp =  new String[]{c4,min4,max4};
			break;
		default:
			break;
		}
		return temp;
	}
	public String getC1() {
		return c1;
	}
	public String getC2() {
		return c2;
	}
	public String getC3() {
		return c3;
	}
	public String[] getC1_1(){
		return new String[]{c1,min1,max1};
	}
	public String[] getC2_2(){
		return new String[]{c2,min2,max2};
	}
	public String[] getC3_3(){
		return new String[]{c3,min3,max3};
	}
}
