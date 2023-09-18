package ng.com.justjava.corebanking.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.jcajce.JcaPGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.operator.PublicKeyDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.jcajce.*;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.Date;
import java.util.Iterator;

@Component
public class PGPUtils {

    final static Logger log = LoggerFactory.getLogger(PGPUtils.class);
    static final byte[] privateKeyStr = Base64.decodeBase64(
        "lQOsBF/4l2wBCAC1JIirC+gkfxdoBkjf7CasJcOnkI897//PQBzRRr19negVWOqh" +
            "HmMbm/TIp4B/h8XV9JWYWMT1tK3o86Bi0fCcHtt9iY8JOBBBaGPHsdWRtAlaEH6K" +
            "a3KfhypXCXYYpqcoDEkFtwQLw+bPBpjPcOSQO0Lm2bJMCQpQfp0ONWYndeEUrxhX" +
            "TdtaU80cJ23Pw5LTxP4Ha9WuCHr2tqIe5ZPsob9wRr4Nv9cDps9vbv0zzfeUH46Y" +
            "SJM9hgc9eMim7WerNE1TgwXc1OMylQf9voeGsk0TUgGZ4A/XkWW/h2lCvK2vuiHY" +
            "rzEkyLhSpYjANVUnyrZJc0gvbN/cbmZiwsD/ABEBAAH/AwMC1oP8eQyYLjtgbqoQ" +
            "S++ygp0vm9ZU6G7L9r1TPm8lFEyaGiL26Wb2JIeE+g2etLlp1yCupK3t0NrQ037v" +
            "pqo+MPYt/fzTlKvmhi9Tzmv77xmtLTZPvFGKal1PfsGuZW8JZL4vqHpRxEqd+duR" +
            "FM/+RjDu8yqa18n1lEuxhwzgHUqfEm5Oh/1dbX31fTMXWxbvQ12S2BarCavU8dce" +
            "c1gzQdDrsSQDWKFgZohk7l3R6MaXFJTE6S7OMZFD+SeQBSSQNsilgeVkPAKDY3ST" +
            "DhaJ/BaISn+qlVU+pGVnqBfIKRkkBob6LyWSKVhcHCOx5wQ4yfmCXRILXr3IllQi" +
            "KOM2ORjYaQGl/RBu+uZU79T5Nka6myrZyJcjSrOG4WV5RvKhRO9nRnUYIb7vzrM9" +
            "UjOoQs+25MkjnnfBWpB/mAwXSohVtSEEJMKz8EXk9BrVrGcw+GW9c6AFDqOjeVdS" +
            "+/LaGqLhxYuRJizj9UgAkORnIrPUfV3L0B3KoyBtXKWasozzz8Sw6k/7l7luRnZl" +
            "ZAHeYfuC3Zj9yqd1IC7NaFNIuYuN+Up8FTIcsE8GMFx/gu0ogBRj6TqvXK/8tpKk" +
            "Vyy1SFJgt8yJ2uddOGm+STa3wZ8vURFjZhD0v8ekd45PpUeTNm26MdZSYImQx/oG" +
            "iUQjP4GsynnaW4S9UdSh6UWBGI+3TnHC5tQ1xIdvBndlp7LfwczYjFbK6p9qYvev" +
            "pP4BWwYjHqRUf57xZWcUpGvw975ipN0bzapW6OBGQFTfS0uvoJXg+LFO/CaSeIY5" +
            "hPUbtgArcW/UTl+EhuVmcppnEO/em7F17UL5YJsbsk6mEJcOaG4a5JnPOl9zmDvf" +
            "AGrATIc+zQPnOYuS9vAdy8qhobKj8lgj0oBq+nsxfrQcYWtpbnJpbmRlQHN5c3Rl" +
            "bXNwZWNzLmNvbS5uZ4kBHAQQAQIABgUCX/iXbAAKCRAv4hQqxbk35MMTCACcDIBo" +
            "N2gjbIIeqW+Bw0MbujqS+DcRQWb1KohAEGYviTxlNTx1h/SIQTXscyH//zfoUpJd" +
            "NpSkigx0h7uBank3wNG6G75aHB0nALO7s1zaUA7gjB2qyQEOIyZvZaUtAKUnoco+" +
            "G9P7rHimbz6Y+42W5120QKLJkNT7CSwjbsywHG1BLjJLKePOoT4D/J8Z04a0UIMM" +
            "DI8j+1oGjzxHSCx/p3bcYW5gVEVYGQtjkPqJTVPNTQG8Cv8lu/w2GuLpS528YmQf" +
            "5QO0smcokgkP7xN2pwCZIuQDqOktyUGLT0VshJzlRDqjYpLnTUoiuieUzeMoHcoN" +
            "HrszB/1rYwERf039");
    static final byte[] publicKeyStr = Base64.decodeBase64(
        "mQENBF/4l2wBCAC1JIirC+gkfxdoBkjf7CasJcOnkI897//PQBzRRr19negVWOqh" +
            "HmMbm/TIp4B/h8XV9JWYWMT1tK3o86Bi0fCcHtt9iY8JOBBBaGPHsdWRtAlaEH6K" +
            "a3KfhypXCXYYpqcoDEkFtwQLw+bPBpjPcOSQO0Lm2bJMCQpQfp0ONWYndeEUrxhX" +
            "TdtaU80cJ23Pw5LTxP4Ha9WuCHr2tqIe5ZPsob9wRr4Nv9cDps9vbv0zzfeUH46Y" +
            "SJM9hgc9eMim7WerNE1TgwXc1OMylQf9voeGsk0TUgGZ4A/XkWW/h2lCvK2vuiHY" +
            "rzEkyLhSpYjANVUnyrZJc0gvbN/cbmZiwsD/ABEBAAG0HGFraW5yaW5kZUBzeXN0" +
            "ZW1zcGVjcy5jb20ubmeJARwEEAECAAYFAl/4l2wACgkQL+IUKsW5N+TDEwgAnAyA" +
            "aDdoI2yCHqlvgcNDG7o6kvg3EUFm9SqIQBBmL4k8ZTU8dYf0iEE17HMh//836FKS" +
            "XTaUpIoMdIe7gWp5N8DRuhu+WhwdJwCzu7Nc2lAO4IwdqskBDiMmb2WlLQClJ6HK" +
            "PhvT+6x4pm8+mPuNluddtECiyZDU+wksI27MsBxtQS4ySynjzqE+A/yfGdOGtFCD" +
            "DAyPI/taBo88R0gsf6d23GFuYFRFWBkLY5D6iU1TzU0BvAr/Jbv8Nhri6UudvGJk" +
            "H+UDtLJnKJIJD+8TdqcAmSLkA6jpLclBi09FbISc5UQ6o2KS501KIronlM3jKB3K" +
            "DR67Mwf9a2MBEX9N/Q==");

