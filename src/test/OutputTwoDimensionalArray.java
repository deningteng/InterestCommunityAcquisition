package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputTwoDimensionalArray {
	private String dir;
	private double[][] array;
	OutputTwoDimensionalArray(String dir,double[][] array){
		this.dir=dir;
		this.array=array;
	}
	
	public void getOutput(){
		File writename = new File(dir);   
        try {
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename)); 
			for(double[] temp:array){
				for(double value:temp){
					out.write(value+" ");
				}
				out.write("\r\n");
			}
	        out.flush(); 
	        out.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 创建新文件  
        
	}
}
