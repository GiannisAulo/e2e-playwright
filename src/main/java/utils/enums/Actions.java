package utils.enums;

public enum Actions {
    EDIT("Edit"),
    DELETE("Delete"),
    UPDATE("Update"),
    ADD("Add"),
    DOWNLOAD("Download"),
    UPLOAD("Upload");

    private final String identifier;

    Actions(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
