package startup;
 
import javax.swing.SwingUtilities;

import gui.frame.MainFrame;
import gui.panel.ConfigPanel;
import gui.panel.MainPanel;
import util.BackupTimer;
import util.GUIUtil;
import util.LogPrintStream;
 
public class Bootstrap {
    public static void main(String[] args) throws Exception{
        GUIUtil.useLNF();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {

                MainFrame.instance.setVisible(true);
                MainPanel.instance.workingPanel.show(ConfigPanel.instance);
                BackupTimer.start();

            }
        });
    }
}