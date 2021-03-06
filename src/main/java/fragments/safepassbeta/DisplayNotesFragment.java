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

import activities.safepassbeta.AddNotesActivity;
import activities.safepassbeta.R;
import adapters.safepassbeta.NotesAdapter;
import objects.safepassbeta.NoteEntry;
import utilities.safepassbeta.LoadingDialog;
import utilities.safepassbeta.FileManager;
import utilities.safepassbeta.Utility;

// Display Notes
public class DisplayNotesFragment extends ListFragment {

    // Local variables
    private ListView list;
    private NotesAdapter notesAdapter;
    private static List<NoteEntry> notesList = new ArrayList<NoteEntry>();

    public static DisplayNotesFragment newInstance() {
        notesList = Utility.noteEntryList;
        return new DisplayNotesFragment();
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
        notesAdapter = new NotesAdapter(getActivity(), R.layout.fragment_notes_adapter, notesList);
        list.setAdapter(notesAdapter);
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
                Intent intent = new Intent(getActivity(), AddNotesActivity.class);
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
        notesAdapter = new NotesAdapter(getActivity(), R.layout.fragment_notes_adapter, notesList);
        list.setAdapter(notesAdapter);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), AddNotesActivity.class);
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
                notesAdapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                final ActionMode newMode = mode;
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        alertDialog.setTitle("Delete?");
                        alertDialog.setMessage("Are you sure you want to delete the selected " +
                                ((list.getCheckedItemCount() > 1) ? "notes?" : "note?"));
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                SparseBooleanArray selected = notesAdapter
                                        .getSelectedIds();
                                for (int i = (selected.size() - 1); i >= 0; i--) {
                                    if (selected.valueAt(i)) {
                                        NoteEntry selecteditem = notesAdapter
                                                .getItem(selected.keyAt(i));
                                        notesAdapter.remove(selecteditem);
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
                notesAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
        });
    }

    private int delete() {
        Utility.noteEntryList = notesList;
        try {
            FileManager.writeData(getActivity(), Utility.DATA3_FILE, Utility.NOTE);
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
