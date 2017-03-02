package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class UserTopicDiversity {
	
	private String[] ids;
	private double[][] vectors;
	private double[][] topicDiversityMaxtrix;
	
	public UserTopicDiversity(){
		
	}
	
	public void vectorLoad(String dir){
		 try {
			FileReader reader = new FileReader(dir);
			BufferedReader br = new BufferedReader(reader);
			String str = null;
			ArrayList<String> idList=new ArrayList<String>();
			ArrayList<double[]> vectorsList=new ArrayList<double[]>();
			while((str = br.readLine()) != null) {
				String[] values = str.split(" ");
				double[] vector=new double[values.length-1];
				for(int index=0;index<values.length;index++){
					if(index==0){
						idList.add(values[index]);
						
					}else{
						vector[index-1]=Double.parseDouble(values[index]);
					}
				}
				vectorsList.add(vector);
			}
			ids=new String[idList.size()];
			vectors=new double[vectorsList.size()][];
			for(int index=0;index<idList.size();index++){
				ids[index]=idList.get(index);
				System.out.print(" "+ids[index]);
			}
			for(int index=0;index<vectorsList.size();index++){
				vectors[index]=vectorsList.get(index);
			}

		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public double vectorLength(double[] vector){
		double vectorLength=0.0;
		for(double value:vector){
			vectorLength +=Math.pow(value, 2);
		}
		vectorLength=Math.sqrt(vectorLength);
		return vectorLength;
	}
	
	public double[] normalization(double[] vector){
		double vectorLength=vectorLength(vector);
		double[] normalization=new double[vector.length];
		for(int index=0;index<vector.length;index++){
			normalization[index]=vector[index]/vectorLength;
		}
		return normalization;
	}
	
	public double[] vectorsAverage(double[] v1,double[] v2){
		double[] average=new double[v1.length];
		for(int index=0;index<v1.length;index++){
			average[index]=(v1[index]+v2[index])/2;
		}
		return average;
	}
	
	public double klDivergence(double[] v1,double[] v2){
		double klDivergence=0.0;
		for(int index=0;index<v1.length;index++){
			klDivergence+=v1[index]*Math.log(v1[index]/v2[index]);
		}
		return klDivergence;
	}
	
	public double jensenShannonDivergence(double[] v1,double[] v2){
		v1=normalization(v1);
		v2=normalization(v2);
		double[] average=vectorsAverage(v1,v2);
		double jsDivergence=0.0;
		jsDivergence=(klDivergence(v1,average)+klDivergence(v2,average))/2;
		return jsDivergence;
	}
	
	public double[][] getUserTopicDiversity(double[][] vectors){
		int num=vectors.length;
		topicDiversityMaxtrix=new double[num][];
		for(int index1=0;index1<num;index1++){
			double[] values=new double[num];
			for(int index2=0;index2<num;index2++){
				values[index2]=Math.sqrt(2*jensenShannonDivergence(vectors[index1],vectors[index2]));
			}
			topicDiversityMaxtrix[index1]=values;
		}
		return topicDiversityMaxtrix;
	}
	
	public String[] getIdList(){
		return ids;
	}
	
	public double[][] getVectors(){
		return vectors;
	}
}
