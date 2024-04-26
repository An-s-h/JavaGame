package com.brainmentors.gaming.sprites;

import java.awt.Graphics;

import javax.swing.ImageIcon;

public abstract class Sprite {
  public int speed;
    public int x;
    public int y;
    public int w;
    public int h;
    public boolean destroyed = false;

    public ImageIcon image;
    public ImageIcon blastImage;
    public ImageIcon fireBall;

    public void draw(Graphics pen) {
      pen.drawImage(image.getImage(), x, y, w, h, null);

    }


}

