package communityDiscover;

import java.util.Map;

/**
 * Structure of community
 * @author 15754
 *
 */
public class Community {
	
	private String[] userid;
	private Map<String,Double> userMessagesCount;
	private double MessagesCountSum;
	//The representative topics vectors of community topic_num*user_num_in_community
	private CommunityTopicVector[] communityTopicVectorList;
	//The similarity between users in community
	private double[][] communityUserSimilarity;
	
	public Community(){
	}

	public String[] getUserid() {
		return userid;
	}

	public void setUserid(String[] userid) {
		this.userid = userid;
	}

	public CommunityTopicVector[] getCommunityTopicVectorList() {
		return communityTopicVectorList;
	}

	public void setCommunityTopicVectorList(CommunityTopicVector[] communityTopicVectorList) {
		this.communityTopicVectorList = communityTopicVectorList;
	}

	public double[][] getCommunityUserSimilarity() {
		return communityUserSimilarity;
	}

	public void setCommunityUserSimilarity(double[][] communityUserSimilarity) {
		this.communityUserSimilarity = communityUserSimilarity;
	}

	public Map<String, Double> getUserMessagesCount() {
		return userMessagesCount;
	}

	public void setUserMessagesCount(Map<String, Double> userMessagesCount) {
		this.userMessagesCount = userMessagesCount;
	}

	public double getMessagesCountSum() {
		return MessagesCountSum;
	}

	public void setMessagesCountSum(double messagesCountSum) {
		MessagesCountSum = messagesCountSum;
	}
	
}
