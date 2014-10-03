package activities.safepassbeta;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.Callable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import objects.safepassbeta.EntryFactory;
import objects.safepassbeta.LoginEntry;
import utilities.safepassbeta.Crypto;
import utilities.safepassbeta.LoadingDialog;
import utilities.safepassbeta.FileManager;
import utilities.safepassbeta.Utility;


// Activity to handle adding logins
public class AddLoginsActivity extends Activity {

    // Local variables
    private boolean isVisible = false;
    private boolean lowercase = true;
    private boolean uppercase = true;
    private boolean digits = true;
    private boolean special = true;
    private int length = 10;

    // On back button pressed, show lock screen
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            Utility.fromAdd = true;
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // On return from background
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    // call super.onResume
    @Override
    protected void onResume() {
        super.onResume();
    }

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_logins);

        // Set title
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Add Login");
        }

        // Check if in edit mode or view mode
        Intent intent = getIntent();
        int val = intent.getIntExtra(Utility.EDIT_MODE, -1);
        boolean viewMode = intent.getBooleanExtra(Utility.VIEW_MODE, false);

        // Text-fields
        final EditText label = (EditText) findViewById(R.id.add_label);
        final EditText username = (EditText) findViewById(R.id.add_username);
        final EditText password = (EditText) findViewById(R.id.add_password);
        final EditText website = (EditText) findViewById(R.id.add_website);
        final EditText comments = (EditText) findViewById(R.id.add_comments);
        final TextView strength = (TextView) findViewById(R.id.pass_strength);

        // Text change listener for password strength
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String percent = Crypto.getPasswordStrength(password.getText().toString());
                strength.setText("Password Strength: " + percent);
                if((password.getText().toString().contains(username.getText().toString()) ||
                    username.getText().toString().contains(password.getText().toString())) &&
                    !username.getText().toString().equals("")) {
                    strength.setText("Password Strength: Guessable");
                }
                if(password.getText().toString().equals("")) {
                    strength.setText("Password Strength: N/A");
                }
            }
        });

        // Generate password button
        ImageButton generatePass = (ImageButton) findViewById(R.id.generate_pass);
        generatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePassword(view);
            }
        });

        // Options menu for password generator
        generatePass.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddLoginsActivity.this);
                builder.setTitle("Password Generator");
                LayoutInflater inflater = AddLoginsActivity.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_password_gen, null);
                builder.setView(v);
                final CheckBox lowercase_box = (CheckBox) v.findViewById(R.id.use_lowercase);
                final CheckBox uppercase_box = (CheckBox) v.findViewById(R.id.use_uppercase);
                final CheckBox digits_box = (CheckBox) v.findViewById(R.id.use_digits);
                final CheckBox special_box = (CheckBox) v.findViewById(R.id.use_special);

                lowercase_box.setChecked(lowercase);
                uppercase_box.setChecked(uppercase);
                digits_box.setChecked(digits);
                special_box.setChecked(special);

                final SeekBar seekBar = (SeekBar) v.findViewById(R.id.seekbar);
                final TextView pass_length = (TextView) v.findViewById(R.id.text_seek);

                seekBar.setMax(28);
                seekBar.setProgress(length - 4);

                pass_length.setText("Password Length: " + length);

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        pass_length.setText("Password Length: " + (i + 4));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lowercase = (lowercase_box.isChecked());
                        uppercase = (uppercase_box.isChecked());
                        digits = (digits_box.isChecked());
                        special = (special_box.isChecked());
                        length = seekBar.getProgress() + 4;
                    }
                });
                builder.create().show();
                return true;
            }
        });

        // If editing mode or view mode
        if(val != -1 || viewMode) {
            label.setText(Utility.loginEntryList.get(val).getLabel());
            username.setText(Utility.loginEntryList.get(val).getUsername());
            password.setText(Utility.loginEntryList.get(val).getPassword());
            if(Utility.loginEntryList.get(val).getWebsite().equals("nUlL")) {
                website.setText("");
            } else {
                website.setText(Utility.loginEntryList.get(val).getWebsite());
            }
            if(Utility.loginEntryList.get(val).getComments().equals("nUlL")) {
                comments.setText("");
            } else {
                comments.setText(Utility.loginEntryList.get(val).getComments());
            }

            if(actionBar != null) {
                actionBar.setTitle("Editing Login: " + label.getText().toString());
            }

            if(viewMode) {

                if(actionBar != null) {
                    actionBar.setTitle("Viewing Login: " + label.getText().toString());
                }

                label.setInputType(InputType.TYPE_NULL);
                label.setTextIsSelectable(true);
                username.setInputType(InputType.TYPE_NULL);
                username.setTextIsSelectable(true);
                password.setInputType(InputType.TYPE_NULL);
                password.setTextIsSelectable(true);
                website.setInputType(InputType.TYPE_NULL);
                website.setTextIsSelectable(true);
                comments.setInputType(InputType.TYPE_NULL);
                comments.setTextIsSelectable(true);
                isVisible = true;
                viewPassword(getCurrentFocus());

                generatePass.setVisibility(View.INVISIBLE);
            }
        }

        if(!viewMode) {
            website.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b && website.getText().toString().length() == 0) {
                        website.setText("http://");
                        website.setSelection("http://".length());
                    } else if(!b && website.getText().toString().equals("http://")) {
                        website.setText("");
                    }

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Intent intent = getIntent();
        boolean viewMode = intent.getBooleanExtra(Utility.VIEW_MODE, false);
        if(!viewMode) {
            getMenuInflater().inflate(R.menu.save_entries, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_save:

                final EditText label = (EditText) findViewById(R.id.add_label);
                final EditText username = (EditText) findViewById(R.id.add_username);
                final EditText password = (EditText) findViewById(R.id.add_password);

                if(Utility.hasText(label, 0) && Utility.hasText(username, 0) &&
                        Utility.hasText(password, 0)) {
                    LoadingDialog.show(AddLoginsActivity.this, new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            return save();
                        }
                    }, "Please wait...", "Saving data...");
                } else {
                    Toast.makeText(getApplicationContext(), "Please input a label, username, & " +
                                                            "password!", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // View password function
    public void viewPassword(View view) {
        EditText password = (EditText) findViewById(R.id.add_password);
        ImageButton viewPass = (ImageButton) findViewById(R.id.view_pass);
        if(!isVisible) {
            viewPass.setImageResource(R.drawable.ic_action_device_access_not_secure);
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            password.setTypeface(Typeface.SANS_SERIF);
            password.setSelection(password.getText().length());
            isVisible = true;
        } else {
            viewPass.setImageResource(R.drawable.ic_action_device_access_secure);
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            password.setTypeface(Typeface.SANS_SERIF);
            password.setSelection(password.getText().length());
            isVisible = false;
        }
    }

    // Generate password function
    public void generatePassword(View view) {
            EditText password = (EditText) findViewById(R.id.add_password);
            password.setText("");
            if(!lowercase && !uppercase && !digits && !special ) {
                Toast.makeText(getApplicationContext(), "Please select at least one option from the " +
                        "password generator settings!", Toast.LENGTH_SHORT).show();
            } else {
                password.setText(Crypto.generatePassword(lowercase, uppercase, digits, special, length));
            }
            isVisible = false;
            viewPassword(view);
    }

    // Save method, saves data
    private Integer save() throws NoSuchPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
            InvalidKeySpecException {

        EditText label = (EditText) findViewById(R.id.add_label);
        EditText username = (EditText) findViewById(R.id.add_username);
        EditText password = (EditText) findViewById(R.id.add_password);
        EditText website = (EditText) findViewById(R.id.add_website);
        EditText comments = (EditText) findViewById(R.id.add_comments);

        Intent intent = getIntent();
        int pos = intent.getIntExtra(Utility.EDIT_MODE, -1);

        if(pos != -1) {
            Utility.loginEntryList.get(pos).setLabel(label.getText().toString());
            Utility.loginEntryList.get(pos).setUsername(username.getText().toString());
            Utility.loginEntryList.get(pos).setPassword(password.getText().toString());
            Utility.loginEntryList.get(pos).setWebsite(website.getText().toString());
            Utility.loginEntryList.get(pos).setComments(comments.getText().toString());
        } else {
            LoginEntry e = (LoginEntry) EntryFactory.getEntry(Utility.LOGIN);
            e.setLabel(label.getText().toString());
            e.setUsername(username.getText().toString());
            e.setPassword(password.getText().toString());
            e.setWebsite(website.getText().toString());
            e.setComments(comments.getText().toString());
            Utility.loginEntryList.add(e);
        }

        FileManager.writeData(getApplicationContext(), Utility.DATA1_FILE, Utility.LOGIN);

        SharedPreferences.Editor editor = getSharedPreferences(Utility.PREFS_FILE, MODE_PRIVATE).edit();
        editor.putBoolean(Utility.DATA1, true).apply();
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        Utility.fromAdd = true;
        finish();

        return 1;
    }
}
