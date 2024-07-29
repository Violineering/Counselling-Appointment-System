package compile;

public class Feedback {
    private String username;
    private String counselorName;
    private String feedbackText;

    public Feedback(String username, String counselorName, String feedbackText) {
        this.username = username;
        this.counselorName = counselorName;
        this.feedbackText = feedbackText;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCounselorName() {
        return counselorName;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public String getFileString() {
        return this.username + ";" + this.counselorName + ";" + this.feedbackText;
    }

}