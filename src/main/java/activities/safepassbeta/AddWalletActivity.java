package activities.safepassbeta;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
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
import objects.safepassbeta.WalletEntry;
import utilities.safepassbeta.LoadingDialog;
import utilities.safepassbeta.FileManager;
import utilities.safepassbeta.Utility;

// Activity to handle adding wallet entry
public class AddWalletActivity extends Activity {

    // Local variables
    private boolean isVisible = false;
    private String cardNumber = "";

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
        setContentView(R.layout.add_wallet);
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Add Credit Card");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, Utility.CARDS);

        Intent intent = getIntent();
        int val = intent.getIntExtra(Utility.EDIT_MODE, -1);
        boolean viewMode = intent.getBooleanExtra(Utility.VIEW_MODE, false);

        final EditText label = (EditText) findViewById(R.id.add_wallet_label);
        final AutoCompleteTextView cardType = (AutoCompleteTextView) findViewById(R.id.add_card_type);
        final EditText name = (EditText) findViewById(R.id.add_wallet_name);
        final EditText number = (EditText) findViewById(R.id.add_number);
        final EditText expiration = (EditText) findViewById(R.id.add_expiration);
        final EditText security = (EditText) findViewById(R.id.add_security_code);

        expiration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if(expiration.getText().toString().length() == 2 && i3 != 0) {
                    expiration.setText(expiration.getText().toString() + "/");
                    expiration.setSelection(expiration.getText().toString().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cardType.setAdapter(adapter);

        cardNumber = number.getText().toString();

        number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    isVisible = true;
                    viewNumber(view);
                } else {
                    isVisible = false;
                    viewNumber(view);
                }
            }
        });

        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if(!number.getText().toString().startsWith("X"))
                    cardNumber = number.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if(val != -1 || viewMode) {
            label.setText(Utility.walletEntryList.get(val).getLabel());
            cardType.setText(Utility.walletEntryList.get(val).getCardType());
            name.setText(Utility.walletEntryList.get(val).getNameOnCard());
            cardNumber = Utility.walletEntryList.get(val).getNumber();
            number.setText(cardNumber);
            expiration.setText(Utility.walletEntryList.get(val).getExpiration());
            security.setText(Utility.walletEntryList.get(val).getSecurityCode());

            if(actionBar != null) {
                actionBar.setTitle("Editing Credit Card: " + label.getText().toString());
            }

            isVisible = true;
            viewNumber(getCurrentFocus());

            if(viewMode) {

                if(actionBar != null) {
                    actionBar.setTitle("Viewing Credit Card: " + label.getText().toString());
                }

                label.setInputType(InputType.TYPE_NULL);
                label.setTextIsSelectable(true);
                cardType.setInputType(InputType.TYPE_NULL);
                cardType.setTextIsSelectable(true);
                name.setInputType(InputType.TYPE_NULL);
                name.setTextIsSelectable(true);
                number.setInputType(InputType.TYPE_NULL);
                number.setTextIsSelectable(true);
                expiration.setInputType(InputType.TYPE_NULL);
                expiration.setTextIsSelectable(true);
                security.setInputType(InputType.TYPE_NULL);
                security.setTextIsSelectable(true);
            }
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
                final EditText label = (EditText) findViewById(R.id.add_wallet_label);
                final AutoCompleteTextView cardType = (AutoCompleteTextView) findViewById(R.id.add_card_type);
                final EditText name = (EditText) findViewById(R.id.add_wallet_name);
                final EditText number = (EditText) findViewById(R.id.add_number);
                final EditText expiration = (EditText) findViewById(R.id.add_expiration);
                final EditText security = (EditText) findViewById(R.id.add_security_code);
                if(Utility.hasText(label, 0) && Utility.hasText(cardType, 0) && Utility.hasText(name, 0)
                        && Utility.hasText(number, 12) && Utility.hasText(expiration, 4)
                        && Utility.hasText(security, 2)) {
                    LoadingDialog.show(AddWalletActivity.this, new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            return save();
                        }
                    }, "Please wait...", "Saving data...");
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill out all the required fields", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Method to view credit card number
    public void viewNumber(View view) {
        ImageButton view_number = (ImageButton) findViewById(R.id.view_number);
        EditText number = (EditText) findViewById(R.id.add_number);
        if(!isVisible) {
            number.setText(cardNumber);
            view_number.setImageResource(R.drawable.ic_action_device_access_not_secure);
            number.setSelection(cardNumber.length());
            isVisible = true;
        } else {
            view_number.setImageResource(R.drawable.ic_action_device_access_secure);
            number.setText(showX(cardNumber));
            number.setSelection(cardNumber.length());
            isVisible = false;
        }
    }

    // Hide credit card number
    private String showX(String str) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.length(); i++) {
            if((str.length() - i <= 4)) {
                sb.append(str.charAt(i));
            } else {
                sb.append("X");
            }
        }
        return sb.toString();
    }

    // Save method, saves data
    private Integer save() throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {

        final EditText label = (EditText) findViewById(R.id.add_wallet_label);
        final AutoCompleteTextView cardType = (AutoCompleteTextView) findViewById(R.id.add_card_type);
        final EditText name = (EditText) findViewById(R.id.add_wallet_name);
        final EditText expiration = (EditText) findViewById(R.id.add_expiration);
        final EditText security = (EditText) findViewById(R.id.add_security_code);

        Intent intent = getIntent();
        int pos = intent.getIntExtra(Utility.EDIT_MODE, -1);

        if(pos != -1) {
            Utility.walletEntryList.get(pos).setLabel(label.getText().toString());
            Utility.walletEntryList.get(pos).setCardType(cardType.getText().toString());
            Utility.walletEntryList.get(pos).setNameOnCard(name.getText().toString());
            Utility.walletEntryList.get(pos).setNumber(cardNumber);
            Utility.walletEntryList.get(pos).setExpiration(expiration.getText().toString());
            Utility.walletEntryList.get(pos).setSecurityCode(security.getText().toString());
        } else {
            WalletEntry e = (WalletEntry) EntryFactory.getEntry(Utility.WALLET);
            e.setLabel(label.getText().toString());
            e.setCardType(cardType.getText().toString());
            e.setNameOnCard(name.getText().toString());
            e.setNumber(cardNumber);
            e.setExpiration(expiration.getText().toString());
            e.setSecurityCode(security.getText().toString());
            Utility.walletEntryList.add(e);
        }

        FileManager.writeData(getApplicationContext(), Utility.DATA2_FILE, Utility.WALLET);

        SharedPreferences.Editor editor = getSharedPreferences(Utility.PREFS_FILE, MODE_PRIVATE).edit();
        editor.putBoolean(Utility.DATA2, true).apply();
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        Utility.fromAdd = true;
        finish();

        return 1;
    }
}
