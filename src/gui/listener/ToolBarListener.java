package gui.listener;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import gui.panel.BackupPanel;
import gui.panel.ConfigPanel;
import gui.panel.MainPanel;
import gui.panel.RecoverPanel;
 
public class ToolBarListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        MainPanel p = MainPanel.instance;
        JButton b = (JButton) e.getSource();
        if (b == p.bConfig)
            p.workingPanel.show(ConfigPanel.instance);
        if (b == p.bBackup)
            p.workingPanel.show(BackupPanel.instance);
        if (b == p.bRecover)
            p.workingPanel.show(RecoverPanel.instance);
 
    }
}