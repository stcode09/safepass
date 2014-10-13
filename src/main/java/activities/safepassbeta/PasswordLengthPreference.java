package activities.safepassbeta;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public final class PasswordLengthPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {

    private static final int DEFAULT_CURRENT_VALUE = 10;
    private static final int DEFAULT_MIN_VALUE = 4;
    private static final int DEFAULT_MAX_VALUE = 32;
    private final int mDefaultValue;
    private final int mMaxValue;
    private final int mMinValue;
    private int mCurrentValue;

    private SeekBar mSeekBar;
    private TextView mValueText;

    public PasswordLengthPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMinValue = DEFAULT_MIN_VALUE;
        mMaxValue = DEFAULT_MAX_VALUE;
        mDefaultValue = DEFAULT_CURRENT_VALUE;
    }

    @Override
    protected View onCreateDialogView() {
        mCurrentValue = getPersistedInt(mDefaultValue);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_slider, null);

        ((TextView) view.findViewById(R.id.min_value)).setText(Integer.toString(mMinValue));
        ((TextView) view.findViewById(R.id.max_value)).setText(Integer.toString(mMaxValue));

        mSeekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        mSeekBar.setMax(mMaxValue - mMinValue);
        mSeekBar.setProgress(mCurrentValue - mMinValue);
        mSeekBar.setOnSeekBarChangeListener(this);

        mValueText = (TextView) view.findViewById(R.id.current_value);
        mValueText.setText(Integer.toString(mCurrentValue));

        return view;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (!positiveResult) {
            return;
        }
        if (shouldPersist()) {
            persistInt(mCurrentValue);
        }
        notifyChanged();
    }

    @Override
    public CharSequence getSummary() {
        String summary = super.getSummary().toString();
        int value = getPersistedInt(mDefaultValue);
        return String.format(summary, value);
    }

    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
        mCurrentValue = value + mMinValue;
        mValueText.setText(Integer.toString(mCurrentValue));
    }

    public void onStartTrackingTouch(SeekBar seek) {
        // Not used
    }

    public void onStopTrackingTouch(SeekBar seek) {
        // Not used
    }
}