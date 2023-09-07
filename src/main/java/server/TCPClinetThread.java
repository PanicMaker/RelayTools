package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import gui.MainView;
import model.PingInfo;

public class TCPClinetThread extends Thread {
	private ArrayList<PingInfo> arraylist;
	private MainView mainView;
	private volatile static boolean stop;
	private ConcurrentLinkedQueue<TCPClinetThread> cc;
	/**
	 * 用flag来表示同一个通道下不同线程的ping结果，true表示都平通了,flase表示未ping通
	 */
	private Boolean flag=true;
/*	private JTextField jtfr;	//反馈显示成功次数
	private String jdqip;		//继电器ip
	private String ip;			//设备ip
	private int td;
	private int port;
	private int dtime;			//
	private int wtime;
	private String tdmsg ;		//通电指令
	private String ddmsg ;
	private int count=0;		//表示ping通次数
	private int defaultping=10;
	private int ddcount;		//表示断电重启次数
	private JButton jbt;*/
	
	
	public TCPClinetThread() {
		super();
	}

	public TCPClinetThread(ArrayList<PingInfo> arraylist, MainView mainView,ConcurrentLinkedQueue<TCPClinetThread> cc) {
		super();
		this.arraylist = arraylist;
		this.mainView =mainView;
		this.cc=cc;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	
	public static boolean isStop() {
		return stop;
	}

	public static void setStop(boolean stop) {
		TCPClinetThread.stop = stop;
	}

	@Override
	public void run() {
		//对所有该通道的指定的的ping 任务进行分析 重启的次数为所有任务重启次数的最大值
		int cqcount=arraylist.get(0).getCycles();
		for(int i=1;i<arraylist.size();i++){
			if(cqcount<arraylist.get(i).getCycles()){
				cqcount=arraylist.get(i).getCycles();
			}
		}
		
		Socket clientSocket=null;
		//创建个线程池用来对每个设备进行ping启动
		ExecutorService pool=Executors.newFixedThreadPool(20);
		//创建一个count变量用来表示重启次数
		
		try{
			int count=0;
			big: while(cqcount>0){		//大循环 表示断电重启次数
				cqcount--;
				if(stop){				//当按下stop后就在下次循环之前跳出循环
					break big;
				}
				//初始化ddmsg(断电指令)和tdmsg(通电指令)
				String ddmsg="AT+STACH"+arraylist.get(0).getTd()+"=0\r\n";
				String tdmsg="AT+STACH"+arraylist.get(0).getTd()+"=1\r\n";
				
				//创建socket链接继电器
				clientSocket=new Socket(InetAddress.getByName(arraylist.get(0).getJdqip()),12345);
				//获得输出流 由client 端 输出到服务器端 包装成打印流
				PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
				//获得输如流 收取反馈结构
				BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				//启动时发送断电指令
				pw.print(ddmsg);
				pw.flush();
				
				String fkmsg=br.readLine();
				//System.out.println(fkmsg);
				
				//延迟时间通知通电(同一个通道使用第一个延迟通电时间)
				try {
					Thread.currentThread().sleep(arraylist.get(0).getTdtime()*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//通知通电
				pw.print(tdmsg);
				pw.flush();
				
				fkmsg=br.readLine();
				//System.out.println(fkmsg);
				
				//ConcurrentHashMap<K, V>		用来阻塞线程的判断值
				ConcurrentLinkedQueue<PingInfo> clq=new ConcurrentLinkedQueue<PingInfo>();
				
				//等待时间后开始ping 这里由于不同的设备同时ping 采用多线程来完成
				for(PingInfo pf:arraylist){			//循环设备，每个设备都有指定的循环次数，如果超过次数 就不再添加线程ping了
					if(count>=pf.getCycles()){
						continue;
					}else{
						Future<Object> f = pool.submit(new PingThread(pf,count,this,clq));
					}
				}
				
				//要阻塞本线程，等线程池的内容处理完成后继续运行
				while(true){
					Thread.currentThread().sleep(1000);
					if(clq.size()==arraylist.size()){
						break;
					}
				}
				//设置一个flag 来表示ping线程是否成功,false 表示未ping通，true表示ping通了
				if(!flag){		//未ping通时跳出循环
					break big;
				}
				//如果没有break 表示重启成功 count++;
				count++;
				try {
						clientSocket.close();
				} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			cc.add(this);
		}
		/*
		if(flag){		//循环完成发现flag还是为true 表示没有异常发生
			JOptionPane.showMessageDialog(
	                mainView,
	                "循环完成",
	                "完成",
	                JOptionPane.DEFAULT_OPTION
	        );
		}
		mainView.getJbstart().setText("启动");
		mainView.getJbstart().setEnabled(true);
		*/
		
	}
	
   
}
