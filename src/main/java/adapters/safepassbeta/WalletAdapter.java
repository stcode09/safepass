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

import activities.safepassbeta.AddWalletActivity;
import activities.safepassbeta.R;
import objects.safepassbeta.WalletEntry;
import utilities.safepassbeta.Utility;

// Custom wallet adapter for listview
public class WalletAdapter extends ArrayAdapter<WalletEntry> {

    // Local variables
    private LayoutInflater inflater;
    private List<WalletEntry> walletList;
    private SparseBooleanArray mSelectedItemsIds;

    public WalletAdapter(Context context, int resource, List<WalletEntry> walletList) {
        super(context, resource, walletList);
        mSelectedItemsIds = new SparseBooleanArray();
        this.walletList = walletList;
        inflater = LayoutInflater.from(context);
    }

    // View holder
    private class ViewHolder {
        TextView label;
        TextView card_type;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View row;
        if(convertView == null) {
            row = inflater.inflate(R.layout.fragment_wallet_adapter, null);
            holder.label = (TextView) row.findViewById(R.id.walletLabel);
            holder.card_type = (TextView) row.findViewById(R.id.walletCardType);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            row = convertView;
        }
        holder.label.setText(walletList.get(position).getLabel());
        holder.card_type.setText(walletList.get(position).getCardType() + " - x" +getLastFour(walletList.get(position).getNumber()));

        ImageButton copyText = (ImageButton) row.findViewById(R.id.copyText);
        copyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.copyToClipboard(getContext(), Utility.CLIP_LABEL,
                        walletList.get(position).getNumber(), "Card Number Copied to Clipboard");
            }
        });
        copyText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Copy to Clipboard:")
                        .setItems(R.array.WalletListItems, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                if(which == 0) {
                                    Utility.copyToClipboard(getContext(), Utility.CLIP_LABEL,
                                            walletList.get(position).getNumber(), "Card Number Copied to Clipboard");
                                } else if(which == 1) {
                                    Utility.copyToClipboard(getContext(), Utility.CLIP_LABEL,
                                            walletList.get(position).getExpiration(), "Expiration Copied to Clipboard");
                                } else if(which == 2) {
                                    Utility.copyToClipboard(getContext(), Utility.CLIP_LABEL,
                                            walletList.get(position).getSecurityCode(), "Security Code Copied to Clipboard");
                                } else if(which == 3) {
                                    Utility.copyToClipboard(getContext(), Utility.CLIP_LABEL,
                                            walletList.get(position).getNameOnCard(), "Name Copied to Clipboard");
                                } else if(which == 4) {
                                    Utility.copyToClipboard(getContext(), Utility.CLIP_LABEL,
                                            walletList.get(position).getCardType(), "Card Type Copied to Clipboard");
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
                Intent intent = new Intent(getContext(), AddWalletActivity.class);
                intent.putExtra(Utility.EDIT_MODE, position);
                getContext().startActivity(intent);
            }
        });
        editEntry.setTag(position);
        return row;
    }

    @Override
    public void remove(WalletEntry object) {
        walletList.remove(object);
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

    private String getLastFour(String str) {
        return str.length() > 4 ? str.substring(str.length() - 4) : str;
    }

}

