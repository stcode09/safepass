package adapters.safepassbeta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import activities.safepassbeta.AddNotesActivity;
import activities.safepassbeta.R;
import objects.safepassbeta.NoteEntry;
import utilities.safepassbeta.Utility;

// Custom logins adapter for listview
public class NotesAdapter extends ArrayAdapter<NoteEntry> {

    // Local variables
    private LayoutInflater inflater;
    private List<NoteEntry> notesList;
    private SparseBooleanArray mSelectedItemsIds;

    public NotesAdapter(Context context, int resource, List<NoteEntry> notesList) {
        super(context, resource, notesList);
        mSelectedItemsIds = new SparseBooleanArray();
        this.notesList = notesList;
        inflater = LayoutInflater.from(context);
    }

    // View holder
    private class ViewHolder {
        TextView title;
        TextView body;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View row;
        if(convertView == null) {
            row = inflater.inflate(R.layout.fragment_notes_adapter, null);
            holder.title = (TextView) row.findViewById(R.id.noteTitle);
            holder.body = (TextView) row.findViewById(R.id.noteBody);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            row = convertView;
        }

        holder.title.setText(notesList.get(position).getTitle());
        holder.body.setText(notesList.get(position).getNote());

        ImageButton copyText = (ImageButton) row.findViewById(R.id.copyText);
        copyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.copyToClipboard(getContext(), Utility.CLIP_LABEL,
                        notesList.get(position).getNote(), "Note Copied to Clipboard");
            }
        });
        copyText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Copy to Clipboard:")
                        .setItems(R.array.NotesListItems, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                if(which == 0) {
                                    Utility.copyToClipboard(getContext(), Utility.CLIP_LABEL,
                                            notesList.get(position).getNote(), "Note Copied to Clipboard");
                                } else if(which == 1) {
                                    Utility.copyToClipboard(getContext(), Utility.CLIP_LABEL,
                                            notesList.get(position).getTitle(), "Title Copied to Clipboard");
                                }
                            }
                        });
                builder.create().show();
                return true;
            }
        });
        copyText.setTag(position);

        ImageButton editEntry = (ImageButton) row.findViewById(R.id.editEntry);
        editEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddNotesActivity.class);
                intent.putExtra(Utility.EDIT_MODE, position);
                getContext().startActivity(intent);
            }
        });
        editEntry.setTag(position);

        return row;
    }

    @Override
    public void remove(NoteEntry object) {
        notesList.remove(object);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, true);
        }
        else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

}

