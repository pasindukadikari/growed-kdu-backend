package lk.kdu.growed.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("GrowED Team <dunuwilapiyumi28@gmail.com>");
            message.setTo(toEmail);
            message.setSubject("GrowED Password Reset OTP");
            message.setText("Your OTP code is: " + code + "\nThis will expire in 10 minutes.");
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
