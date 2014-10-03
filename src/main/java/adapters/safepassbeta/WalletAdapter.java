package adapters.safepassbeta;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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

import activities.safepassbeta.AddWalletActivity;
import activities.safepassbeta.R;
import objects.safepassbeta.WalletEntry;
import utilities.safepassbeta.Utility;

// Custom wallet adapter for listview
public class WalletAdapter extends ArrayAdapter<WalletEntry> {

    // Local variables
    private Context context;
    private LayoutInflater inflater;
    private List<WalletEntry> walletList;
    private SparseBooleanArray mSelectedItemsIds;

    public WalletAdapter(Context context, int resource, List<WalletEntry> walletList) {
        super(context, resource, walletList);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
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
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("SafePass:CreditCard", walletList.get(position).getNumber());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, "Credit Card Number copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        copyText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Copy to clipboard:")
                        .setItems(R.array.WalletListItems, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                // The 'which' argument contains the index position
                                // of the selected item
                                if(which == 0) {
                                    ClipData clipData = ClipData.newPlainText("SafePass:CardNumber", walletList.get(position).getNumber());
                                    clipboardManager.setPrimaryClip(clipData);
                                    Toast.makeText(context, "Card Number copied to clipboard", Toast.LENGTH_SHORT).show();
                                } else if(which == 1) {
                                    ClipData clipData = ClipData.newPlainText("SafePass:CardExpiration", walletList.get(position).getExpiration());
                                    clipboardManager.setPrimaryClip(clipData);
                                    Toast.makeText(context, "Card Expiration copied to clipboard", Toast.LENGTH_SHORT).show();
                                } else if(which == 2) {
                                        ClipData clipData = ClipData.newPlainText("SafePass:CardSecurity", walletList.get(position).getSecurityCode());
                                        clipboardManager.setPrimaryClip(clipData);
                                        Toast.makeText(context, "Security Code copied to clipboard", Toast.LENGTH_SHORT).show();
                                } else if(which == 3) {
                                        ClipData clipData = ClipData.newPlainText("SafePass:CardName", walletList.get(position).getNameOnCard());
                                        clipboardManager.setPrimaryClip(clipData);
                                        Toast.makeText(context, "Name on Card copied to clipboard", Toast.LENGTH_SHORT).show();
                                } else if(which == 4) {
                                    ClipData clipData = ClipData.newPlainText("SafePass:CardType", walletList.get(position).getCardType());
                                    clipboardManager.setPrimaryClip(clipData);
                                    Toast.makeText(context, "Card Type copied to clipboard", Toast.LENGTH_SHORT).show();
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

    public List<WalletEntry> getWalletEntry() {
        return walletList;
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

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    private String getLastFour(String str) {
        return str.length() > 4 ? str.substring(str.length() - 4) : str;
    }

}

