package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CommunityAcquisition {
	//id list of all users
	private String[] idList;
	//user topic probability vectors
	private double[][] vectors;
	//num of topic 
	private int k;
	//the largest diversity of cluster centers in last program of iteration
	private double mu;
	//the largest times of number
	private int iterationNum;
	//the result of cluster,the values present index in idList
	private int[][] clusterResult;
	//check out whether or not to continue iteration
	boolean criticism=true;
	//the centers of cluster,the values present topic probability of communities
	double[][] centerVectors;
	
	public CommunityAcquisition(){
		
	}
	
	public CommunityAcquisition(String[] idList,double[][] vectors){
		this.idList=idList;
		this.vectors=vectors;
	}
	//set configuration value of kmeans
	public void setKmeansConfiguration(int k, double mu,int iterationNum){
		this.k=k;
		this.mu=mu;
		this.iterationNum=iterationNum;
		clusterResult=new int[k][];
		centerVectors=new double[k][];
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
	
	public double getDist(double[] v1,double[] v2){
		return Math.sqrt(2*jensenShannonDivergence(v1,v2));
	}
	//Get the index of min value in array
	public int getMinIndex(double[] arr){  
	    int minIndex = 0;  
	    for(int i=0; i<arr.length; i++){  
	        if(arr[i] < arr[minIndex]){  
	            minIndex = i;  
	        }  
	    }  
	    return minIndex;  
	}  
	//init cluster centers
	private void iniCenter(){
		int pointNum=idList.length;
		int[] centers=new int[k];
		for(int index=0;index<centers.length;index++){
			centers[index]=-1;
		}
		centers[0]=(int)(Math.random()*pointNum);
		boolean flag=true;
		int index=1;
		while(flag){
			int num=(int)(Math.random()*pointNum);
			boolean equal=false;
			for(int temp:centers){
				if(temp==num){
					equal=true;
				}
			}
			if(!equal){
				centers[index-1]=num;
				index++;
			}
			if(index>10){
				flag=false;
			}
			
		}
		for(int index1=0;index1<k;index1++){
			centerVectors[index1]=vectors[centers[index1]];
		}
		
	}
	
	public double[][] getCenters(){
		return centerVectors;
	}
	
	public void Kmeans(){
		iniCenter();
		for(int index=0;index<iterationNum&&criticism;index++){
			double[][] newCenter=updateCluster();
			criticism(newCenter);
			centerVectors=newCenter;
			System.out.println("iteration times:"+(index+1));
		}
		System.out.println("Kmeans done!");
	}
	//Judge whether or not to stop iteration 
	public void criticism(double[][] newCenter){
		double[] dist=new double[k];
		for(int index=0;index<k;index++){
			dist[index]=getDist(newCenter[index],centerVectors[index]);
		}
		int num=0;
		for(int index=0;index<k;index++){
			if(dist[index]<=mu){
				num++;
			}
		}
		if(num>=k){
			criticism=false;
		}
	}
	/**
	 * update clusters and cluster centers
	 * @return
	 */
	public double[][] updateCluster(){
		int pointNum=idList.length;
		double[][] newCenter=new double[k][];
		double[][] distMatrix=new double[pointNum][k];
		Map<Integer, ArrayList<Integer>> map=new HashMap<Integer, ArrayList<Integer>>();
		for(int index=0;index<k;index++){
			map.put(index, new ArrayList<Integer>());
		}
		for(int index1=0;index1<pointNum;index1++){
			for(int index2=0;index2<k;index2++){
				distMatrix[index1][index2]=getDist(vectors[index1],centerVectors[index2]);
			}
		}
		for(int index=0;index<pointNum;index++){
			map.get(getMinIndex(distMatrix[index])).add(index);
		}
		for(int index=0;index<k;index++){
			ArrayList<Integer> pointList=map.get(index);
			int[] list=new int[pointList.size()];
			for(int index1=0;index1<pointList.size();index1++){
				list[index1]=pointList.get(index1);
			}
			clusterResult[index]=list;
		}
		for(int index=0;index<k;index++){
			newCenter[index]=getAverageVector(clusterResult[index]);
		}
		return newCenter;
	}
	
	public double[] getAverageVector(int[] idList){
		double[] vector=new double[vectors[0].length];
		for(int index1=0;index1<vectors[0].length;index1++){
			double v=0.0;
			for(int index2=0;index2<idList.length;index2++){
				v+=vectors[idList[index2]][index1];
			}
			vector[index1]=v/idList.length;
		}
		return vector;
	}
	//get the cluster result ,the values represent user id in each community
	public String[][] getClusterResult(){
		String[][] clusterList=new String[k][];
		for(int index=0;index<k;index++){
			String[] cluster=new String[clusterResult[index].length];
			for(int index1=0;index1<clusterResult[index].length;index1++){
				cluster[index1]= idList[clusterResult[index][index1]];
			}
			clusterList[index]=cluster;
		}
		return clusterList;
	}
	
	
}
