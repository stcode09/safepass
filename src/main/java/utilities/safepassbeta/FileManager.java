package utilities.safepassbeta;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import objects.safepassbeta.EntryFactory;
import objects.safepassbeta.LoginEntry;
import objects.safepassbeta.NoteEntry;
import objects.safepassbeta.WalletEntry;

public class FileManager {


    // Returns an array list of the lines in a file
    public static List<String> readFile(Context context, String file) {
        List<String> lines = new ArrayList<String>();
        try {
            // Open file
            InputStream inputStream = context.openFileInput(file);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    lines.add(str);
                }
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("Utility Activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Utility Activity", "Cannot read file: " + e.toString());
        }
        return lines;
    }

    // Writes each line to a file
    public static void writeToFile(Context context, String file, List<String> data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE));
            for(String str : data) {
                outputStreamWriter.write(str);
            }
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static void readData(Context context, String file, String entry_type) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {

        List<String> lines = readFile(context, file);

        for(String entry : lines) {
            if(entry_type.equalsIgnoreCase(Utility.LOGIN)) {
                String str = Crypto.decrypt(Crypto.obfuscateText(entry), Utility.str1);
                String[] parts = str.split("<=>");
                LoginEntry e = (LoginEntry) EntryFactory.getEntry(entry_type);
                e.setLabel(parts[0]);
                e.setUsername(parts[1]);
                e.setPassword(parts[2]);
                e.setWebsite(parts[3]);
                e.setComments(parts[4]);
                Utility.loginEntryList.add(e);
            }
            if(entry_type.equalsIgnoreCase(Utility.WALLET)) {
                String str = Crypto.decrypt(Crypto.obfuscateText(entry), Utility.str1);
                String[] parts = str.split("<=>");
                WalletEntry e = (WalletEntry) EntryFactory.getEntry(entry_type);
                e.setLabel(parts[0]);
                e.setCardType(parts[1]);
                e.setNameOnCard(parts[2]);
                e.setNumber(parts[3]);
                e.setExpiration(parts[4]);
                e.setSecurityCode(parts[5]);
                Utility.walletEntryList.add(e);
            }
            if(entry_type.equalsIgnoreCase(Utility.NOTE)) {
                String str = Crypto.decrypt(Crypto.obfuscateText(entry), Utility.str1);
                String[] parts = str.split("<=>");
                NoteEntry e = (NoteEntry) EntryFactory.getEntry(entry_type);
                e.setTitle(parts[0]);
                e.setNote(parts[1]);
                Utility.noteEntryList.add(e);
            }
        }
    }

    public static void writeData(Context context, String file, String entry_type) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        List<String> lines = new ArrayList<String>();
        if(entry_type.equalsIgnoreCase(Utility.LOGIN)) {
            for(LoginEntry e : Utility.loginEntryList) {
                lines.add(Crypto.obfuscateText(Crypto.encrypt(e.toString(), Utility.str1)) + Utility.nl);
            }
        }
        if(entry_type.equalsIgnoreCase(Utility.WALLET)) {
            for(WalletEntry e : Utility.walletEntryList) {
                lines.add(Crypto.obfuscateText(Crypto.encrypt(e.toString(), Utility.str1)) + Utility.nl);
            }
        }
        if(entry_type.equalsIgnoreCase(Utility.NOTE)) {
            for(NoteEntry e : Utility.noteEntryList) {
                lines.add(Crypto.obfuscateText(Crypto.encrypt(e.toString(), Utility.str1)) + Utility.nl);
            }
        }
        writeToFile(context, file, lines);
    }

}
