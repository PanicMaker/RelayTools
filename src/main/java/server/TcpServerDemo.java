package server;

import java.io.*;
import java.net.*;

public class TcpServerDemo {

	private BufferedReader reader;
	private BufferedWriter writer;
	private ServerSocket server;
	private Socket client;
	//�����ʹ�õĶ˿�
	private int serverPort = 6000;

	public void TcpServerDemo() {

		try {
			System.out.println("������Ϊ�����");
			System.out.println("�����˿ڣ�" + serverPort);
			server = new ServerSocket(serverPort);

			System.out.println("�ȴ��ͻ���������");
			client = server.accept();
			System.out.println("���ӳɹ�");
			String clientAddr = client.getRemoteSocketAddress().toString().substring(1);//��Ҫ����һ��ϲ�
			String[] clientAddrs = clientAddr.split(":");
			System.out.println("�ͻ���IP��" + clientAddrs[0] + " �˿ڣ�" + clientAddrs[1]);

			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

			controllerMessage();

			System.out.println("�������ָ�����ϸ��������ġ�TCP-KP������̵���ATָ���.PDF��");
            System.out.println("��Demo�����ڿ����ο�������ֱ��������ҵ��Ŀʵʩ��");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}


	private void controllerMessage() {

		try {
			System.out.println("TCP-KPϵ������̵������ܲ��ԣ�");

			System.out.println("���Ƽ̵���ͨ��1�������Ϸ��ͣ�AT+STACH1=1\\r\\n");
			String msg = "AT+STACH1=1\r\n";
			//��������
			writer.write(msg);
			writer.flush();
			//������
			msg = reader.readLine();
			msg = msg.replace("\r\n", "\\r\\n");
			System.out.println("����̵���Ӧ��" + msg);
			System.out.println("ָ��ִ�гɹ�!");
			Thread.sleep(500);
			
			//===========================================================
			System.out.println("���Ƽ̵���ͨ��1�����Ͽ����ͣ�AT+STACH1=0\\r\\n");
			msg = "AT+STACH1=0\r\n";
			//��������
			writer.write(msg);
			writer.flush();
			//������
			msg = reader.readLine();
			msg = msg.replace("\r\n", "\\r\\n");
			System.out.println("����̵���Ӧ��" + msg);
			System.out.println("ָ��ִ�гɹ�!");
			Thread.sleep(500);

			//===========================================================
			System.out.println("���Ƽ̵���ͨ��1�����ӿ�����15���Ͽ����ͣ�AT+STACH1=1,15\\r\\n");
			msg = "AT+STACH1=1,15\r\n";
			//��������
			writer.write(msg);
			writer.flush();
			//������
			msg = reader.readLine();
			msg = msg.replace("\r\n", "\\r\\n");
			System.out.println("����̵���Ӧ��" + msg);
			System.out.println("ָ��ִ�гɹ�!");
			Thread.sleep(500);

			//===========================================================
			System.out.println("��ȡ����������ӿ�1��״̬���ͣ�AT+OCCH1=?\\r\\n");
			msg = "AT+OCCH1=?\r\n";
			//��������
			writer.write(msg);
			writer.flush();
			//������
			msg = reader.readLine();
			msg = msg.replace("\r\n", "\\r\\n");
			System.out.println("����̵���Ӧ��" + msg);
			System.out.println("ָ��ִ�гɹ�!");
			Thread.sleep(500);

		}catch(Exception e) {
			e.printStackTrace();
		}

		try {
			System.out.println("�رտͻ������ӣ�");
			if(reader != null) reader.close();
			if(client != null) client.close();
			if(writer != null) writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("������ɣ�");
	}

	
	public static void main(String[] args) {
		TcpServerDemo tcp = new TcpServerDemo();
		tcp.TcpServerDemo();
	}

}