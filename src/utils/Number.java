package utils;

public class Number implements Comparable<Number>{
    Double data;
    int index;

    Number(double d, int i){
        this.data = d;
        this.index = i;
    }

    @Override
    public int compareTo(Number o) {
    	if(o.data-this.data>=0){
    		return 1;
    	}else{
    		return -1;
    	}
        
    }
}
