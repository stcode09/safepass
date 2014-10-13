package utilities.safepassbeta;

import android.content.Context;
import android.preference.PreferenceScreen;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import objects.safepassbeta.ExportEntry;
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

    public static void readData(Context context, String file, String entry_type, boolean isImport,
                                List<String> data, boolean mergeDuplicates) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {

        List<String> lines = (isImport) ? data : readFile(context, file);

        for(String entry : lines) {
            if(entry_type.equalsIgnoreCase(Utility.LOGIN)) {
                String str = Crypto.decrypt(entry, (isImport) ? Utility.str2 : Utility.str1);
                String[] parts = str.split("<=>");
                LoginEntry e = (LoginEntry) EntryFactory.getEntry(entry_type);
                e.setLabel(parts[0]);
                e.setUsername(parts[1]);
                e.setPassword(parts[2]);
                e.setWebsite(parts[3]);
                e.setComments(parts[4]);
                if(isImport) {
                    if(mergeDuplicates) {
                        if (!Utility.loginEntryList.contains(e)) {
                            Utility.loginEntryList.add(e);
                        }
                    } else {
                        Utility.loginEntryList.add(e);
                    }
                } else {
                    Utility.loginEntryList.add(e);
                }
            }
            if(entry_type.equalsIgnoreCase(Utility.WALLET)) {
                String str = Crypto.decrypt(entry, (isImport) ? Utility.str2 : Utility.str1);
                String[] parts = str.split("<=>");
                WalletEntry e = (WalletEntry) EntryFactory.getEntry(entry_type);
                e.setLabel(parts[0]);
                e.setCardType(parts[1]);
                e.setNameOnCard(parts[2]);
                e.setNumber(parts[3]);
                e.setExpiration(parts[4]);
                e.setSecurityCode(parts[5]);
                if(isImport) {
                    if (mergeDuplicates) {
                        if(!Utility.walletEntryList.contains(e)) {
                            Utility.walletEntryList.add(e);
                        }
                    } else {
                        Utility.walletEntryList.add(e);
                    }
                } else {
                    Utility.walletEntryList.add(e);
                }
            }
            if(entry_type.equalsIgnoreCase(Utility.NOTE)) {
                String str = Crypto.decrypt(entry, (isImport) ? Utility.str2 : Utility.str1);
                String[] parts = str.split("<=>");
                NoteEntry e = (NoteEntry) EntryFactory.getEntry(entry_type);
                e.setTitle(parts[0]);
                e.setNote(parts[1]);
                if(isImport) {
                    if(mergeDuplicates) {
                        if (!Utility.noteEntryList.contains(e)) {
                            Utility.noteEntryList.add(e);
                        }
                    } else {
                        Utility.noteEntryList.add(e);
                    }
                } else {
                    Utility.noteEntryList.add(e);
                }
            }
        }
    }

    public static void writeData(Context context, String file, String entry_type) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        List<String> lines = new ArrayList<String>();
        if(entry_type.equalsIgnoreCase(Utility.LOGIN)) {
            for(LoginEntry e : Utility.loginEntryList) {
                lines.add(Crypto.encrypt(e.toString(), Utility.str1) + Utility.nl);
            }
        }
        if(entry_type.equalsIgnoreCase(Utility.WALLET)) {
            for(WalletEntry e : Utility.walletEntryList) {
                lines.add(Crypto.encrypt(e.toString(), Utility.str1) + Utility.nl);
            }
        }
        if(entry_type.equalsIgnoreCase(Utility.NOTE)) {
            for(NoteEntry e : Utility.noteEntryList) {
                lines.add(Crypto.encrypt(e.toString(), Utility.str1) + Utility.nl);
            }
        }
        writeToFile(context, file, lines);
    }

    public static void exportData(Context context, String file) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(file));
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            ExportEntry e = new ExportEntry();
            for(LoginEntry l : Utility.loginEntryList) {
                e.setLoginItem(Crypto.encrypt(l.toString(), Utility.str1));
            }
            for(WalletEntry w : Utility.walletEntryList) {
                e.setWalletItem(Crypto.encrypt(w.toString(), Utility.str1));
            }
            for(NoteEntry n : Utility.noteEntryList) {
                e.setNoteItem(Crypto.encrypt(n.toString(), Utility.str1));
            }
            e.setT1(Crypto.computeHash(Utility.str1));
            out.writeObject(e);
            out.close();
            fileOutputStream.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static boolean importData(Context context, String file, boolean mergeDuplicates) throws ClassNotFoundException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(file));
            ObjectInputStream input = new ObjectInputStream(fileInputStream);
            ExportEntry e = (ExportEntry) input.readObject();
            if(Crypto.validateHash(e.getT1(), Utility.str2)) {
                readData(context, file, Utility.LOGIN, true, e.getL1(), mergeDuplicates);
                readData(context, file, Utility.WALLET, true, e.getW1(), mergeDuplicates);
                readData(context, file, Utility.NOTE, true, e.getN1(), mergeDuplicates);
                return true;
            } else {
                return false;
            }
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        return false;
    }

}
