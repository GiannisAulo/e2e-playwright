package utils.enums;

public enum Actors {
    ECAuthAdmin("EC_OFF"),
    ECAuthReportReviewer("EC_OFF"),
    ECAuthReportReviewAdmin("EC_OFF"),
    ECAuthUploadSURV3Data("EC_OFF"),
    ECAuthorityViewer("EC_OFF"),
    ECAuthRefDataAdmin("EC_OFF"),
    ECAuthSensitiveRefDataAdmin("EC_OFF");

    public final String label;

    Actors(String label) {
        this.label = label;
    }

    public final String getLabel() {
        return label;
    }
}
