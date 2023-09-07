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
	 * ��flag����ʾͬһ��ͨ���²�ͬ�̵߳�ping�����true��ʾ��ƽͨ��,flase��ʾδpingͨ
	 */
	private Boolean flag=true;
/*	private JTextField jtfr;	//������ʾ�ɹ�����
	private String jdqip;		//�̵���ip
	private String ip;			//�豸ip
	private int td;
	private int port;
	private int dtime;			//
	private int wtime;
	private String tdmsg ;		//ͨ��ָ��
	private String ddmsg ;
	private int count=0;		//��ʾpingͨ����
	private int defaultping=10;
	private int ddcount;		//��ʾ�ϵ���������
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
		//�����и�ͨ����ָ���ĵ�ping ������з��� �����Ĵ���Ϊ���������������������ֵ
		int cqcount=arraylist.get(0).getCycles();
		for(int i=1;i<arraylist.size();i++){
			if(cqcount<arraylist.get(i).getCycles()){
				cqcount=arraylist.get(i).getCycles();
			}
		}
		
		Socket clientSocket=null;
		//�������̳߳�������ÿ���豸����ping����
		ExecutorService pool=Executors.newFixedThreadPool(20);
		//����һ��count����������ʾ��������
		
		try{
			int count=0;
			big: while(cqcount>0){		//��ѭ�� ��ʾ�ϵ���������
				cqcount--;
				if(stop){				//������stop������´�ѭ��֮ǰ����ѭ��
					break big;
				}
				//��ʼ��ddmsg(�ϵ�ָ��)��tdmsg(ͨ��ָ��)
				String ddmsg="AT+STACH"+arraylist.get(0).getTd()+"=0\r\n";
				String tdmsg="AT+STACH"+arraylist.get(0).getTd()+"=1\r\n";
				
				//����socket���Ӽ̵���
				clientSocket=new Socket(InetAddress.getByName(arraylist.get(0).getJdqip()),12345);
				//�������� ��client �� ������������� ��װ�ɴ�ӡ��
				PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
				//��������� ��ȡ�����ṹ
				BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				//����ʱ���Ͷϵ�ָ��
				pw.print(ddmsg);
				pw.flush();
				
				String fkmsg=br.readLine();
				//System.out.println(fkmsg);
				
				//�ӳ�ʱ��֪ͨͨ��(ͬһ��ͨ��ʹ�õ�һ���ӳ�ͨ��ʱ��)
				try {
					Thread.currentThread().sleep(arraylist.get(0).getTdtime()*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//֪ͨͨ��
				pw.print(tdmsg);
				pw.flush();
				
				fkmsg=br.readLine();
				//System.out.println(fkmsg);
				
				//ConcurrentHashMap<K, V>		���������̵߳��ж�ֵ
				ConcurrentLinkedQueue<PingInfo> clq=new ConcurrentLinkedQueue<PingInfo>();
				
				//�ȴ�ʱ���ʼping �������ڲ�ͬ���豸ͬʱping ���ö��߳������
				for(PingInfo pf:arraylist){			//ѭ���豸��ÿ���豸����ָ����ѭ������������������� �Ͳ�������߳�ping��
					if(count>=pf.getCycles()){
						continue;
					}else{
						Future<Object> f = pool.submit(new PingThread(pf,count,this,clq));
					}
				}
				
				//Ҫ�������̣߳����̳߳ص����ݴ�����ɺ��������
				while(true){
					Thread.currentThread().sleep(1000);
					if(clq.size()==arraylist.size()){
						break;
					}
				}
				//����һ��flag ����ʾping�߳��Ƿ�ɹ�,false ��ʾδpingͨ��true��ʾpingͨ��
				if(!flag){		//δpingͨʱ����ѭ��
					break big;
				}
				//���û��break ��ʾ�����ɹ� count++;
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
		if(flag){		//ѭ����ɷ���flag����Ϊtrue ��ʾû���쳣����
			JOptionPane.showMessageDialog(
	                mainView,
	                "ѭ�����",
	                "���",
	                JOptionPane.DEFAULT_OPTION
	        );
		}
		mainView.getJbstart().setText("����");
		mainView.getJbstart().setEnabled(true);
		*/
		
	}
	
   
}
