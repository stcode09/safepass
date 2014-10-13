package activities.safepassbeta;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import utilities.safepassbeta.Crypto;
import utilities.safepassbeta.LoadingDialog;
import utilities.safepassbeta.FileManager;
import utilities.safepassbeta.Utility;


// Activity to handle setup and login
public class SetupAndLoginActivity extends Activity {

    // Local variables
    private boolean hasMetRequirements = false;
    private boolean passwordsMatch = false;
    private int tries;


    // Clear text if app returns from background
    @Override
    protected void onRestart() {
        super.onRestart();
        EditText enterPass = (EditText) findViewById(R.id.enter_pass);
        enterPass.setText("");
    }

    // Call super.onResume
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences(Utility.PREFS_FILE, MODE_PRIVATE);
        if(sharedPreferences.getBoolean("clear_clipboard", false)) {
            Utility.copyToClipboard(getApplicationContext(), "", "", "Clipboard Cleared");
        }
    }

    // Show lock screen if returning from background or another activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_CANCELED) {
                setContentView(R.layout.activity_login);
                EditText enterPass = (EditText) findViewById(R.id.enter_pass);
                enterPass.setText("");
            }
        }
    }

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get preferences file
        SharedPreferences sharedPreferences = getSharedPreferences(Utility.SYS_PREFS_FILE, MODE_PRIVATE);

        // Check whether the app is running for the first time
        boolean isFirstRun = sharedPreferences.getBoolean(Utility.FIRST_RUN, true);

        SharedPreferences sharedPreferences1 = getSharedPreferences(Utility.PREFS_FILE, MODE_PRIVATE);
        // Get number of allowed tries before locking the app
        Utility.tries = Integer.parseInt(sharedPreferences1.getString(Utility.TRIES, "3"));
        tries = Utility.tries;

        // If first run
        if(isFirstRun) {

            // Set to setup layout
            setContentView(R.layout.activity_setup);

            // Text-fields
            final EditText masterPass = (EditText) findViewById(R.id.master_pass);
            final EditText confirmPass = (EditText) findViewById(R.id.confirm_pass);
            final CheckBox requiredLengthBox = (CheckBox) findViewById(R.id.required_length);
            final CheckBox upperCaseBox = (CheckBox) findViewById(R.id.required_uppercase);
            final CheckBox numericalBox = (CheckBox) findViewById(R.id.required_numerical);
            final CheckBox specialBox = (CheckBox) findViewById(R.id.required_special);

            // Check for password requirements
            masterPass.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    String str1 = masterPass.getText().toString();
                    String str2 = confirmPass.getText().toString();

                    // Mark the requirements if met
                    requiredLengthBox.setChecked(Crypto.hasRequiredLength(str1));
                    upperCaseBox.setChecked(Crypto.hasUppercaseChar(str1));
                    numericalBox.setChecked(Crypto.hasNumericalChar(str1));
                    specialBox.setChecked(Crypto.hasSpecialChar(str1));

                    if (requiredLengthBox.isChecked()) {
                        requiredLengthBox.setPaintFlags(strikeThrough(requiredLengthBox));
                    } else {
                        requiredLengthBox.setPaintFlags(deStrikeThrough(requiredLengthBox));
                    }

                    if (upperCaseBox.isChecked()) {
                        upperCaseBox.setPaintFlags(strikeThrough(upperCaseBox));
                    } else {
                        upperCaseBox.setPaintFlags(deStrikeThrough(upperCaseBox));
                    }

                    if (numericalBox.isChecked()) {
                        numericalBox.setPaintFlags(strikeThrough(numericalBox));
                    } else {
                        numericalBox.setPaintFlags(deStrikeThrough(numericalBox));
                    }

                    if (specialBox.isChecked()) {
                        specialBox.setPaintFlags(strikeThrough(specialBox));
                    } else {
                        specialBox.setPaintFlags(deStrikeThrough(specialBox));
                    }

                    if (!str1.equals("") && str2.equals(str1)) {
                        masterPass.setBackgroundResource(R.drawable.textfield_activated_holo_dark);
                        confirmPass.setBackgroundResource(R.drawable.textfield_activated_holo_dark);
                        passwordsMatch = true;
                    } else {
                        masterPass.setBackgroundResource(R.drawable.textfield_default_holo_dark);
                        confirmPass.setBackgroundResource(R.drawable.textfield_default_holo_dark);
                        passwordsMatch = false;
                    }

                    hasMetRequirements = (requiredLengthBox.isChecked() && upperCaseBox.isChecked()
                                          && numericalBox.isChecked() && specialBox.isChecked());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            // Check for password requirements
            confirmPass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    String str1 = masterPass.getText().toString();
                    String str2 = confirmPass.getText().toString();

                    if (!str1.equals("") && str2.equals(str1)) {
                        masterPass.setBackgroundResource(R.drawable.textfield_activated_holo_dark);
                        confirmPass.setBackgroundResource(R.drawable.textfield_activated_holo_dark);
                        passwordsMatch = true;
                    } else {
                        masterPass.setBackgroundResource(R.drawable.textfield_default_holo_dark);
                        confirmPass.setBackgroundResource(R.drawable.textfield_default_holo_dark);
                        passwordsMatch = false;
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            setContentView(R.layout.activity_login);
            EditText enterPass = (EditText) findViewById(R.id.enter_pass);
            enterPass.setText("");
        } // If not first run

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // Setup master password
    public void setupMasterPassword(View view) throws NoSuchAlgorithmException, InvalidKeySpecException {

        // If requirements are met
        if(passwordsMatch && hasMetRequirements) {

            View parent = view.getRootView();
            final EditText masterPass = (EditText) parent.findViewById(R.id.master_pass);
            String str1 = masterPass.getText().toString();

            // Get preferences file editor
            SharedPreferences.Editor editor = getSharedPreferences(Utility.SYS_PREFS_FILE, MODE_PRIVATE).edit();

            // Edit values
            editor.putBoolean(Utility.FIRST_RUN, false);
            editor.putString(Utility.TOKEN1, Crypto.computeHash(str1));
            editor.apply();

            // Set str1
            Utility.str1 = str1;

            // Create app directory
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File directory = new File(Environment.getExternalStorageDirectory()+File.separator+Utility.BACKUP_FOLDER);
                directory.mkdirs();
            }

            List<String> s = new ArrayList<String>() {{
                    add("");
            }};

            // Create files
            FileManager.writeToFile(getApplicationContext(), Utility.DATA1_FILE, s);
            FileManager.writeToFile(getApplicationContext(), Utility.DATA2_FILE, s);
            FileManager.writeToFile(getApplicationContext(), Utility.DATA3_FILE, s);

            // Show loading dialog
            LoadingDialog.show(SetupAndLoginActivity.this, new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return handleLogin();
                }
            }, "Please wait...", "Unlocking Vault...");

        } else if(!passwordsMatch) {
            Toast.makeText(getApplicationContext(), "Passwords don't match!", Toast.LENGTH_SHORT).show();
        } else if(!hasMetRequirements) {
            Toast.makeText(getApplicationContext(), "Requirements not met!", Toast.LENGTH_SHORT).show();
        }

    }

    // Unlock vault
    public void unlockVault(View view) throws NoSuchAlgorithmException, InvalidKeySpecException {
        View parent = view.getRootView();

        // Get preferences file
        SharedPreferences sharedPreferences = getSharedPreferences(Utility.SYS_PREFS_FILE, MODE_PRIVATE);

        // Fields
        final EditText enterPass = (EditText) parent.findViewById(R.id.enter_pass);
        String str1 = enterPass.getText().toString();
        String str2 = sharedPreferences.getString(Utility.TOKEN1, null);

        // If password hash matches then allow entry
        if(!str1.equals("") && str1.length() >= 8) {
            if (Crypto.validateHash(str2, str1)) {
                Utility.str1 = str1;
                LoadingDialog.show(SetupAndLoginActivity.this, new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return handleLogin();
                    }
                }, "Please wait...", "Unlocking Vault...");
            } else {
                Toast.makeText(getApplicationContext(), "Incorrect Password! " + tries
                        + ((tries == 1) ? " try" : " tries") + " remaining!", Toast.LENGTH_SHORT).show();
                if (tries <= 0) {
                    finish();
                }
                tries--;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enter a valid password!", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle login
    private int handleLogin() {

        if(Utility.loginEntryList.size() == 0) {
            try {
                FileManager.readData(getApplicationContext(), Utility.DATA1_FILE, Utility.LOGIN, false, null, false);
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }

        if(Utility.walletEntryList.size() == 0) {
            try {
                FileManager.readData(getApplicationContext(), Utility.DATA2_FILE, Utility.WALLET, false, null, false);
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }

        if(Utility.noteEntryList.size() == 0) {
            try {
                FileManager.readData(getApplicationContext(), Utility.DATA3_FILE, Utility.NOTE, false, null, false);
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }

        // Start dashboard activity
        Intent intent = new Intent(SetupAndLoginActivity.this, DashboardActivity.class);
        startActivityForResult(intent, 1);

        return 0;
    }

    // Strike through text of checkbox
    private int strikeThrough(CheckBox checkBox) {
        return checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG;
    }

    // Un-strike through text of checkbox
    private int deStrikeThrough(CheckBox checkBox) {
        return checkBox.getPaintFlags() & ~ Paint.STRIKE_THRU_TEXT_FLAG;
    }

}

