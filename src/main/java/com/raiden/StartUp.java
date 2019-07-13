package com.raiden;

import com.raiden.viwe.SwingConsole;
import com.raiden.viwe.TextAreaFrame;

/**
 * *主函数入口，启动类
 * @author Raiden
 *
 */
public class StartUp {

	public static void main(String[] args) {
		SwingConsole.run(new TextAreaFrame(), 1250, 1000);
	}
}
