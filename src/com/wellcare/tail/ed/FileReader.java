/**
 * 
 */
package com.wellcare.tail.ed;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;


/**
 * com.wellcare.tail.ed.FileReader
 */
public class FileReader {

	private Main main;
	private long lastlength = 0;

	private Component txtarea;
	private ReaderWorker worker;

	/**
	 * Constructor
	 *
	 */
	public FileReader(Main main, Component txtarea) {
		this.main = main;
		this.txtarea = txtarea;
	}

	public void startWork() {
		worker = new ReaderWorker(main, txtarea);
		new Thread(worker).start();
	}

	/**
	 * @param file to read
	 * @param numberLines, lines from end of file to read.
	 * @return StringBuilder the data read from the file.
	 * @throws FileNotFoundException
	 * @throws IOException StringBuilder
	 */
	public StringBuilder readFile(File file, int numberLines) throws FileNotFoundException, IOException {
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
		int lines = 0;
		StringBuilder builder = new StringBuilder();
		long length = file.length();
		if (lastlength < length) {
			long start = length;

			length--;
			randomAccessFile.seek(lastlength);
			for (long seek = length; seek >= lastlength; --seek) {
				randomAccessFile.seek(seek);
				char c = (char) randomAccessFile.read();
				builder.insert(0, c);
				if (c == '\n') {
					lines++;
					if (numberLines != -1 && lines == numberLines) {
						break;
					}
				}
			}
			lastlength = start;
		}
		randomAccessFile.close();
		return builder;
	}

	public class ReaderWorker implements Runnable {
		private Main main;
		private String[] patterns;
		private boolean running = true;
		private Component txtarea;
		private WriteEventToWindows writeEvent;
		private SendEmail sendEmail;

		public ReaderWorker(Main main, Component txtarea) {
			this.main = main;
			String pattern = main.getProperties().getProperty("pattern");
			if (pattern.contains(",")) {
				patterns = pattern.split(",");
			} else {
				patterns = new String[] { pattern };
			}
			writeEvent = new WriteEventToWindows();
			sendEmail = new SendEmail(main);
			this.txtarea = txtarea;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				File file = new File(main.getOptions().getFile());
				int lineNumber = main.getOptions().getNumberLines();
				while (running) {
					StringBuilder strBld = readFile(file, lineNumber);
					String[] messages = strBld.toString().split("\n");
					for (String text : messages) {
						if (!"".equals(text)) {
							System.out.println(text);
							for (String pattern : patterns) {
								if (text.contains(pattern)) {
									if (txtarea != null) {
										((JTextArea) txtarea).append(text + "\n\r");
										((JTextArea) txtarea).setCaretPosition(((JTextArea) txtarea).getDocument().getLength());
									}
									if (main.getOptions().getApplicationName() != null) {
										writeEvent.writeEvent(text, main.getOptions().getApplicationName());
									}
									if (main.getOptions().isSendEmail()) {
										sendEmail.sendMessage(text);
									}
								}
							}
						}
					}
					lineNumber = -1;
					Thread.sleep(4000);
				}
				if(!running){
					((JTextArea) txtarea).getDocument().remove(0, ((JTextArea) txtarea).getDocument().getLength());
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		
		public void stop(){
			running = false;
		}

	}

	/**
	 *  void
	 */
	public void stop() {
		if(worker!=null){
			worker.stop();
		}
	}
}
