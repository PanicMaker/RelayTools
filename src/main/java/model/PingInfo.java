package model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PingInfo {
	
	private JCheckBox jping;

	/**
	 * 用来指定链接继电器的IP地址
	 */
	private JTextField jpingIp;
	/**
	 * 设备断电后到重启时间
	 */
	private JTextField jwtime;
	/**
	 * 设备重启次数
	 */
	private JTextField jcycles;
	/**
	 * 断电后到通电时间
	 */
	private JTextField jtdtime;
	/**
	 * 继电器通道选择
	 */
	private JTextField jjdqtd;
	/**
	 * 反馈统计ping通次数结果
	 */
	private JTextField jresult;
	/**
	 * 异常提示
	 */
	private JLabel jerror;
	
	
	/**
	 * 用来做数据处理
	 *
	 * @return
	 */
	private String ip;		//设备ip
	private int wtime;		//等待重启时间
	private int cycles;		//设备重启次数
	private int tdtime;		//延迟通电
	private int td;			//继电器通道
	
	private int result;		//ping通结果
	private String jdqip;	//继电器ip

	/*public JLabel getJping() {
		return jping;
	}
	public void setJping(JLabel jping) {
		this.jping = jping;
	}*/
	public JTextField getJpingIp() {
		return jpingIp;
	}
	public JCheckBox getJping() {
		return jping;
	}
	
	public void setJping(JCheckBox jping) {
		this.jping = jping;
	}
	public void setJpingIp(JTextField jpingIp) {
		this.jpingIp = jpingIp;
	}
	public JTextField getJwtime() {
		return jwtime;
	}
	public void setJwtime(JTextField jwtime) {
		this.jwtime = jwtime;
	}
	public JTextField getJcycles() {
		return jcycles;
	}
	public void setJcycles(JTextField jcycles) {
		this.jcycles = jcycles;
	}
	public JTextField getJtdtime() {
		return jtdtime;
	}
	public void setJtdtime(JTextField jtdtime) {
		this.jtdtime = jtdtime;
	}
	public JTextField getJjdqtd() {
		return jjdqtd;
	}
	public void setJjdqtd(JTextField jjdqtd) {
		this.jjdqtd = jjdqtd;
	}
	public JTextField getJresult() {
		return jresult;
	}
	public void setJresult(JTextField jresult) {
		this.jresult = jresult;
	}
	public JLabel getJerror() {
		return jerror;
	}
	public void setJerror(JLabel jerror) {
		this.jerror = jerror;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getWtime() {
		return wtime;
	}
	public void setWtime(int wtime) {
		this.wtime = wtime;
	}
	public int getCycles() {
		return cycles;
	}
	public void setCycles(int cycles) {
		this.cycles = cycles;
	}
	public int getTdtime() {
		return tdtime;
	}
	public void setTdtime(int tdtime) {
		this.tdtime = tdtime;
	}
	public String getJdqip() {
		return jdqip;
	}
	public void setJdqip(String jdqip) {
		this.jdqip = jdqip;
	}
	public int getTd() {
		return td;
	}
	public void setTd(int td) {
		this.td = td;
	}
	public PingInfo(JCheckBox jping, JTextField jpingIp, JTextField jwtime, JTextField jcycles, JTextField jtdtime,
			JTextField jjdqtd, JTextField jresult, JLabel jerror) {
		super();
		this.jping = jping;
		this.jpingIp = jpingIp;
		this.jwtime = jwtime;
		this.jcycles = jcycles;
		this.jtdtime = jtdtime;
		this.jjdqtd = jjdqtd;
		this.jresult = jresult;
		this.jerror = jerror;
	}
	public PingInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	
	
}
