package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Obtain user communities
 * @author 15754
 *
 */
public class CommunityAcquisition {
	//id list of all users
	private String[] idList;
	//user topic probability vectors
	private double[][] vectors;
	//num of communities
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
	/**
	 * set configuration value of kmeans
	 * @param k :number of communities
	 * @param mu :deviation of last iteration
	 * @param iterationNum :largest iteration times
	 */
	public void setKmeansConfiguration(int k, double mu,int iterationNum){
		this.k=k;
		this.mu=mu;
		this.iterationNum=iterationNum;
		clusterResult=new int[k][];
		centerVectors=new double[k][];
	}
	/**
	 * Get length of vector
	 * @param vector
	 * @return
	 */
	private double vectorLength(double[] vector){
		double vectorLength=0.0;
		for(double value:vector){
			vectorLength +=Math.pow(value, 2);
		}
		vectorLength=Math.sqrt(vectorLength);
		return vectorLength;
	}
	/**
	 * Get normalization of vector
	 * @param vector
	 * @return
	 */
	private double[] normalization(double[] vector){
		double vectorLength=vectorLength(vector);
		double[] normalization=new double[vector.length];
		for(int index=0;index<vector.length;index++){
			normalization[index]=vector[index]/vectorLength;
		}
		return normalization;
	}
	
	/**
	 * Get the average of two vectors
	 * @param v1
	 * @param v2
	 * @return
	 */
	private double[] vectorsAverage(double[] v1,double[] v2){
		double[] average=new double[v1.length];
		for(int index=0;index<v1.length;index++){
			average[index]=(v1[index]+v2[index])/2;
		}
		return average;
	}
	/**
	 * Get KL Divergence of two probability distribution
	 * @param v1
	 * @param v2
	 * @return
	 */
	private double klDivergence(double[] v1,double[] v2){
		double klDivergence=0.0;
		for(int index=0;index<v1.length;index++){
			klDivergence+=v1[index]*Math.log(v1[index]/v2[index]);
		}
		return klDivergence;
	}
	
	/**
	 * Get JS Divergence of two probability distribution
	 * @param v1
	 * @param v2
	 * @return
	 */
	private double jensenShannonDivergence(double[] v1,double[] v2){
		v1=normalization(v1);
		v2=normalization(v2);
		double[] average=vectorsAverage(v1,v2);
		double jsDivergence=0.0;
		jsDivergence=(klDivergence(v1,average)+klDivergence(v2,average))/2;
		return jsDivergence;
	}
	/**
	 * Get distance of two distribution
	 * @param v1
	 * @param v2
	 * @return
	 */
	private double getDist(double[] v1,double[] v2){
		return Math.sqrt(2*jensenShannonDivergence(v1,v2));
	}
	/**
	 * Get the index of min value in array
	 * @param arr
	 * @return
	 */
	private int getMinIndex(double[] arr){  
	    int minIndex = 0;  
	    for(int i=0; i<arr.length; i++){  
	        if(arr[i] < arr[minIndex]){  
	            minIndex = i;  
	        }  
	    }  
	    return minIndex;  
	}
	/**
	 * Get the index of max value in array
	 * @param arr
	 * @return
	 */
	private int getMaxIndex(double[] arr){  
	    int maxIndex = 0;  
	    for(int i=0; i<arr.length; i++){  
	        if(arr[i] > arr[maxIndex]){  
	        	maxIndex = i;  
	        }  
	    }  
	    return maxIndex;  
	}
	/**
	 * init cluster centers
	 */
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
	/**
	 * Get cluster centers
	 * @return
	 */
	public double[][] getCenters(){
		return centerVectors;
	}
	
	/**
	 * Entrance of kmeans
	 */
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
	/**
	 * Judge whether or not to stop iteration 
	 * @param newCenter
	 */
	private void criticism(double[][] newCenter){
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
	/**
	 * Get average vector of probability distribution which user index are in idlist
	 * @param idList :contain user index
	 * @return
	 */
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
	/**
	 * get the cluster result ,the values represent user id in each community
	 * @return All user id in community community_num*user_num
	 */
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
	/**
	 * Get user similarity between each other user_num*user_num
	 */
	public double[][] getUserSimilarityMatrix(double[][] vectors){
		int num=vectors.length;
		double[][] userSimilarityMatrix=new double[num][];
		for(int index1=0;index1<num;index1++){
			double[] values=new double[num];
			for(int index2=0;index2<num;index2++){
				values[index2]=Math.sqrt(2*jensenShannonDivergence(vectors[index1],vectors[index2]));
				values[index2]=Math.exp(-values[index2]);
			}
			userSimilarityMatrix[index1]=values;
		}
		return userSimilarityMatrix;
	}
	/**
	 * After Kmeans, get community info from cluster result
	 * @param threshold, limit the least value of topic representation
	 * @return communities
	 */
	public Community[] outputCommunities(double threshold){
		double[][] userSimilarityMatrix=getUserSimilarityMatrix(vectors);
		String[][] clusterResultWithUserid=getClusterResult();
		Community[] communities=new Community[centerVectors.length];
		//Get info for each community
		for(int index1=0 ;index1<centerVectors.length;index1++){
			Community community=new Community();
			community.setUserid(clusterResultWithUserid[index1]);
			//Get all user index in community
			int[] list=clusterResult[index1];
			//Get user similarity each other in community
			double[][] communityUserSimilarity=new double[list.length][list.length];
			for(int term1=0 ;term1<list.length;term1++){
				for(int term2=0 ;term2<list.length;term2++){
					communityUserSimilarity[term1][term2]=userSimilarityMatrix[list[term1]][term2];
				}
			}
			community.setCommunityUserSimilarity(communityUserSimilarity);
			
			//topic index,representation value of topic
			Map<Integer,Double> representationOfTopic=new HashMap<Integer,Double>();
			int representTopicNum=0;
			//Get representative topic of community, choose at least one topic
			for(int index2=0 ;index2<centerVectors[index1].length;index2++){
				if(centerVectors[index1][index2]>=threshold){
					representTopicNum++;
					representationOfTopic.put(index2, centerVectors[index1][index2]);
				}
			}
			if(representTopicNum==0){
				int maxIndex=getMaxIndex(centerVectors[index1]);
				representationOfTopic.put(maxIndex,centerVectors[index1][maxIndex]);
				representTopicNum++;
			}
			//Get representative topics of community
			CommunityTopicVector[] communityTopicVectorList=new CommunityTopicVector[representTopicNum];
			int representTopicIndex=0;
			for(Map.Entry<Integer, Double> entry : representationOfTopic.entrySet()){
				CommunityTopicVector communityTopicVector=new CommunityTopicVector();
				communityTopicVector.setTopicid(entry.getKey());
				communityTopicVector.setRepresentation(entry.getValue());
				double[] vector=new double[clusterResult[index1].length];
				for(int index3=0;index3<clusterResult[index1].length;index3++){
					int index=clusterResult[index1][index3];
					vector[index3]=vectors[index][entry.getKey()];

				}
				communityTopicVector.setVector(vector);
				communityTopicVectorList[representTopicIndex]=communityTopicVector;
				representTopicIndex++;
				
			}
			community.setCommunityTopicVectorList(communityTopicVectorList);
	        communities[index1]=community;
		}
		return communities;
	}
}
