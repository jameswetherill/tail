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
	private ReaderWorker[] workers;

	/**
	 * Constructor
	 *
	 */
	public FileReader(Main main, Component txtarea) {
		this.main = main;
		this.txtarea = txtarea;
	}

	public void startWork() {
		workers = new ReaderWorker[main.getOptions().getFiles().length];
		for (int i = 0; i < main.getOptions().getFiles().length;i++){
			ReaderWorker worker = new ReaderWorker(main.getOptions().getFiles()[i], main, txtarea);
			new Thread(worker, main.getOptions().getFiles()[i]).start();
			workers[i] = worker;		
		}
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
		private String filePath;

		public ReaderWorker(String filePath, Main main, Component txtarea)
		{
			this.filePath = filePath;
			this.main = main;
			patterns = main.getProperties().getPatterns();			
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
				File file = new File(filePath);
				
				int lineNumber = main.getOptions().getNumberLines();
				while (running) {
					file = checkForNewFile(file);
					StringBuilder strBld = readFile(file, lineNumber);
					String[] messages = strBld.toString().split("\n");
					StringBuilder errors = new StringBuilder();
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
									if (main.getOptions().isSendEmail())
									{
										errors.append("\n"+text);
									}
								}
							}
						}
					}
					if (main.getOptions().isSendEmail() && errors.length() > 0) {
						sendEmail.sendMessage(errors.toString(), filePath);
					}
					lineNumber = -1;
					Thread.sleep(main.getProperties().getPauseTime());
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
		
		
		private File checkForNewFile(File file){
			File nf = null;			
			File parent = file.getParentFile();
			String fname = file.getName();
			String name = fname.substring(0, fname.lastIndexOf("."));
			long last = file.lastModified();
			for(File fl:parent.listFiles()){
				if(fl.getName().contains(name) && fl.lastModified() > last){
					nf = fl;
					lastlength = 0;
				}
			}
			if(nf == null){
				nf = file;
			}
			
			return nf;
		}

	}

	/**
	 *  void
	 */
	public void stop() {
		if(workers[0]!=null){
			workers[0].stop();
		}
	}
	/**
	 *  void
	 */
	public void stop(int index) {
		if(workers[index]!=null){
			workers[index].stop();
		}
	}
}
