/**
 * Creator : James Wetherill
 */
package com.jw.tail.ed;

import java.awt.BorderLayout;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFrame;

import com.github.jankroken.commandline.CommandLineParser;
import com.github.jankroken.commandline.OptionStyle;

/**
 * com.jw.tail.ed.Main
 */
public class Main {
	private CommandArgs options;
	private TailProperties applicationProps;

	/**
	 * Constructor
	 *
	 */
	public Main() {

	}

	/**
	 * @param args void
	 */
	public static void main(String[] args) {
		try {
			Main main = new Main();
			main.setOptions(CommandLineParser.parse(CommandArgs.class, args, OptionStyle.SIMPLE));
			try {
				main.loadProperties();
			} catch (IOException ex) {
				System.err.println("No Properties found!!");
				System.err.println(main.getOptions().getHelp());
				System.exit(-1);
			}
			main.init();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param options2 void
	 */
	private void init() {
		// check have the correct things to operate the program..
		if (!options.isShowGui() && options.getFile() == null) {
			System.err.println(options.getHelp());
			System.exit(-1);
		}
		if (options.isShowGui()) {
			JFrame frame = new JFrame("Tail");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(new TailForEdGUI(this), BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
		} else {
			FileReader reader = new FileReader(this, null);
			reader.startWork();
		}
	}

	private void loadProperties() throws IOException {
		String prop = "./TailForEd.properties";
		if (options.getPropertiesFile() != null) {
			prop = options.getPropertiesFile();
		}
		applicationProps = new TailProperties();
		FileInputStream in = new FileInputStream(prop);
		applicationProps.load(in);
		in.close();
	}

	public CommandArgs getOptions() {
		return options;
	}

	public void setOptions(CommandArgs options) {
		this.options = options;
	}

	/**
	 * @return Properties
	 */
	public TailProperties getProperties() {
		return applicationProps;
	}

}
