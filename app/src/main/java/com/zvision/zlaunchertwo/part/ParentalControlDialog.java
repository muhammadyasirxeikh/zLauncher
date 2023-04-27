package com.zvision.zlaunchertwo.part;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zvision.zlaunchertwo.R;
import com.zvision.zlaunchertwo.base.BaseActivity;
import com.zvision.zlaunchertwo.base.Constants;
import com.zvision.zlaunchertwo.shared_storage.SharedPreferenceStorage;

public class ParentalControlDialog extends Dialog implements View.OnClickListener, View.OnFocusChangeListener {
    private BaseActivity mBase;
    private TextView tvActivateDeActivateBtn, tvLockUnlockBtn, tvHideAllBtn, tvUnHideAllBtn;

    public ParentalControlDialog (BaseActivity context) {
        super(context);
        try {
            mBase = context;
            setCancelable(true);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.parental_control_dialog);
            getWindow().setLayout(Constants.DEVICE_SCREEN_WIDTH / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
            doOnCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doOnCreate() {
        try {
            tvActivateDeActivateBtn = findViewById(R.id.tvActivateDeActivateBtn);
            tvActivateDeActivateBtn.setOnClickListener(this);
            tvActivateDeActivateBtn.setOnFocusChangeListener(this);
            tvActivateDeActivateBtn.setText(SharedPreferenceStorage.getBooleanValue(mBase, Constants.Storage.APPS_LOCK_ENABLED.name(), false) ? "Deactivate apps lock" : "Activate apps lock");

            tvLockUnlockBtn = findViewById(R.id.tvLockUnlockBtn);
            tvLockUnlockBtn.setOnClickListener(this);
            tvLockUnlockBtn.setOnFocusChangeListener(this);
            tvLockUnlockBtn.setText(SharedPreferenceStorage.getStringValue(mBase, Constants.Storage.APP_LOCK_PASSWORD.name(), Constants.APP_LOCK_BLANK_PASSWORD).equalsIgnoreCase(Constants.APP_LOCK_BLANK_PASSWORD) ? "Create app lock/hide password" : "Change app lock/hide password");

            tvHideAllBtn = findViewById(R.id.tvHideAllBtn);
            tvHideAllBtn.setOnClickListener(this);
            tvHideAllBtn.setOnFocusChangeListener(this);

            tvUnHideAllBtn = findViewById(R.id.tvUnHideAllBtn);
            tvUnHideAllBtn.setOnClickListener(this);
            tvUnHideAllBtn.setOnFocusChangeListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.tvActivateDeActivateBtn:
                if (b) {
                    tvActivateDeActivateBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvActivateDeActivateBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvActivateDeActivateBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvActivateDeActivateBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
            case R.id.tvLockUnlockBtn:
                if (b) {
                    tvLockUnlockBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvLockUnlockBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvLockUnlockBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvLockUnlockBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
            case R.id.tvHideAllBtn:
                if (b) {
                    tvHideAllBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvHideAllBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvHideAllBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvHideAllBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
            case R.id.tvUnHideAllBtn:
                if (b) {
                    tvUnHideAllBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvUnHideAllBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvUnHideAllBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvUnHideAllBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
        }
    }

    public interface OnItemClickListener {
        void onClicked(int item);
    }

    private LongPressOptionDialog.OnItemClickListener mOnItemClickListener;

    public void showDialog(LongPressOptionDialog.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
        if (!mBase.isFinishing())
            show();
    }

    @Override
    public void onClick(View view) {
        dismiss();
        if (mOnItemClickListener != null) {
            switch (view.getId()) {
                case R.id.tvActivateDeActivateBtn:
                    mOnItemClickListener.onClicked(0);
                    break;
                case R.id.tvLockUnlockBtn:
                    mOnItemClickListener.onClicked(1);
                    break;
                case R.id.tvHideAllBtn:
                    mOnItemClickListener.onClicked(2);
                    break;
                case R.id.tvUnHideAllBtn:
                    mOnItemClickListener.onClicked(3);
                    break;
            }
        }

    }

}
