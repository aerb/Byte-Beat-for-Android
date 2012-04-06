package com.tasty.fish.interfaces;

public interface IDroidBeatView {
	void DisplayBuffer(byte[] samples);
	void updateT(int time);
	void postInvalidate();
	public void updateSeekerSpeedPostion(double value);
	void updateSeekerPostion(int id, double speed);
}
