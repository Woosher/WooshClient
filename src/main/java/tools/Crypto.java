package tools;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Crypto {

    static byte[] SALT = {
            (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };
    static int ITCOUNT = 19;

    static String ALGORITHM = "PBEWithMD5AndDES";
    static String CHARSET = "UTF-8";

    public static String encrypt(String secretKey, String plainText)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            UnsupportedEncodingException,
            IllegalBlockSizeException,
            BadPaddingException {

        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), SALT, ITCOUNT);
        SecretKey key = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITCOUNT);

        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        byte[] in = plainText.getBytes(CHARSET);
        byte[] out = cipher.doFinal(in);
        return new String(Base64.getEncoder().encode(out));
    }

    public static String decrypt(String secretKey, String encryptedText)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException,
            IOException {

        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), SALT, ITCOUNT);
        SecretKey key = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITCOUNT);

        Cipher decipher = Cipher.getInstance(key.getAlgorithm());
        decipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        byte[] enc = Base64.getDecoder().decode(encryptedText.trim());
        byte[] utf8 = decipher.doFinal(enc);
        return new String(utf8, CHARSET);
    }


}
