package com;

public class StockDetails {
	private float high,low,open,close,ltp;
	StockDetails(){
		high = 0;
		low = 0;
		open = 0;
		close = 0;
		ltp = 0;
	}
	StockDetails(float h, float l, float o, float c, float lt){
		high = h;
		low = l;
		open = o;
		close = c;
		ltp = lt;
	}
	public float getHigh() {
		return high;
	}
	public void setHigh(float high) {
		this.high = high;
	}
	public float getLow() {
		return low;
	}
	public void setLow(float low) {
		this.low = low;
	}
	public float getOpen() {
		return open;
	}
	public void setOpen(float open) {
		this.open = open;
	}
	public float getClose() {
		return close;
	}
	public void setClose(float close) {
		this.close = close;
	}
	public float getLtp() {
		return ltp;
	}
	public void setLtp(float ltp) {
		this.ltp = ltp;
	}
	
}
