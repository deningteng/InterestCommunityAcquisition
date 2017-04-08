package utils;



import java.util.Arrays;
public class SortedIndex {
	private double[] data;
	private int[] index;
	private double[] a;
	
	public SortedIndex(double[] a){
		this.a=a;
		data=new double[a.length];
		index=new int[a.length];
	}
    public void getSorted(){
        
        Number sorted[] = new Number[a.length];
        for (int i = 0; i < a.length; ++i) {
            sorted[i] = new Number(a[i], i);
        }
        
        Arrays.sort(sorted);

        //print sorted array
        for(int index1=0;index1<sorted.length;index1++){
        	index[index1]=sorted[index1].index;
        	data[index1]=sorted[index1].data;
        }
//        for (double n : data){
//            System.out.print("" + n +",");
//        }
//        System.out.println();
//
//        // print original index
//        for (int n: index){
//            System.out.print("" + n + ",");
//        }
//        System.out.println();
    }
	public double[] getData() {
		return data;
	}
	public int[] getIndex() {
		return index;
	}
    
}
