package it.unitn.favourexchange.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


//Class is extending AsyncTask because this class is going to perform a networking operation
public class SendMail extends AsyncTask<Void, Void, Void> {
    
    //Variable declaration
    private Context context;
    private Session emailSession;
    private MimeMessage emailMessage;

    //Information to send Email
    private String toEmail;
    private String subject;
    private String body;

    //Progressdialog to show while sending toEmail
    private ProgressDialog progressDialog;

    //Emails are sent from this account.
    public static final String EMAIL_FROM ="example@mail.com";
    public static final String EMAIL_FROM_PASSWORD ="password";

    //SMTP configuration
    public static final String EMAIL_HOST ="smtp.yandex.ru";
    public static final String EMAIL_PORT ="465";


    //Class Constructor
    public SendMail(Context context, String email, String subject, String message) {
        //Initializing variables
        this.context = context;
        this.toEmail = email;
        this.subject = subject;
        this.body = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending toEmail
        progressDialog = ProgressDialog.show(context, "Registering", "Please wait...", false, false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success body
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Almost done")
                .setMessage("Check your toEmail account to complete the registration.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties emailProperties = createEmailProperties();

        //Creating a new emailSession
        emailSession = createEmailSession(emailProperties);
        //Create email message
        emailMessage = createEmailMessage(emailSession, toEmail, subject, body);

        sendMessage(emailSession, emailMessage);

        return null;
    }

    private Properties createEmailProperties() {
        Properties properties = new Properties();

        //Configuring properties for Yandex
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.host", EMAIL_HOST);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", EMAIL_PORT);
        properties.put("mail.smtp.socketFactory.port", EMAIL_PORT);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.quitwait", "false");

        return properties;
    }

    private Session createEmailSession(Properties emailProperties) {
        return Session.getDefaultInstance(emailProperties,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL_FROM, EMAIL_FROM_PASSWORD);
                    }
                });
    }

    private MimeMessage createEmailMessage(Session session, String emailRecipient, String emailSubject, String emailBody) {

        MimeMessage emailMessage = new MimeMessage(session);

        try {
            emailMessage.setFrom(new InternetAddress(EMAIL_FROM, EMAIL_FROM));
            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress((emailRecipient)));
            emailMessage.setSubject(emailSubject);
            emailMessage.setContent(emailBody, "text/html");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return emailMessage;
    }

    public void sendMessage(Session emailSession, MimeMessage emailMessage) {

        try {
            Transport transport = emailSession.getTransport("smtp");
            transport.connect(EMAIL_HOST, Integer.parseInt(EMAIL_PORT),
                    EMAIL_FROM, EMAIL_FROM_PASSWORD);
            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
            transport.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}