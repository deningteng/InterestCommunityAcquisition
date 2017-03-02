package test;

import main.CommunityAcquisition;
import main.UserTopicDiversity;

public class TestCase {
	
	public static void main(String[] args){
//		testLoadUserTopicVectors();
		testUserTopicDiversity();

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
		OutputTwoDimensionalArray outputArray1=
				new OutputTwoDimensionalArray(".\\result\\outputTwocommunityCenters.txt",communityAcquisition.getCenters());
		outputArray1.getOutput();
		for(String[] cluster:communityAcquisition.getClusterResult()){
			System.out.println(cluster.length);
		}
	}
}
