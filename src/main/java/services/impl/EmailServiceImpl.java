package services.impl;

import model.Appointment;
import model.DayOff;
import model.User;
import services.EmailService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${spring.mail.username}")
    private String sender;

    private final String senderName = "Neo Vet Clinic";

    @Value("classpath:/mail-images/logo.png")
    private Resource logo;

    @Override
    public void sendPassword(User user) {

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", user.getFirstName() + " " + user.getLastName());
        templateModel.put("password", user.getPassword());
        templateModel.put("senderName", senderName);
        templateModel.put("date", LocalDateTime.now());

        try {
            sendEmailWithTemplate(user.getEmail(), "Welcome to the clinic", templateModel, "sendPasswordTemplate.ftl");
        } catch (IOException | MessagingException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendAppointmentConfirmation(Appointment appointment) {

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", appointment.getOwnerFirstName() + " " + appointment.getOwnerLastName());
        templateModel.put("appointmentDate", appointment.getDateReservation());
        templateModel.put("patientName", appointment.getPatientName());
        templateModel.put("procedure", appointment.getProcedure().getName());
        templateModel.put("duration", appointment.getProcedure().getDuration());
        templateModel.put("senderName", senderName);
        templateModel.put("logo", logo);

        try {

            sendEmailWithTemplate(appointment.getOwnerEmail(), "Appointment confirmation", templateModel, "sendAppointmentConfirmationTemplate.ftl");
        } catch (IOException | MessagingException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendAppointmentCancellation(Appointment appointment) {

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", appointment.getOwnerFirstName() + " " + appointment.getOwnerLastName());
        templateModel.put("appointmentDate", appointment.getDateReservation());
        templateModel.put("patientName", appointment.getPatientName());
        templateModel.put("procedure", appointment.getProcedure().getName());
        templateModel.put("senderName", senderName);
        templateModel.put("logo", logo);

        try {

            sendEmailWithTemplate(appointment.getOwnerEmail(), "Appointment cancellation", templateModel, "sendAppointmentCancellationTemplate.ftl");
        } catch (IOException | MessagingException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendDayOffConfirmation(DayOff dayOff) {

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", dayOff.getMedic().getFirstName() + " " + dayOff.getMedic().getLastName());
        templateModel.put("dayOffDate", dayOff.getFreeDay());
        templateModel.put("senderName", senderName);
        templateModel.put("logo", logo);

        try {

            sendEmailWithTemplate(dayOff.getMedic().getEmail(), "Day off confirmation", templateModel, "sendDayOffConfirmationTemplate.ftl");
        } catch (IOException | MessagingException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPasswordResetLink(String email, String resetLink) {

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", "");
        templateModel.put("resetLink", resetLink);
        templateModel.put("senderName", senderName);

        try {
            sendEmailWithTemplate(email, "Password Reset Request", templateModel, "sendPasswordResetLinkTemplate.ftl");
        } catch (IOException | MessagingException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.addInline("logo", logo);
        Runnable runnable = () -> {
            try {
                javaMailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void sendEmailWithTemplate(String to, String subject, Map<String, Object> templateModel, String templateName) throws IOException, MessagingException, TemplateException {
        Template freemarkerTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);

        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);

        sendHtmlMessage(to, subject, htmlBody);
    }
}
