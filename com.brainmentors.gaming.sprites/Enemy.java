package com.brainmentors.gaming.sprites;

import java.awt.Graphics;
import javax.swing.ImageIcon;

public class Enemy extends Sprite {
    public boolean blastDisplayed = false;
    public int blasts = 0;

    public Enemy(int x, int y, int speed) {
        this.y = y;
        this.x = x;
        this.speed = speed;
        w = 300;
        h = 250;
        image = new ImageIcon(Enemy.class.getResource("pokemon-enemy.gif"));
        blastImage = new ImageIcon(Enemy.class.getResource("blast.gif"));
        // fireBall = new ImageIcon(Enemy.class.getResource("fire-ball.gif"));
    }

    public void move() {
        if (y > 900) {
            y = 0;
        }
        y = y + speed;
    }

    public void draw(Graphics pen) {
        if (!destroyed) {
            pen.drawImage(image.getImage(), x, y, w, h, null);
        } else if(blasts<15) {
            // Display the blast animation only once
           
            pen.drawImage(blastImage.getImage(), x, y, w, h, null);
            blasts++;
           
             // Mark blast as displayed
        }
    }
    
}


  

