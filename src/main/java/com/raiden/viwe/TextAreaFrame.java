package com.raiden.viwe;

import com.raiden.base.Strategy;
import com.raiden.controller.Controller;
import com.raiden.logs.LogHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextAreaFrame extends JFrame{
	/**
	 *
	 */
	private static final long serialVersionUID = 2568996309431093667L;

    private static final String CONTENT_TEXT = "请输入要比较的json。";
	private JButton b1 = new JButton("排序");
	private JButton b2 = new JButton("比较");
    private JButton b3 = new JButton("比较属性名");
	private JTextPane left = new JTextPane();
    private JTextPane right = new JTextPane();
	private Controller controller = new Controller();
	private LogHandler handler = LogHandler.newInstance();

	public TextAreaFrame() {
		controller.start();
		b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.add(left,right);
            }
        });
		b2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.add(Strategy.COMPARE, left,right);
			}
		});
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.add(Strategy.COMPARE_FIELD_NAME, left,right);
            }
        });
        left.setText(CONTENT_TEXT);
        right.setText(CONTENT_TEXT);
        JPanel p = new JPanel();
        p.add(new JScrollPane(left));
        p.add(new JScrollPane(right));
        p.setLayout(new GridLayout(1, 3, 10, 30));
        add(p);
        JPanel p2 = new JPanel();
        p2.add(b1);
        p2.add(b2);
        p2.add(b3);
        p2.setLayout(new FlowLayout(1,3,2));
        add(p2, BorderLayout.SOUTH);
		handler.start();
	}
}