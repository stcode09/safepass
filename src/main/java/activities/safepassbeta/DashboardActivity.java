package activities.safepassbeta;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import fragments.safepassbeta.DisplayLoginsFragment;
import fragments.safepassbeta.DisplayNotesFragment;
import fragments.safepassbeta.DisplayWalletFragment;
import objects.safepassbeta.ExportEntry;
import objects.safepassbeta.LoginEntry;
import objects.safepassbeta.NoteEntry;
import objects.safepassbeta.WalletEntry;
import utilities.safepassbeta.Crypto;
import utilities.safepassbeta.FileManager;
import utilities.safepassbeta.LoadingDialog;
import utilities.safepassbeta.Utility;


// Main dashboard activity
public class DashboardActivity extends FragmentActivity {

    // Local variables
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    // On back button pressed, show lock screen
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // On return from background
    @Override
    protected void onRestart() {
        super.onRestart();
        if(!Utility.fromAdd) {
            SharedPreferences sharedPreferences = getSharedPreferences(Utility.PREFS_FILE, MODE_PRIVATE);
            long timeout = Integer.parseInt(sharedPreferences.getString("autolock_list", "0"));
            boolean lock = (System.currentTimeMillis()/1000 - Utility.currentTime) > timeout;
            // Toast.makeText(getApplicationContext(), "Lock: " + lock + " fromAdd: " + !Utility.fromAdd, Toast.LENGTH_SHORT).show();
            if(lock) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                Utility.str1 = "";
                finish();
            }
        }
        Utility.fromAdd = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Utility.currentTime = System.currentTimeMillis()/1000;
    }

    // Call super.onResume
    @Override
    protected void onResume() {
        super.onResume();
    }

    // Check if app returns from another activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_CANCELED) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            } else if(resultCode == RESULT_OK) {
                // do nothing
            }
        }
    }

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), UserSettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_clear:
                ClipboardManager clipboardManager = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("", "");
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "Clipboard cleared", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_reset_password:
                showResetPasswordDialog();
                return true;
            case R.id.action_export:
                showExportDialog();
                return true;
            case R.id.action_import:
                showImportDialog();
                return true;
            case R.id.action_help:
                Uri url = Uri.parse("http://stcode09.github.io/safepass/index.html#-faq-");
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, url);
                startActivity(urlIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Display relevant fragments after/before swipe
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return DisplayLoginsFragment.newInstance();
                case 1:
                    return DisplayWalletFragment.newInstance();
                case 2:
                    return DisplayNotesFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_logins).toUpperCase(l);
                case 1:
                    return getString(R.string.title_wallet).toUpperCase(l);
                case 2:
                    return getString(R.string.title_notes).toUpperCase(l);
            }
            return null;
        }
    }

    private void showResetPasswordDialog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        LayoutInflater inflater = DashboardActivity.this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_reset, null);
        alertDialog.setView(v);
        alertDialog.setTitle("Reset Master Password?");

        final EditText oldPass = (EditText) v.findViewById(R.id.old_pass);
        final EditText newPass = (EditText) v.findViewById(R.id.new_pass);
        final EditText newPassConfirm = (EditText) v.findViewById(R.id.new_pass_confirm);

        alertDialog.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {
                String str1 = oldPass.getText().toString();
                String str2 = newPass.getText().toString();
                String str3 = newPassConfirm.getText().toString();
                if(str1.equals(Utility.str1) && str2.equals(str3) && !str1.equals(str2) && !str1.equals("")
                        && Crypto.hasLowercaseChar(str2) && Crypto.hasUppercaseChar(str2) &&
                        Crypto.hasNumericalChar(str2) && Crypto.hasSpecialChar(str2)) {
                    Utility.str1 = str2;
                    SharedPreferences.Editor editor = getSharedPreferences(Utility.SYS_PREFS_FILE, MODE_PRIVATE).edit();
                    try {
                        editor.putString(Utility.TOKEN1, Crypto.computeHash(str2));
                        editor.apply();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }
                    LoadingDialog.show(DashboardActivity.this, new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            return handleReset();
                        }
                    }, "Please wait...", "Reseting password...");

                    Toast.makeText(getApplicationContext(), "Password reset successful", Toast.LENGTH_SHORT).show();
                } else if(str1.equals(str2) && !str2.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a different password", Toast.LENGTH_SHORT).show();
                } else if(!str1.equals(Utility.str1) || str1.equals("")) {
                    Toast.makeText(getApplicationContext(), "Incorrect Master Password", Toast.LENGTH_SHORT).show();
                } else if(!str2.equals(str3)) {
                    Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                } else if(!Crypto.hasLowercaseChar(str2) || !Crypto.hasUppercaseChar(str2)
                            || Crypto.hasNumericalChar(str2) || Crypto.hasSpecialChar(str2)) {
                    Toast.makeText(getApplicationContext(), "Password requirements not met", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.create().show();
    }

    private Integer handleReset() {
        try {
            FileManager.writeData(getApplicationContext(), Utility.DATA1_FILE, Utility.LOGIN);
            FileManager.writeData(getApplicationContext(), Utility.DATA2_FILE, Utility.WALLET);
            FileManager.writeData(getApplicationContext(), Utility.DATA3_FILE, Utility.NOTE);
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

        return 0;
    }

    private void showExportDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        alertDialog.setTitle("Export Backup");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + Utility.BACKUP_FOLDER);
            if(folder.isDirectory() && folder.exists()) {
                final String file = folder.getAbsolutePath()+File.separator+Utility.BACKUP_FILE;
                alertDialog.setMessage("Export backup to:" + Utility.nl + file + "");
                alertDialog.setPositiveButton("Export", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoadingDialog.show(DashboardActivity.this, new Callable<Integer>() {
                            @Override
                            public Integer call() throws Exception {
                                return handleExport();
                            }
                        }, "Please wait...", "Exporting data...");
                        Toast.makeText(getApplicationContext(), "Backup exported", Toast.LENGTH_SHORT).show();
                        File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + Utility.BACKUP_FOLDER + File.separator + Utility.BACKUP_FILE);
                        if(file1.exists()) {
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file1));
                            shareIntent.setType("text/plain");
                            startActivity(Intent.createChooser(shareIntent, "Share File"));
                        }
                    }
                });
            } else {
                alertDialog.setMessage("No backup folder found. Cannot export.");
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        }
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.create().show();
    }

    private void showImportDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        alertDialog.setTitle("Import Backup");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + Utility.BACKUP_FOLDER
                                + File.separator + Utility.BACKUP_FILE);
            if (file.exists()) {
                alertDialog.setMessage("Backup file found at:" + Utility.nl + file.getAbsolutePath() +
                                        Utility.nl + Utility.nl + "Please enter the master password associated " +
                                                "with the backup file below");
                final EditText password = new EditText(this);
                final LinearLayout layout = new LinearLayout(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params.setMargins(10, 5, 10, 5);
                password.setId(0);
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                password.setTypeface(Typeface.SANS_SERIF);
                password.setHint("Password");
                password.setLayoutParams(params);
                layout.addView(password);
                alertDialog.setView(layout);
                alertDialog.setPositiveButton("Import", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoadingDialog.show(DashboardActivity.this, new Callable<Integer>() {
                            @Override
                            public Integer call() throws Exception {
                                EditText e = (EditText) layout.findViewById(0);
                                Utility.str2 = e.getText().toString();
                                return handleImport();
                            }
                        }, "Please wait...", "Importing data...");
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            } else {
                alertDialog.setMessage("No backup file was found.");
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        } else {
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }
        alertDialog.create().show();
    }

    private Integer handleExport() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + Utility.BACKUP_FOLDER
                                + File.separator + Utility.BACKUP_FILE);
            FileManager.exportData(getApplicationContext(), file.getAbsolutePath());
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
        return 0;
    }

    private Integer handleImport() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(Utility.PREFS_FILE, MODE_PRIVATE);
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + Utility.BACKUP_FOLDER
                    + File.separator + Utility.BACKUP_FILE);
            if(FileManager.importData(getApplicationContext(), file.getAbsolutePath(), sharedPreferences.getBoolean("merge_duplicates", true))) {
                FileManager.writeData(getApplicationContext(), Utility.DATA1_FILE, Utility.LOGIN);
                FileManager.writeData(getApplicationContext(), Utility.DATA2_FILE, Utility.WALLET);
                FileManager.writeData(getApplicationContext(), Utility.DATA3_FILE, Utility.NOTE);
                finish();
                startActivity(getIntent());
                return 0;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        return 1;
    }

}