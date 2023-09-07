package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import listener.StartButtonActionListener;
import model.PingInfo;

public class MainView extends JFrame {
	private int width=658;
	private int height=428;
	private int pingHeight=20;
	private JScrollPane jscrollPane;
	private JPanel jps;
	//private static String image="/image/aobo.png";
	/**
	 * ����ָ�����Ӽ̵�����IP��ַ
	 */
	private JTextField jdqIp;
	/**
	 * ping����Ϣ
	 */
	private List<PingInfo> list=new ArrayList<PingInfo>();
	
	private JButton jbstart;
	private JButton jbstop;
	private JButton jbadd;
	private JButton jbdet;
	
	public static void main(String[] args) {
		new MainView();
	}
	private MainView()  {

		initContentPanel(this);
		initJFrame(this);
	}
	
	
	public void initContentPanel(MainView mainView){
		//mainView.setUndecorated(true);
		Container con = mainView.getContentPane();
		con.setLayout(null);	
		//con.setBackground(new Color(120,120,120,255) );
		//����panel���
		jscrollPane=new JScrollPane();
		jscrollPane.setBounds(0,0, 656, 355);
		jscrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		jps=new JPanel();
		jps.setPreferredSize(new Dimension(585, 50));
		jps.setBackground(new Color(180,180,180,255) );
		//jps.setBackground(new Color(18,18,18,255) );
		//jps.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		//jps.setSize(590,230);
		jps.setLayout(null);
		jscrollPane.setViewportView(jps);
		
		//���ñ��� 
		//JLabel jlabel=new JLabel("ѡ��");
		JLabel jlabel1=new JLabel("�豸��ַ");
		JLabel jlabel2=new JLabel("�ȴ�����(s)");
		JLabel jlabel3=new JLabel("��������");
		JLabel jlabel4=new JLabel("�ӳ�ͨ��(s)");
		JLabel jlabel5=new JLabel("ͨ��ѡ��");
		JLabel jlabel6=new JLabel("�������");
		
		/*jlabel.setBounds(5,0,25, 20);
		jlabel.setHorizontalAlignment(SwingConstants.CENTER);*/
		
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
		
		//jps.add(jlabel);
		jps.add(jlabel1);
		jps.add(jlabel2);
		jps.add(jlabel3);
		jps.add(jlabel4);
		jps.add(jlabel5);
		jps.add(jlabel6);
		
		//������Ϣ
		//ping�豸 IP��ַ
		/*JLabel jping=new JLabel("PING");
		jping.setBounds(0,20, 30, 20);*/
		JCheckBox jping=new JCheckBox();
		jping.setBounds(5,22, 25, 15);
		jping.setBackground(new Color(180,180,180,255) );
		
		
		JTextField pingIp=new JTextField();
		pingIp.setBounds(30,20,110, 20);
		pingIp.setText("192.168.100.88");
		pingIp.setBorder(BorderFactory.createEmptyBorder());
		
		//�豸������ʱ��
		JTextField wtime=new JTextField();
		wtime.setBounds(150,20, 70, 20);
		wtime.setText("30");
		wtime.setBorder(BorderFactory.createEmptyBorder());
		
		//��������
		JTextField cycles=new JTextField();
		cycles.setBounds(230,20, 60, 20);
		cycles.setText("5");
		cycles.setBorder(BorderFactory.createEmptyBorder());

		//�ϵ��ͨ��ʱ��
		JTextField tdtime=new JTextField();
		tdtime.setBounds(300, 20, 70, 20);
		tdtime.setText("20");
		tdtime.setBorder(BorderFactory.createEmptyBorder());

		//�̵���ͨ��ѡ��
		JTextField jdqtd=new JTextField();
		jdqtd.setBounds(380,20, 60, 20);
		jdqtd.setText("1");
		jdqtd.setBorder(BorderFactory.createEmptyBorder());
		
		//�����ʾ
		JTextField result=new JTextField();
		result.setBounds(450,20, 60, 20);
		result.setEditable(false);
		result.setText("");
		result.setBorder(BorderFactory.createEmptyBorder());
		
		//�쳣��Ϣ��������
		JLabel error=new JLabel("");
		error.setBounds(515,20, 70, 20);
		error.setBorder(BorderFactory.createEmptyBorder());
		
		jps.add(jping);
		jps.add(pingIp);
		jps.add(wtime);
		jps.add(cycles);
		jps.add(tdtime);
		jps.add(jdqtd);
		jps.add(result);
		jps.add(error);
		
		con.add(jscrollPane);

		
		//���ü̵���ip��ַ
		JLabel jlabel7=new JLabel("�̵�����ַ");
		jlabel7.setBounds(40,368, 70, 20);
		jdqIp=new JTextField();
		jdqIp.setBounds(110,368, 120, 20);
		jdqIp.setText("10.10.10.113");
		jdqIp.setBorder(BorderFactory.createEmptyBorder());
		con.add(jlabel7);
		con.add(jdqIp);
		
		//��ʼ����ť
		jbstart=new JButton("����");
		jbstart.setBounds(260, 362, 80, 30);
		con.add(jbstart);
		
		jbstop=new JButton("ֹͣ");
		jbstop.setBounds(350, 362, 80, 30);
		con.add(jbstop);
		
		jbadd=new JButton("���");
		jbadd.setBounds(440, 362, 80, 30);
		con.add(jbadd);

		jbdet=new JButton("ɾ��");
		jbdet.setBounds(530, 362, 80, 30);
		con.add(jbdet);
		//�����е�text�������������
		list.add(new PingInfo(jping, pingIp, wtime, cycles, tdtime, jdqtd, result, error));
		
		//��strat/stop/pause��ť����¼�������
		StartButtonActionListener listener = new StartButtonActionListener(mainView);
		jbstart.addActionListener(listener);
		jbstop.addActionListener(listener);
		jbadd.addActionListener(listener);
		jbdet.addActionListener(listener);
	}
	
	
	public void initJFrame(JFrame jframe){
		/*ImageIcon icon=new ImageIcon(jframe.getClass().getResource(image));
		jframe.setIconImage(icon.getImage());*/
		
		jframe.setTitle("����������С����");
		jframe.setSize(width,height);
		jframe.setResizable(false);
		jframe.setVisible(true);
		jframe.setLocationRelativeTo(jframe.getOwner());
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public JTextField getJdqIp() {
		return jdqIp;
	}
	public void setJdqIp(JTextField jdqIp) {
		this.jdqIp = jdqIp;
	}
	public List<PingInfo> getList() {
		return list;
	}
	public void setList(List<PingInfo> list) {
		this.list = list;
	}
	public JButton getJbstart() {
		return jbstart;
	}
	public void setJbstart(JButton jbstart) {
		this.jbstart = jbstart;
	}
	public JButton getJbstop() {
		return jbstop;
	}
	public void setJbstop(JButton jbstop) {
		this.jbstop = jbstop;
	}
	public JButton getJbadd() {
		return jbadd;
	}
	public void setJbadd(JButton jbadd) {
		this.jbadd = jbadd;
	}
	public int getPingHeight() {
		return pingHeight;
	}
	public void setPingHeight(int pingHeight) {
		this.pingHeight = pingHeight;
	}
	public JButton getJbdet() {
		return jbdet;
	}
	public void setJbdet(JButton jbdet) {
		this.jbdet = jbdet;
	}
	public JScrollPane getJscrollPane() {
		return jscrollPane;
	}
	public void setJscrollPane(JScrollPane jscrollPane) {
		this.jscrollPane = jscrollPane;
	}
	public JPanel getJps() {
		return jps;
	}
	public void setJps(JPanel jps) {
		this.jps = jps;
	}
	
	
	
}
