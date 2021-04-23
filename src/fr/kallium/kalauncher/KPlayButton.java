package fr.kallium.kalauncher;

import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.abstractcomponents.AbstractButton;

import java.awt.*;
import java.awt.image.BufferedImage;

public class KPlayButton extends AbstractButton {
    private Image texture;
    private Image textureHover;
    public static Image textureDisabled;
    public static Image textureDisabled2;




    public KPlayButton(BufferedImage texture) {
        this(texture, (BufferedImage)null, (BufferedImage)null);
    }

    public KPlayButton(BufferedImage texture, BufferedImage textureHover) {
        this(texture, textureHover, (BufferedImage)null);

    }

    public KPlayButton(BufferedImage texture, BufferedImage textureHover, BufferedImage textureDisabled) {
        if (texture == null) {
            throw new IllegalArgumentException("texture == null");
        } else {
            this.texture = texture;
            if (textureHover == null) {
                this.textureHover = Swinger.fillImage(Swinger.copyImage(texture), Swinger.HOVER_COLOR, this.getParent());
            } else {
                this.textureHover = textureHover;
            }
                this.textureDisabled = Swinger.fillImage(Swinger.copyImage(texture), Swinger.DISABLED_COLOR, this.getParent());
                this.textureDisabled2 = textureDisabled;


        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image texture;
        if (!this.isEnabled()) {
            texture = this.textureDisabled;
        } else if (super.isHover()) {
            texture = this.textureHover;
        } else {
            texture = this.texture;
        }

        Swinger.drawFullsizedImage(g, this, texture);
        if (this.getText() != null) {
            Swinger.activateAntialias(g);
            if (this.getTextColor() != null) {
                g.setColor(this.getTextColor());
            }

            Swinger.drawCenteredString(g, this.getText(), this.getBounds());
        }

    }

    public void setTexture(Image texture) {
        if (texture == null) {
            throw new IllegalArgumentException("texture == null");
        } else {
            this.texture = texture;
            this.repaint();
        }
    }

    public void setTextureHover(Image textureHover) {
        if (textureHover == null) {
            throw new IllegalArgumentException("textureHover == null");
        } else {
            this.textureHover = textureHover;
            this.repaint();
        }
    }

    public void setTextureDisabled(Image textureDisabled) {
        if (textureDisabled == null) {
            throw new IllegalArgumentException("textureDisabled == null");
        } else {
            this.textureDisabled = textureDisabled;
            this.repaint();
        }
    }

    public Image getTexture() {
        return this.texture;
    }

    public Image getTextureHover() {
        return this.textureHover;
    }

    public Image getTextureDisabled() {
        return this.textureDisabled;
    }

    public void setBounds(int x, int y) {
        this.setBounds(x, y, this.texture.getWidth(this.getParent()), this.texture.getHeight(this.getParent()));
    }
}
