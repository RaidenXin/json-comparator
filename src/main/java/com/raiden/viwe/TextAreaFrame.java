package com.raiden.viwe;

import com.raiden.controller.Controller;
import com.raiden.logs.LogHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class TextAreaFrame extends JFrame{
	/**
	 *
	 */
	private static final long serialVersionUID = 2568996309431093667L;

    private static final String CONTENT_TEXT = "请输入要比较的json。";
	private JButton b1 = new JButton("sort");
	private JButton b2 = new JButton("Obtain");
	private JTextArea t = new JTextArea();
    private JTextArea t2 = new JTextArea();
	private Controller controller = new Controller();
	private LogHandler handler = LogHandler.newInstance();

	public TextAreaFrame() {
		controller.start();
		b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.add(Arrays.asList(t,t2));
            }
        });
//		b2.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				controller.obtainFiles(pathText.getText(), contentText.getText());
//			}
//		});
        t.setText(CONTENT_TEXT);
        t2.setText(CONTENT_TEXT);
        t.setLineWrap(true);
        t2.setLineWrap(true);
        JPanel p = new JPanel();
        p.add(new JScrollPane(t));
        p.add(new JScrollPane(t2));
        p.setLayout(new GridLayout(1, 2, 10, 30));
        add(p);
        JPanel p2 = new JPanel();
        p2.add(b1);
//		p2.add(b2);
        p2.setLayout(new FlowLayout(1,2,2));
        add(p2, BorderLayout.SOUTH);
		handler.start();
	}
}