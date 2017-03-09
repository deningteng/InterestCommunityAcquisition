package utils;

public class ProbabilityDistributionDiversity {
	
	public ProbabilityDistributionDiversity(){
		
	}
	/**
	 * Get distance of two distribution
	 * @param v1
	 * @param v2
	 * @return
	 */
	public double getDiversity(double[] v1,double[] v2){
		return Math.sqrt(2*jensenShannonDivergence(v1,v2));
	}
	
	/**
	 * Get length of vector
	 * @param vector
	 * @return
	 */
	private double vectorLength(double[] vector){
		double vectorLength=0.0;
		for(double value:vector){
			vectorLength +=Math.pow(value, 2);
		}
		vectorLength=Math.sqrt(vectorLength);
		return vectorLength;
	}
	/**
	 * Get normalization of vector
	 * @param vector
	 * @return
	 */
	private double[] normalization(double[] vector){
		double vectorLength=vectorLength(vector);
		double[] normalization=new double[vector.length];
		for(int index=0;index<vector.length;index++){
			normalization[index]=vector[index]/vectorLength;
		}
		return normalization;
	}
	
	/**
	 * Get the average of two vectors
	 * @param v1
	 * @param v2
	 * @return
	 */
	private double[] vectorsAverage(double[] v1,double[] v2){
		double[] average=new double[v1.length];
		for(int index=0;index<v1.length;index++){
			average[index]=(v1[index]+v2[index])/2;
		}
		return average;
	}
	/**
	 * Get KL Divergence of two probability distribution
	 * @param v1
	 * @param v2
	 * @return
	 */
	private double klDivergence(double[] v1,double[] v2){
		double klDivergence=0.0;
		for(int index=0;index<v1.length;index++){
			klDivergence+=v1[index]*Math.log(v1[index]/v2[index]);
		}
		return klDivergence;
	}
	
	/**
	 * Get JS Divergence of two probability distribution
	 * @param v1
	 * @param v2
	 * @return
	 */
	private double jensenShannonDivergence(double[] v1,double[] v2){
		v1=normalization(v1);
		v2=normalization(v2);
		double[] average=vectorsAverage(v1,v2);
		double jsDivergence=0.0;
		jsDivergence=(klDivergence(v1,average)+klDivergence(v2,average))/2;
		return jsDivergence;
	}
}
