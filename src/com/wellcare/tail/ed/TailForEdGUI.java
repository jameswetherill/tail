/**
 * 
 */
package com.wellcare.tail.ed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

/**
 * com.wellcare.tail.ed.TailForEdGUI
 */
public class TailForEdGUI extends JPanel implements ActionListener {
	private static final long serialVersionUID = -3629941745008250496L;
	private JFileChooser fc;
	private JTextField fileNameField;
	private JTextArea textArea;
	private Main main;
	private JButton fileDialog;
	private JButton execute;
	private JButton stop;
	private JCheckBox email;
	private FileReader reader;

	/**
	 * Constructor
	 *
	 * @param main
	 */
	public TailForEdGUI(Main main) {
		this.main = main;
		this.setSize(400, 400);
		init();

	}

	/**
	 * THis lays out the panel that has all the fields in it. void
	 */
	private void init() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		textArea = new JTextArea(20, 80);
		JScrollPane scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		JLabel fileChooserLabel = new JLabel("File");
		fileNameField = new JTextField(40);

		fileDialog = new JButton("Select File");
		fileDialog.addActionListener(this);

		email = new JCheckBox("Use Email", false);
		email.addActionListener(this);

		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);

		execute = new JButton("execute");
		execute.addActionListener(this);
		execute.setEnabled(false);

		stop = new JButton("stop");
		stop.addActionListener(this);
		stop.setEnabled(false);
				
		JPanel top = new JPanel();
		GroupLayout layout2 = new GroupLayout(top);
		top.setLayout(layout2);
		layout2.setAutoCreateGaps(true);
		layout2.setAutoCreateContainerGaps(true);

		layout2.setHorizontalGroup(layout2.createSequentialGroup().addGroup(layout2.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(email).addComponent(fileChooserLabel)).addComponent(fileNameField).addComponent(fileDialog));

		layout2.setVerticalGroup(layout2.createSequentialGroup().addComponent(email).addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(fileChooserLabel).addComponent(fileNameField).addComponent(fileDialog)));

		JPanel bottom = new JPanel();
		GroupLayout layout3 = new GroupLayout(bottom);
		bottom.setLayout(layout3);
		layout3.setAutoCreateGaps(true);
		layout3.setAutoCreateContainerGaps(true);

		layout3.setHorizontalGroup(layout3.createSequentialGroup().addGroup(layout3.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(sep).addComponent(scrollPane).addComponent(execute).addComponent(stop)));

		layout3.setVerticalGroup(layout3.createSequentialGroup().addComponent(sep).addComponent(scrollPane).addComponent(execute).addComponent(stop));

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(top).addComponent(bottom)));

		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(top).addComponent(bottom));

	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent) This is so that we can listen to
	 * the fields and buttons.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(fileDialog)) {
			// Set up the file chooser.
			if (fc == null) {
				fc = new JFileChooser();
				fc.setMultiSelectionEnabled(false);
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setFileFilter(new MyFileFilter());
			}

			// Show it.
			int returnVal = fc.showDialog(this, "Select");

			// Process the results.
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				fileNameField.setText(file.getAbsolutePath());
				main.getOptions().setFile(file.getAbsolutePath());
				execute.setEnabled(true);
			}
			// Reset the file chooser for the next time it's shown.
			fc.setSelectedFile(null);
		}
		if (e.getSource().equals(email)) {
			main.getOptions().setSendEmail(email.isSelected());
		}
		if (e.getSource().equals(execute)) {
			if (reader == null) {
				reader = new FileReader(main, textArea);
			}
			execute.setEnabled(false);
			stop.setEnabled(true);
			reader.startWork();
		}
		if (e.getSource().equals(stop)) {
			if (reader != null) {
				reader.stop();
				stop.setEnabled(false);
				execute.setEnabled(true);
				
			}
		}
	}

	/* ImageFilter.java is used by FileChooserDemo2.java. */
	public class MyFileFilter extends FileFilter {

		// Accept all directories and all gif, jpg, tiff, or png files.
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}

			String extension = f.getName().substring(f.getName().lastIndexOf(".") + 1, f.getName().length());
			if (extension != null) {
				if (extension.equals("txt") || extension.equals("log")) {
					return true;
				} else {
					return false;
				}
			}

			return false;
		}

		// The description of this filter
		public String getDescription() {
			return "Just txt or logs";
		}
	}

}
