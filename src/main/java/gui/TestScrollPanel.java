package gui;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class TestScrollPanel extends JFrame{
	
	public TestScrollPanel(){
		initContentPanel(this);
		initJFrame(this);
	}
	private void initJFrame(TestScrollPanel jframe) {
		// TODO Auto-generated method stub
		jframe.setTitle("交换机重启小程序");
		jframe.setSize(600,500);
		jframe.setResizable(false);
		jframe.setVisible(true);
		jframe.setLocationRelativeTo(jframe.getOwner());
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	private void initContentPanel(TestScrollPanel testScrollPanel) {
		// TODO Auto-generated method stub
		Container con = testScrollPanel.getContentPane();
		con.setLayout(null);
		/*JScrollPane pen1 = new JScrollPane();
		pen1.setBounds(0, 0, 590, 490);
		pen1.setLayout(null);*/
		
		JPanel jp=new JPanel();
		jp.setBounds(0, 0, 590, 490);
		jp.setLayout(null);
		
		JScrollPane pen = new JScrollPane();
		pen.setBounds(10, 10, 580, 480);
		
		JTextPane tex = new JTextPane();
		tex.setLayout(null);
		addcom(tex);

		pen.setViewportView(tex);
		//pen.add(tex);
		jp.add(pen);
		con.add(jp);
		
	}
	public static void main(String[] args) {
		new TestScrollPanel();
		
	}
	
	public void addcom(JTextPane te){
		int i=100;
		while(i-->0){
			JButton b = new JButton("ceshi"+i);
			b.setBounds(i*10, i*10, 8, 8);
			te.add(b);
		}
		
	}
}
