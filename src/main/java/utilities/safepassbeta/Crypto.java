package utilities.safepassbeta;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
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

    // Computes a PBKDF2WithHmacSHA1 hash
    public static String computeHash(String str) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = generateSalt(key_length2 / 8);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(str.toCharArray(), salt, iterations, key_length2);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
        return obfuscateText(toHex(salt) + ":" + toHex(hash));
    }

    // Checks if hash generated from str2 matches str1
    public static boolean validateHash(String stored, String entered) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = obfuscateText(stored).split(":");
        byte[] salt = toByte(parts[0]);
        String stored_hash = parts[1];
        PBEKeySpec pbeKeySpec = new PBEKeySpec(entered.toCharArray(), salt, iterations, key_length2);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] new_hash = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
        return stored_hash.equals(toHex(new_hash));
    }

    public static String encrypt(String plaintext, String pass) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] salt = generateSalt(key_length1 / 8);
        KeySpec keySpec = new PBEKeySpec(pass.toCharArray(), salt, iterations, key_length1);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = secretKeyFactory.generateSecret(keySpec).getEncoded();
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = generateSalt(cipher.getBlockSize());

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

        return toHex(salt) + ":" + toHex(iv) + ":" + toHex(ciphertext);
    }

    public static String decrypt(String ciphertext, String pass) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String[] parts = ciphertext.split(":");
        byte[] salt = toByte(parts[0]);
        byte[] iv = toByte(parts[1]);
        byte[] cipherBytes = toByte(parts[2]);

        KeySpec keySpec = new PBEKeySpec(pass.toCharArray(), salt, iterations, key_length1);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = secretKeyFactory.generateSecret(keySpec).getEncoded();
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] plaintext = cipher.doFinal(cipherBytes);

        return new String(plaintext);

    }


    // Generates a random salt
    private static byte[] generateSalt(int length) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[length];
        secureRandom.nextBytes(salt);
        return salt;
    }

    public static String obfuscateText(String str) {
        byte strArr[] = str.getBytes();
        byte newArr[] = new byte[str.length()];
        for(int i = 0; i < str.length(); i++) {
            newArr[i] = (byte) (strArr[i] ^ obfuscate[i % obfuscate.length]);
        }
        return new String(newArr);
    }

    // Converts a byte array to hexadecimal
    private static String toHex(byte[] arr) throws NoSuchAlgorithmException {
        BigInteger bigInteger = new BigInteger(1, arr);
        String hex = bigInteger.toString(16);
        int padding = (arr.length * 2) - hex.length();
        if(padding > 0) {
            return String.format("%0" + padding + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    // Converts hexadecimal to byte array
    private static byte[] toByte(String str) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i]  = (byte) Integer.parseInt(str.substring(2*i, 2*i+2), 16);
        }
        return bytes;
    }

    // Returns a reversed string
    private static String transformReverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    // Returns a transformed capitalized string
    private static String transformCapitalize(String str) {
        Locale l = Locale.getDefault();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.length(); i++) {
            if(i % 2 == 0) {
                sb.append(str.toUpperCase(l).charAt(i));
            }
            else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    // Returns a transformed string
    private static String transformString(String str) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.length(); i++) {
            if(i % 2 == 0) {
                sb.append(str.charAt(i));
            } else {
                int charValue = str.charAt(i);
                sb.append(String.valueOf((char) (charValue + 1)));
            }
        }
        return sb.toString();
    }

    // Returns a transformed numerical string
    protected static String transformDigits(String str) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == 'E') {
                sb.append("3");
            }
            else if(str.charAt(i) == 's') {
                sb.append("5");
            }
            else if(str.charAt(i) == 'b') {
                sb.append("6");
            }
            else if(str.charAt(i) == 'A') {
                sb.append("4");
            }
            else if(str.charAt(i) == 'L') {
                sb.append("7");
            }
            else if(str.charAt(i) == 'O') {
                sb.append("0");
            }
            else if(str.charAt(i) == 'I') {
                sb.append("1");
            }
            else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    public static String performTransform(String str) {
        return transformReverse(transformDigits(transformString(transformCapitalize(str))));
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