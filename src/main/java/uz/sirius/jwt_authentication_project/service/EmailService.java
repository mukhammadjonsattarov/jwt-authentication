package uz.sirius.jwt_authentication_project.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String token) {
        String subject = "Account Verification";
        String verificationUrl = "http://localhost:8080/api/auth/verify?token=" + token;
        String message = """
                Salom!
                Ro‘yxatdan o‘tganingiz uchun rahmat.
                Iltimos, akkauntingizni faollashtirish uchun quyidagi havolani bosing:
                
                %s
                
                Agar siz bu amaliyotni bajarmagan bo‘lsangiz, e’tiborsiz qoldiring.
                """.formatted(verificationUrl);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
