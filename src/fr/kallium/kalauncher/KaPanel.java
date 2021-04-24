package fr.kallium.kalauncher;

import fr.litarvan.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.animation.Animator;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class KaPanel extends JPanel implements SwingerEventListener {

    private Image background = Swinger.getResource("image/bg.png");

    private Saver saver = new Saver(new File(KaLauncher.KA_DIR, "launcher.properties"));

    private JTextField usernameField = new JTextField(this.saver.get("username"));
    private JPasswordField passwordField = new JPasswordField(this.saver.get("password"));

    private KPlayButton playButton = new KPlayButton(Swinger.getResource("buttons/StartButtonDefault.png"), Swinger.getResource("buttons/StartButtonHover.png"), Swinger.getResource("buttons/StartButtonActive.png"));
    private KTexturedButton closeButton = new KTexturedButton(Swinger.getResource("buttons/CloseButtonDefault.png"), Swinger.getResource("buttons/CloseButtonHover.png"));
    private KTexturedButton minimizeButton = new KTexturedButton(Swinger.getResource("buttons/MinimizeButtonDefault.png"), Swinger.getResource("buttons/MinimizeButtonHover.png"));
    private KTexturedButton settingsButton = new KTexturedButton(Swinger.getResource("buttons/SettingsButtonDefault.png"), Swinger.getResource("buttons/SettingsButtonHover.png"));

    private SColoredBar progressBar = new SColoredBar(new Color(255, 255, 255, 50), new Color(128, 128, 128, 100));
    private JLabel infoLabel = new JLabel("", SwingConstants.CENTER);

    public boolean idIsValid = false;

    public KaPanel() {
        this.setLayout(null);

        InputStream is = null;
        Font customFont = null;
        GraphicsEnvironment ge = null;
        is = KaPanel.class.getResourceAsStream("resources/font.ttf");
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(customFont);

        usernameField.setForeground(Color.WHITE);
        usernameField.setBounds(425, 340, 441, 50);
        usernameField.setBorder(null);
        usernameField.setOpaque(false);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setFont(customFont);
        usernameField.setFont(usernameField.getFont().deriveFont(20F));
        this.add(usernameField);

        passwordField.setForeground(Color.WHITE);
        passwordField.setBounds(425, 450, 441, 50);
        passwordField.setBorder(null);
        passwordField.setOpaque(false);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setFont(passwordField.getFont().deriveFont(20F));
        this.add(passwordField);

        playButton.setBounds(530,571, 220, 45);
        playButton.addEventListener(this);
        this.add(playButton);

        closeButton.setBounds(1226, 0);
        closeButton.addEventListener(this);
        closeButton.setSize(54, 30);
        this.add(closeButton);

        minimizeButton.setBounds(1172, 0, 54, 30);
        minimizeButton.addEventListener(this);
        this.add(minimizeButton);

        settingsButton.setBounds(0, 0, 52, 30);
        settingsButton.addEventListener(this);
        this.add(settingsButton);

        progressBar.setBounds(64, 680, 1152, 25);
        progressBar.setVisible(false);
        this.add(progressBar);

        infoLabel.setBounds(64, 640, 1152, 50);
        infoLabel.setForeground(Color.white);
        infoLabel.setFont(usernameField.getFont().deriveFont(20F));
        infoLabel.setVisible(false);
        this.add(infoLabel);
    }

    public void onEvent(SwingerEvent e) {
        if(e.getSource() == playButton){
            setFieldsEnable(false);

            if (usernameField.getText().replaceAll(" ", "").length() == 0 || passwordField.getText().length() == 0){
                JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer un identifiant et un mot de passe valide", "Erreur", JOptionPane.ERROR_MESSAGE);
                setFieldsEnable(true);
                return;
            }

            KPlayButton.textureDisabled = KPlayButton.textureDisabled2;
            this.repaint();

            Thread t = new Thread() {
                public void run() {
                    try {
                        KaLauncher.auth(usernameField.getText(), passwordField.getText());
                    } catch (AuthenticationException e) {
                        System.out.println("Username: " + usernameField.getText() + "  |  " + "Password: " + passwordField.getText());
                        JOptionPane.showMessageDialog(KaPanel.this, "Erreur, impossible de se connecter " + e.getErrorModel().getErrorMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        setFieldsEnable(true);
                        return;
                    }
                    progressBar.setVisible(true);
                    infoLabel.setVisible(true);

                    try {
                        KaLauncher.update();
                    } catch (Exception e) {
                        KaLauncher.interruptThread();
                        JOptionPane.showMessageDialog(KaPanel.this, "Erreur, impossible de mettre Kallium a jour : " + e , "Erreur", JOptionPane.ERROR_MESSAGE);
                        setFieldsEnable(true);
                        return;
                    }
                    saver.set("username", usernameField.getText());
                    saver.set("password", passwordField.getText());

                }

            };

            t.start();

        } else if (e.getSource() == closeButton) {
            Animator.fadeOutFrame(KaFrame.getInstance(), 1, new Runnable() {
                @Override
                public void run() {
                    System.exit(0);
                }
            });
        }
        else if(e.getSource() == minimizeButton) {

            KaFrame.getInstance().setState(JFrame.ICONIFIED);
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    private void setFieldsEnable(boolean enable) {
        usernameField.setEnabled(enable);
        passwordField.setEnabled(enable);
        playButton.setEnabled(enable);
    }

    public SColoredBar getProgressBar() {
        return progressBar;
    }

    public void setInfoText(String text) {

        infoLabel.setText(text);

    }

}
