package com.brainmentors.gaming.sprites;

import java.awt.Graphics;
import javax.swing.ImageIcon;

public class Player extends Sprite {


  public Player() {
    w = 300;
    h = 200; 
    x = 100;
    y = 400;
    
    image = new ImageIcon(Player.class.getResource("player-right.gif"));
  }

  public void move() {
    x = x + speed;

  }
public void rightpressed()
{
  image = new ImageIcon(Player.class.getResource("player-right.gif"));
}

public void leftpressed()
{
  image = new ImageIcon(Player.class.getResource("player-left.gif"));
  }

  public boolean outOfScreen() {
    return x > 1500;
  }
}