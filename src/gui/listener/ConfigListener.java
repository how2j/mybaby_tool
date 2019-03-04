package gui.listener;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import gui.panel.ConfigPanel;
import service.ConfService;
import util.GUIUtil;
 
public class ConfigListener implements ActionListener{
 
	
	
    @Override
    public void actionPerformed(ActionEvent e) {
        ConfigPanel p = ConfigPanel.instance;
        if(!GUIUtil.checkEmpty(p.tfIp, "IP 地址"))
            return;
        
        p.tfIp.setText(p.tfIp.getText().toLowerCase().replaceAll("http://", ""));
        
        System.out.println(p.tfIp.getText());
        
        if(!GUIUtil.checkEmpty(p.tfPort, "端口号"))
        	return;
//        if(!GUIUtil.checkEmpty(p.tfContext, "应用名称"))
//        	return;
//
        
        String password = p.tfPassword.getText();
        String ip = p.tfIp.getText();
		String port = p.tfPort.getText();
		String context = p.tfContext.getText();
		
		ConfService cs= new ConfService();
		boolean check =cs.check(ip, port, context, password);
		
		if(check) {
	        cs.update(ip,port,context,password);
	        JOptionPane.showMessageDialog(p, "设置修改成功");
		}
    }
 
}