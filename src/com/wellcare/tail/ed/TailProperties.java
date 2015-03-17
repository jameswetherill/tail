/**
 * 
 */
package com.wellcare.tail.ed;

import java.util.Properties;

/**
 * com.wellcare.tail.ed.TailProperties
 */
public class TailProperties extends Properties {
	private static final long serialVersionUID = 2271229112893720050L;

	public long getPauseTime(){
		return Long.parseLong(getProperty("reader.pause.time"));
	}

	public String[] getPatterns(){
		return getProperty("patterns").split(",");
	}
}
