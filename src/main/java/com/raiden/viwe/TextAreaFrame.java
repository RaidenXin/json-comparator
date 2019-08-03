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

    public static final String CONTENT_TEXT = "请输入要比较的json。";


	private JButton b1 = new JButton("排序");
	private JButton b2 = new JButton("比较");
    private JButton b3 = new JButton("转换");
	private JTextPane left = new JTextPane();
    private JTextPane right = new JTextPane();
	private Controller controller = new Controller();
	private LogHandler handler = LogHandler.newInstance();

	public TextAreaFrame() {
		controller.start();
		//设置排序按钮
		b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.add(Strategy.SORT,left,right);
            }
        });
		//设置比较按钮
		b2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.add(Strategy.COMPARE, left, right);
			}
		});
        //设置比较按钮
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.add(Strategy.CONVERT, left, right);
            }
        });
        left.setText(CONTENT_TEXT);
        right.setText(CONTENT_TEXT);
        JPanel p = new JPanel();
        //------------------设置滚动条
        JScrollBar jScrollBar = new JScrollBar();
        JScrollPane leftPane = new JScrollPane(left);
        leftPane.getVerticalScrollBar().setModel(jScrollBar.getModel());
        JScrollPane rightPane = new JScrollPane(right);
        rightPane.getVerticalScrollBar().setModel(jScrollBar.getModel());
        //-----------------
        p.add(leftPane);
        p.add(rightPane);
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