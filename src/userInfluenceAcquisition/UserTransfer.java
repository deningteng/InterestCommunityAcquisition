package userInfluenceAcquisition;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import communityDiscover.Community;

public class UserTransfer {
	//user community
	private Community community;
	//the directory of weibo_follower
	private String dir;
	//user id, followers of the user
	private Map<String,ArrayList<String>> map;
	//id of users in the community
	private String[] userid;
	private static String strClassName = UserTransfer.class.getName();  
    private static Logger logger = Logger.getLogger(strClassName);
	
	public UserTransfer(Community community,String dir){
		this.community=community;
		this.dir=dir;
	}
	/**
	 * init weibo user and followers
	 * @param userid
	 */
	private void initWeiboUser(String[] userid){
		logger.info("initWeiboUser");
		map=new HashMap<String,ArrayList<String>>();
		for(String id:userid){
			map.put(id, new ArrayList<String>());
		}
	}
	/**
	 * Get common followers number of two users
	 * @param index1
	 * @param index2
	 * @return
	 */
	private int getCommonFollowersNum(int index1,int index2){
		logger.info("getCommonFollowersNum");
		int num=0;
		ArrayList<String> list1=map.get(userid[index1]);
		ArrayList<String> list2=map.get(userid[index2]);
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
	private double[][] calcualteWeight(){
		logger.info("calcualteWeight");
		int userNum=map.size();
		double[][] weight=new double[userNum][userNum];
		for(int index1=0 ;index1<userNum;index1++){
			for(int index2=0;index2<userNum;index2++){
				if(index1==index2){
					weight[index1][index2]=1.0;
				}else{
					double num=getCommonFollowersNum(index1,index2);
					if(num!=0){
						weight[index1][index2]=num/map.get(userid[index1]).size();
					}else{
						weight[index1][index2]=0.0;
					}
					
				}
			}
		}
		return weight;
	}
	/**
	 * Get weight between users
	 * @return
	 */
	private double[][] getWeight(){
		logger.info("getWeight");
		userid=community.getUserid();
		initWeiboUser(userid);
		FileReader reader;
		try {
			reader = new FileReader(dir);
			BufferedReader br = new BufferedReader(reader);
			
			String str=null;
			try {
				int index=0;
				while((str = br.readLine()) != null) {
					if(index!=0){
						//[userid,followerid]
						String[] weiboFollowers=str.split(",");
						boolean flag=true;
						for(int index1=0;index1<userid.length&&flag;index1++){
							if(userid[index1].equals(weiboFollowers[0])){
								map.get(userid[index1]).add(weiboFollowers[1]);
								flag=false;
							}
						}
					}
					System.out.println("read lines:"+index);
					index++;
				}
				br.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return calcualteWeight();
	}
	/**
	 * Get user transfer matrix 
	 * @return user transfer matrix
	 */
	public double[][] getUserTransferMaxtrix(){
		logger.info("getUserTransferMaxtrix");
		int num=community.getUserid().length;
		double[][] userTransferMaxtrix=new double[num][num];
		double[][] weight=getWeight();
		double[][] userSimilarity=community.getCommunityUserSimilarity();
		for(int index1=0;index1<num;index1++){
			for(int index2=0;index2<num;index2++){
				userTransferMaxtrix[index1][index2]=weight[index1][index2]*userSimilarity[index1][index2];
			}
		}
		return userTransferMaxtrix;
	}
	
}
