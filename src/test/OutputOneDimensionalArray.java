package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputOneDimensionalArray {
	private String dir;
	private String[] array;
	OutputOneDimensionalArray(String dir,String[] array){
		this.dir=dir;
		this.array=array;
	}
	
	public void getOutput(){
		File writename = new File(dir);   
        try {
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename)); 
			for(String temp:array){
				
				out.write(temp+"");
				
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
