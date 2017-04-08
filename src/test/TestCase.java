package test;

import java.util.logging.Logger;
import communityDiscover.Community;
import communityDiscover.CommunityAcquisition;
import communityDiscover.UserTopicDiversity;
import email.MainSendEmail;
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
	
//	public static void testLoadUserTopicVectors(){
//		String directory="C:\\Users\\lupy\\workspace\\step_one_data\\result\\document_topic.txt";
//		UserTopicDiversity userTopicDiversity=new UserTopicDiversity();
//		userTopicDiversity.vectorLoad(directory);
////		String[] ids=userTopicDiversity.getIdList();
//		double[][] vectors=userTopicDiversity.getVectors();
//		double[][] diversity=userTopicDiversity.getUserTopicDiversity(vectors);
//
//		OutputTwoDimensionalArray outputArray=
//				new OutputTwoDimensionalArray(".\\result_standard\\outputTwoDimensionalArray.txt",diversity);
//		outputArray.getOutput();
//		System.out.println("done");
//	}
//	
//	public static void testUserTopicDiversity(){
//		String directory="F:\\Git Repository\\result\\document_topic.txt";
//		UserTopicDiversity userTopicDiversity=new UserTopicDiversity();
//		userTopicDiversity.vectorLoad(directory);
//		double[][] vectors=userTopicDiversity.getVectors();
//		String[] idList=userTopicDiversity.getIdList();
//		CommunityAcquisition communityAcquisition=new CommunityAcquisition(idList,vectors);
//		communityAcquisition.setKmeansConfiguration(10, 0.00000000000001, 500);
//		communityAcquisition.Kmeans();
//		OutputTwoDimensionalArray communityCenters=
//				new OutputTwoDimensionalArray(".\\result_standard\\outputCommunityCenters.txt",communityAcquisition.getCenters());
//		communityCenters.getOutput();
//		for(int index=0;index<communityAcquisition.getClusterResult().length;index++){
//			System.out.println(communityAcquisition.getClusterResult()[index].length);
//		}
//		Community[] communities=communityAcquisition.outputCommunities(0.2);
//		System.out.println("done ;"+communities.length);
//		
//	}
	
	public static void testUserTransferMatrixCalculation(){
		String directory="C:\\Users\\lupy\\workspace\\step_one_data\\result\\document_topic.txt";
		UserTopicDiversity userTopicDiversity=new UserTopicDiversity();
		userTopicDiversity.vectorLoad(directory);
		double[][] vectors=userTopicDiversity.getVectors();
		String[] idList=userTopicDiversity.getIdList();
		userTopicDiversity=null;
		CommunityAcquisition communityAcquisition=new CommunityAcquisition(idList,vectors);
		communityAcquisition.setKmeansConfiguration(10, 0.00000000000001, 500);
		communityAcquisition.Kmeans();
//		OutputTwoDimensionalArray communityCenters=
//				new OutputTwoDimensionalArray(".\\result_standard\\outputCommunityCenters.txt",communityAcquisition.getCenters());
//		communityCenters.getOutput();
		for(int index=0;index<communityAcquisition.getClusterResult().length;index++){
			System.out.println(communityAcquisition.getClusterResult()[index].length);
		}
		logger.info("community cluster done");
		Community[] communities=communityAcquisition.outputCommunities(0.3);
		communityAcquisition=null;
		for(int index=0;index<communities.length;index++){
			OutputOneDimensionalArray oda=new OutputOneDimensionalArray(".\\result_standard\\memberOfCommunity"+(index+1),
					communities[index].getUserid());
			oda.getOutput();
			
//			OutputTwoDimensionalArray otda=new OutputTwoDimensionalArray(".\\result_standard\\UserSimilarityOfCommunity"+(index+1),
//					communities[index].getCommunityUserSimilarity());
//			otda.getOutput();
			
		}
		System.out.println("done ;"+communities.length);
		MultiUserTransfer userTransfer=new MultiUserTransfer(communities,idList,"C:\\Users\\lupy\\workspace\\step_one_data\\weibo");
		double[][][] userTransferMaxtrix=userTransfer.getMultiUserTransfer();
		double[][][] base1=userTransfer.getBaseline1TransferMatrix();//only user similarity
		double[][][] base2=userTransfer.getBaseline2TransferMatrix();//only weight
		
		userTransfer=null;
//		for(int index=0;index<userTransferMaxtrix.length;index++){
//			OutputTwoDimensionalArray otda=new OutputTwoDimensionalArray(".\\result_standard\\userTransferMaxtrix"+(index+1),userTransferMaxtrix[index]);
//			otda.getOutput();
//		}
		
		System.out.println("user transfer matrix done :"+userTransferMaxtrix.length);
		InfluenceRankAcquisition ira=new InfluenceRankAcquisition(communities,userTransferMaxtrix);
		//The number 100 is useless
		UserInfluenceScore[][] uis=ira.getInfluenceRank(100);
		ira=null;
		OutputUserInfluenceResult ouir=new OutputUserInfluenceResult(communities,uis,".\\result_standard");
		ouir.getOutput();
		ouir=null;
		
		
		
		InfluenceRankAcquisition ira1=new InfluenceRankAcquisition(communities,base1);
		//The number 100 is useless
		UserInfluenceScore[][] uis1=ira1.getInfluenceRank(100);
		ira1=null;
		OutputUserInfluenceResult ouir1=new OutputUserInfluenceResult(communities,uis1,".\\result_base1");
		ouir1.getOutput();
		ouir1=null;
		
		InfluenceRankAcquisition ira2=new InfluenceRankAcquisition(communities,base2);
		//The number 100 is useless
		UserInfluenceScore[][] uis2=ira2.getInfluenceRank(100);
		ira2=null;
		OutputUserInfluenceResult ouir2=new OutputUserInfluenceResult(communities,uis2,".\\result_base2");
		ouir2.getOutput();
		ouir2=null;
		

		MainSendEmail se=new MainSendEmail();
		se.sendEmail();
	}
}
