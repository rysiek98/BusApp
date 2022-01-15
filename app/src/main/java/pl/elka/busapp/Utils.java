package pl.elka.busapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class Utils {

    private final static int READ_WRITE_BLOCK_BUFFER = 1024;
    //Coś z tym zrobic!!!!!
    private final static String ENCRYPTOR = "AES/CBC/PKCS5Padding";
    private final static String SECRET_KEY = "DUPA";

    public static void encryptToFile(String keyStr, String specStr, InputStream in, OutputStream out)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        cryptToFile(keyStr, specStr, in, out, Cipher.ENCRYPT_MODE);
    }

    public static void decryptToFile(String keyStr, String specStr, InputStream in, OutputStream out)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        cryptToFile(keyStr, specStr, in, out, Cipher.DECRYPT_MODE);
    }

    private static void cryptToFile(String keyStr, String specStr, InputStream in, OutputStream out, int cipherMode)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        try {
            IvParameterSpec iv = new IvParameterSpec(specStr.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec keySpec = new SecretKeySpec(keyStr.getBytes(StandardCharsets.UTF_8), SECRET_KEY);
            Cipher c = Cipher.getInstance(ENCRYPTOR);
            c.init(cipherMode, keySpec, iv);
            out = new CipherOutputStream(out, c);
            int count = 0;
            byte[] buffer = new byte[READ_WRITE_BLOCK_BUFFER];
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
