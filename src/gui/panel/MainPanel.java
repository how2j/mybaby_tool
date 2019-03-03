package gui.panel;
 
import java.awt.BorderLayout;
 
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
 
import gui.listener.ToolBarListener;
import util.CenterPanel;
import util.GUIUtil;
 
public class MainPanel extends JPanel {
    static {
        GUIUtil.useLNF();
    }
 
    public static MainPanel instance = new MainPanel();
    public JToolBar tb = new JToolBar();
    public JButton bConfig = new JButton();
    public JButton bBackup = new JButton();
    public JButton bRecover = new JButton();
 
    public CenterPanel workingPanel;
 
    private MainPanel() {
        GUIUtil.setImageIcon(bConfig, "config.png", "设置");
        GUIUtil.setImageIcon(bBackup, "backup.png", "备份");
        GUIUtil.setImageIcon(bRecover, "restore.png", "恢复");

        tb.add(bConfig);
        tb.add(bBackup);
        tb.add(bRecover);
        tb.setFloatable(false);
 
        workingPanel = new CenterPanel(0.8);
 
        setLayout(new BorderLayout());
        add(tb, BorderLayout.NORTH);
        add(workingPanel, BorderLayout.CENTER);
         
        addListener();
    }
 
    private void addListener() {
        ToolBarListener listener = new ToolBarListener();
         
        bConfig.addActionListener(listener);
        bBackup.addActionListener(listener);
        bRecover.addActionListener(listener);
         
    }
    
    public void frezz() {
    	bConfig.setEnabled(false);
    	bBackup.setEnabled(false);
    	bRecover.setEnabled(false);
    }
    public void unfrezz() {
    	bConfig.setEnabled(true);
    	bBackup.setEnabled(true);
    	bRecover.setEnabled(true);
    }
 
    public static void main(String[] args) {
        GUIUtil.showPanel(MainPanel.instance, 1);
    }
}