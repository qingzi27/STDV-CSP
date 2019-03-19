package model;

public class Pattern {
	// 正类阈值
	private double posSup = 0.4;
	
	// 负类阈值
	private double negSup = 0.2;
	
	// 单个模式紧密度阈值
	private long tightness = 3600L;
	
	// 模式分布紧密度阈值
	private long dTightness = 14400L;
	
	// 正负类模式中间的间隔差阈值
	private long timeValue = 240L; 

	public double getPosSup() {
		return posSup;
	}

	public void setPosSup(double posSup) {
		this.posSup = posSup;
	}

	public double getNegSup() {
		return negSup;
	}

	public void setNegSup(double negSup) {
		this.negSup = negSup;
	}

	public long getTightness() {
		return tightness;
	}

	public void setTightness(long tightness) {
		this.tightness = tightness;
	}

	public long getdTightness() {
		return dTightness;
	}

	public void setdTightness(long dTightness) {
		this.dTightness = dTightness;
	}

	public long getTimeValue() {
		return timeValue;
	}

	public void setTimeValue(long timeValue) {
		this.timeValue = timeValue;
	}

	public Pattern() {
	}

}
