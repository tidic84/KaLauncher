package fr.kallium.kalauncher;

import com.sun.awt.AWTUtilities;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.animation.Animator;
import fr.theshark34.swinger.util.WindowMover;

import javax.swing.*;

public class KaFrame extends JFrame{

    private static KaFrame instance;
    private KaPanel kaPanel;

    private JLabel toolBar = new JLabel();

    public KaFrame() {
        this.setTitle("Kallium Launcher");
        this.setSize(1280, 720);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setIconImage(Swinger.getResource("image/icon.png"));
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        this.setContentPane(kaPanel = new KaPanel());
        AWTUtilities.setWindowOpacity(this, 0.0F );

        toolBar.setIcon(new javax.swing.ImageIcon(Swinger.getResource("image/bar.png")));
        WindowMover mover = new WindowMover(this);
        toolBar.addMouseListener(mover);
        toolBar.addMouseMotionListener(mover);
        toolBar.setBounds(0, 0, 1280, 29);
        toolBar.setOpaque(false);
        this.add(toolBar);

        this.setVisible(true);

        Animator.fadeInFrame(this, 1);
    }

    public static void main(String[] args) {
        Swinger.setSystemLookNFeel();
        Swinger.setResourcePath("/fr/kallium/kalauncher/resources/");

        instance = new KaFrame();
    }

    public static KaFrame getInstance() {
        return instance;
    }

    public KaPanel getKaPanel() {
        return this.kaPanel;
    }
}
