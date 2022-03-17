package com.expense.dashboard;

import java.util.ArrayList;

import com.expense.user.User;
import com.google.gson.Gson;

public class ChartData {
	private String type = "doughnut";
	private Data data;
	

	public ChartData(User user, String duration, String type) {
		this.type = "doughnut";
		this.data = new Data(user, duration, type);
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ChartData [type=" + type + ",\ndata=" + data + "]";
	}
	
	
}
