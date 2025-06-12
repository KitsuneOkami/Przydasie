package org.example.email;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.example.model.Auction;

import java.util.Properties;
import java.util.logging.Logger;

@Stateless
public class EmailService
{
	private static final Logger logger = Logger.getLogger(EmailService.class.getName());

	@Resource(name = "java:/mail/przydasie")
	private Session session;

	public void sendAuctionWinEmail(String recipientEmail, Auction auction)
	{
		try
		{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("no-reply@przydasie.pl"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
			message.setSubject("Gratulacje! Wygrałeś aukcję: "+auction.getName());
			message.setText("""
					Dziękujemy za udział w naszej aukcji!
					Zespół Przydasie.pl""");

			Transport.send(message);
		} catch(MessagingException e)
		{
			logger.severe("Failed to send auction win email: "+e.getMessage());
			throw new RuntimeException("Failed to send auction win email", e);
		} finally
		{
			logger.info("Auction win email sent to: "+recipientEmail);
		}
	}
}
