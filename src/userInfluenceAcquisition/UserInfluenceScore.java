package userInfluenceAcquisition;

public class UserInfluenceScore {
	private String userid;
	private double score;
	
	public UserInfluenceScore(String userid,double score){
		this.userid=userid;
		this.score=score;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
}
