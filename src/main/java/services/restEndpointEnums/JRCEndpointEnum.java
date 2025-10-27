package services.restEndpointEnums;

public enum JRCEndpointEnum {
    GET_JRC_VALUES("/cbam-euauth-rest/api/reference-data/getJRCValues"),
    GET_DRAFT_JRC_VALUES("/cbam-euauth-rest/api/reference-data/jrcValues/draft"),
    UPLOAD_JRC_FILE("/cbam-euauth-rest/api/reference-data/uploadJRCFile"),
    LIST_JRC_FILE_UPLOADS("/cbam-euauth-rest/api/reference-data/listJRCFileUploads"),
    JRC_ENTRY_HISTORY("/cbam-euauth-rest/api/reference-data/jrcValues/history/"),
    PUBLISH_ALL("/cbam-euauth-rest/api/reference-data/jrcValues/draft/publish"),
    PUBLISH_SPECIFIC("/cbam-euauth-rest/api/reference-data/jrcValues/draft/publish/"),
    DELETE_DRAFT_ENTRY("/cbam-euauth-rest/api/reference-data/jrcValues/draft/");

    JRCEndpointEnum(String path) {
        this.path = path;
    }

    private String path;

    public String getPath() {
        return path;
    }

}
