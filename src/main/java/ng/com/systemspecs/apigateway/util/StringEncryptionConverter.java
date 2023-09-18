package ng.com.systemspecs.apigateway.util;

import org.postgresql.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.security.Key;


@Converter
public class StringEncryptionConverter implements AttributeConverter<String, String> {
 /*   @Value("${app.sort.algorithm}")
    private static String ENCRYPTION_ALGORITHM;

    @Value("${app.sort.key}")
    static String sortKey;*/

    private static final String ENCRYPTION_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static final byte[] SORT_KEY = "secret-key-12345".getBytes();

    @Override
    public String convertToDatabaseColumn(String columnField) {

        // do some encryption
        if (columnField != null) {
            Key key = new SecretKeySpec(SORT_KEY, "AES");
            try {
                Cipher c = Cipher.getInstance(ENCRYPTION_ALGORITHM);
                c.init(Cipher.ENCRYPT_MODE, key);
                return Base64.encodeBytes(c.doFinal(columnField.getBytes()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }

    @Override
    public String convertToEntityAttribute(String dbDATA) {
        // do some decryption
        if (dbDATA != null) {
            Key key = new SecretKeySpec(SORT_KEY, "AES");
            try {
                Cipher c = Cipher.getInstance(ENCRYPTION_ALGORITHM);
                c.init(Cipher.DECRYPT_MODE, key);
                String s = new String(c.doFinal(Base64.decode(dbDATA)));
                return s;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }

    public static void main(String[] args) {

        StringEncryptionConverter stringEncryptionConverter = new StringEncryptionConverter();
        String cash_account = stringEncryptionConverter.convertToDatabaseColumn("Cash Account");
        System.out.println("ACCOUNT NAME CASH ACCOUNT : " + cash_account);

        String cash_out = stringEncryptionConverter.convertToDatabaseColumn("Cash-Out Income account");
        System.out.println("Cash-Out Income account" + cash_out);

        String cashIN = stringEncryptionConverter.convertToDatabaseColumn("Cash-In Income account");
        System.out.println("CASH IN : " + cashIN);

        String correspondent_account = stringEncryptionConverter.convertToDatabaseColumn("Correspondent Account");
        System.out.println(" Correspondent Account : " + correspondent_account);

        String number = stringEncryptionConverter.convertToDatabaseColumn("0.0");
        System.out.println(" Balance : " + number);


        String nullValue = stringEncryptionConverter.convertToDatabaseColumn("EMPTY");
        System.out.println(" NullValue : " + nullValue);

        String maryamAcct = stringEncryptionConverter.convertToDatabaseColumn("1100016946");
        System.out.println(" maryam Acct : " + maryamAcct);


    }
}
