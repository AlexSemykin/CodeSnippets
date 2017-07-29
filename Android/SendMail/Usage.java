//Sends an email to verify email account.
SendMail sm = new SendMail(getActivity(), email, subject, url);
sm.execute();
