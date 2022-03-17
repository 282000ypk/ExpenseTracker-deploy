package com.expense.dashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import com.expense.expense.Expense;
import com.expense.user.User;
import com.google.gson.Gson;

public class Data {
	private HashSet<String> labels;
	private ArrayList<Dataset> datasets;
	
	public Data(User user, String duration, String type) {
		this.datasets = new ArrayList<Dataset>();
		Dataset temp_dataset = new Dataset(user, duration, type);
		this.labels = temp_dataset.category_list;
		this.datasets.add(temp_dataset);
	}
	
	public HashSet<String> getLabels() {
		return labels;
	}

	public void setLabels(HashSet<String> labels) {
		this.labels = labels;
	}

	public ArrayList<Dataset> getDatasets() {
		return datasets;
	}

	public void setDatasets(ArrayList<Dataset> datasets) {
		this.datasets = datasets;
	}
	
	@Override
	public String toString() {
		return "Data [labels=" + labels + ", datasets=" + datasets + "]";
	}



	// sub class dataset
	public class Dataset
	{
		private String label;
		private ArrayList<Double> data;
		private ArrayList<String> backgroundColor;
		private ArrayList<String> borderColor;
		private int borderWidth;
		public HashSet<String> category_list;
		
		public Dataset(User user, String duration, String type) {
			this.label = "# total transactions";
			this.data = new ArrayList<Double>();
			this.backgroundColor = new ArrayList<String>();
			this.borderColor = new ArrayList<String>();
			ArrayList<Expense> expense_list = Expense.getExpenseByDuration(user, duration);
			HashMap<String, Double> category_total = new HashMap<String, Double>();
			this.category_list = new HashSet<String>();
			for(Expense expense: expense_list)
			{
				String temp_type = expense.getTransaction_type();
				String temp_category = expense.getCategory();
				Double temp_amount = expense.getAmount();
				Double temp_map_amount = (category_total.get(temp_category) == null)? 0.0 : category_total.get(temp_category);
				
				if(temp_type.equals(type))
				{
					category_total.put(temp_category, (temp_map_amount + temp_amount));
					this.category_list.add(temp_category);
				}
				//System.out.println("type=" + temp_type + " category=" + temp_category + " amount=" + temp_amount + " Map_amount=" + temp_map_amount + "\n" + category_total + "\n");
			}
			
			int red, green, blue;
			for(String category: this.category_list)
			{
				this.data.add((category_total.get(category) == null)? 1.0 : (Double)category_total.get(category));
				
				red = (int)new Random().nextInt(255 - 100) + 0;
				green = (int)new Random().nextInt(255 - 100) + 0;
				blue = (int)new Random().nextInt(255 - 100) + 0;
				
				this.backgroundColor.add("rgba(" + red + ", " + green + ", " + blue + ", 0.2)");
				this.borderColor.add("rgba(" + red + ", " + green + ", " + blue + ", 1)");
			}
			/*System.out.println(expense_list);
			System.out.println(category_total);
			System.out.println(category_list);*/
			
			this.borderWidth = 1;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public ArrayList<Double> getData() {
			return data;
		}

		public void setData(ArrayList<Double> data) {
			this.data = data;
		}

		public ArrayList<String> getBackgroundColor() {
			return backgroundColor;
		}

		public void setBackgroundColor(ArrayList<String> backgroundColor) {
			this.backgroundColor = backgroundColor;
		}

		public ArrayList<String> getBorderColor() {
			return borderColor;
		}

		public void setBorderColor(ArrayList<String> borderColor) {
			this.borderColor = borderColor;
		}

		public int getBorderWidth() {
			return borderWidth;
		}

		public void setBorderWidth(int borderWidth) {
			this.borderWidth = borderWidth;
		}
		

		@Override
		public String toString() {
			return "\nDataset [label=" + label + ", \ndata=" + data + ", \nbackgroundColor=" + backgroundColor
					+ ", \nborderColor=" + borderColor + ", \nborderWidth=" + borderWidth + "]";
		}
		
		
	}
	
}
