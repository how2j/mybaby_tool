package service;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import cn.hutool.core.thread.ThreadUtil;
import gui.panel.BackupPanel;
import gui.panel.ConfigPanel;
import gui.panel.MainPanel;
import gui.panel.TimerBackupPanel;
import util.ColorUtil;
import util.GUIUtil;
import util.Progress;

public class BackupService {

	public static void backup(BackupPanel p) {
    	
        p.pbRecord.setValue(0);
        p.pbPicture.setValue(0);
        p.pbVideo.setValue(0);
        
        ConfService confService = new ConfService();
        boolean check =confService.check();
        if(check) {
        	SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
        		protected Object doInBackground() {
        			BackupService.backuppingOrRecovering = true;
        			try {
						int localRecordSize = confService.getLocalRecordSize();
						int serverRecordSize = confService.getServerRecordSize();
						
						System.out.println("serverRecordSize:"+serverRecordSize);
						System.out.println("localRecordSize:"+localRecordSize);
						
						if(serverRecordSize<localRecordSize) {
							JOptionPane.showMessageDialog(p, "本地备份数据比服务器数据还丰富，拒绝进行备份，因为备份会把本地数据都抹掉，可能会有丢失");
						}
						else {
							boolean backupConfig = confService.backupConfig();
							if(backupConfig) {
						    	boolean backupAllInfo= confService.backupAllInfo();
						    	if(backupAllInfo) {
						    		boolean success = true;
						    		p.pbRecord.setValue(100);
						            try {
						        		Progress progress4picture = new Progress() {
						        			@Override
						        			public void progress(int percentage) {
						        				System.out.println(Thread.currentThread().getName());
						        				System.out.println("percentage:"+percentage);
						        				p.pbPicture.setValue(percentage);
						        			}
						        		};
										confService.backupPictures(progress4picture);
									} catch (Exception e1) {
										success = false;
										JOptionPane.showMessageDialog(p, "备份图片过程出现异常:"+ e1);
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
						            try {
						            	Progress progress4video = new Progress() {
						            		@Override
						            		public void progress(int percentage) {
						            			p.pbVideo.setValue(percentage);
						            		}
						            	};
						            	confService.backupVideos(progress4video);
						            } catch (Exception e1) {
						            	success = false;
						            	JOptionPane.showMessageDialog(p, "备份视频过程出现异常:"+ e1);
						            	// TODO Auto-generated catch block
						            	e1.printStackTrace();
						            }
						            ThreadUtil.sleep(3000);

						        	
						        	if(success) {
						        		if(p instanceof TimerBackupPanel) {
						        	        GUIUtil.setColor(ColorUtil.greenColor, TimerBackupPanel.instance.lBackup);
//						        			TimerBackupPanel.instance.lBackup.setText("自动定时备份中...");
						        			TimerBackupPanel.instance.lBackup.setText("自动备份完成");
						        		}
						        		else {
							        		JOptionPane.showMessageDialog(MainPanel.instance, "备份已经完成");      						        			
						        		}
						        		
						        		
						        	}
						        	
						        	
						        	
						    	}
							}
						}
			        	p.bBackup.setEnabled(true);
			        	MainPanel.instance.unfrezz();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			BackupService.backuppingOrRecovering = false;
    				return null;
        		}
        	};
        	worker.execute();
        	p.bBackup.setEnabled(false);
        	MainPanel.instance.frezz();
        }
	}

	public static boolean backuppingOrRecovering = false;
}
