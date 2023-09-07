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
	 * 默认的ping次数，超过该次数还未ping通,就认为是重启失败，否则就重启成功
	 */
	private int defaultping=5;
	/**
	 * 用来存放重启成功的次数
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
			//线程等待设备重启时间
			Thread.currentThread().sleep(pf.getWtime()*1000);

			//循环ping设备
			Runtime rt = Runtime.getRuntime();
			String com="cmd /c ping "+pf.getIp();
			while(defaultping>=0){
				
				if(ping(rt,com)){		//如果ping通，就不用继续ping了直接打破循环,请增加一次
					count++;
					break;				//打破循环后表示不再继续ping
				}else{					//睡1秒再进行ping
					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
				defaultping--;			//ping次数控制
			}
			
			if(defaultping<0){			//如果次数小于0表示没ping通 ,那么就设置TCPClientThread的flag为false
				//System.out.println("没有ping通");
				pf.getJerror().setText("重启失败");
				tct.setFlag(false);
			}
			//正常运行到这里表示本次ping通了  需要将count 数++显示出去
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

    //若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
    private static int getCheckResult(String line) {  // System.out.println("控制台输出的结果为:"+line);  
        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",    Pattern.CASE_INSENSITIVE);  
        Matcher matcher = pattern.matcher(line);  
        while (matcher.find()) {
            return 1;
        }
        return 0; 
    }



}
