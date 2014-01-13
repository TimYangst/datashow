package logic;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

//by realmagician

public class backpro {
	
	public static void main(String args[]){
		//dataPrecessing("train.in", 12, 12, 12);
	}
	public static double[] dataPrecessing(ArrayList<Double> src, String traindata, int attN, int hidN, int outN){
		//String filename=new String("train.in");
		try {			
			//////////FileInputStream fileInputStream=new FileInputStream(filename);
			Scanner sinScanner=new Scanner(traindata);
			int samN;
					
			ArrayList<ArrayList<Double>> samin = new ArrayList<ArrayList<Double>>();
			ArrayList<ArrayList<Double>> samout = new ArrayList<ArrayList<Double>>();
			double testin[]=new double[attN];
			
			if(sinScanner.hasNext()){
				int j;
				int i=0;
				double tmp;
				ArrayList<Double> in = new ArrayList<Double>();
				for (j=0;(j<attN)&&(sinScanner.hasNext());++j){
					tmp = sinScanner.nextDouble();
					in.add(tmp);
					src.add(tmp);
					testin[(i++)%attN] = tmp;
				}
					
				while (sinScanner.hasNext()){
					ArrayList<Double> out = new ArrayList<Double>();
					for (j=0;(j<outN)&&(sinScanner.hasNext());++j){
						tmp = sinScanner.nextDouble();
						out.add(tmp);
						src.add(tmp);
						testin[(i++)%attN] = tmp;
					}
						
					if (j<outN) break;
					samin.add(in);
					samout.add(out);
					in = new ArrayList<Double>();
					in.addAll(out);
				}
				if(samin.size()!=samout.size())
					try {
						throw new Exception("wrong samin/out");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				samN  = samin.size();
				return train(attN, hidN, outN, samN, samin, samout, testin);
				/*double samin[][]=new double[samN][attN];
				double samout[][]=new double[samN][outN];
				for(int i=0;i<samN;++i)
				{
					for(int j=0;j<attN;++j)
					{
						samin[i][j]=sinScanner.nextDouble();
					}
					for(int j=0;j<outN;++j)
					{
						samout[i][j]=sinScanner.nextDouble();					
					}
				}*/
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
			// TODO: handle exception
		}
		return null;
	}
			
	public static double[] train(int attN, int hidN, int outN, int samN, ArrayList<ArrayList<Double>> samin, ArrayList<ArrayList<Double>> samout, double[] testin)
	{
		System.out.println(attN+" "+outN+" "+hidN+" "+samN);
			int times=10000;
			double rate=0.5;
			BP2 bp2=new BP2(attN,outN,hidN,samN,times,rate);
			bp2.train(samin, samout);
			for(int i=0;i<hidN;++i)
			{
				for(int j=0;j<attN;++j)
					System.out.print(bp2.dw1[i][j]+" ");
				System.out.println();
			}
			for(int i=0;i<outN;++i)
			{
				for(int j=0;j<hidN;++j)
					System.out.print(bp2.dw2[i][j]+" ");
				System.out.println();
			}
			double[] res = new double[10*attN];
			int k= 0;
			for (int j =0; j< 10; j++)
			{
				//double testout[]=new double[outN];
				//double testin[]=new double[attN];
				/*Scanner testinScanner=new Scanner(System.in);
				for(int i=0;i<attN;++i)
				{
					System.out.println("\t"+testin[i]);
				}*/
				testin=bp2.getResault(testin);
				for(int i=0;i<outN;++i){
					System.out.print(testin[i]+"\n");
					res[k++] = testin[i];
				}
				System.out.println();
			}
			return res;		
	}
}
class BP2//包含一个隐含层的神经网络
{
	double dw1[][],dw2[][];
	int hidN;//隐含层单元个数
	int samN;//学习样例个数
	int attN;//输入单元个数
	int outN;//输出单元个数
	int times;//迭代次数
	double rate;//学习速率
	boolean trained=false;//保证在得结果前，先训练
	BP2(int attN,int outN,int hidN,int samN,int times,double rate)
	{
		this.attN=attN;
		this.outN=outN;
		this.hidN=hidN;
		this.samN=samN;
		dw1=new double[hidN][attN+1];//每行最后一个是阈值w0
		for(int i=0;i<hidN;++i)//每行代表所有输入到i隐藏单元的权值
		{			
			for(int j=0;j<=attN;++j)
				dw1[i][j]=Math.random()/2;
		}
		dw2=new double[outN][hidN+1];//输出层权值,每行最后一个是阈值w0
		for(int i=0;i<outN;++i)//每行代表所有隐藏单元到i输出单元的权值
		{			
			for(int j=0;j<=hidN;++j)
				dw2[i][j]=Math.random()/2;
		}
		this.times=times;
		this.rate=rate;
	}
	public void train(ArrayList<ArrayList<Double>> samin,ArrayList<ArrayList<Double>> samout)
	{
		double dis=0;//总体误差
		int count=times;
		double temphid[]=new double[hidN];
		double tempout[]=new double[outN];
		double wcout[]=new double[outN];
		double wchid[]=new double[hidN];
		while((count--)>0)//迭代训练
		{
			dis=0;
			for(int i=0;i<samN;++i)//遍历每个样例 samin[i]
			{
				for(int j=0;j<hidN;++j)//计算每个隐含层单元的结果
				{
					temphid[j]=0;
					for(int k=0;k<attN;++k)
						temphid[j]+=dw1[j][k]*samin.get(i).get(k); //[i][k];
					temphid[j]+=dw1[j][attN];//计算阈值产生的隐含层结果
					temphid[j]=1.0/(1+Math.exp(-temphid[j] ));
				}
				for(int j=0;j<outN;++j)//计算每个输出层单元的结果
				{
					tempout[j]=0;
					for(int k=0;k<hidN;++k)
						tempout[j]+=dw2[j][k]*temphid[k];
					tempout[j]+=dw2[j][hidN];//计算阈值产生的输出结果
					tempout[j]=1.0/(1+Math.exp( -tempout[j] ));
				}
				//计算每个输出单元的误差项
				
				for(int j=0;j<outN;++j)
				{
					double tmp = samout.get(i).get(j);
					wcout[j]=tempout[j]*(1-tempout[j])*(tmp -tempout[j]); //[i][j]
					dis+=Math.pow((tmp-tempout[j]),2);
				}
				//计算每个隐藏单元的误差项
				
				for(int j=0;j<hidN;++j)
				{
					double wche=0;
					for(int k=0;k<outN;++k)//计算输出项误差和
					{
						wche+=wcout[k]*dw2[k][j];
					}
					wchid[j]=temphid[j]*(1-temphid[j])*wche;
				}
				//改变输出层的权值
				for(int j=0;j<outN;++j)
				{
					for(int k=0;k<hidN;++k)
					{
						dw2[j][k]+=rate*wcout[j]*temphid[k];
					}
					dw2[j][hidN]=rate*wcout[j];
				}
				//改变隐含层的权值
				for(int j=0;j<hidN;++j)
				{
					for(int k=0;k<attN;++k)
					{
						dw1[j][k]+=rate*wchid[j]*samin.get(i).get(k);
					}
					dw1[j][attN]=rate*wchid[j];
				}

			}
			if(dis<0.003)
				break;
		}
		trained=true;
	}
	
	public double[] getResault(double samin[])
	{
		double temphid[]=new double[hidN];
		double tempout[]=new double[outN];
		if(trained==false)
			return null;

		for(int j=0;j<hidN;++j)//计算每个隐含层单元的结果
		{
			temphid[j]=0;
			for(int k=0;k<attN;++k)
				temphid[j]+=dw1[j][k]*samin[k];
			temphid[j]+=dw1[j][attN];//计算阈值产生的隐含层结果
			temphid[j]=1.0/(1+Math.exp(-temphid[j] ));
		}
		for(int j=0;j<outN;++j)//计算每个输出层单元的结果
		{
			tempout[j]=0;
			for(int k=0;k<hidN;++k)
				tempout[j]+=dw2[j][k]*temphid[k];
			tempout[j]+=dw2[j][hidN];//计算阈值产生的输出结果
			tempout[j]=1.0/(1+Math.exp( -tempout[j]));			
			//System.out.print(tempout[j]+" ");			
		}
		return tempout;		
	}
}

