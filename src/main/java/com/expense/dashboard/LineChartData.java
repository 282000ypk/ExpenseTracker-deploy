package com.expense.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import com.expense.expense.Expense;
import com.expense.user.User;

public class LineChartData 
{
	private String type;
	private Data data;

	public LineChartData(User user, String duration, String type) {
		this.type = "line";
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

	class Data
	{
		private HashSet<String> labels;
	 	private ArrayList<Dataset> datasets;

	 	public Data(User user, String duration, String type) {
	 		this.datasets = new ArrayList<Dataset>();
	 		Dataset temp_dataset = new Dataset(user, duration, type);
			datasets.add(temp_dataset);
	 		this.labels = temp_dataset.labels;
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



		class Dataset
	 	{
	 		private String label;
	 		private ArrayList<Double> data;
	 		private boolean fill;
	 		private String borderColor;
	 		private Double tension;
	 		HashSet<String> labels;
	 		
	 		
	 		
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

			public boolean isFill() {
				return fill;
			}

			public void setFill(boolean fill) {
				this.fill = fill;
			}

			public String getBorderColor() {
				return borderColor;
			}

			public void setBorderColor(String borderColor) {
				this.borderColor = borderColor;
			}

			public Double getTension() {
				return tension;
			}

			public void setTension(Double tension) {
				this.tension = tension;
			}

			public HashSet<String> getLabels() {
				return labels;
			}

			public void setLabels(HashSet<String> labels) {
				this.labels = labels;
			}

			Dataset(User user, String duration, String type)
	 		{
	 			this.label = "Transactions for Duration";
	 			this.fill = false;
	 			this.borderColor = (type.equals("credit"))? "rgb(0,255,0)": "rgb(255,0,0)";
	 			this.data = new ArrayList<Double>();
	 			this.tension = 1.0;
	 			//computation
	 			
	 			//temp variables
	 			ArrayList<Expense> expense_list = Expense.getExpenseByDuration(user, duration);
	 			HashSet<String> temp_labels = new HashSet<String>();
	 			HashMap<String, Double> temp_data = new HashMap<String, Double>();
	 			
	 			for(Expense expense: expense_list)
				{
					String temp_type = expense.getTransaction_type();
					Double temp_amount = expense.getAmount();
					String temp_date = expense.getDate();
					String temp_time = expense.getTime();
					Double temp_old_amount = 0.0;
					if(temp_type.equals(type))
					{
						if(duration.equals("day"))
						{
							temp_old_amount = (temp_data.get(temp_time) == null)? 0.0 : temp_data.get(temp_time);
							temp_data.put(temp_time, temp_old_amount + temp_amount);
							temp_labels.add(temp_time);
						}
						else
						{
							temp_old_amount = (temp_data.get(temp_date) == null)? 0.0 : temp_data.get(temp_date);
							temp_data.put(temp_date, temp_old_amount + temp_amount);
							temp_labels.add(temp_date);
						}
						
					}
				}
	 			
	 			for(String data: temp_labels)
				{
					this.data.add((temp_data.get(data) == null)? 0.0 : (Double)temp_data.get(data));						
				}
				
				this.labels = temp_labels;
	 		}
	 	}
	}
}
