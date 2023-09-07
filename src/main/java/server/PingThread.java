package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import model.PingInfo;

public class PingThread implements Callable {
	private ConcurrentLinkedQueue<PingInfo> clq;
	private PingInfo pf;
	/**
	 * Ĭ�ϵ�ping�����������ô�����δpingͨ,����Ϊ������ʧ�ܣ�����������ɹ�
	 */
	private int defaultping=5;
	/**
	 * ������������ɹ��Ĵ���
	 */
	private int count;
	private TCPClinetThread tct;
	public PingThread(PingInfo pf ,int count , TCPClinetThread tct,ConcurrentLinkedQueue<PingInfo> clq) {
		super();
		this.pf = pf;
		this.count=count;
		this.tct=tct;
		this.clq=clq;
	}


	@Override
	public Object call() {
		try {
			//�̵߳ȴ��豸����ʱ��
			Thread.currentThread().sleep(pf.getWtime()*1000);

			//ѭ��ping�豸
			Runtime rt = Runtime.getRuntime();
			String com="cmd /c ping "+pf.getIp();
			while(defaultping>=0){
				
				if(ping(rt,com)){		//���pingͨ���Ͳ��ü���ping��ֱ�Ӵ���ѭ��,������һ��
					count++;
					break;				//����ѭ�����ʾ���ټ���ping
				}else{					//˯1���ٽ���ping
					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
				defaultping--;			//ping��������
			}
			
			if(defaultping<0){			//�������С��0��ʾûpingͨ ,��ô������TCPClientThread��flagΪfalse
				//System.out.println("û��pingͨ");
				pf.getJerror().setText("����ʧ��");
				tct.setFlag(false);
			}
			//�������е������ʾ����pingͨ��  ��Ҫ��count ��++��ʾ��ȥ
			pf.getJresult().setText(String.valueOf(count));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clq.add(pf);
		return pf;
	}
	
	
	private boolean ping(Runtime rt,String con) throws IOException{
		Process rs = rt.exec(con);
		BufferedReader br=new BufferedReader(new InputStreamReader(rs.getInputStream()));
		String line="";
		int re=0;
		while((line=br.readLine())!=null){
			re=getCheckResult(line);
			if(re==1){
				return true;
			}
		}
		return false;
	}

    //��line����=18ms TTL=16����,˵���Ѿ�pingͨ,����1,��t����0.
    private static int getCheckResult(String line) {  // System.out.println("����̨����Ľ��Ϊ:"+line);  
        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",    Pattern.CASE_INSENSITIVE);  
        Matcher matcher = pattern.matcher(line);  
        while (matcher.find()) {
            return 1;
        }
        return 0; 
    }



}
