package com.company;

import javax.swing.*;

public class Frame extends JFrame {
    Frame(){
        this.add(new Panel());
        this.setTitle("SNAKE");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        // Frame is aligned center
        this.setLocationRelativeTo(null);
    }
}
