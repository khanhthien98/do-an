package com.example.helper.scheduler;

import java.util.Properties;
import java.util.concurrent.Callable;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.example.helper.entity.EmailJob;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailJobTask implements Callable<EmailJobTask> {

	private boolean error = false;
	private String port;
	private String emailRoot;
	private String passRoot;
	private EmailJob emailJob;

	public EmailJobTask(String port, String emailRoot, String passRoot, EmailJob emailJob) {
		super();
		this.port = port;
		this.emailRoot = emailRoot;
		this.passRoot = passRoot;
		this.emailJob = emailJob;
	}

	@Override
	public EmailJobTask call() throws Exception {
		try {
			if(emailJob.getUser().getUserEmail()!=null && !emailJob.getUser().getUserEmail().isEmpty()) {
				
				Properties mailServerProperties;
				Session getMailSession;
				MimeMessage mailMessage;
				
				// Step1: setup Mail Server
				mailServerProperties = System.getProperties();
				mailServerProperties.put("mail.smtp.port", "587");
				mailServerProperties.put("mail.smtp.auth", "true");
				mailServerProperties.put("mail.smtp.starttls.enable", "true");
				
				// Step2: get Mail Session
				getMailSession = Session.getDefaultInstance(mailServerProperties, null);
				mailMessage = new MimeMessage(getMailSession);
				
				mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailJob.getUser().getUserEmail())); 
				mailMessage.setSubject(emailJob.getSubject());
				String emailBody = emailJob.getContent();
				mailMessage.setContent(emailBody, "text/html");
				
				// Step3: Send mail
				Transport transport = getMailSession.getTransport("smtp");
				
				// Thay your_gmail thành gmail của bạn, thay your_password thành mật khẩu gmail
				// của bạn
				transport.connect("smtp.gmail.com", this.emailRoot, this.passRoot);
				transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
				transport.close();
				// done 
				this.emailJob.setStatus(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.emailJob.setStatus(1);
			this.setError(true);
		}
		return this;
	}

	public boolean isError() {
		return this.error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
