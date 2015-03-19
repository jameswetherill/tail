/**
 * Creator : James Wetherill
 */
package com.wellcare.tail.ed;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * com.wellcare.tail.ed.SendEmail
 */
public class SendEmail {

	private Main main;

	/**
	 * Constructor
	 *
	 * @param main
	 */
	public SendEmail(Main main) {
		this.main = main;
	}

	public void sendMessage(String messText, String filePath) {

		Session session = Session.getDefaultInstance(main.getProperties());
		

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(main.getProperties().getProperty("from.email")));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(main.getProperties().getProperty("to.email")));
			message.setSubject(main.getProperties().getProperty("subject.email") + " : " +filePath);
			message.setText(messText);

			// Send message
			Transport.send(message);
			System.out.println("message sent successfully....");

		} catch (MessagingException mex) {
			System.err.println(mex.getMessage());
		}
	}

}
