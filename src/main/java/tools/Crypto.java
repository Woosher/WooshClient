package tools;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import static values.Constants.ENC_ALGORITHM;
import static values.Constants.CHARSET_UTF8;

public class Crypto {

    static byte[] SALT = {
            (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };
    static int ITCOUNT = 19;


    public static String encrypt(String secretKey, String plainText) throws Exception{

        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), SALT, ITCOUNT);
        SecretKey key = SecretKeyFactory.getInstance(ENC_ALGORITHM).generateSecret(spec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITCOUNT);

        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        byte[] in = plainText.getBytes(CHARSET_UTF8);
        byte[] out = cipher.doFinal(in);
        return new String(Base64.getEncoder().encode(out));
    }

    public static String decrypt(String secretKey, String encryptedText) throws Exception{

        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), SALT, ITCOUNT);
        SecretKey key = SecretKeyFactory.getInstance(ENC_ALGORITHM).generateSecret(spec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITCOUNT);

        Cipher decipher = Cipher.getInstance(key.getAlgorithm());
        decipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        byte[] enc = Base64.getDecoder().decode(encryptedText.trim());
        byte[] utf8 = decipher.doFinal(enc);
        return new String(utf8, CHARSET_UTF8);
    }


}