    static final byte[] cDataPublicKeyStr = Base64.decodeBase64(
        "mQENBFwvufwBCAC9ZI0h5DejqbbTuJeSVeizkYZVmGxYFwX80MVyBi0xVs6gDLiw" +
            "YhfksNjHRs93wl3+n+9c23XxtqXlXfUDJ0J+pyprDmF+yiU/b2Le52oMPiYYvUt0" +
            "vYgVh65/IBzm0WK/HNyTjXH2a8AZuS29xvtDSahqW8UOm3i7W+jwBFrpc1f+Lsok" +
            "KFPEKvtAmQ8NI3W7bGfu0RDOa5JQwpYsfAselkyor9Az6o+Xt+3/xXF4otAfi57r" +
            "u4TOo+3CnRt16XdoKM+4/isAQj9Pq7YTj1SN83wO8454/l2Ye5chh7P5lNiNpDrk" +
            "VdMGr1PAokQMfn/KNrc5TONWi3sZDX0i0QA7ABEBAAG0GHRvYmlha2ludGFyb0B5" +
            "YWhvby5jby51a4kBHAQQAQIABgUCXC+5/AAKCRBCsCk8ucAGnFvsCACOj81XnaCg" +
            "azRw8O6gLupcx70F1N8E0PoAvrYA5fkPCRm9OCb1HiMI4iF3ms2cHj3TNR5XJzNy" +
            "a/r/CZRIG9wyeNwALhyw/Ik47LM3hN3ow7qOXtGTSnT/tfsgdqIK6jl2yK/oTt7N" +
            "OKGZzyxSTBappykQupqinxol4KjbATP2WWLyc51PEE08TQZZdFnq9UN3CTlzKitV" +
            "9ITAO5LU9LN0WciSAXYJOO6ux/z9aqKsGzWRaAdpb3bpR6GjWKAoutjonixWjQOF" +
            "2+2J99HkeirYI4jWEgx5BruNwsDh2YE8ATrUqOfn8gmdAYGdx329OyJepLowhKYM" +
            "nzAdlEM9n0zv");

