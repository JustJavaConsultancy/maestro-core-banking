package ng.com.systemspecs.apigateway.service.stp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ng.com.systemspecs.apigateway.service.dto.stp.EncodedMessage;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Map;

@Component
public class RequestHelper {
    private static final Gson GSON = new Gson();


    public String decodePayload(String json) {
        String contents = json;
        byte[] bytes = contents.getBytes();

        if (Base64.isBase64(bytes)) {
            contents = new String(Base64.decodeBase64(bytes));
        }

        return contents;
    }

    public byte[] decodePayloadToBytes(String json) {
        String contents = json;
        byte[] bytes = contents.getBytes();


        return bytes;
    }

    public <T> T getPayload(String json, Class<T> tClass) {
        String contents = decodePayload(json);
        T output = GSON.fromJson(contents, tClass);
        return output;
    }

    public <T> Map<String, Object> convertPayload(String jsonString) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> payload = GSON.fromJson(jsonString, type);
        return payload;
    }


    public <T> String encodeResponse(T payload) {
        if (payload != null) {
            String json = GSON.toJson(payload);
            String encodedJson = new String(Base64.encodeBase64(json.getBytes()));
            EncodedMessage response = new EncodedMessage(encodedJson);
            return GSON.toJson(response);
        }
        return StringUtils.EMPTY;
    }

//
//    public <T> String resolveResponse(Message<T> message) {
//        if (message != null) {
//            T payload = message.getPayload();
//            String json = GSON.toJson(payload);
//            String encodedJson = new String(json.getBytes());
//            EncodedMessage response = new EncodedMessage(encodedJson);
//            return GSON.toJson(response);
//        }
//        return StringUtils.EMPTY;
//    }


}
