package gui.listener;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import gui.panel.BackupPanel;
import service.BackupService;
 
public class BackupListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        BackupPanel p  =BackupPanel.instance;
		if(BackupService.backupping) {
    		JOptionPane.showMessageDialog(p, "正在备份");
    		return;
    	};
        BackupService.backup(p);
    }
}