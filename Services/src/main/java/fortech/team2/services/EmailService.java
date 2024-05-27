package fortech.team2.services;

import fortech.team2.model.Appointment;
import fortech.team2.model.DayOff;
import fortech.team2.model.User;

public interface EmailService {
    void sendPassword(User user);

    void sendAppointmentConfirmation(Appointment appointment);
    void sendPasswordResetLink(String email, String resetLink);

    void sendAppointmentCancellation(Appointment appointment);

    void sendDayOffConfirmation(DayOff dayOff);
}
