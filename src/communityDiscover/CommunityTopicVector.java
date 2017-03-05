package communityDiscover;
/**
 * Structure of representative topic vector
 * @author 15754
 *
 */
public class CommunityTopicVector {
	private int topicid;
	private double representation;
	//Representative topic vector,number of dimension is equal to the count of users in community
	private double[] vector;
	
	public CommunityTopicVector(){
		
	}
	
	public CommunityTopicVector(int topicid,double representation,double[] vector){
		this.topicid=topicid;
		this.representation=representation;
		this.vector=vector;
	}

	public int getTopicid() {
		return topicid;
	}

	public void setTopicid(int topicid) {
		this.topicid = topicid;
	}

	public double getRepresentation() {
		return representation;
	}

	public void setRepresentation(double representation) {
		this.representation = representation;
	}

	public double[] getVector() {
		return vector;
	}

	public void setVector(double[] vector) {
		this.vector = vector;
	}
	
}
