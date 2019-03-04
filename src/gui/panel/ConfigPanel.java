package gui.panel;
 
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cn.hutool.system.SystemUtil;
import gui.listener.ConfigListener;
import service.ConfService;
import util.ColorUtil;
import util.GUIUtil;
 
public class ConfigPanel extends WorkingPanel {
    static {
        GUIUtil.useLNF();
    }
 
    public static ConfigPanel instance = new ConfigPanel();
 
//    后台管理密码
//    访问地址
//    端口号
//    应用名称
//    ip地址或者域名
    
    JLabel lPassword = new JLabel("后台管理密码");
    public JTextField tfPassword = new JTextField("admin");

    JLabel lIp = new JLabel("IP地址或者域名");
    public JTextField tfIp = new JTextField("127.0.0.1");

    JLabel lport = new JLabel("端口");
    public JTextField tfPort = new JTextField("8080");

    JLabel lContext = new JLabel("应用名称");
    public JTextField tfContext = new JTextField("mybaby");

    JLabel lBackupFolder = new JLabel("万一恢复功能失败，还可以在这个目录找到你心爱宝宝的所有资料");
    public JTextField tfBackupFolder = new JTextField("");
 
    JButton bSubmit = new JButton("更新");
 
    public ConfigPanel() {
        GUIUtil.setColor(ColorUtil.grayColor,lIp, lport,lContext,lPassword, lBackupFolder);
        GUIUtil.setColor(ColorUtil.blueColor, bSubmit);
 
        JPanel pInput = new JPanel();
        JPanel pSubmit = new JPanel();
        int gap = 10;
        pInput.setLayout(new GridLayout(11, 1, gap, gap));
 
        pInput.add(lPassword);
        pInput.add(tfPassword);
        pInput.add(lIp);
        pInput.add(tfIp);
        pInput.add(lport);
        pInput.add(tfPort);
        pInput.add(lContext);
        pInput.add(tfContext);
        pInput.add(lBackupFolder);
        pInput.add(tfBackupFolder);
        
 
        pSubmit.add(bSubmit);
 
        this.setLayout(new BorderLayout());
        this.add(pInput, BorderLayout.NORTH);
        this.add(pSubmit, BorderLayout.CENTER);
 
        addListener();
    }
 
    public static void main(String[] args) {
        GUIUtil.showPanel(ConfigPanel.instance);
    }
 
    public void addListener() {
        ConfigListener l = new ConfigListener();
        bSubmit.addActionListener(l);
    }
 
    @Override
    public void updateData() {
    	ConfService cs= new ConfService();
    	String ip = cs.getIp();
    	String port = cs.getPort();
    	String context = cs.getContext();
    	String password = cs.getPassword();
    	
    	
    	tfIp.setText(ip);
    	tfPort.setText(port);
    	tfContext.setText(context);
    	tfPassword.setText(password);
    	tfBackupFolder.setText(new File(SystemUtil.getUserInfo().getCurrentDir(),"backup").getAbsolutePath());
    	tfBackupFolder.setEditable(false);
    	
    	
        tfPassword.grabFocus();
    }
 
}