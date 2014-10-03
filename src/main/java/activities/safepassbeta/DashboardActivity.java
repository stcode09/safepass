package activities.safepassbeta;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

import fragments.safepassbeta.DisplayLoginsFragment;
import fragments.safepassbeta.DisplayNotesFragment;
import fragments.safepassbeta.DisplayWalletFragment;
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
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
            finish();
        }
        Utility.fromAdd = false;
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

}