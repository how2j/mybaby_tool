package gui.panel;
 
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import util.ColorUtil;
import util.GUIUtil;
 
public class TimerBackupPanel extends BackupPanel {
    static {
        GUIUtil.useLNF();
    }
    
    public static TimerBackupPanel instance = new TimerBackupPanel();
 
    public JLabel lBackup = new JLabel();
    public TimerBackupPanel() {
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
        
 
        pSubmit.add(lBackup);

        this.setLayout(new BorderLayout());

        this.add(pSubmit, BorderLayout.CENTER);
        this.add(pInput, BorderLayout.NORTH);
        
        
		pbRecord.setMaximum(100);
        pbRecord.setStringPainted(true);

        pbPicture.setMaximum(100);
        pbPicture.setStringPainted(true);

        pbVideo.setMaximum(100);
        pbVideo.setStringPainted(true);
        
        pbRecord.setValue(55);
        
        addListener();
    }
 
    public static void main(String[] args) {
        GUIUtil.showPanel(TimerBackupPanel.instance);
    }
 
    @Override
    public void updateData() {
    	pbPicture.setValue(0);
    	pbRecord.setValue(0);
    	pbVideo.setValue(0);
    	

    }
 
    @Override
    public void addListener() {
//        BackupListener listener = new BackupListener();
//        bBackup.addActionListener(listener);
    }
 
}