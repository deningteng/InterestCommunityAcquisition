package test;

import java.util.logging.Logger;
import communityDiscover.Community;
import communityDiscover.CommunityAcquisition;
import communityDiscover.UserTopicDiversity;
import userInfluenceAcquisition.InfluenceRankAcquisition;
import userInfluenceAcquisition.MultiUserTransfer;
import userInfluenceAcquisition.OutputUserInfluenceResult;
import userInfluenceAcquisition.UserInfluenceScore;

public class TestCase {
	
	static String strClassName = TestCase.class.getName();  
    static Logger logger = Logger.getLogger(strClassName);
	
	public static void main(String[] args){
//		testLoadUserTopicVectors();
//		testUserTopicDiversity();
		testUserTransferMatrixCalculation();

	}
	
	public static void testLoadUserTopicVectors(){
		String directory="F:\\Git Repository\\result\\document_topic.txt";
		UserTopicDiversity userTopicDiversity=new UserTopicDiversity();
		userTopicDiversity.vectorLoad(directory);
//		String[] ids=userTopicDiversity.getIdList();
		double[][] vectors=userTopicDiversity.getVectors();
		double[][] diversity=userTopicDiversity.getUserTopicDiversity(vectors);

		OutputTwoDimensionalArray outputArray=
				new OutputTwoDimensionalArray(".\\result\\outputTwoDimensionalArray.txt",diversity);
		outputArray.getOutput();
		System.out.println("done");
	}
	
	public static void testUserTopicDiversity(){
		String directory="F:\\Git Repository\\result\\document_topic.txt";
		UserTopicDiversity userTopicDiversity=new UserTopicDiversity();
		userTopicDiversity.vectorLoad(directory);
		double[][] vectors=userTopicDiversity.getVectors();
		String[] idList=userTopicDiversity.getIdList();
		CommunityAcquisition communityAcquisition=new CommunityAcquisition(idList,vectors);
		communityAcquisition.setKmeansConfiguration(10, 0.00000000000001, 500);
		communityAcquisition.Kmeans();
		OutputTwoDimensionalArray communityCenters=
				new OutputTwoDimensionalArray(".\\result\\outputCommunityCenters.txt",communityAcquisition.getCenters());
		communityCenters.getOutput();
		for(int index=0;index<communityAcquisition.getClusterResult().length;index++){
			System.out.println(communityAcquisition.getClusterResult()[index].length);
		}
		Community[] communities=communityAcquisition.outputCommunities(0.3);
		System.out.println("done ;"+communities.length);
		
	}
	
	public static void testUserTransferMatrixCalculation(){
		String directory="F:\\Git Repository\\result\\document_topic.txt";
		UserTopicDiversity userTopicDiversity=new UserTopicDiversity();
		userTopicDiversity.vectorLoad(directory);
		double[][] vectors=userTopicDiversity.getVectors();
		String[] idList=userTopicDiversity.getIdList();
		CommunityAcquisition communityAcquisition=new CommunityAcquisition(idList,vectors);
		communityAcquisition.setKmeansConfiguration(10, 0.00000000000001, 500);
		communityAcquisition.Kmeans();
		OutputTwoDimensionalArray communityCenters=
				new OutputTwoDimensionalArray(".\\result\\outputCommunityCenters.txt",communityAcquisition.getCenters());
		communityCenters.getOutput();
		for(int index=0;index<communityAcquisition.getClusterResult().length;index++){
			System.out.println(communityAcquisition.getClusterResult()[index].length);
		}
		logger.info("community cluster done");
		Community[] communities=communityAcquisition.outputCommunities(0.3);
		System.out.println("done ;"+communities.length);
		MultiUserTransfer userTransfer=new MultiUserTransfer(communities,idList,"G:\\weibodata");
		double[][][] userTransferMaxtrix=userTransfer.getMultiUserTransfer();
		System.out.println("user transfer matrix done :"+userTransferMaxtrix.length);
		InfluenceRankAcquisition ira=new InfluenceRankAcquisition(communities,userTransferMaxtrix);
		UserInfluenceScore[][] uis=ira.getInfluenceRank(10);
		
		OutputUserInfluenceResult ouir=new OutputUserInfluenceResult(communities,uis,".\\result");
		ouir.getOutput();
	}
}
