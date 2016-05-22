package com.server.game.data;

import java.util.ArrayList;

public class ConfigDeskChip {
	private int level = 0;
	private String name = "";
	private ArrayList<Integer> chips = new ArrayList<>();
	private int enterMin = 0;
	private int enterMax = 0;
	private int defaultBet = 0;
	public ConfigDeskChip() {
		// TODO Auto-generated constructor stub
	}
	public int getMaxChip(){
		int maxChip = 0;
		for (int chip : chips) {
			maxChip = chip > maxChip ? chip : maxChip;
		}
		return maxChip;
	}
	public int  getMinChip() {
		int minChip = Integer.MAX_VALUE;
		for (int chip : chips) {
			minChip = chip < minChip ? chip : minChip;
		}
		return minChip;
	}
	
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Integer> getChips() {
		return chips;
	}
	public void setChips(ArrayList<Integer> chips) {
		this.chips = chips;
	}
	public int getEnterMin() {
		return enterMin;
	}
	public void setEnterMin(int enterMin) {
		this.enterMin = enterMin;
	}
	public int getEnterMax() {
		return enterMax;
	}
	public void setEnterMax(int enterMax) {
		this.enterMax = enterMax;
	}
	public int getDefaultBet() {
		return defaultBet;
	}
	public void setDefaultBet(int defaultBet) {
		this.defaultBet = defaultBet;
	}
}
