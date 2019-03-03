package gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import cn.hutool.core.util.StrUtil;
import gui.panel.MainPanel;
import gui.panel.RecoverPanel;
import service.ConfService;
import util.Progress;

public class RecoverListener implements ActionListener {

	ConfService configService = new ConfService();
	@Override
	public void actionPerformed(ActionEvent e) {
		
		RecoverPanel p = RecoverPanel.instance;


		boolean check = configService.check();
		if (check) {
			try {
				int localSize = configService.getLocalRecordSize();
				if (0 == localSize) {
					JOptionPane.showMessageDialog(p, "备份中并无记录，无法进行恢复工作");
				} else {
					int serverSize = configService.getServerRecordSize();
					if(serverSize>localSize) {
						JOptionPane.showMessageDialog(p, "服务器上数据更多，不可以进行恢复工作");
						return; 
					}
					
					System.out.println("size:"+serverSize);
					if (0 != serverSize) {
						Object[] options = { "覆盖", "追加", "取消" };
						int n = JOptionPane.showOptionDialog(p, "服务器上是有数据的，是要覆盖还是追加？\n覆盖 - 服务器所有原数据会被删除 \n追加 - 服务器原数据会被保留 ", "提示",
								JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
								options[2]);
						// int a= JOptionPane.showConfirmDialog(p,
						// "服务器上是有数据的，是要覆盖还是追加？");
						System.out.println(n);
						
						//取消
						if(2==n) {
							return;
						}
						else {
							

				        	
							String password= JOptionPane.showInputDialog("请再次输入管理员密码，以确定要进行恢复行为");
							if(null==password)
								return;
							password = StrUtil.trim(password);
							
//							System.out.println(password);
							
							boolean valid = configService.check(password);
							System.out.println("password:"+password);
							System.out.println("valid:"+valid);
							if(!valid) {
								return;
							}
							else {
								
								//覆盖
								if(0==n) {
									
									replace();
									
								}
								
								//追加
								if(1==n) {
									append();
								}
							}
							
						}
					}
					else {
						//覆盖
						replace();
					}

				}

			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(p, "备份文件已破损，无法进行恢复工作");
			}
		}
	}

	private void append() {
		
		RecoverPanel p = RecoverPanel.instance;
    	p.bRecover.setEnabled(false);
    	MainPanel.instance.frezz();

    	p.pbRecord.setValue(0);
    	p.pbPicture.setValue(0);
    	p.pbVideo.setValue(0);
    	
		SwingWorker worker = new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {
				RecoverPanel p = RecoverPanel.instance;
				Progress recordProgress = new Progress() {
					@Override
					public void progress(int percentage) {
						p.pbRecord.setValue(percentage);
					}
				};
				Progress pictureProgress = new Progress() {
					@Override
					public void progress(int percentage) {
						p.pbPicture.setValue(percentage);
					}
				};
				Progress videoProgress = new Progress() {
					@Override
					public void progress(int percentage) {
						p.pbVideo.setValue(percentage);
					}
				};
				
				configService.recover(recordProgress,pictureProgress,videoProgress);
	        	p.bRecover.setEnabled(true);
	        	MainPanel.instance.unfrezz();
	        	JOptionPane.showMessageDialog(MainPanel.instance, "恢复已经完成");
				return null;
			}
			
		};
    	worker.execute();
		

		
	}

	private void replace() {
		//删除服务端数据
		configService.reset();
		//追加
		append();
		
	}

}