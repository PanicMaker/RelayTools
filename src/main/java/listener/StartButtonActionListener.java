package listener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import gui.MainView;
import model.PingInfo;
import server.TCPClinetThread;

public class StartButtonActionListener implements ActionListener {

	private MainView mainView;

	private ExecutorService pool;
	


	public StartButtonActionListener() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public StartButtonActionListener(MainView mainView) {
		super();
		this.mainView = mainView;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String com=e.getActionCommand();
		if("停止".equals(com)){
			/*	//线程池不能立马停止  需要用打破循环的方法
			 * if(pool==null){
				JOptionPane.showMessageDialog(
						mainView,
		                "未启动程序",
		                "提示消息",
		                JOptionPane.DEFAULT_OPTION
		        );
			}else{
				try {
					pool.shutdownNow();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}*/
			
			if(pool==null){
				JOptionPane.showMessageDialog(
						mainView,
		                "未启动程序",
		                "提示消息",
		                JOptionPane.DEFAULT_OPTION
		        );
			}else{
				try {
					TCPClinetThread.setStop(true);
					pool.shutdownNow();
					pool=null;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			/*mainView.getJbstart().setText("启动");
			mainView.getJbstart().setEnabled(true);*/
		}else if("添加".equals(com)){
			addPingInfo(mainView);
		}else if("删除".equals(com)){
			deletPingInfo(mainView);
		}else{
			//初始化stop flag为false
			TCPClinetThread.setStop(false);
			//表示启动
			List<PingInfo> list = mainView.getList();
			for(PingInfo p:list){
				p.getJerror().setText(""); 			//清空错误信息
			}
			if(checkdata()){		//check值有没有问题，并将值封装咋对象中
				mainView.getJbstart().setText("运行中");
				mainView.getJbstart().setEnabled(false);
				//获得所有的ping信息
				//List<PingInfo> list=mainView.getList();
				//根据通道信息进行分类，一个通道一个线程（由于多个线程同时操控通道时会造成通道频繁切换，产生错误） 目前继电器只有4个通道
				ArrayList<PingInfo> [] arraylist=new ArrayList[4];
				for(PingInfo pf:list){
					if(pf.getTd()<=0||pf.getTd()>4){
						return;
					}
					if(arraylist[pf.getTd()-1]==null){
						arraylist[pf.getTd()-1]=new ArrayList<PingInfo>();
					}
					arraylist[pf.getTd()-1].add(pf);
				}
				
				pool=Executors.newFixedThreadPool(5);
				ConcurrentLinkedQueue<TCPClinetThread> clq=new ConcurrentLinkedQueue<TCPClinetThread>();
				//循环通道，如果长度为0 表示没有改通道的信息
				for(int i=0;i<arraylist.length;i++){		
					if(arraylist[i]==null){			//如果这个通道为null 表示没有该通道的ping 任务 即不进行开该通道的线程
						clq.add(new TCPClinetThread());
						continue;
					}
					pool.execute(new TCPClinetThread(arraylist[i],mainView,clq));
				}
				new Thread(){
					public void run() {
						try{
							while(true){
								Thread.currentThread().sleep(2000);
								if(clq.size()==4){
									break;
								}
							}
						}catch(Exception e1){
							
						}
						mainView.getJbstart().setText("启动");
						mainView.getJbstart().setEnabled(true);
						
					};
				}.start();
				/*
				mainView.getJbstart().setText("启动");
				mainView.getJbstart().setEnabled(true);
				*/
				
				//new TCPClinetThread(ui,jtfr,jdip,ip,td,12345,dtime,wtime,ddcount,jbt).start();
			}else{
				JOptionPane.showMessageDialog(
						mainView,
		                "输入内容错误或者存在空域未输入",
		                "错误警告",
		                JOptionPane.ERROR_MESSAGE
		        );
			}
		}
	}


	public boolean checkdata(){
		//判断继电器ip是否为空
		String jdqip="";
		try{
			//继电器IP
			jdqip=mainView.getJdqIp().getText();
			if("".equals(jdqip.trim())){
				throw new RuntimeException("未设置继电器ip");
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		List<PingInfo> list = mainView.getList();
		//循环list,来checkdata是否是OK的
		for(PingInfo pf:list){
			try{
				//ping 的ip不能为空
				if("".equals(pf.getJpingIp().getText().trim())){
					throw new RuntimeException("ip为空");
				}
				pf.setIp(pf.getJpingIp().getText().trim());
				
				//设备重启的时间不能为空
				if("".equals(pf.getJwtime().getText().trim())){
					throw new RuntimeException("等待重启时间为空");
				}
				pf.setWtime(Integer.valueOf(pf.getJwtime().getText()));				
				
				//设备重启的次数不能为空
				if("".equals(pf.getJcycles().getText().trim())){
					throw new RuntimeException("重启次数未指定");
				}
				pf.setCycles(Integer.valueOf(pf.getJcycles().getText()));
				
				//延迟通电时间不能为空
				if("".equals(pf.getJtdtime().getText().trim())){
					throw new RuntimeException("未设置延迟重启时间");
				}
				pf.setTdtime(Integer.valueOf(pf.getJtdtime().getText()));
				
				//继电器通道不能为空
				if("".equals(pf.getJjdqtd().getText().trim())||Integer.valueOf(pf.getJjdqtd().getText().trim())>4){
					throw new RuntimeException("未设置通道或者通道错误");
				}
				pf.setTd(Integer.valueOf(pf.getJjdqtd().getText()));
				
				pf.setJdqip(jdqip);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			
		}

		return true;
	}
	public void deletPingInfo(MainView mv){
		/*List<PingInfo> list = mv.getList();
		mv.setPingHeight(mv.getPingHeight()-30);
		if(list.size()>0){
			mv.getJps().remove(list.get(list.size()-1).getJping());
			mv.getJps().remove(list.get(list.size()-1).getJpingIp());
			mv.getJps().remove(list.get(list.size()-1).getJwtime());
			mv.getJps().remove(list.get(list.size()-1).getJcycles());
			mv.getJps().remove(list.get(list.size()-1).getJtdtime());
			mv.getJps().remove(list.get(list.size()-1).getJjdqtd());
			mv.getJps().remove(list.get(list.size()-1).getJresult());
			mv.getJps().remove(list.get(list.size()-1).getJerror());
			list.remove(list.size()-1);	
		}
		
		mv.initJFrame(mv);		//需要重新初始化jfm
		 */	
		List<PingInfo> list = mv.getList();
		int startnum=list.size();
		for(int i=list.size()-1;i>=0;i--){
			if(list.get(i).getJping().isSelected()){	//如果被选择那么旧删除
				list.remove(i);
			}
		}
		if(startnum==list.size()){
			return;
		}
		
		resetPingInfo(mv,list,mv.getJps().getPreferredSize().height-(startnum-list.size())*30);
		
	}
	public void resetPingInfo(MainView mv,List<PingInfo> list,int height){
		mv.getJps().removeAll();
		mv.setPingHeight(20);
		JPanel jps = mv.getJps();
		int hight=0;
		
		//设置标题 
		JLabel jlabel1=new JLabel("设备地址");
		JLabel jlabel2=new JLabel("等待重启(s)");
		JLabel jlabel3=new JLabel("重启次数");
		JLabel jlabel4=new JLabel("延迟通电(s)");
		JLabel jlabel5=new JLabel("通道选择");
		JLabel jlabel6=new JLabel("结果反馈");
		
		jlabel1.setBounds(30,0,110, 20);
		jlabel1.setHorizontalAlignment(SwingConstants.CENTER);
		
		jlabel2.setBounds(150,0, 70, 20);
		jlabel2.setHorizontalAlignment(SwingConstants.CENTER);
		
		jlabel3.setBounds(230,0, 60, 20);
		jlabel3.setHorizontalAlignment(SwingConstants.CENTER);
		
		jlabel4.setBounds(300, 0, 70, 20);
		jlabel4.setHorizontalAlignment(SwingConstants.CENTER);
		
		jlabel5.setBounds(380,0, 60, 20);
		jlabel5.setHorizontalAlignment(SwingConstants.CENTER);
		
		jlabel6.setBounds(450,0, 60, 20);
		jlabel6.setHorizontalAlignment(SwingConstants.CENTER);
		
		jps.add(jlabel1);
		jps.add(jlabel2);
		jps.add(jlabel3);
		jps.add(jlabel4);
		jps.add(jlabel5);
		jps.add(jlabel6);
		
		
		for(PingInfo pi:list){
			hight= mv.getPingHeight();

			pi.getJping().setBounds(5,hight+2, 25, 15);
			jps.add(pi.getJping());
			
			pi.getJpingIp().setBounds(30,hight,110, 20);
			jps.add(pi.getJpingIp());
			
			pi.getJwtime().setBounds(150,hight, 70, 20);
			jps.add(pi.getJwtime());
			
			pi.getJcycles().setBounds(230,hight, 60, 20);
			jps.add(pi.getJcycles());
			
			pi.getJtdtime().setBounds(300, hight, 70, 20);
			jps.add(pi.getJtdtime());
			
			pi.getJjdqtd().setBounds(380,hight, 60, 20);
			jps.add(pi.getJjdqtd());
			
			pi.getJresult().setBounds(450,hight, 60, 20);
			jps.add(pi.getJresult());
			
			pi.getJerror().setBounds(515,hight, 70, 20);
			jps.add(pi.getJerror());
			
			hight+=30;
			mv.setPingHeight(hight);
		}
		mv.setPingHeight(mv.getPingHeight()-30);
		jps.setPreferredSize(new Dimension(585,height));
		mv.getJscrollPane().setViewportView(jps);
		mv.initJFrame(mv);	
	}
	public void addPingInfo(MainView mv){
				int hight = mv.getPingHeight();
				hight+=30;
				mv.setPingHeight(hight);
				//设置信息
				//ping设备 IP地址
				/*JLabel jping=new JLabel("PING");
				jping.setBounds(0,hight, 30, 20);*/
				JCheckBox jping=new JCheckBox();
				//jping.setBounds(0,hight, 30, 20);
				jping.setBounds(5,hight+2, 25, 15);
				jping.setBackground(new Color(180,180,180,255) );
				
				JTextField pingIp=new JTextField();
				pingIp.setBounds(30,hight,110, 20);
				pingIp.setText("192.168.100.88");	
				pingIp.setBorder(BorderFactory.createEmptyBorder());
				
				//设备重启的时间
				JTextField wtime=new JTextField();
				wtime.setBounds(150,hight, 70, 20);
				wtime.setText("30");
				wtime.setBorder(BorderFactory.createEmptyBorder());
				
				//重启次数
				JTextField cycles=new JTextField();
				cycles.setBounds(230,hight, 60, 20);
				cycles.setText("5");
				cycles.setBorder(BorderFactory.createEmptyBorder());

				//断电后到通电时间
				JTextField tdtime=new JTextField();
				tdtime.setBounds(300, hight, 70, 20);
				tdtime.setText("20");
				tdtime.setBorder(BorderFactory.createEmptyBorder());

				//继电器通道选择
				JTextField jdqtd=new JTextField();
				jdqtd.setBounds(380,hight, 60, 20);
				jdqtd.setText("1");
				jdqtd.setBorder(BorderFactory.createEmptyBorder());
				
				//结果显示
				JTextField result=new JTextField();
				result.setBounds(450,hight, 60, 20);
				result.setEditable(false);
				result.setText("");
				result.setBorder(BorderFactory.createEmptyBorder());
				
				//异常信息反馈窗口
				JLabel error=new JLabel("");
				error.setBounds(515,hight, 70, 20);
				error.setBorder(BorderFactory.createEmptyBorder());
				
				JPanel jps = mv.getJps();
				jps.add(jping);
				jps.add(pingIp);
				jps.add(wtime);
				jps.add(cycles);
				jps.add(tdtime);
				jps.add(jdqtd);
				jps.add(result);
				jps.add(error);
				
				jps.setPreferredSize(new Dimension(585,jps.getPreferredSize().height+30));
				mv.getJscrollPane().setViewportView(jps);
				mv.getList().add(new PingInfo(jping, pingIp, wtime, cycles, tdtime, jdqtd, result, error));
				
				mv.initJFrame(mv);		//需要重新初始化jfm
	}
}
