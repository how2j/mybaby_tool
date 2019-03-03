package gui.listener;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import cn.hutool.core.io.FileUtil;
import gui.frame.MainFrame;
import gui.panel.BackupPanel;
import gui.panel.MainPanel;
import service.ConfService;
import util.Progress;
 
public class BackupListener implements ActionListener {
 
    @Override
    public void actionPerformed(ActionEvent e) {
        BackupPanel p  =BackupPanel.instance;
        p.pbRecord.setValue(0);
        p.pbPicture.setValue(0);
        p.pbVideo.setValue(0);
        
        ConfService confService = new ConfService();
        boolean check =confService.check();
        if(check) {
        	SwingWorker worker = new SwingWorker() {
        		protected Object doInBackground() throws Exception {
        			int localRecordSize = confService.getLocalRecordSize();
        			int serverRecordSize = confService.getServerRecordSize();
        			
        			System.out.println("serverRecordSize:"+serverRecordSize);
        			System.out.println("localRecordSize:"+localRecordSize);
        			
        			if(serverRecordSize<localRecordSize) {
        				
        				JOptionPane.showMessageDialog(p, "本地备份数据比服务器数据还丰富，拒绝进行备份，因为备份会把本地数据都抹掉，可能会有丢失");
                    	p.bBackup.setEnabled(true);
                    	MainPanel.instance.unfrezz();
        				return null;
        			}
        			
        			boolean backupConfig = confService.backupConfig();
        			
        			if(!backupConfig) 
        				return null;
        			
        			
                	boolean backupAllInfo= confService.backupAllInfo();
                	if(!backupAllInfo) 
                		return null;
                		
            		boolean success = true;
            		p.pbRecord.setValue(100);
                    

            		
            		
            		
                    try {
                		Progress progress4picture = new Progress() {
                			@Override
                			public void progress(int percentage) {
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
                    
                	p.bBackup.setEnabled(true);
                	MainPanel.instance.unfrezz();
                	if(success)
                		JOptionPane.showMessageDialog(MainPanel.instance, "备份已经完成");

        			return null;
        		}
        	};
        	worker.execute();
        	p.bBackup.setEnabled(false);
        	MainPanel.instance.frezz();
        	
        }
    }
}