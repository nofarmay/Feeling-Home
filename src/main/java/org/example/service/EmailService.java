package org.example.service;

import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private UserRepository userRepository;

    public void sendEmergencyEmailToAllUsers(String subject, String message) {
        try {
            //
            List<String> testEmails = Arrays.asList(
//                    "odelya.tfilin@gmail.com",
//                    "orhoter12@gmail.com",
                    "nofarmaymon789@gmail.com",
                    "nofarmaymon123@gmail.com"
//                    "ben.catalan@grunitech.com",
//                    "ran.elias@amdocs.com",
//                    "max.delmer@amdocs.com",
//                    "nofar.maymon@grunitech.com",
//                    "or.harush@grunitech.com",
//                    "odelya.tfilin@grunitech.com",
//                    "ilan.akoun@grunitech.com",
//                    "einav.balestra@grunitech.com",
//                    "jonathane.chicheportiche@grunitech.com",
//                    "michal.cohen@grunitech.com",
//                    "inbar.hason@grunitech.com",
//                    "noy.kakoun@grunitech.com",
//                    "vladislav.spivak@grunitech.com",
//                    "nofarmaymon112@gmail.com",
//                    "lorin.cohen@grunitech.com",
//                    "yagel.hagbi@grunitech.com",
//                    "uria.kohn@grunitech.com",
//                    "alex.mays@grunitech.com",
//                    "michal.pinto.b@grunitech.com",
//                    "yelena.sibirets@grunitech.com",
//                    "daniel.vinokur@grunitech.com",
//                    "harel.vollman@grunitech.com",
//                    "maayan.wasserbach@grunitech.com",
//                    "nissim.brami@grunitech.com",
//                    "gal.cohen@grunitech.com",
//                    "margishim@sderot.matnasim.co.il",
//                    "eladk@sederot.muni.il",
//                    "hofit.brahami@amdocs.com"
            );
            List<User> users = userRepository.findByEmailIn(testEmails);

            // לוג המשתמשים שנמצאו
            for (User user : users) {
                logger.info("נמצא משתמש: {} עם אימייל: {}", user.getUsername(), user.getEmail());
            }

            // שליחת המיילים
            for (User user : users) {
                try {
                    SimpleMailMessage mailMessage = new SimpleMailMessage();
                    mailMessage.setTo(user.getEmail());
                    mailMessage.setFrom("margishim2@gmail.com");
                    mailMessage.setSubject("FEELING HOME - הכרזה על פרש תורכי");

                    String formattedMessage = String.format("""
                        שלום %s,
                        
                        הריני להודיע כי הוכרז פרש תורכי במערכת FEELING HOME.
                        
                        במידה וחלו שינויים בנסיבות המיוחדות שלך, אנא עדכן/י את המערכת בהקדם.
                        חשוב לנו להדגיש כי קהילת FEELING HOME זמינה עבורך תמיד, ובמיוחד במצבים כאלה.
                        
                        %s
                        
                        אנחנו כאן בשבילך,
                        טל כהן
                        רכזת FEELING HOME
                        """,
                            user.getUsername() != null ? user.getUsername() : "משתמש יקר",
                            message
                    );

                    mailMessage.setText(formattedMessage);
                    emailSender.send(mailMessage);
                    logger.info("מייל נשלח בהצלחה ל: {}", user.getEmail());
                } catch (MailException e) {
                    logger.error("שגיאה בשליחת מייל ל-{}: {}", user.getEmail(), e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("שגיאה קריטית בשליחת מיילים: {}", e.getMessage());
            throw new RuntimeException("שגיאה בשליחת מיילים", e);
        }
    }
}