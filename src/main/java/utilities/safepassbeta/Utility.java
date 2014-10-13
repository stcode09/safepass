package utilities.safepassbeta;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import objects.safepassbeta.LoginEntry;
import objects.safepassbeta.NoteEntry;
import objects.safepassbeta.WalletEntry;

public class Utility {

    public static String str1;
    public static String str2;
    public static List<LoginEntry> loginEntryList = new ArrayList<LoginEntry>();
    public static List<WalletEntry> walletEntryList = new ArrayList<WalletEntry>();
    public static List<NoteEntry> noteEntryList = new ArrayList<NoteEntry>();
    public static String nl = System.getProperty("line.separator");
    public static boolean fromAdd = false;
    public static int tries;
    public static long currentTime = 0;
    public static final String EDIT_MODE = "EDIT_MODE";
    public static final String VIEW_MODE = "VIEW_MODE";
    public static final String DATA1_FILE = ".data1.crypt";
    public static final String DATA2_FILE = ".data2.crypt";
    public static final String DATA3_FILE = ".data3.crypt";
    public static final String BACKUP_FILE = "backup.crypt";
    public static final String BACKUP_FOLDER = "SafePass";
    public static final String LOGIN = "LOGIN";
    public static final String WALLET = "WALLET";
    public static final String NOTE = "NOTE";
    public static final String SYS_PREFS_FILE = "Preferences";
    public static final String PREFS_FILE = "activities.safepassbeta_preferences";
    public static final String FIRST_RUN = "isFirstRun";
    public static final String TRIES = "tries_list";
    public static final String TOKEN1 = "token1";
    public static final String DATA1 = "data1";
    public static final String DATA2 = "data2";
    public static final String DATA3 = "data3";
    public static final String CLIP_LABEL = "SafePass";
    public static final String SPECIAL = "&lt;&eq;&gt;";
    public static final String[] CARDS = new String[]{
            "American Express", "Carte Blanche", "Diners Club", "Discover", "enRoute", "JCB",
            "MasterCard", "Visa"
    };
    public static boolean hasText(Object o, int i) {
        if (o.getClass().getSimpleName().equalsIgnoreCase("EditText")) {
            return ((EditText) o).getText().toString().length() > i;
        } else if (o.getClass().getSimpleName().equalsIgnoreCase("AutoCompleteTextView")) {
            return ((AutoCompleteTextView) o).getText().toString().length() > i;
        }
        return false;
    }

    public static void copyToClipboard(Context context, String clipLabel, String clipMsg, String toastMsg) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(clipLabel, clipMsg);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();
    }

    public static void launchWebsite(Context context, String websiteURL) {
        Uri url = Uri.parse(websiteURL);
        Intent urlIntent = new Intent(Intent.ACTION_VIEW, url);
        context.startActivity(urlIntent);
    }

    public static String strip(String s) {
        if(s.contains("<=>")) {
            return s.replaceAll("<=>", Utility.SPECIAL);
        }
        return s;
    }

    public static String unStrip(String s) {
        if(s.contains(Utility.SPECIAL)) {
            return s.replaceAll(Utility.SPECIAL, "<=>");
        }
        return s;
    }

}
