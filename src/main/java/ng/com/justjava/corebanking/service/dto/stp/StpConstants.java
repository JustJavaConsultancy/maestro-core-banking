package ng.com.justjava.corebanking.service.dto.stp;

public class StpConstants {

    public static final String SESSION_NUMBER = "SESSION_NUMBER";

    public static final String RETRY_EXECUTION = "EXEC_RETRY";

    public static final String EXECUTION_CHANNEL_NAME = "EXEC_CHANNEL_NAME";

    public static final String PAYMENT_INSTRUCTION_COUNT = "paymentInstructionCount";

    public static final String ENCRYPTED_FILE = "ENCRYPTED_FILE";

    public static final String ENCRYPTED_KEY = "ENCRYPTED_KEY";

    public static final String DECRYPTED_FILE = "DECRYPTED_FILE";

    public static final String REQUEST_STATUS = "REQUEST_STATUS";

    public static final String CBA_POST_STARTED = "CBA_POST_STARTED";

    public static final String PROCESS_ERROR = "PROCESS_ERROR";

    public static final String PROCESSED_OK = "00";

    public static final String PROCESSED_WITH_ERROR = "PX01";

    public static final String SESSION_ALREADY_PROCESSED_CODE = "PX02";

    public static final String REQUEST_ID = "REQUEST_ID";

    public static final String REQUEST_TYPE = "requestType";

    public static final String SESSION_ALREADY_PROCESSED = "SESSION_ALREADY_PROCESSED";

    public static final String CURRENT_REQUEST_STATUS = "CURRENT_REQUEST_STATUS";

    public static final String ETL_IN_FILE = "ETL_IN_FILE";

    public static final String ETL_JOB_LAUNCH_STATUS = "ETL_JOB_LAUNCH_STATUS";

    public static final String ETL_IN_FILE_PARSE_ERROR = "ETL_IN_FILE_PARSE_ERROR";

    public static final String RESP_OUT_FILE = "input.file.name";

    public static final String RESP_JOB_LAUNCH_STATUS = "RESP_JOB_LAUNCH_STATUS";

    public static final String RESP_OUT_FILE_PARSE_ERROR = "RESP_OUT_FILE_PARSE_ERROR";

    public static final String RESP_JOB_TIME_STAMP = "RESP_JOB_TIME_STAMP";

    public static final String RESP_JOB_EXPECTED_LINE_COUNT = "RESP_JOB_EXPECTED_LINE_COUNT";

    public static final String FDE_FILE_STATUS = "FDE_FILE_STATUS";

    public static final String FDE_FILE_LOCATION = "FDE_FILE_LOCATION";

    public static final String ERR_FILE_STATUS = "ERR_FILE_STATUS";

    public static final String ERR_FILE_LOCATION = "ERR_FILE_LOCATION";

    public static final String FDE_FILE_STATUS_MESSAGE = "FDE_FILE_STATUS_MESSAGE";

    public static final String ALL_RESPONSE_CODE = "ALL_RESPONSE_CODE";

    public static final String GEN_FEEDBACK = "GEN_FEEDBACK";

    public static final String GET_ARCHIVE = "GET_ARCHIVE";

    public static final String IGNORE_COUNT = "IGNORE_COUNT";

    public static final String FILE_INTEGRATION = "FILE_INTEGRATION";

    public static final String CURRUPT_FILE = "CURRUPT_FILE";

    public static final String CURRUPT_FILE_CODE = "PX03";

    public static final String IS_CURRUPT_FILE = "IS_CURRUPT_FILE";

    public static final String IS_ERROR = "IS_ERROR";

    public static final String TIME_STAMP = "TIME_STAMP";

    public static final String GRID_SIZE = "GRID_SIZE";

    public static final String CBA_POST = "CBA_POST";

    public static final String CBA_FEEDBACK_GEN = "CBA_FEEDBACK_GEN";

    public static final String MAX_ATTEMPTS = "MAX_ATTEMPTS";

    public static final String RETRY_WAIT_TIME = "RETRY_WAIT_TIME";

    public static final String MAX_RETRY_WAIT_TIME = "MAX_RETRY_WAIT_TIME";

    public static final String RETRY_MULTIPLIER = "RETRY_MULTIPLIER";

    public static final String RETY_COUNT = "RETY_COUNT";

    public static final String RETRY_CODES = "RETRY_CODES";

    public static final String BIG_FILE = "BIG_FILE";

    public static final String FED_O_EXT = "FED_O_EXT";

    public static final String RESTART_REQUEST_FILE_NAME = "RemitaStp102";

    public static final String CTL_REQUEST_ID = "controlRequestId";

    public static final String RESPONSE_CODE = "responseCode";

    public static final String RESPONSE_MESSAGE = "responseMessage";

    public static final String ACCOUNT_BALANCE_RESPONSE = "accountBalanceResponse";

    public static final String CONFIG = "CONFIG";

