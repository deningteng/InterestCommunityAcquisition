package userInfluenceAcquisition;

import java.util.logging.Logger;

import communityDiscover.Community;
/**
 * Obtain top n influence score user in community 
 * @author 15754
 *
 */
public class InfluenceRankAcquisition {
	private Community[] communities;
	private double[][][] userTransferMaxtrix;
	private static String strClassName = InfluenceRankAcquisition.class.getName();  
    private static Logger logger = Logger.getLogger(strClassName);
	
	public InfluenceRankAcquisition(Community[] communities,double[][][] userTransferMaxtrix){
		this.communities=communities;
		this.userTransferMaxtrix=userTransferMaxtrix;
	}
	/**
	 * Get the index of min value in array
	 * @param arr
	 * @return
	 */
	private int getMinIndex(double[] arr){  
		logger.info("getMinIndex");
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
		logger.info("getMaxIndex");
	    int maxIndex = 0;  
	    for(int i=0; i<arr.length; i++){  
	        if(arr[i] > arr[maxIndex]){  
	        	maxIndex = i;  
	        }  
	    }  
	    return maxIndex;  
	}
	/**
	 * Get top n max value index in array
	 * @param list
	 * @param topN
	 * @return
	 */
	private int[] getTopNIndex(double[] list,int topN){
		logger.info("getTopNIndex");
		int[] index=new int[topN];
		double min=list[getMinIndex(list)];
		for(int index1=0;index1<topN;index1++){
			index[index1]=getMaxIndex(list);
			list[index[index1]]=min;
		}
		return index;
	}
	/**
	 * Get Top n influence user in community
	 * @param topNum
	 * @return
	 */
	public UserInfluenceScore[][] getInfluenceRank(int topNum){
		logger.info("getInfluenceRank");
		int communityNum=communities.length;
		UserInfluenceScore[][] userInfluenceScores=new UserInfluenceScore[communityNum][];
		for(int index1=0;index1<communityNum;index1++){
			String[] userlist=communities[index1].getUserid();
			InfluenceScoreAcquisition isa=
					new InfluenceScoreAcquisition(0.5,userTransferMaxtrix[index1],
							communities[index1].getCommunityTopicVectorList(),
							1000,0.0001);
			double[] scores=isa.getInfluenceScore();
			UserInfluenceScore[]uis=new UserInfluenceScore[topNum];
			int[] topNIndex=getTopNIndex(scores,topNum);
			for(int index2=0;index2<topNum;index2++){
				int term=topNIndex[index2];
				UserInfluenceScore ui=new UserInfluenceScore(userlist[term],scores[term]);
				uis[index2]=ui;
			}
			userInfluenceScores[index1]=uis;
		}
		return userInfluenceScores;
	}
}
