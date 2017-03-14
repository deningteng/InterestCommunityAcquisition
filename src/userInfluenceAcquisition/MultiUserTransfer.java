package userInfluenceAcquisition;


import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import communityDiscover.Community;


public class MultiUserTransfer {
	//all communities
	private Community[] communities;
	//all user id list
	private String[] allUserList ;
	//directory of data
	private String dir;
	
	private ArrayList<Map<String, ArrayList<String>>> maplist=new ArrayList<Map<String, ArrayList<String>>>();
	private static String strClassName = MultiUserTransfer.class.getName();  
    private static Logger logger = Logger.getLogger(strClassName);
	
	public MultiUserTransfer(Community[] communities,String[] allUserList,String dir){
		this.communities=communities;
		this.allUserList=allUserList;
		this.dir=dir;
	}
	
	
	/**
	 * Get common followers number of two users
	 * @param index1
	 * @param index2
	 * @return
	 */
	private int getCommonFollowersNum(int index1,int index2,String[] userInCommunity){
		logger.info("getCommonFollowersNum");
		int num=0;
		ArrayList<String> list1=new ArrayList<String>();
		ArrayList<String> list2=new ArrayList<String>();
		for(Map<String, ArrayList<String>> map:maplist){
			if(map.get(userInCommunity[index1])!=null){
				list1.addAll(map.get(userInCommunity[index1]));
			}
			
			if(map.get(userInCommunity[index2])!=null){
				list2.addAll(map.get(userInCommunity[index2]));
			}
			
		}
		for(String id1:list1){
			boolean flag=true;
			for(int term=0;term<list2.size()&&flag;term++){
				if(id1.equals(list2.get(term))){
					num++;
					flag=false;
				}
			}
		}
		return num;
	}
	/**
	 * Calculate the weight between users
	 * @return
	 */
	private double[][] calcualteWeight(String[] userInCommunity){
		logger.info("calcualteWeight");
		int userNum=userInCommunity.length;
		double[][] weight=new double[userNum][userNum];
		for(int index1=0 ;index1<userNum;index1++){
			for(int index2=0;index2<userNum;index2++){
				if(index1==index2){
					weight[index1][index2]=1.0;
				}else{
					double num=getCommonFollowersNum(index1,index2,userInCommunity);
					if(num!=0){
						int size=0;
						for(Map<String, ArrayList<String>> map:maplist){
							if(map.get(userInCommunity[index1])!=null){
								size+=map.get(userInCommunity[index1]).size();
							}
						}
						weight[index1][index2]=num/size;
					}else{
						weight[index1][index2]=0.0;
					}
					
				}
			}
		}
		return weight;
	}
	/**
	 * Get followers of all users
	 * @param dir
	 */
	private void getFollowers(String dir){
		logger.info("getFollowers");
		File folder = new File(dir);
		String[] filenames=folder.list();
		ExecutorService exec = Executors.newCachedThreadPool();  
        ArrayList<Future<Map<String, ArrayList<String>>>>  results = new ArrayList<Future<Map<String, ArrayList<String>>>>();
		for(String name:filenames){
			results.add(exec.submit(new FollowersAcquisition(allUserList,dir+"\\"+name)));
		}
		for(Future<Map<String, ArrayList<String>>> fs : results){  
            try{  
            	maplist.add(fs.get());
            }catch(Exception e){  
                e.printStackTrace();  
            }finally{  
                exec.shutdown();  
            }  
        }  
	}
	/**
	 * Get all communities user transfer matrix
	 * @return
	 */
	public double[][][] getMultiUserTransfer(){
		logger.info("getMultiUserTransfer");
		int communityNum=communities.length;
		double[][][] multiUserTransferMatrix=new double[communityNum][][];
		getFollowers(dir);
		for(int index1=0;index1<communityNum;index1++){
			String[] userInCommunity=communities[index1].getUserid();
			int num=userInCommunity.length;
			double[][] userTransfer=new double[num][num];
			double[][] userSimilarity=communities[index1].getCommunityUserSimilarity();
			double[][] weights=calcualteWeight(userInCommunity);
			for(int index2=0;index2<num;index2++){
				for(int index3=0;index3<num;index3++){
					userTransfer[index2][index3]=weights[index2][index3]*userSimilarity[index2][index3];
				}
			}
			multiUserTransferMatrix[index1]=userTransfer;
		}
		
		return multiUserTransferMatrix;
	}
	
}
