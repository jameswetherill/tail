/**
 * Creator : James Wetherill
 */
package com.jw.tail.ed;

import java.util.Properties;

/**
 * com.jw.tail.ed.TailProperties
 */
public class TailProperties extends Properties {
	private static final long serialVersionUID = 2271229112893720050L;

	public long getPauseTime(){
		return Long.parseLong(getProperty("reader.pause.time"));
	}

	public String[] getPatterns() {
	    if( getProperty("patterns.regular.expression").equals("true")){
	        String pat = getProperty("patterns");
	        return new String[]{pat};
	    }
		return getProperty("patterns").split(",");
	}
}
