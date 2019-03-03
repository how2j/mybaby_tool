package gui.panel;
 
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import gui.listener.RecoverListener;
import util.ColorUtil;
import util.GUIUtil;
 
public class RecoverPanel extends WorkingPanel {
    static{
        GUIUtil.useLNF();
    }
    JLabel lRecord = new JLabel("记录恢复进度条");
    public JProgressBar pbRecord = new JProgressBar();
    JLabel lPicture = new JLabel("图片恢复进度条");
    public JProgressBar pbPicture = new JProgressBar();
    JLabel lVideo = new JLabel("视频恢复进度条");
    public JProgressBar pbVideo = new JProgressBar();
    
    public static RecoverPanel instance = new RecoverPanel();
 
    public JButton bRecover =new JButton("恢复");
 
    public RecoverPanel() {
        GUIUtil.setColor(ColorUtil.blueColor, bRecover);
        this.add(bRecover);
        
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
        
 
        pSubmit.add(bRecover);

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
        GUIUtil.showPanel(RecoverPanel.instance);
    }
 
    @Override
    public void updateData() {
        // TODO Auto-generated method stub
         pbPicture.setValue(0);
         pbRecord.setValue(0);
         pbVideo.setValue(0);
    }
 
    @Override
    public void addListener() {
        RecoverListener listener = new RecoverListener();
        bRecover.addActionListener(listener);
    }
 
}