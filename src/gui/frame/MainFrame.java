package gui.frame;
 
import javax.swing.JFrame;
 
import gui.panel.MainPanel;
import util.GUIUtil;
 
public class MainFrame extends JFrame{
    public static MainFrame instance = new MainFrame();
     
    private MainFrame(){
        this.setSize(650,650);
        this.setTitle("宝宝相册 - powered by how2j.cn");
        this.setContentPane(MainPanel.instance);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
     
    public static void main(String[] args) {
        instance.setVisible(true);
    }
     
}