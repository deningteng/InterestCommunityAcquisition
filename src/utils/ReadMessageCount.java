package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ReadMessageCount {
	//Path of user messages count file
	private String dir;
	private double sum;
	private HashMap<String,Double> messageCount;
	public ReadMessageCount(String dir){
		this.dir=dir;
	}
	public void getMessageCount(){
		messageCount=new HashMap<String,Double>();
		File file=new File(dir);
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String line;
			while ((line = br.readLine()) != null)
	        {
	            String[] words = line.split(",");
	            messageCount.put(words[0], Double.parseDouble(words[1]));
	        }
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public Map<String,Double> getMessageCount(String[] user){
		sum=0;
		Map<String,Double> map=new HashMap<String,Double>();
		for(String item:user){
			if(messageCount.containsKey(item)){
				map.put(item,messageCount.get(item));
				
			}else{
				map.put(item,0.0);
			}
			sum+=map.get(item);
		}
		return map;
	}
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	
}
