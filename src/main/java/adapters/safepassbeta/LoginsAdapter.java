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
import android.widget.Toast;

import java.util.List;

import activities.safepassbeta.AddLoginsActivity;
import activities.safepassbeta.R;
import objects.safepassbeta.LoginEntry;
import utilities.safepassbeta.Utility;

// Custom logins adapter for listview
public class LoginsAdapter extends ArrayAdapter<LoginEntry> {

    // Local variables
    private LayoutInflater inflater;
    private List<LoginEntry> loginsList;
    private SparseBooleanArray mSelectedItemsIds;

    public LoginsAdapter(Context context, int resource, List<LoginEntry> loginsList) {
        super(context, resource, loginsList);
        mSelectedItemsIds = new SparseBooleanArray();
        this.loginsList = loginsList;
        inflater = LayoutInflater.from(context);
    }

    // View holder
    private class ViewHolder {
        TextView label;
        TextView username;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View row;
        if(convertView == null) {
            row = inflater.inflate(R.layout.fragment_logins_adapter, null);
            holder.label = (TextView) row.findViewById(R.id.loginsLabel);
            holder.username = (TextView) row.findViewById(R.id.loginsUsername);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            row = convertView;
        }
        holder.label.setText(loginsList.get(position).getLabel());
        holder.username.setText(loginsList.get(position).getUsername());

        ImageButton copyText = (ImageButton) row.findViewById(R.id.copyText);
        copyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.copyToClipboard(getContext(), Utility.CLIP_LABEL,
                        loginsList.get(position).getPassword(), "Password Copied to Clipboard");
            }
        });
        copyText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Copy To Clipboard:")
                        .setItems(R.array.LoginsListItems, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                if(which == 0) {
                                    Utility.copyToClipboard(getContext(), Utility.CLIP_LABEL,
                                            loginsList.get(position).getPassword(),
                                            "Password Copied to Clipboard");
                                } else if(which == 1) {
                                    Utility.copyToClipboard(getContext(), Utility.CLIP_LABEL,
                                            loginsList.get(position).getUsername(),
                                            "Username Copied to Clipboard");
                                } else if(which == 2) {
                                    if(loginsList.get(position).getWebsite().equals("nUlL")) {
                                        Toast.makeText(getContext(), "No Website", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Utility.launchWebsite(getContext(), loginsList.get(position).getWebsite());
                                    }
                                } else if(which == 3) {
                                    if(loginsList.get(position).getComments().equals("nUlL")) {
                                        Toast.makeText(getContext(), "No Comments to Copy", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Utility.copyToClipboard(getContext(), Utility.CLIP_LABEL,
                                                loginsList.get(position).getComments(),
                                                "Comments Copied to Clipboard");
                                    }
                                } else if(which == 4) {
                                    Utility.copyToClipboard(getContext(), Utility.CLIP_LABEL,
                                            loginsList.get(position).getLabel(), "Label Copied to Clipboard");
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
                Intent intent = new Intent(getContext(), AddLoginsActivity.class);
                intent.putExtra(Utility.EDIT_MODE, position);
                getContext().startActivity(intent);
            }
        });
        editEntry.setTag(position);

        return row;
    }

    @Override
    public void remove(LoginEntry object) {
        loginsList.remove(object);
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

