package logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Scanner;


public class reqRatesCounter {

	/**
	 * @param minrate2 
	 * @param maxrate2 
	 * @param minrate 
	 * @param maxrate 
	 * @param args
	 * @throws Exception 
	 */
	public static String counter(int[] maxrate2, int[] minrate2, String path ) throws Exception{
		String res = "";
		String errorres = "";
		File dir = new File(path);
		if (!dir.exists() || !dir.isDirectory()) {
			throw new Exception("No such App exists!");
		}
		String appname = dir.getName();
		BufferedReader bin;
		hourInfo[] hourinfo = new hourInfo[24];
		int minrate= Integer.MAX_VALUE;
		int maxrate= 0;
		File targetfile;
		File[] files = dir.listFiles();
		Arrays.sort(files);
		for (File day : files){
			String fname = day.getName();
			System.out.println(fname);
			errorres += fname+"\t";
			String dayname  = fname.split(".t")[0];
			String str;
			int hour;
			int errorNum;
			int period;
			String []strs;
			for (int i=0;i<24;i++)
				hourinfo[i] =  new hourInfo();
			int i=0;
			try {
				bin = new BufferedReader(new FileReader(day));
				while ((str = bin.readLine())!=null){
					//int j=0;
					strs = str.split("\t");
					hour = Integer.parseInt(strs[1].split(" ")[1].split(":")[0]);
					hourinfo[hour].hourRate++;
					if (Integer.parseInt(strs[2])!=200){
						hourinfo[hour].errorNum++;
						errorres += strs[2] + "," + strs[6] + " " + strs[7] + ";";
					}
					hourinfo[hour].avgPeriod+=Integer.parseInt(strs[5]);
					i++;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			errorres += "\n";
			for (int j=0;j<24;j++){
				if (hourinfo[j].hourRate>maxrate) 
					maxrate = hourinfo[j].hourRate;
				if (hourinfo[j].hourRate < minrate)
					minrate = hourinfo[j].hourRate;
				
				if (hourinfo[j].hourRate!=0)
					hourinfo[j].avgPeriod /= hourinfo[j].hourRate;
				res += dayname + "\t" + hourinfo[j].hourRate + "\t" + hourinfo[j].errorNum + "\t" + hourinfo[j].avgPeriod+ "\t" + "\n";
			}
			//res += "\t" + i + "\n";
		}
		
		Scanner sinScanner=new Scanner(res);
		String line;
		String[] strs;
		String fres = "";
		int rate;
		int interval = maxrate - minrate;
		while (sinScanner.hasNext()){
			line = sinScanner.nextLine();
			strs = line.split("\t");
			rate = Integer.parseInt(strs[1]);
			fres += 1.0 * (rate - minrate)/ interval + "\t" + strs[1] + "\t" + strs[2] + "\t" +strs[3] + "\t" + strs[0] + "\n";
		}
		File errorfile = new File(dir.getParentFile(),appname + ".err");
		if (errorfile.exists()) errorfile.delete();
		targetfile =  new File(dir.getParentFile(),appname + ".txt");
		if (targetfile.exists()) targetfile.delete();
		BufferedWriter bout;
		BufferedWriter bout1;
		try {
			bout = new BufferedWriter(new FileWriter(targetfile,true));
								//   ip time stutus inb outb per mtd pth bsu bwr 
			bout1 = new BufferedWriter(new FileWriter(errorfile,true));
			bout.write(fres);
			bout1.write(errorres);
			bout1.close();
			bout.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		minrate2[0] = minrate;
		maxrate2[0] = maxrate;
		return fres;
	}
	
	public static String normalize(File file, int maxrate, int minrate){
		String res= "";
		BufferedReader bin;
		String str;
		int rate;
		int interval = maxrate - minrate;
		try {
			bin = new BufferedReader(new FileReader(file));
			String[] strs;
			while ((str = bin.readLine())!=null){
				strs = str.split("\t");
				rate = Integer.parseInt(strs[1]);
				res += 1.0 * (rate - minrate)/ interval + "\t" + strs[2] + "\t" +strs[3] + "\n";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;		
	}
	
	public static void main(String[] args) throws Exception {
		Scanner inScanner=new Scanner(System.in);
		String appName = inScanner.nextLine();
		//reqRatesCounter.counter("D:\\logAnalyzer140102\\data\\" + appName);
		System.out.println("end!");
	}

}