    @Value("${app.pgp.publicKeyFile}")
    private static String publicKeyFilePath;
    @Value("${app.pgp.secretKeyFile}")
    private static String secretKeyFilePath;
    @Value("${app.pgp.passphrase}")
    private static String passphrase;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /*  private PGPUtils() {
          throw new IllegalAccessError("PGPUtils class");
      }
  */
    public static void main(String[] args)
        throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        /*elgamalExample();
        ecExample();*/
/*
        PGPPublicKey pgpPublicKey = getPgpPublicKey();
        System.err.println(Long.toHexString(pgpPublicKey.getKeyID()));

        PGPPrivateKey pgpPrivateKey = getPgpPrivateKey();

        System.err.println(Long.toHexString(pgpPrivateKey.getKeyID()));*/

      /*  System.out.println("==== " + publicKeyFilePath);

        readPublicKey(publicKeyFilePath);
//        readPrivateKey(secretKeyFilePath, passphrase);*/


      /*  byte[] encryptedData = createEncryptedData(getPgpPublicKey(), "Hello world".getBytes());
        System.out.println("ENCRYPTED DATA " + encryptedData.toString() );
        System.out.println("USING FROM BYTE ARRAY FOR ENCRYPTED DATA " +Strings.fromByteArray(encryptedData));

        byte[] bytes = extractPlainTextData(getPgpPrivateKey(), encryptedData);
        System.out.println("EXTRACTED DATA IN BYTES "  + bytes);
        System.out.println("USING FROM BYTE ARRAY " +Strings.fromByteArray(bytes));*/


        PGPPublicKey cDataPublicKey = getCDataPublicKey();
        System.out.println(cDataPublicKey.getKeyID());

