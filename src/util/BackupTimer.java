package util;

import javax.swing.SwingWorker;

import cn.hutool.core.thread.ThreadUtil;
import gui.panel.MainPanel;
import gui.panel.TimerBackupPanel;
import service.BackupService;

public class BackupTimer implements Runnable{

	public static void start() {


		
		new Thread(new BackupTimer()).start();
	}

	@Override
	public void run() {
		while(true) {
			ThreadUtil.sleep(3000);
			working();
		}
	}

	public void working() {
		TimerBackupPanel p = TimerBackupPanel.instance;
		if(BackupService.backupping) {
//    		JOptionPane.showMessageDialog(p, "正在备份");
    		return;
    	};
        GUIUtil.setColor(ColorUtil.blueColor, TimerBackupPanel.instance.lBackup);
		TimerBackupPanel.instance.lBackup.setText("自动定时备份中...");

    	MainPanel.instance.workingPanel.show(TimerBackupPanel.instance);
    	BackupService.backup(p);
	}
}