    public static final String START_REQUEST_FILE_NAME = "RemitaStp101";

    public static final String STOP_REQUEST_FILE_NAME = "RemitaStp100";

    public static final String LOG_REQUEST_FILE_NAME = "RemitaStp103";

    public static final String UPDATE_REQUEST_FILE_NAME = "RemitaStp104";

    public static final String MONITOR = "MONITOR";

    public static final String ACCOUNT_NUMBER = "accountNumber";

    public static final String OTP = "otp";

    public static final String STATEMENT_DATE = "statementDate";

    public static final String STATEMENT_START_DATE = "statementStartDate";

    public static final String STATEMENT_END_DATE = "statementEndDate";

    public static final String START_PAGE_NO = "startPageNo";

    public static final String PARTITION_ID = "PARTITION_ID";

    public static final String PAGE_SIZE = "PAGE_SIZE";

    public static final String UPLOAD_SQL = "UPLOAD_SQL";

    public static final String MOD_DIVISOR_KEY = "modDivisor";

    public static final String MOD_REMAINDER_KEY = "modRemainder";

    public static final String TRANS_REF = "transRef";

    public static final String TRANS_TRACE = "transTrace";

    public static final String WRITER_SQL = "WRITER_SQL";

    public static final String IGNORE_FILE_WRITE = "IGNORE_FILE_WRITE";

    public static final String SESSION_NUMBER_REST = "sessionNumber";

    public static final String REQUEST_ID_REST = "requestId";

    public static final String DOWNLOAD_SQL = "DOWNLOAD_SQL";

