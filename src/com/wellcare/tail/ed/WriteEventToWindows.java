/**
 * 
 */
package com.wellcare.tail.ed;

import java.io.IOException;
import java.util.logging.Level;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.nt.NTEventLogAppender;

/**
 * com.wellcare.tail.ed.WriteEventToWindows
 */
public class WriteEventToWindows {

	public enum SEVERITY {
		Critical(30), Error(40), Warning(50), Information(80), Verbose(100);
		private int level;

		SEVERITY(int level) {
			this.level = level;
		}

		public int getLevel() {
			return level;
		}
	}

	private static Logger logger = Logger.getLogger(WriteEventToWindows.class);
	NTEventLogAppender eventLogAppender;
	/**
	 * Constructor
	 *
	 */
	public WriteEventToWindows() {
		BasicConfigurator.configure(); 
		eventLogAppender = new NTEventLogAppender();
		logger.addAppender(eventLogAppender);
	}

	public void writeEvent(String description, String string) throws IOException {		
		eventLogAppender.setSource(string); 
		eventLogAppender.setLayout(new PatternLayout("%m")); 
		eventLogAppender.activateOptions(); 
		logger.info(description); 
		
	}
	

}
