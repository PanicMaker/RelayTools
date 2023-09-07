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
		if("ֹͣ".equals(com)){
			/*	//�̳߳ز�������ֹͣ  ��Ҫ�ô���ѭ���ķ���
			 * if(pool==null){
				JOptionPane.showMessageDialog(
						mainView,
		                "δ��������",
		                "��ʾ��Ϣ",
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
		                "δ��������",
		                "��ʾ��Ϣ",
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
			/*mainView.getJbstart().setText("����");
			mainView.getJbstart().setEnabled(true);*/
		}else if("���".equals(com)){
			addPingInfo(mainView);
		}else if("ɾ��".equals(com)){
			deletPingInfo(mainView);
		}else{
			//��ʼ��stop flagΪfalse
			TCPClinetThread.setStop(false);
			//��ʾ����
			List<PingInfo> list = mainView.getList();
			for(PingInfo p:list){
				p.getJerror().setText(""); 			//��մ�����Ϣ
			}
			if(checkdata()){		//checkֵ��û�����⣬����ֵ��װզ������
				mainView.getJbstart().setText("������");
				mainView.getJbstart().setEnabled(false);
				//������е�ping��Ϣ
				//List<PingInfo> list=mainView.getList();
				//����ͨ����Ϣ���з��࣬һ��ͨ��һ���̣߳����ڶ���߳�ͬʱ�ٿ�ͨ��ʱ�����ͨ��Ƶ���л����������� Ŀǰ�̵���ֻ��4��ͨ��
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
				//ѭ��ͨ�����������Ϊ0 ��ʾû�и�ͨ������Ϣ
				for(int i=0;i<arraylist.length;i++){		
					if(arraylist[i]==null){			//������ͨ��Ϊnull ��ʾû�и�ͨ����ping ���� �������п���ͨ�����߳�
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
						mainView.getJbstart().setText("����");
						mainView.getJbstart().setEnabled(true);
						
					};
				}.start();
				/*
				mainView.getJbstart().setText("����");
				mainView.getJbstart().setEnabled(true);
				*/
				
				//new TCPClinetThread(ui,jtfr,jdip,ip,td,12345,dtime,wtime,ddcount,jbt).start();
			}else{
				JOptionPane.showMessageDialog(
						mainView,
		                "�������ݴ�����ߴ��ڿ���δ����",
		                "���󾯸�",
		                JOptionPane.ERROR_MESSAGE
		        );
			}
		}
	}


	public boolean checkdata(){
		//�жϼ̵���ip�Ƿ�Ϊ��
		String jdqip="";
		try{
			//�̵���IP
			jdqip=mainView.getJdqIp().getText();
			if("".equals(jdqip.trim())){
				throw new RuntimeException("δ���ü̵���ip");
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		List<PingInfo> list = mainView.getList();
		//ѭ��list,��checkdata�Ƿ���OK��
		for(PingInfo pf:list){
			try{
				//ping ��ip����Ϊ��
				if("".equals(pf.getJpingIp().getText().trim())){
					throw new RuntimeException("ipΪ��");
				}
				pf.setIp(pf.getJpingIp().getText().trim());
				
				//�豸������ʱ�䲻��Ϊ��
				if("".equals(pf.getJwtime().getText().trim())){
					throw new RuntimeException("�ȴ�����ʱ��Ϊ��");
				}
				pf.setWtime(Integer.valueOf(pf.getJwtime().getText()));				
				
				//�豸�����Ĵ�������Ϊ��
				if("".equals(pf.getJcycles().getText().trim())){
					throw new RuntimeException("��������δָ��");
				}
				pf.setCycles(Integer.valueOf(pf.getJcycles().getText()));
				
				//�ӳ�ͨ��ʱ�䲻��Ϊ��
				if("".equals(pf.getJtdtime().getText().trim())){
					throw new RuntimeException("δ�����ӳ�����ʱ��");
				}
				pf.setTdtime(Integer.valueOf(pf.getJtdtime().getText()));
				
				//�̵���ͨ������Ϊ��
				if("".equals(pf.getJjdqtd().getText().trim())||Integer.valueOf(pf.getJjdqtd().getText().trim())>4){
					throw new RuntimeException("δ����ͨ������ͨ������");
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
		
		mv.initJFrame(mv);		//��Ҫ���³�ʼ��jfm
		 */	
		List<PingInfo> list = mv.getList();
		int startnum=list.size();
		for(int i=list.size()-1;i>=0;i--){
			if(list.get(i).getJping().isSelected()){	//�����ѡ����ô��ɾ��
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
		
		//���ñ��� 
		JLabel jlabel1=new JLabel("�豸��ַ");
		JLabel jlabel2=new JLabel("�ȴ�����(s)");
		JLabel jlabel3=new JLabel("��������");
		JLabel jlabel4=new JLabel("�ӳ�ͨ��(s)");
		JLabel jlabel5=new JLabel("ͨ��ѡ��");
		JLabel jlabel6=new JLabel("�������");
		
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
				//������Ϣ
				//ping�豸 IP��ַ
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
				
				//�豸������ʱ��
				JTextField wtime=new JTextField();
				wtime.setBounds(150,hight, 70, 20);
				wtime.setText("30");
				wtime.setBorder(BorderFactory.createEmptyBorder());
				
				//��������
				JTextField cycles=new JTextField();
				cycles.setBounds(230,hight, 60, 20);
				cycles.setText("5");
				cycles.setBorder(BorderFactory.createEmptyBorder());

				//�ϵ��ͨ��ʱ��
				JTextField tdtime=new JTextField();
				tdtime.setBounds(300, hight, 70, 20);
				tdtime.setText("20");
				tdtime.setBorder(BorderFactory.createEmptyBorder());

				//�̵���ͨ��ѡ��
				JTextField jdqtd=new JTextField();
				jdqtd.setBounds(380,hight, 60, 20);
				jdqtd.setText("1");
				jdqtd.setBorder(BorderFactory.createEmptyBorder());
				
				//�����ʾ
				JTextField result=new JTextField();
				result.setBounds(450,hight, 60, 20);
				result.setEditable(false);
				result.setText("");
				result.setBorder(BorderFactory.createEmptyBorder());
				
				//�쳣��Ϣ��������
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
				
				mv.initJFrame(mv);		//��Ҫ���³�ʼ��jfm
	}
}
