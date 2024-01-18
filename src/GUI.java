package src;

import javax.swing.*;
import java.awt.*;

public class GUI {
    public GUI(){
        JFrame frame = new JFrame("Library");
        CardLayout cardLayout = new CardLayout();

        // Login panelen
        JPanel loginPanel = new JPanel();

        // Hemsida panel  / boka böcker sida
        JPanel homepagePanel = new JPanel();

        // Min sida panel
        JPanel mypagePanel = new JPanel();








        frame.setSize(500,500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
