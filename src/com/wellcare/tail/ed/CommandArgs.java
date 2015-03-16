/**
 * 
 */
package com.wellcare.tail.ed;

import com.github.jankroken.commandline.annotations.LongSwitch;
import com.github.jankroken.commandline.annotations.Option;
import com.github.jankroken.commandline.annotations.ShortSwitch;
import com.github.jankroken.commandline.annotations.SingleArgument;
import com.github.jankroken.commandline.annotations.Toggle;

public class CommandArgs {

	private String file;
	private boolean sendEmail = false;
	private boolean showGui = false;
	private int numberLines = 200;
	private String propertiesFile;
	private String applicationName;

	@Option
	@ShortSwitch("f")
	@LongSwitch("file")
	@SingleArgument
	public void setFile(String file) {
		this.file = file;
	}

	@Option
	@ShortSwitch("s")
	@LongSwitch("sendemail")
	@Toggle(true)
	public void setSendEmail(boolean sendemail) {
		this.sendEmail = sendemail;
	}

	@Option
	@ShortSwitch("g")
	@LongSwitch("gui")
	@Toggle(true)
	public void setShowGui(boolean gui) {
		this.showGui = gui;
	}

	@Option
	@ShortSwitch("n")
	@LongSwitch("numberlines")
	@SingleArgument
	public void setNumberLines(String numberlines) {
		this.numberLines = Integer.parseInt(numberlines);
	}

	/**
	 * @return String the file to return.
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @return boolean the sendEmail to return.
	 */
	public boolean isSendEmail() {
		return sendEmail;
	}

	/**
	 * @return boolean the showGui to return.
	 */
	public boolean isShowGui() {
		return showGui;
	}

	public int getNumberLines() {
		return numberLines;
	}

	public String getHelp() {
		StringBuffer bu = new StringBuffer();
		bu.append("The usage is: java com.wellcare.tail.ed.Main (-f, -filename) <filename> (-g -gui) (-s -sendmail) (-p -properties) (-a -application)");
		bu.append("\n   (-f, -filename) <filename> include path example C:/temp/test.log. \n\t Filename is manditory if no GUI is shown.  ");
		bu.append("\n\t If GUI is shown then the file can be selected from it.");
		bu.append("\n   (-g -gui) this will show the GUI. Default is don't show gui.");
		bu.append("\n   (-s -sendmail) this will sendemails to the people in the email list in the properties. \n\t Default is don't send Emails.");
		bu.append(" \n\t If GUI is shown then a checkbox can be set sendemails");
		bu.append("\n   (-p -properties) this is the properties file. \n\t Default is the jar location. \n\t Properties file format is: \n\t name=value\n\t name2=value");
		bu.append("\n   (-a -application) this will set the application name so that the values are set to the event viewer. \n\t If not set then nothing will be sent to the event log.");

		return bu.toString();
	}

	/**
	 * @return String the propertiesFile to return.
	 */
	public String getPropertiesFile() {
		return propertiesFile;
	}

	/**
	 * @param properties the String:propertiesFile to set.
	 */
	@Option
	@ShortSwitch("p")
	@LongSwitch("properties")
	@SingleArgument
	public void setPropertiesFile(String properties) {
		this.propertiesFile = properties;
	}

	/**
	 * @return String the applicationName to return.
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName the String:applicationName to set.
	 */
	@Option
	@ShortSwitch("a")
	@LongSwitch("application")
	@SingleArgument
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
}