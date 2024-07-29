package compile;

import java.util.Date;

public class Appointment {
    private String username;
    private Date appointmentDate;
    private String counselorName;

    public Appointment(String username, Date appointmentDate, String counselorName) {
        this.username = username;
        this.appointmentDate = appointmentDate;
        this.counselorName = counselorName;
    }

    public String getUsername() {
        return username;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public String getCounselorName() {
        return counselorName;
    }

    @Override
    public String toString() {
        return "Username: " + username + ", Date: " + appointmentDate + ", Counselor Name: " + counselorName;
    }
}