        System.out.println("Our public " + getPgpPublicKey().getKeyID());
        System.out.println("Our private " + getPgpPrivateKey().getKeyID());

    }

    private static PGPPrivateKey getPgpPrivateKey() throws PGPException, IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(privateKeyStr);
        return readPrivateKey(inputStream, "020775Rashy!");
    }

    private static PGPPublicKey getPgpPublicKey() throws IOException, PGPException {
        ByteArrayInputStream in = new ByteArrayInputStream(publicKeyStr);
        return readPublicKey(in);
    }

    private static PGPPublicKey getCDataPublicKey() throws IOException, PGPException {
        ByteArrayInputStream in = new ByteArrayInputStream(cDataPublicKeyStr);
        return readPublicKey(in);
    }

    @SuppressWarnings("unchecked")
    public static PGPPublicKey readPGPPublicKey(InputStream in) throws IOException, PGPException {
        in = org.bouncycastle.openpgp.PGPUtil.getDecoderStream(in);

        JcaPGPPublicKeyRingCollection pgpPub = new JcaPGPPublicKeyRingCollection(in);
        in.close();

        PGPPublicKey key = null;
        Iterator<PGPPublicKeyRing> rIt = pgpPub.getKeyRings();
        while (key == null && rIt.hasNext()) {
            PGPPublicKeyRing kRing = rIt.next();
            Iterator<PGPPublicKey> kIt = kRing.getPublicKeys();
            while (key == null && kIt.hasNext()) {
                PGPPublicKey k = kIt.next();

                if (k.isEncryptionKey()) {
                    key = k;
                }
            }
        }
        return key;

    }

    @SuppressWarnings("unchecked")
    public static PGPPrivateKey readPGPPrivateKey(InputStream inputStream) throws IOException, PGPException {

       /* PGPSecretKey secretKey = new SExprParser(
            new JcaPGPDigestCalculatorProviderBuilder().build()).parseSecretKey(inputStream, new JcePBEProtectionRemoverFactory("020775Rashy!".toCharArray()), new JcaKeyFingerprintCalculator());
        PGPPrivateKey privateKey = secretKey.extractPrivateKey(null);
        System.err.println(Long.toHexString(privateKey.getKeyID()));
        return privateKey;*/

        PGPSecretKey secretKey = readSecretKey(inputStream);

        return secretKey.extractPrivateKey(
            new JcePBESecretKeyDecryptorBuilder().setProvider("BC").build("020775Rashy!".toCharArray()));
    }

    public static PGPPublicKey readPublicKey(InputStream is) throws IOException, PGPException {
        PGPPublicKeyRingCollection pgpPub =
            new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(is), new JcaKeyFingerprintCalculator());

        Iterator keyRingIter = pgpPub.getKeyRings();

        while (keyRingIter.hasNext()) {
            PGPPublicKeyRing keyRing = (PGPPublicKeyRing) keyRingIter.next();
            Iterator keyIter = keyRing.getPublicKeys();

            while (keyIter.hasNext()) {
                PGPPublicKey key = (PGPPublicKey) keyIter.next();

                if (key.isEncryptionKey()) {
                    return key;
                }
            }
        }

        throw new IllegalArgumentException("Can't find encryption key in key ring.");
    }

    public static PGPPublicKey readPublicKey(String filePath) throws IOException, PGPException {
        PGPPublicKey publicKey;

        try (InputStream is = new FileInputStream(filePath)) {
            publicKey = readPublicKey(is);
        }

        return publicKey;
    }

    public static PGPPublicKey readPublicKeyFromString(String key) throws IOException, PGPException {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("Public key string is null");
        }

        return readPublicKey(IOUtils.toInputStream(key, Charset.defaultCharset()));
    }

    private static PGPSecretKey readSecretKey(InputStream is) throws IOException, PGPException {
        PGPSecretKeyRingCollection pgpSec =
            new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(is), new JcaKeyFingerprintCalculator());
        Iterator keyRingIter = pgpSec.getKeyRings();

        while (keyRingIter.hasNext()) {
            PGPSecretKeyRing keyRing = (PGPSecretKeyRing) keyRingIter.next();
            Iterator keyIter = keyRing.getSecretKeys();

            while (keyIter.hasNext()) {
                PGPSecretKey key = (PGPSecretKey) keyIter.next();

                if (key.isSigningKey()) {
                    return key;
                }
            }
        }

        throw new IllegalArgumentException("Can't find signing key in key ring.");
    }

    public static PGPPrivateKey readPrivateKey(InputStream is, String password) throws PGPException, IOException {
        PGPSecretKey secretKey = readSecretKey(is);

        return secretKey.extractPrivateKey(
            new JcePBESecretKeyDecryptorBuilder().setProvider("BC").build(password.toCharArray()));
    }

    public static PGPPrivateKey readPrivateKey(String filePath, String password) throws PGPException, IOException {
        PGPPrivateKey privateKey;

        try (InputStream is = new FileInputStream(filePath)) {
            privateKey = readPrivateKey(is, password);
        }

        return privateKey;
    }

    public static String getFingerprint(PGPPublicKey publicKey) {
        return Hex.toHexString(publicKey.getFingerprint());
    }

    public static String getOwnerString(PGPPublicKey publicKey) {
        return publicKey.getUserIDs().next();
    }

    public byte[] createRsaEncryptedObject(PGPPublicKey encryptionKey, byte[] data) throws PGPException, IOException {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
        OutputStream pOut = lData.open(bOut, PGPLiteralData.BINARY, PGPLiteralData.CONSOLE, data.length, new Date());
        pOut.write(data);
        pOut.close();
        byte[] plainText = bOut.toByteArray();
        ByteArrayOutputStream encOut = new ByteArrayOutputStream();
        PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(new JcePGPDataEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_256).setWithIntegrityPacket(true).setSecureRandom(new SecureRandom()).setProvider("BCFIPS"));
        encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(encryptionKey).setProvider("BCFIPS"));
        OutputStream cOut = encGen.open(encOut, plainText.length);
        cOut.write(plainText);
        cOut.close();
        return encOut.toByteArray();
    }

    public byte[] extractRsaEncryptedObject(PGPPrivateKey privateKey, byte[] pgpEncryptedData)
        throws PGPException, IOException {
        PGPObjectFactory pgpFact = new JcaPGPObjectFactory(pgpEncryptedData);
        PGPEncryptedDataList encList = (PGPEncryptedDataList) pgpFact.nextObject();// note: we can only do this because we know we match the first encrypted data object
        PGPPublicKeyEncryptedData encData = (PGPPublicKeyEncryptedData) encList.get(0);
        PublicKeyDataDecryptorFactory dataDecryptorFactory = new JcePublicKeyDataDecryptorFactoryBuilder().setProvider("BCFIPS").build(privateKey);
        InputStream clear = encData.getDataStream(dataDecryptorFactory);
        byte[] literalData = Streams.readAll(clear);
        if (encData.verify()) {
            PGPObjectFactory litFact = new JcaPGPObjectFactory(literalData);
            PGPLiteralData litData = (PGPLiteralData) litFact.nextObject();
            byte[] data = Streams.readAll(litData.getInputStream());
            return data;
        }
        throw new IllegalStateException("modification check failed");
    }

    /**
     * Create an encrypted data blob using an AES-256 session key and the
     * passed in public key.
     *
     * @param encryptionKey the public key to use.
     * @param data          the data to be encrypted.
     * @return a PGP binary encoded version of the encrypted data.
     */
    public byte[] createEncryptedData(PGPPublicKey encryptionKey, byte[] data) throws PGPException, IOException {
        PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
            new JcePGPDataEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_256)
                .setWithIntegrityPacket(true)
                .setSecureRandom(new SecureRandom()).setProvider("BC"));

        encGen.addMethod(
            new JcePublicKeyKeyEncryptionMethodGenerator(encryptionKey)
                .setProvider("BC"));

        ByteArrayOutputStream encOut = new ByteArrayOutputStream();

        // create an indefinite length encrypted stream
        OutputStream cOut = encGen.open(encOut, new byte[4096]);

        // write out the literal data
        PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
        OutputStream pOut = lData.open(
            cOut, PGPLiteralData.BINARY,
            PGPLiteralData.CONSOLE, data.length, new Date());
        pOut.write(data);
        pOut.close();

        // finish the encryption
        cOut.close();

        return encOut.toByteArray();
    }

    /**
     * Extract the plain text data from the passed in encoding of PGP
     * encrypted data. The routine assumes the passed in private key
     * is the one that matches the first encrypted data object in the
     * encoding.
     *
     * @param privateKey       the private key to decrypt the session key with.
     * @param pgpEncryptedData the encoding of the PGP encrypted data.
     * @return a byte array containing the decrypted data.
     */
    public byte[] extractPlainTextData(PGPPrivateKey privateKey, byte[] pgpEncryptedData) throws PGPException, IOException {
        PGPObjectFactory pgpFact = new JcaPGPObjectFactory(pgpEncryptedData);

        PGPEncryptedDataList encList = (PGPEncryptedDataList) pgpFact.nextObject();

        // find the matching public key encrypted data packet.
        PGPPublicKeyEncryptedData encData = null;
        /*for (PGPEncryptedData pgpEnc : encList) {
            PGPPublicKeyEncryptedData pkEnc
                = (PGPPublicKeyEncryptedData) pgpEnc;
            if (pkEnc.getKeyID() == privateKey.getKeyID()) {
                System.out.println(pkEnc.getKeyID() + "  ====== " + privateKey.getKeyID());
                encData = pkEnc;
                break;
            }
        }*/

        if (encData == null) {
            throw new IllegalStateException("matching encrypted data not found");
        }

        System.out.println("ENC DATA " + encData);

        // build decryptor factory
        PublicKeyDataDecryptorFactory dataDecryptorFactory =
            new JcePublicKeyDataDecryptorFactoryBuilder()
                .setProvider("BC")
                .build(privateKey);

        InputStream clear = encData.getDataStream(dataDecryptorFactory);
        byte[] literalData = Streams.readAll(clear);
        clear.close();

        // check data decrypts okay
        if (encData.verify()) {
            // parse out literal data
            PGPObjectFactory litFact = new JcaPGPObjectFactory(literalData);
            PGPLiteralData litData = (PGPLiteralData) litFact.nextObject();
            byte[] data = Streams.readAll(litData.getInputStream());
            return data;
        }
        throw new IllegalStateException("modification check failed");
    }

    private void elgamalExample()
        throws Exception {
        byte[] msg = Strings.toByteArray("Hello, world!");

        KeyPairGenerator kpGen = KeyPairGenerator.getInstance("DH", "BC");
        kpGen.initialize(2048);

        KeyPair kp = kpGen.generateKeyPair();

        PGPKeyPair elgKp = new JcaPGPKeyPair(
            PGPPublicKey.ELGAMAL_ENCRYPT, kp, new Date());

        byte[] encData = createEncryptedData(elgKp.getPublicKey(), msg);

        byte[] decData = extractPlainTextData(elgKp.getPrivateKey(), encData);

        System.out.println(Strings.fromByteArray(decData));
    }

    private void ecExample()
        throws Exception {
        byte[] msg = Strings.toByteArray("Hello, world!");

        KeyPairGenerator kpGen = KeyPairGenerator.getInstance("EC", "BC");
        kpGen.initialize(new ECGenParameterSpec("P-256"));

        KeyPair kp = kpGen.generateKeyPair();

        PGPKeyPair ecdhKp = new JcaPGPKeyPair(PGPPublicKey.ECDH, kp, new Date());


        byte[] encData = createEncryptedData(ecdhKp.getPublicKey(), msg);

        byte[] decData = extractPlainTextData(ecdhKp.getPrivateKey(), encData);

        System.out.println(Strings.fromByteArray(decData));
    }

    public String extractPlainTextData(byte[] payload) {
        String requestString = null;
        try {
            byte[] decData = extractPlainTextData(getPgpPrivateKey(), payload);
            Base64.encodeBase64(decData);
            requestString = Strings.fromByteArray(decData);
        } catch (PGPException | IOException e) {
            e.printStackTrace();
        }
        return requestString;
    }

    public byte[] createEncryptedData(byte[] payload) {
        String requestString = null;
        try {
            byte[] decData = createEncryptedData(getCDataPublicKey(), payload);
            log.info("DEC DATA " + decData);
            return decData;
//            requestString = Strings.fromByteArray(decData);
//            log.info("REQUEST STRING " + requestString);
        } catch (PGPException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] createRsaEncryptedObject(byte[] payload) {
        String requestString = null;
        try {
            byte[] decData = createRsaEncryptedObject(getCDataPublicKey(), payload);
            log.info("DEC DATA " + decData);
            return decData;
           /* requestString = Strings.fromByteArray(decData);
            log.info("REQUEST STRING " + requestString);*/
        } catch (PGPException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] extractRsaEncryptedObject(byte[] decode) {
        try {
            return extractRsaEncryptedObject(getPgpPrivateKey(), decode);
        } catch (PGPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
