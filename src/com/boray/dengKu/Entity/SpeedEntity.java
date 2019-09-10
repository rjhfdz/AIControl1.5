package com.boray.dengKu.Entity;

public class SpeedEntity {
	private String s1,s2,s3;
	private String min1,min2,min3;
	private String max1,max2,max3;
	private String direct1,direct2,direct3;
	public SpeedEntity(){
		s1 = "无";s2 = "无";s3 = "无";
		min1 = "0";min2 = "0";min3 = "0";
		max1 = "0";max2 = "0";max3 = "0";
		direct1 = "正向";direct2 = "正向";direct3 = "正向";
	}
	public void setS1(String s,String min,String max,String direct){
		this.s1 = s;
		this.min1 = min;
		this.max1 = max;
		this.direct1 = direct;
	}
	public void setS2(String s,String min,String max,String direct){
		this.s2 = s;
		this.min2 = min;
		this.max2 = max;
		this.direct2 = direct;
	}
	public void setS3(String s,String min,String max,String direct){
		this.s3 = s;
		this.min3 = min;
		this.max3 = max;
		this.direct3 = direct;
	}
	public String[] getS(int i){
		String[] temp = null;
		switch (i) {
		case 0:
			temp = new String[]{s1,min1,max1,direct1};
			break;
		case 1:
			temp = new String[]{s2,min2,max2,direct2};
			break;
		case 2:
			temp = new String[]{s3,min3,max3,direct3};
			break;
		default:
			break;
		}
		return temp;
	}
	public String getS1() {
		return s1;
	}
	public String getS2() {
		return s2;
	}
	public String getS3() {
		return s3;
	}
	public String[] getS1_1(){
		return new String[]{s1,min1,max1,direct1};
	}
	public String[] getS2_2(){
		return new String[]{s2,min2,max2,direct2};
	}
	public String[] getS3_3(){
		return new String[]{s3,min3,max3,direct3};
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
	public String getDirect1() {
		return direct1;
	}
	public void setDirect1(String direct1) {
		this.direct1 = direct1;
	}
	public String getDirect2() {
		return direct2;
	}
	public void setDirect2(String direct2) {
		this.direct2 = direct2;
	}
	public String getDirect3() {
		return direct3;
	}
	public void setDirect3(String direct3) {
		this.direct3 = direct3;
	}
	public void setS1(String s1) {
		this.s1 = s1;
	}
	public void setS2(String s2) {
		this.s2 = s2;
	}
	public void setS3(String s3) {
		this.s3 = s3;
	}
}
