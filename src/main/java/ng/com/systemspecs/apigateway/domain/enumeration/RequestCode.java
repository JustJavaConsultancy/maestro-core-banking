package ng.com.systemspecs.apigateway.domain.enumeration;

public enum RequestCode {

    CONNECTION_STATUS_CBA("cba_connection_status"), CONNECTION_STATUS_CBA_SUMMARY("cba_connection_status_summary"), CONNECTION_STATUS("connection_status"), CONNECTION_STATUS_SUMMARY("connection_status_summary"), CONNECTION_STATUS_LIST("connection_status_list"), CONNECTION_STATUS_RECENT_SUMMARIES("connection_status_recent_summaries"),

    DIRECTORY_CONTENTS("directory_contents"), DIRECTORY_FILE_CONTENTS("file_contents"), DIRECTORY_FILE_DOWNLOAD_AND_REMOVE("download_and_remove_file"), DIRECTORY_FILE_REMOVE("delete_file"), DIRECTORY_FILE_UPLOAD("upload_file"),

    SESSION_FEEDBACK("session_feedback"), SESSION_STATUS("session_status"), SESSION_STOP("stop_processing"), SESSION_TRASH("trash_session"), SESSION_PRIORITIZE("session_prioritize"), SESSION_RETRY("retry_session"), SESSION_RESEND("resend_session"), SESSION_REGENERATE("regenerate_session"), SESSION_CLOSE("close_session"),

    SERVICE_START("start_service"), SERVICE_STOP("stop_service"), SERVICE_RESTART("restart_service"), SERVICE_CONFIG("service_config"), SERVICE_CONFIG_EDIT("eidt_service_config"), SERVICE_NETWORK_ANALYSIS("trace_service"), SERVICE_GET_LOG("get_log"), SERVICE_CURRENT_STATUS("current_status"),

    PREFERENCES_LIST("preferences_list"), PREFERENCES_EDIT("preferences_edit"),

    ACCOUNT_STATEMENT("account_statement"), ACCOUNT_BALANCE("account_balance"), FUNDS_TRANSFER("funds_transfer"),

    ENGINE_STATUS("stp_engine_status"), ENGINE_SERVICE_START("start_stp_engine_service"), ENGINE_SERVICE_STOP("stop_stp_engine_service"), TRANSACTION_TRACE("transaction_trace"), TRANSACTION_DETAILS("transaction_trace"), POST_PAYMENT("post_payment"), OTP_VALIDATE("otp_validate"), MATRICS("Matrics"), CLOSE_SESSION("Close_session"), SESSION_ARCHIVE("Archive_session");

    private final String label;

    RequestCode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
