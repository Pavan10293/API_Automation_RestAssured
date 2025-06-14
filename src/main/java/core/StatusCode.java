package core;

public enum StatusCode {

    SUCCESS(200, "The request succeeded"),
    CREATED(201, "A new resource was created"),
    NO_CONTENT(204, "No content to send in the response body"),
    BAD_REQUEST(400, "Missing required field: name"),
    UNAUTHORISED(401, "Invalid access token"),
    NOT_FOUND(404, "Connot find the requested resource");

    public final int code;
    public final String responseMessage;

    StatusCode(int code, String responseMessage) {
        this.code = code;
        this.responseMessage = responseMessage;
    }

}
