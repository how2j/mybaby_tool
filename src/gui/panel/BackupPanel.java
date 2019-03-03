package gui.panel;
 
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import gui.listener.BackupListener;
import util.ColorUtil;
import util.GUIUtil;
import util.LogPrintStream;
 
public class BackupPanel extends WorkingPanel {
    static {
        GUIUtil.useLNF();
    }
    
    JLabel lRecord = new JLabel("记录下载进度条");
    public JProgressBar pbRecord = new JProgressBar();
    JLabel lPicture = new JLabel("图片下载进度条");
    public JProgressBar pbPicture = new JProgressBar();
    JLabel lVideo = new JLabel("视频下载进度条");
    public JProgressBar pbVideo = new JProgressBar();
 
    public static BackupPanel instance = new BackupPanel();
    public JButton bBackup = new JButton("备份");
 
    public BackupPanel() {
        GUIUtil.setColor(ColorUtil.blueColor, bBackup);
        this.add(bBackup);
        
        JPanel pInput = new JPanel();
        JPanel pSubmit = new JPanel();
        
        int gap = 20;
        pInput.setLayout(new GridLayout(8, 1, gap, gap));
 
        pInput.add(lRecord);
        pInput.add(pbRecord);
        pInput.add(lPicture);
        pInput.add(pbPicture);
        pInput.add(lVideo);
        pInput.add(pbVideo);
        
 
        pSubmit.add(bBackup);

        this.setLayout(new BorderLayout());

        this.add(pSubmit, BorderLayout.CENTER);
        this.add(pInput, BorderLayout.NORTH);
        
        
		pbRecord.setMaximum(100);
        pbRecord.setStringPainted(true);

        pbPicture.setMaximum(100);
        pbPicture.setStringPainted(true);

        pbVideo.setMaximum(100);
        pbVideo.setStringPainted(true);
        
        addListener();
    }
 
    public static void main(String[] args) {
        GUIUtil.showPanel(BackupPanel.instance);
    }
 
    @Override
    public void updateData() {
    	pbPicture.setValue(0);
    	pbRecord.setValue(0);
    	pbVideo.setValue(0);
    }
 
    @Override
    public void addListener() {
        BackupListener listener = new BackupListener();
        bBackup.addActionListener(listener);
    }
 
}