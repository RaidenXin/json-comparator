package com.raiden.viwe;

import javax.swing.*;

public class SwingConsole {

	public static void run(final JFrame f,final int width,final int height) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				f.setTitle("JsonUtil");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setSize(width, height);
				f.setVisible(true);
				f.setResizable(true);
			}
		});
	}
}
