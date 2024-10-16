package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Car extends GameMap implements ActionListener, KeyListener {

    private Timer t;
    private BufferedImage playerCar, secondCar, thirdCar;
    private int playerCarX = 980, playerCarY = 730;
    private int secondCarX = 150, secondCarY = 400;
    private int thirdCarX = 400, thirdCarY = 0;
    private int speed = 2, velocityX = 0, velocityY = 0, score = 0;
    
    // Restart and Exit buttons
    private JButton restartButton, exitButton;
    private boolean gameOver = false;

    public Car() {
        super.setDoubleBuffered(true);
        t = new javax.swing.Timer(0, this);
        JOptionPane.showMessageDialog(this, "Press Ok to start the game", "Car Race", JOptionPane.INFORMATION_MESSAGE);
        t.start();
        this.addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        
        // Initialize buttons but don't display yet
        initButtons();
    }

    public Timer getTimer() {
        return t;
    }

    // Initialize buttons (but hidden at first)
    public void initButtons() {
        // Create Restart Button
        restartButton = new JButton("Restart") {
            // Override the paintComponent method to create curved edges
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());  // Set the button background color
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);  // Create rounded rectangle
                super.paintComponent(g);
            }
        };
        restartButton.setBounds(210, 600, 150, 50);
        restartButton.setOpaque(false);  // To make the custom background visible
        restartButton.setContentAreaFilled(false);  // Remove default button fill
        restartButton.setBorderPainted(false);  // Remove default button border
        restartButton.setForeground(Color.WHITE);  // Set text color
        restartButton.setBackground(new Color(60, 179, 113));  // Set background color (green)
        restartButton.setVisible(false);
        restartButton.addActionListener(e -> resetGame());
    
        // Create Exit Button
        exitButton = new JButton("Exit") {
            // Override to create curved edges
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        exitButton.setBounds(430, 600, 150, 50);
        exitButton.setOpaque(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(255, 69, 0));  // Set background color (orange-red)
        exitButton.setVisible(false);
        exitButton.addActionListener(e -> System.exit(0));
    
        // Set custom layout and add buttons
        this.setLayout(null);
        this.add(restartButton);
        this.add(exitButton);
    }

    public void resetGame() {
        playerCarX = 980;
        playerCarY = 730;
        secondCarY = 400;
        thirdCarY = 0;
        score = 0;
        velocityX = 0;
        velocityY = 0;
        gameOver = false;

        // Hide buttons again
        restartButton.setVisible(false);
        exitButton.setVisible(false);
        
        t.start();  // Restart the game timer
        repaint();  // Refresh screen
    }

    public void OtherCars() {
        try {
            playerCar = ImageIO.read(new File("Pics/PlayerCar.png"));
            secondCar = ImageIO.read(new File("Pics/secondCar.png"));
            thirdCar = ImageIO.read(new File("Pics/thirdCar.png"));
        } catch (IOException ex) {
            System.out.println("Image not found");
        }
    }

    public void paintComponent(Graphics g) {
        OtherCars();
        super.paintComponent(g);
        if (playerCar != null) {
            g.drawImage(playerCar, playerCarX, playerCarY, null);
        }
    
        // Draw the second car if the image has been successfully loaded
        if (secondCar != null) {
            g.drawImage(secondCar, secondCarX, secondCarY, null);
        }
    
        // Draw the third car if the image has been successfully loaded
        if (thirdCar != null) {
            g.drawImage(thirdCar, thirdCarX, thirdCarY, null);
        }

        g.setColor(Color.yellow);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Score: " + score, 1, 30);
        
        if (gameOver) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 80));
            g.drawString("Game Over", 170, 300);
            
            // Make buttons visible when game is over
            restartButton.setVisible(true);
            exitButton.setVisible(true);
        }
    }

    public void move() {
        secondCarY = secondCarY + speed;
        thirdCarY = thirdCarY + speed;
        
        if (secondCarY == 920) {
            secondCarY = -250;
            score++;
        }
        if (thirdCarY == 920) {
            thirdCarY = -250;
            score++;
        }

        if (checkCollision()) {
            gameOver = true;
            t.stop();  // Stop the game when the player collides
        }
        repaint();
    }

    // Method to check collision of cars
public boolean checkCollision() {
    // Ensure that the images are loaded before checking collisions
    if (playerCar == null || secondCar == null || thirdCar == null) {
        return false;  // If any image is not loaded, collision cannot happen
    }

    // Create Rectangles for collision detection
    Rectangle playerRect = new Rectangle(playerCarX + 65, playerCarY, playerCar.getWidth() - 130, playerCar.getHeight());
    Rectangle secondRect = new Rectangle(secondCarX, secondCarY, secondCar.getWidth() - 75, secondCar.getHeight());
    Rectangle thirdRect = new Rectangle(thirdCarX + 65, thirdCarY, thirdCar.getWidth() - 100, thirdCar.getHeight());

    // Return true if there's any collision
    return playerRect.intersects(secondRect) || playerRect.intersects(thirdRect);
}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        if (c == KeyEvent.VK_LEFT) {
            velocityX = -35;
            velocityY = 0;
        }
        if (c == KeyEvent.VK_UP) {
            velocityX = 0;
            velocityY = -35;
        }
        if (c == KeyEvent.VK_RIGHT) {
            velocityX = 35;
            velocityY = 0;
        }
        if (c == KeyEvent.VK_DOWN) {
            velocityX = 0;
            velocityY = 35;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    // Method to limit the car's movement to the road only
    public void limit() {
        if (playerCarX < 100) {
            velocityX = 0;
            playerCarX = 100;
        }
        if (playerCarX > 450) {
            velocityX = 0;
            playerCarX = 450;
        }
        if (playerCarY < 0) {
            velocityY = 0;
            playerCarY = 0;
        }
        if (playerCarY > 780) {
            velocityY = 0;
            playerCarY = 780;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OtherCars();
        move();
        limit();
        playerCarX += velocityX;
        velocityX = 0;
        playerCarY += velocityY;
        velocityY = -1;
        repaint();
    }
}
