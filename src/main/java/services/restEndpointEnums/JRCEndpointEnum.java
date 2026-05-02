package services.restEndpointEnums;

public enum JRCEndpointEnum {
    GET_JRC_VALUES("/}-rest/api/reference-data/getJRCValues"),
    GET_DRAFT_JRC_VALUES("/rest/api/reference-data/jrcValues/draft"),
    UPLOAD_JRC_FILE("/rest/api/reference-data/uploadJRCFile"),
    LIST_JRC_FILE_UPLOADS("/rest/api/reference-data/listJRCFileUploads"),
    JRC_ENTRY_HISTORY("/rest/api/reference-data/jrcValues/history/"),
    PUBLISH_ALL("/rest/api/reference-data/jrcValues/draft/publish"),
    PUBLISH_SPECIFIC("/rest/api/reference-data/jrcValues/draft/publish/"),
    DELETE_DRAFT_ENTRY("/rest/api/reference-data/jrcValues/draft/");

    JRCEndpointEnum(String path) {
        this.path = path;
    }

    private String path;

    public String getPath() {
        return path;
    }

}
