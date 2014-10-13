package utilities.safepassbeta;

import android.util.Base64;
import android.util.Log;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    // Variables
    private final static int MIN_LENGTH = 8;
    private final static int iterations = 1024;
    private final static int key_length1 = 256;
    private final static int key_length2 = 512;

    private static String base64Encode(byte[] b) {
        return Base64.encodeToString(b, Base64.NO_WRAP | Base64.NO_PADDING);
    }

    private static byte[] base64Decode(String s) {
        return Base64.decode(s, Base64.NO_WRAP | Base64.NO_PADDING);
    }

    // Computes a PBKDF2WithHmacSHA1 hash
    public static String computeHash(String str) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = generateSalt(key_length2 / 8);
        byte[] hash = generateHash(str.toCharArray(), salt, key_length2);
        return base64Encode(salt) + ":" + base64Encode(hash);
    }

    private static byte[] generateHash(char[] arr, byte[] salt, int length) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(arr, salt, iterations, length);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
    }

    // Checks if hash generated from str2 matches str1
    public static boolean validateHash(String stored, String entered) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = stored.split(":");
        byte[] salt = base64Decode(parts[0]);
        String stored_hash = parts[1];
        byte[] hash = generateHash(entered.toCharArray(), salt, key_length2);
        return stored_hash.equals(base64Encode(hash));
    }

    public static String encrypt(String plaintext, String pass) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] salt = generateSalt(key_length1 / 8);
        byte[] keyBytes = generateHash(pass.toCharArray(), salt, key_length1);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = generateSalt(cipher.getBlockSize());
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
        return base64Encode(salt) + ":" + base64Encode(iv) + ":" + base64Encode(ciphertext);
    }

    public static String decrypt(String ciphertext, String pass) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String[] parts = ciphertext.split(":");
        byte[] salt = base64Decode(parts[0]);
        byte[] iv = base64Decode(parts[1]);
        byte[] cipherBytes = base64Decode(parts[2]);
        byte[] keyBytes = generateHash(pass.toCharArray(), salt, key_length1);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] plaintext = cipher.doFinal(cipherBytes);
        return new String(plaintext);

    }


    // Generates a random salt
    public static byte[] generateSalt(int length) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[length];
        secureRandom.nextBytes(salt);
        return salt;
    }

    // Checks if a string has the required length
    public static boolean hasRequiredLength(String str) {
        return str.length() >= MIN_LENGTH;
    }

    // Checks if the string has at least one uppercase character
    public static boolean hasUppercaseChar(String str) {
        return str.matches(".*[A-Z].*");
    }

    // Checks if the string has at least one numerical character
    public static boolean hasNumericalChar(String str) {
        return str.matches(".*[0-9].*");
    }

    public static boolean hasLowercaseChar(String str) {
        return str.matches(".*[a-z].*");
    }

    // Checks if the string has at least one special character
    public static boolean hasSpecialChar(String str) {
       return str.matches(".*[^A-Za-z0-9].*");
    }

    public static String generatePassword(boolean lowercase, boolean uppercase,
                                          boolean digit, boolean special, int length) {
        String str;
        while(true) {
            str = _generatePassword(lowercase, uppercase, digit, special, length);
            if((!lowercase || hasLowercaseChar(str)) && (!uppercase || hasUppercaseChar(str))
                    && (!digit || hasNumericalChar(str)) && (!special || hasSpecialChar(str)))
                break;
        }
        return str;
    }

    private static String _generatePassword(boolean lowercase, boolean uppercase,
                                            boolean digit, boolean special, int length) {
        String str = getAllPasswordRequirements(lowercase, uppercase, digit, special);
        StringBuilder sb = new StringBuilder();
        SecureRandom rand = new SecureRandom();
        for(int i = 0; i < length; i++) {
            sb.append(str.charAt(rand.nextInt(str.length())));
        }
        return sb.toString();
    }

    public static String getPasswordStrength(String str) {

        if(PasswordList.passwordList.contains(str) || PasswordList.checkForSubstring(str)) {
            return "Guessable";
        }

        int multiplier = 0;
        if(hasLowercaseChar(str)) {
            multiplier += 26;
        }
        if(hasUppercaseChar(str)) {
            multiplier += 26;
        }
        if(hasNumericalChar(str)) {
            multiplier += 10;
        }
        if(hasSpecialChar(str)) {
            multiplier += 28;
        }

        double percent = ((multiplier*str.length())/850.00) * 100.00;

        if(percent >= 0 && percent <= 25) {
            return "Very Weak";
        } else if(percent > 25 && percent <= 50) {
            return "Weak";
        } else if(percent > 50 && percent <= 75) {
            return "Medium";
        } else if(percent > 75 && percent <= 85) {
            return "Strong";
        } else {
            return "Very Strong";
        }

    }

    private static String getAllPasswordRequirements(boolean lowercase, boolean uppercase,
                                                     boolean digit, boolean special) {
        StringBuilder sb = new StringBuilder();
        if(lowercase) {
            sb.append("abcdefghijklmnopqrstuvwxyz");
        }
        if(uppercase) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        if(digit) {
            sb.append("0123456789");
        }
        if(special) {
            sb.append("!@#$%^&*:;~`+-=?,)([]|/\\<>\'\"");
        }
        return sb.toString();
    }

}
