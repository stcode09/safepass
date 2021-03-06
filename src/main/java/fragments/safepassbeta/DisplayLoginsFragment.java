package fragments.safepassbeta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

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

import activities.safepassbeta.AddLoginsActivity;
import activities.safepassbeta.R;
import adapters.safepassbeta.LoginsAdapter;
import objects.safepassbeta.LoginEntry;
import utilities.safepassbeta.LoadingDialog;
import utilities.safepassbeta.FileManager;
import utilities.safepassbeta.Utility;

// Display logins
public class DisplayLoginsFragment extends ListFragment {

    // Local variables
    private ListView list;
    private LoginsAdapter loginsAdapter;
    private static List<LoginEntry> loginsList = new ArrayList<LoginEntry>();

    public static DisplayLoginsFragment newInstance() {
        loginsList = Utility.loginEntryList;
        return new DisplayLoginsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display_entries, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        list = (ListView) getView().findViewById(android.R.id.list);
        loginsAdapter = new LoginsAdapter(getActivity(), R.layout.fragment_logins_adapter, loginsList);
        list.setAdapter(loginsAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_entry, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(getActivity(), AddLoginsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        list = (ListView) getView().findViewById(android.R.id.list);
        loginsAdapter = new LoginsAdapter(getActivity(), R.layout.fragment_logins_adapter, loginsList);
        list.setAdapter(loginsAdapter);

        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), AddLoginsActivity.class);
                intent.putExtra(Utility.EDIT_MODE, position);
                intent.putExtra(Utility.VIEW_MODE, true);
                startActivity(intent);
            }

        });

        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                final int checkedCount = list.getCheckedItemCount();
                mode.setTitle(checkedCount + " Selected");
                loginsAdapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                final ActionMode newMode = mode;
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        alertDialog.setTitle("Delete?");
                        alertDialog.setMessage("Are you sure you want to delete the selected " +
                                ((list.getCheckedItemCount() > 1) ? "logins?" : "login?"));
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                SparseBooleanArray selected = loginsAdapter
                                        .getSelectedIds();
                                for (int i = (selected.size() - 1); i >= 0; i--) {
                                    if (selected.valueAt(i)) {
                                        LoginEntry selecteditem = loginsAdapter
                                                .getItem(selected.keyAt(i));
                                        loginsAdapter.remove(selecteditem);
                                    }
                                }
                                LoadingDialog.show(getActivity(), new Callable<Integer>() {
                                    @Override
                                    public Integer call() throws Exception {
                                        return delete();
                                    }
                                }, "Please wait...", "Deleting Entry...");
                                newMode.finish();
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                newMode.finish();
                            }
                        });
                        alertDialog.create().show();

                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.delete_entry, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                loginsAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
        });
    }

    private int delete() {
        Utility.loginEntryList = loginsList;
        try {
            FileManager.writeData(getActivity(), Utility.DATA1_FILE, Utility.LOGIN);
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

}
