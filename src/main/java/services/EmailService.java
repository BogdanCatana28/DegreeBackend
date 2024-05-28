package services;

import model.Appointment;
import model.DayOff;
import model.User;

public interface EmailService {
    void sendPassword(User user);

    void sendAppointmentConfirmation(Appointment appointment);
    void sendPasswordResetLink(String email, String resetLink);

    void sendAppointmentCancellation(Appointment appointment);

    void sendDayOffConfirmation(DayOff dayOff);
}
