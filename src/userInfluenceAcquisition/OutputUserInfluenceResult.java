package userInfluenceAcquisition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import communityDiscover.Community;
import communityDiscover.CommunityTopicVector;

public class OutputUserInfluenceResult {
	private Community[] communities;
	private UserInfluenceScore[][] uis;
	private String dir;
	private static String strClassName = MultiUserTransfer.class.getName();  
    private static Logger logger = Logger.getLogger(strClassName);
	
	public OutputUserInfluenceResult(Community[] communities,UserInfluenceScore[][] uis,String dir){
		this.communities=communities;
		this.uis=uis;
		this.dir=dir;
	}
	
	public void getOutput(){
		logger.info("getOutput");
		for(int index1=0;index1<communities.length;index1++){
			File writename = new File(dir+"\\community_"+(index1+1));   
	        try {
				writename.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(writename)); 
				int userNum=communities[index1].getUserid().length;
				CommunityTopicVector[] ctvl=communities[index1].getCommunityTopicVectorList();
				out.write("user count in community:"+userNum);
				out.write("\r\n");
				for(int index2=0;index2<ctvl.length;index2++){
					int topicId=ctvl[index2].getTopicid();
					double representation=ctvl[index2].getRepresentation();
					out.write("Topic id:"+topicId+",Representation:"+representation);
					out.write("\r\n");
				}
				out.write("userid,score");
				out.write("\r\n");
				for(int index2=0;index2<uis[index1].length;index2++){
					out.write(uis[index1][index2].getUserid()+","+uis[index1][index2].getScore());
					out.write("\r\n");
				}
		        out.flush(); 
		        out.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
	}
}
