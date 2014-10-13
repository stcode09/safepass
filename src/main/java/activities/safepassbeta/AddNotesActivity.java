package activities.safepassbeta;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import objects.safepassbeta.NoteEntry;
import utilities.safepassbeta.LoadingDialog;
import utilities.safepassbeta.FileManager;
import utilities.safepassbeta.Utility;

// Activity to handle adding notes
public class AddNotesActivity extends Activity {

    private boolean onRestart = false;

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
        onRestart = true;
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!onRestart) {
            Utility.currentTime = System.currentTimeMillis() / 1000;
        }
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
        setContentView(R.layout.add_notes);
        ActionBar actionBar = getActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Add Note");
        }

        Intent intent = getIntent();
        int val = intent.getIntExtra(Utility.EDIT_MODE, -1);
        boolean viewMode = intent.getBooleanExtra(Utility.VIEW_MODE, false);

        final EditText title = (EditText) findViewById(R.id.add_notes_title);
        final EditText note = (EditText) findViewById(R.id.add_note);

        if(val != -1 || viewMode) {
            title.setText(Utility.noteEntryList.get(val).getTitle());
            note.setText(Utility.noteEntryList.get(val).getNote());
            if(actionBar != null) {
                actionBar.setTitle("Editing Note: " + title.getText().toString());
            }
            if(viewMode) {
                if(actionBar != null) {
                    actionBar.setTitle("Viewing Note: " + title.getText().toString());
                }
                title.setInputType(InputType.TYPE_NULL);
                title.setTextIsSelectable(true);
                note.setSingleLine(false);
                note.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                note.setTextIsSelectable(true);
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
                final EditText title = (EditText) findViewById(R.id.add_notes_title);
                final EditText note = (EditText) findViewById(R.id.add_note);
                if(Utility.hasText(title, 0) && Utility.hasText(note, 0)) {
                    LoadingDialog.show(AddNotesActivity.this, new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            return save();
                        }
                    }, "Please wait...", "Saving data...");
                } else {
                    Toast.makeText(getApplicationContext(), "Please input a title and note!",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Save method, saves data
    private Integer save() throws NoSuchPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
            InvalidKeySpecException {

        EditText title = (EditText) findViewById(R.id.add_notes_title);
        EditText note = (EditText) findViewById(R.id.add_note);

        Intent intent = getIntent();
        int pos = intent.getIntExtra(Utility.EDIT_MODE, -1);

        if(pos != -1) {
            Utility.noteEntryList.get(pos).setTitle(title.getText().toString());
            Utility.noteEntryList.get(pos).setNote(note.getText().toString());
        } else {
            NoteEntry e = (NoteEntry) EntryFactory.getEntry(Utility.NOTE);
            e.setTitle(title.getText().toString());
            e.setNote(note.getText().toString());
            Utility.noteEntryList.add(e);
        }

        FileManager.writeData(getApplicationContext(), Utility.DATA3_FILE, Utility.NOTE);
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        Utility.fromAdd = true;
        finish();

        return 1;
    }
}
