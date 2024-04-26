package com.brainmentors.gaming;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import com.brainmentors.gaming.sprites.Enemy;
import com.brainmentors.gaming.sprites.Player;

public class Board extends JPanel {
    BufferedImage backgroundImage;
    Player player;
    Timer timer;
    Enemy enemies[] = new Enemy[3];
    int score = 0;
    private boolean isPaused = false;
    private boolean isGameStarted = false; // Track if the game is started
    private int remainingTime = 0; // Remaining time in seconds
    private int gameDuration = 60; // Default game duration in seconds
    private long startTime = 0; // Start time of the game

    public Board() {
        setSize(1500, 920);
        loadBackgroundImage();
        player = new Player();
        // Initialize enemies array with null values
        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = null;
        }
        loadEnemies();
        gameLoop();
        setFocusable(true);
        bindEvents();
    }

    private void gameOver(Graphics pen) {
        // Check if all enemies are destroyed
        boolean allDestroyed = true;
        for (Enemy enemy : enemies) {
            if (!enemy.destroyed) {
                allDestroyed = false;
                break;
            }
        }
        if (remainingTime <= 0) {
            pen.setFont(new Font("times", Font.BOLD, 100));
            pen.setColor(Color.WHITE);
            pen.drawString("Time's Up!", 1050 / 2, 900 / 2);
            timer.stop();
        }
        if (allDestroyed) {
            // Reset enemies and load new ones
            loadEnemies();
        }
        if (player.outOfScreen()) {
            pen.setFont(new Font("times", Font.BOLD, 100));
            pen.setColor(Color.WHITE);
            pen.drawString("PlayerGone", 1200 / 2, 900 / 2);
            timer.stop();
        }
        if (score == 100) {
            pen.setFont(new Font("times", Font.BOLD, 100));
            pen.setColor(Color.WHITE);
            pen.drawString("Game-WIN", 1100 / 2, 900 / 2);
            timer.stop();
        }
        for (Enemy enemy : enemies) {
            isCollide(enemy);
        }
    }

    private boolean isCollide(Enemy enemy) {
        if (!enemy.destroyed) {
            int xDistance = Math.abs(player.x - enemy.x);
            int yDistance = Math.abs(player.y - enemy.y);
            int maxH = Math.max(player.h, enemy.h);
            int maxW = Math.max(player.w, enemy.w);
            boolean collided = xDistance <= maxW - 200 && yDistance <= maxH - 150;
            if (collided) {
                enemy.destroyed = true;
                score += 10;
            }
            return collided;
        }
        return false;
    }

    private void bindEvents() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
                player.speed = 0;
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (!isGameStarted && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Start the game if Enter is pressed and the game is not started
                    isGameStarted = true;
                    startTime = System.currentTimeMillis();
                    timer.start();
                } else if (isGameStarted) {
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        player.rightpressed();
                        player.speed = 10;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        player.leftpressed();
                        player.speed = -10;
                    }
                }
            }
        });
    }

    private void loadEnemies() {
        int x = 300;
        int gap = 400;
        int y = 100;
        int speed = 5;
        Random random = new Random();

        // Keep track of how many enemies are loaded
        int loadedEnemies = 0;

        // Iterate through enemies array
        for (int i = 0; i < enemies.length; i++) {
            // If an enemy is not destroyed or already loaded, don't load a new one
            if (enemies[i] != null && !enemies[i].destroyed) {
                loadedEnemies++;
                continue;
            }

            // If maximum loaded enemies reached, break loop
            if (loadedEnemies >= 3) {
                break;
            }

            // Randomize speed for variety
            speed = random.nextInt(5) + 5;

            // Load new enemy
            enemies[i] = new Enemy(x, y, speed);
            x = x + gap;

            // Increment loadedEnemies count
            loadedEnemies++;
        }
    }

    private void gameLoop() {
        timer = new Timer(50, (e) -> {
            updateTimer();
            repaint();
        });
    }

    private void updateTimer() {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        remainingTime = gameDuration - (int) elapsedTime;
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(Board.class.getResource("game-bg.png"));
        } catch (IOException e) {
            System.out.println("Image not found");
            System.exit(1);
            e.printStackTrace();
        }
    }

    private void printEnemies(Graphics pen) {
        for (Enemy enemy : enemies) {
            if (!enemy.destroyed) {
                enemy.draw(pen);
                enemy.move();
            } else {
                enemy.draw(pen);
                enemy.move();
            }
        }
    }

    private void togglePause() {
        if (isPaused) {
            isPaused = false;
            timer.start(); // Resume the game timer
        } else {
            isPaused = true;
            drawPaused(getGraphics());
            timer.stop(); // Pause the game timer
        }
    }

    @Override
    public void paintComponent(Graphics pen) {
        super.paintComponent(pen);// clean up
        // all printing logic will be here
        pen.drawImage(backgroundImage, 0, 0, 1500, 920, null);
        if (!isGameStarted) {
            // Display "Press Enter to start the game"
            pen.setColor(Color.BLACK);
            pen.setFont(new Font("Times", Font.BOLD, 50));
            pen.drawString("Press Enter to start the game", 350, 400);
            pen.drawString(" Reach 100 score before 60s", 350, 500);
        } else {
            // Game is started, draw game elements
            player.draw(pen);
            player.move();
            printEnemies(pen);
            gameOver(pen);
            drawScore(pen);
            drawTimer(pen);
        }
    }

    private void drawScore(Graphics pen) {
        pen.setColor(Color.WHITE);
        pen.setFont(new Font("Arial", Font.BOLD, 30));
        pen.drawString("Score: " + score, 20, 40);
    }

    private void drawPaused(Graphics pen) {
        pen.setFont(new Font("Arial", Font.BOLD, 100));
        pen.setColor(Color.WHITE);
        pen.drawString("Game-Paused", 1050 / 2, 900 / 2);
    }

    private void drawTimer(Graphics pen) {
        pen.setColor(Color.WHITE);
        pen.setFont(new Font("Arial", Font.BOLD, 30));
        pen.drawString("Time: " + remainingTime + "s", 20, 80);
    }
}
