package userInfluenceAcquisition;

import java.util.logging.Logger;

import communityDiscover.CommunityTopicVector;
import utils.ProbabilityDistributionDiversity;
/**
 * Obtain user influence score in community
 * @author 15754
 *
 */
public class InfluenceScoreAcquisition {
	
	private double[][] userTransferMaxtrix;
	//damping coefficient
	private double r;
	private CommunityTopicVector[] communityTopicVector;
	//iteration number
	private int iteration;
	//deviation in iteration
	private double deviation;
	private static String strClassName = InfluenceScoreAcquisition.class.getName();  
    private static Logger logger = Logger.getLogger(strClassName);
	
	public InfluenceScoreAcquisition(double r,double[][] userTransferMaxtrix,CommunityTopicVector[] communityTopicVector,int iteration,double deviation){
		this.r=r;
		this.userTransferMaxtrix=userTransferMaxtrix;
		this.communityTopicVector=communityTopicVector;
		this.iteration=iteration;
		this.deviation=deviation;
	}
	/**
	 * init influnece score
	 * @param n
	 * @return
	 */
	private double[] initInfluenceScore(int n){
		logger.info("initInfluenceScore");
		double[] influenceScore=new double[n];
		for(int index=0;index<n;index++){
			influenceScore[index]=1.0;
		}
		return influenceScore;
	}
	/**
	 * calculation of influence score
	 * @param influenceScore
	 * @param topic
	 * @return
	 */
	private double[] forumlaCalculation(double[] influenceScore,double[] topic){
		logger.info("forumlaCalculation");
		double[] result=new double[influenceScore.length];
		double[] product=new double[influenceScore.length];
		for(int index1=0;index1<userTransferMaxtrix.length;index1++){
			double term=0.0;
			for(int index2=0;index2<userTransferMaxtrix[index1].length;index2++){
				term+=r*userTransferMaxtrix[index1][index2]*topic[index2];
			}
			product[index1]=term;
		}
		for(int index1=0;index1<userTransferMaxtrix.length;index1++){
			result[index1]=product[index1]+(1-r)*topic[index1];
		}
		
		return result;
	}
	/**
	 * Iteration calculation of influence score
	 * @param influenceScore
	 * @param topic
	 * @return
	 */
	private double[] influenceScoreIterationCalculation(double[] influenceScore,double[] topic){
		logger.info("influenceScoreIterationCalculation");
		boolean flag=true;
		for(int index1=0;index1<iteration&&flag;index1++){
			double[] temporary=forumlaCalculation(influenceScore,topic);
			flag=criticism(influenceScore,temporary);
			influenceScore=temporary;
		}
		
		return influenceScore;
	}
	/**
	 * criticism function of iteration
	 * @param v1
	 * @param v2
	 * @return
	 */
	private boolean criticism(double[] v1,double[] v2){
		logger.info("criticism");
		boolean flag=true;
		ProbabilityDistributionDiversity p=new ProbabilityDistributionDiversity();
		double diversity=p.getDiversity(v1, v2);
		if(deviation>diversity){
			flag=false;
		}
		
		return flag;
	}
	/**
	 * Calling interface of influence score calculation
	 * @return array of influence score
	 */
	public double[] getInfluenceScore(){
		logger.info("getInfluenceScore");
		int userNum=userTransferMaxtrix.length;
		double[] result=new double[userNum];
		for(int index1=0;index1<communityTopicVector.length;index1++){
			double representation=communityTopicVector[index1].getRepresentation();
			double[] topic=communityTopicVector[index1].getVector();
			double[] influenceScore=initInfluenceScore(userNum);
			influenceScore=influenceScoreIterationCalculation(influenceScore,topic);
			for(int index2=0;index2<userNum;index2++){
				result[index2]+=influenceScore[index2]*representation;
			}
		}
		return result;
	}
	
	
}
