package com.brainmentors.gaming;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
  public GameFrame() {
    Board board = new Board();
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setTitle("Game dev in Java");
    this.setSize(1500, 920);
    this.setLocationRelativeTo(null);
    add(board);
    this.setVisible(true);
  }

  public static void main(String[] args) {
    new GameFrame();

  }
}
