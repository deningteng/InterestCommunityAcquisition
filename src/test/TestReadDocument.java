package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestReadDocument {
	
	public static void main(String[] args){
		FileReader reader;
		try {
			reader = new FileReader("H:\\SMPData\\Weibo.Corpus\\weibo_follows.csv");
			BufferedReader br = new BufferedReader(reader);
			String str=null;
			try {
				int index=0;
				while((str = br.readLine()) != null&&index<100) {
					if(index!=0){
						String[] weiboFollowers=str.split(",");
						System.out.println(weiboFollowers[0]+"<-"+weiboFollowers[1]);
					}
					index++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
