package gui;

import java.awt.*;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class JScrollPaneTest extends JFrame {
	public JScrollPaneTest() {
		this.setLayout(null);
		JPanel jp = new JPanel();
		jp.setPreferredSize(new Dimension(400, 320));
		JScrollPane sp = new JScrollPane();
		sp.setViewportView(jp);
		this.setBounds(10, 10, 660, 560);
		sp.setBounds(10, 10, 580, 400);
		this.getContentPane().add(sp);
		
		JButton bu = new JButton("Ìí¼Ó");
		bu.setBounds(300, 460, 80, 30);
		bu.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if("Ìí¼Ó".equalsIgnoreCase(e.getActionCommand())){
					int si = jp.getPreferredSize().height;
					jp.setPreferredSize(new Dimension(400,si+30));
					sp.setViewportView(jp);
				}
			}
			
		});
		this.getContentPane().add(bu);
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

	}
	public static void main(String[] args) {
		new JScrollPaneTest();
	}
}
