package userInfluenceAcquisition;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class FollowersAcquisition implements Callable<Map<String,ArrayList<String>>>{

	private Map<String,ArrayList<String>> map;
	private String[] allUserList ;
	private String filePath;
	
	public FollowersAcquisition(String[] allUserList,String filePath){
		this.allUserList=allUserList;
		this.filePath=filePath;
	}
	/**
	 * init weibo user and followers
	 * @param userid
	 */
	private void initWeiboUser(String[] allUserid){
		map=new HashMap<String,ArrayList<String>>();
		for(String id:allUserid){
			map.put(id, new ArrayList<String>());
		}
	}
	
	private void getFollowers(String filePath){
		FileReader reader;
		try {
			reader = new FileReader(filePath);
			BufferedReader br = new BufferedReader(reader);
			String str=null;
			try {
				int index=0;
				while((str = br.readLine()) != null) {
					if(index!=0){
						//[userid,followerid]
						String[] weiboFollowers=str.split(",");
						boolean flag=true;
						for(int index1=0;index1<allUserList.length&&flag;index1++){
							if(allUserList[index1].equals(weiboFollowers[0])){
								map.get(allUserList[index1]).add(weiboFollowers[1]);
								flag=false;
							}
						}
					}
					System.out.println(filePath+" read lines:"+index);
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
	}
	@Override
	public Map<String, ArrayList<String>> call() throws Exception {
		initWeiboUser(allUserList);
		getFollowers(filePath);
		return map;
	}

}