    public static final String DUPLICATE_REQUEST_RESPONSE_CODE = "X212";
    public static final String AUTO_RETRY_CODES = "X94,X02,X002,X03,X03B,X03A,X03C,X03E,X04,X11,X13,X25,X20,XX25,8002,X420,X400, ";
    public static final String NO_FREE_CONNECTOR_ERROR_CODE = "03A";
    public static final String CONNECTOR_TIMEOUT_ERROR_CODE = "03B";
    public static final String INVALID_RESPONSE_ERROR_CODE = "05";
    public static final String INVALID_RESPONSE_CODE = "X05";
    public static final String NO_CONNECTOR_RESPONSE_CODE = "X03A";
    public static final String NO_CONNECTOR_TO_BANK_FROM_BRIDGE_RESPONSE_CODE = "X03C";
    public static final String NO_CONNECTOR_TO_BANK_FROM_BRIDGE_ERROR_CODE = "03C";
    public static final String MESSAGE_INTEGRITY_RESPONSE_CODE = "X902";
    public static final String PROCESSING_PENDING_CODE = "X68";
    public static final String REVERSAL_ERROR_CODE = "X400";
    public static final String REVERSAL_CODE = "X420";
    public static final String INSUFFICIENT_ACCOUNT_BALANCE = "X51";
    public static final String CONNECTOR_TIMEOUT_RESPONSE_CODE = "X03B";
    public static final String AUTO_RECOVER_CODE = "X94,X03,X03A,X03C,X03D,X03E,X04,X05,X08,X09,X100,X902,X20,X25,XX25,7707,8001,9999";
    public static final String UNKNOWN_TRANS_REF = "X25";
    public static final String UNKNOWN_TRANS_REF_PRE_POST = "XX25";
    public static final String REQUEST_FILE_CHECKSUM = "REQUEST_FILE_CHECK_SUM";
    public static final String STP_ENGINE_INTERRUPTED_RESPONSE_CODE = "X03E";
    public static final String MARKED_FOR_SEND_CODE = "X002";
    public static final String UNMARKED_FOR_SEND_CODE = "X02";
    public static final String PROCESS_RESTART = "PROCESS_RESTART";
    public static final String MESSAGE_INTEGRITY_RESPONSE_CODE_1 = "X903";
    public static final String TXN_STATUS_CUT_OFF = "X905";
    public static final String TXN_STATUS_NOT_AVAILABLE = "X906";
    public static final String UNKNOW_ERROR_CODE = "X11";
    public static final String NULL_REPONSE_CODE = "PS01";
    public static final String PRE_SEND = "X002";
    public static final String DB_CONNECTION_ERROR_CODE = "DBX01";
    public static final String DB_LOCK_ERROR_CODE = "DBX02";
    public static final String DB_BAD_SQL_ERROR_CODE = "DBX04";
    public static final String STATUS = "STATUS";
    public static final String POST = "POST";
    public static final String UNKNOW_CBA_SERVER_ERROR_CODE = "X12";
    public static final String DECRYPTION_ERROR_CODE = "X10";
    public static final String ENCRYPTION_ERROR_CODE = "X09";
    public static final String CONVERSION_ERROR_CODE = "X08";
    public static final String DEFAULT_SEND_RESPONSE = "X06";
    public static final String CLIENT_CONNECTION_ERROR_CODE = "X13";
    public static final String CLIENT_NO_CONNECTION_ERROR_CODE = "X20";
    public static final String CHECK_STATUS_CONFIGURATION_ERROR_CODE = "X16";
    public static final String PREVIOUS_INSTACE_FAILED_POSTING = "IS01";
    public static final String DUPLICATE_RESPONSE_CODE = "X94";
    public static final String HANGING_POST = "HANGING_POST";
    public static final String UPLOAD = "UPLOAD";
    public static final String HANGING_UPLOAD = "HANGING_UPLOAD";
    public static final String HANGING_DOWNLOAD = "HANGING_DOWNLOAD";
    public static final String DOWNLOAD = "DOWNLOAD";
    public static final String BANK_CODE = "bankCode";
    public static final String EMAIL_FLAG = "emailFlag";
    public static final String SMS_FLAG = "smsFlag";
    public static final String DOWNLOAD_ERROR = "PX04";
    public static final String UPLOAD_ERROR = "PX05";
    public static final String SESSION_SEPERATOR = "_";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String NEW_SESSION_METRIC = "stp.sessions.new.count";
    public static final String RECIEVED_SESSION_METRIC = "stp.sessions.in.count";
    public static final String PROCESSING_SESSION_METRIC = "stp.sessions.processing.count";
    public static final String PROCESSED_SESSION_METRIC = "stp.sessions.processed.count";
    public static final String ERROR_SESSION_METRIC = "stp.sessions.processed.error";
    public static final String FT_PROCESS_OK_METRIC = "stp.ft.post.ok.count";
    public static final String FT_PROCESS_ERROR_METRIC = "stp.ft.post.error.count";
    public static final String FT_STATUS_PROCESS_METRIC = "stp.ft.status.count";
    public static final String FT_STATUS_PROCESS_ERROR_METRIC = "stp.ft.status.error.count";
    public static final String OTP_PROCESS_METRIC = "stp.otp.request.count";
    public static final String OTP_PROCESS_ERROR_METRIC = "stp.otp.request.error.count";
    public static final String OTP_PROCESS_VALIDATE_METRIC = "stp.otp.validate.count";
    public static final String OTP_PROCESS_VALIDATE_ERROR_METRIC = "stp.otp.validate.error.count";
    public static final String OTP_PROCESS_NAME_ENQUIRY_OK_METRIC = "stp.otp.name.enquiry.count";
    public static final String OTP_PROCESS_NAME_ENQUIRY_ERROR_METRIC = "stp.otp.name.enquiry.error.count";
    public static final String AB_PROCESS_METRIC = "stp.ab.request.count";
    public static final String AB_PROCESS_ERROR_METRIC = "stp.ab.request.error.count";
    public static final String AB_PROCESS_OK_METRIC = "stp.ab.request.ok.count";
    public static final String OTP_PROCESS_VALIDATE_OK_METRIC = "stp.otp.validate.ok.count";
    public static final String OTP_PROCESS_OK_METRIC = "stp.otp.request.ok.count";
    public static final String FT_PROCESS_METRIC = "stp.ft.post.count";
    public static final String FT_STATUS_PROCESS_OK_METRIC = "stp.ft.status.ok.count";
    public static final String INTERFACE_TEST = "PAYMENT_INTERFACE_TEST";
    public static final String ST_INTERFACE_TEST = "PAYMENT_STATUS_INTERFACE_TEST";
    public static final String AB_INTERFACE_TEST = "ACCOUNT_BALANCE_INTERFACE_TEST";
    public static final String LOG_OUT = "USER_LOG_OUT";
    public static final String AVS_INTERFACE_TEST = "AVS_INTERFACE_TEST";
    public static final String SVC_STATE_CHANGE = "SVC_STATE_CHANGE";
    public static final String COMP_SYSTEMSPECS = "SYSTEMSPECS";
    public static final String LOG_IN = "USER_LOG_IN";
    public static final String TRANSACTION_ID = "TRANSACTION_ID";
    public static final String SESSION_ADMIN = "SESSION_ADMIN";
    public static final String AVS_DATA_MISS_MATCH_CODE = "X250";
    public static final String AVS_CORP_ACCOUNT = "X251";
    public static final String AVS_INVALID_ACCOUNT_TYPE = "X252";
    public static final String THREAD_LOCAL_REQUEST_CONTEXT = "thread.local.request.context";
    public static final String DEVICE_UUID = "deviceUuid";
    public static final String PAN_FIRST_3 = "panFirst3";
    public static final String PAN_LAST_4 = "panLast4";
    static final String AUTO_RETRY_NO_STATUS_CODES = "X94,X02,X002,X03,X03B,X03A,X03C,X03E,X04,X11,X13,X20,X25,XX25,8002,X420,X400, ";
    static final String AUTO_RETRY_CODES2 = "X94,X02,X002,X03A,X03C,X03E,X25,XX25,X420,X400, ";
    private static final int BUFFER = 2048;
}
