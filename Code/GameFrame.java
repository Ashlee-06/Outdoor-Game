package src;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame implements ActionListener {
    private Car car;  // Reference to the Car object for controlling game state
    
    public GameFrame() {
        super.setTitle("Car Race");
        super.setBounds(0, 0, 760, 1035);
        car = new Car();
        super.add(car);
        
        // Create Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Menu");
        
        // Pause Menu Item
        JMenuItem pauseItem = new JMenuItem("Pause");
        pauseItem.addActionListener(this);
        gameMenu.add(pauseItem);
        
        // Restart Menu Item
        JMenuItem restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(this);
        gameMenu.add(restartItem);
        
        // Add menu to the bar
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
        
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
    }
    
    // Handle menu actions
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (command.equals("Pause")) {
            car.getTimer().stop();  
        } 
        else if (command.equals("Restart")) {
            car.getTimer().restart();
            car.resetGame();  
        }
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
