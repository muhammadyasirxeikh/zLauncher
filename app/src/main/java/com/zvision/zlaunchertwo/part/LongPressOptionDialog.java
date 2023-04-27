package com.zvision.zlaunchertwo.part;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zvision.zlaunchertwo.R;
import com.zvision.zlaunchertwo.base.BaseActivity;
import com.zvision.zlaunchertwo.base.Constants;
import com.zvision.zlaunchertwo.shared_storage.SharedPreferenceStorage;


public class LongPressOptionDialog extends Dialog implements View.OnClickListener, View.OnFocusChangeListener {

    private BaseActivity mBase;
    private TextView tvUpdateBtn, tvReassignBtn, tvDeleteAppBtn, tvDeleteAppDataBtn, tvDeleteAppCacheBtn, tvUnlockAppBtn, tvLockAppBtn, tvHideUnHideAppBtn, tvAppInfoBtn, tvHelpInfoBtn;


    public LongPressOptionDialog(@NonNull BaseActivity context) {
        super(context);
        try {
            mBase = context;
            setCancelable(true);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.long_press_dialog);
            getWindow().setLayout(Constants.DEVICE_SCREEN_WIDTH / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
            doOnCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doOnCreate() {
        try {
            tvUpdateBtn = findViewById(R.id.tvUpdateBtn);
            tvUpdateBtn.setOnClickListener(this);
            tvUpdateBtn.setOnFocusChangeListener(this);

            tvReassignBtn = findViewById(R.id.tvReassignBtn);
            tvReassignBtn.setOnClickListener(this);
            tvReassignBtn.setOnFocusChangeListener(this);

            tvDeleteAppBtn = findViewById(R.id.tvDeleteAppBtn);
            tvDeleteAppBtn.setOnClickListener(this);
            tvDeleteAppBtn.setOnFocusChangeListener(this);

            tvDeleteAppDataBtn = findViewById(R.id.tvDeleteAppDataBtn);
            tvDeleteAppDataBtn.setOnClickListener(this);
            tvDeleteAppDataBtn.setOnFocusChangeListener(this);

            tvDeleteAppCacheBtn = findViewById(R.id.tvDeleteAppCacheBtn);
            tvDeleteAppCacheBtn.setOnClickListener(this);
            tvDeleteAppCacheBtn.setOnFocusChangeListener(this);

            tvUnlockAppBtn = findViewById(R.id.tvUnlockAppBtn);
            tvUnlockAppBtn.setOnClickListener(this);
            tvUnlockAppBtn.setOnFocusChangeListener(this);

            tvLockAppBtn = findViewById(R.id.tvLockAppBtn);
            tvLockAppBtn.setOnClickListener(this);
            tvLockAppBtn.setOnFocusChangeListener(this);

            tvHideUnHideAppBtn = findViewById(R.id.tvHideUnHideAppBtn);
            tvHideUnHideAppBtn.setOnClickListener(this);
            tvHideUnHideAppBtn.setOnFocusChangeListener(this);

            tvAppInfoBtn = findViewById(R.id.tvAppInfoBtn);
            tvAppInfoBtn.setOnClickListener(this);
            tvAppInfoBtn.setOnFocusChangeListener(this);

            tvHelpInfoBtn = findViewById(R.id.tvHelpInfoBtn);
            tvHelpInfoBtn.setOnClickListener(this);
            tvHelpInfoBtn.setOnFocusChangeListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.tvUpdateBtn:
                if (b) {
                    tvUpdateBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvUpdateBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvUpdateBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvUpdateBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
            case R.id.tvReassignBtn:
                if (b) {
                    tvReassignBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvReassignBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvReassignBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvReassignBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
            case R.id.tvDeleteAppBtn:
                if (b) {
                    tvDeleteAppBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvDeleteAppBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvDeleteAppBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvDeleteAppBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
            case R.id.tvDeleteAppDataBtn:
                if (b) {
                    tvDeleteAppDataBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvDeleteAppDataBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvDeleteAppDataBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvDeleteAppDataBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
            case R.id.tvDeleteAppCacheBtn:
                if (b) {
                    tvDeleteAppCacheBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvDeleteAppCacheBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvDeleteAppCacheBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvDeleteAppCacheBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
            case R.id.tvUnlockAppBtn:
                if (b) {
                    tvUnlockAppBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvUnlockAppBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvUnlockAppBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvUnlockAppBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
            case R.id.tvLockAppBtn:
                if (b) {
                    tvLockAppBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvLockAppBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvLockAppBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvLockAppBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
            case R.id.tvHideUnHideAppBtn:
                if (b) {
                    tvHideUnHideAppBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvHideUnHideAppBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvHideUnHideAppBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvHideUnHideAppBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
            case R.id.tvHelpInfoBtn:
                if (b) {
                    tvHelpInfoBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvHelpInfoBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvHelpInfoBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvHelpInfoBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
            case R.id.tvAppInfoBtn:
                if (b) {
                    tvAppInfoBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvAppInfoBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvAppInfoBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvAppInfoBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
        }
    }

    public interface OnItemClickListener {
        void onClicked(int item);
    }

    private OnItemClickListener mOnItemClickListener;

    public void showDialog(String btnId, String helpInfoUrl, OnItemClickListener mOnItemClickListener) {
        setUpHideBtnText(btnId);
        this.mOnItemClickListener = mOnItemClickListener;
        if (!mBase.isFinishing())
            show();
    }

    private void setUpHideBtnText(final String btnId) {
        try {
            mBase.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (btnId != null) {
                            tvHideUnHideAppBtn.setVisibility(View.VISIBLE);
                            boolean isHidden = SharedPreferenceStorage.getBooleanValue(mBase, btnId + Constants.Storage._isAppHidden.name(), false);
                            tvHideUnHideAppBtn.setText(isHidden ? "SHOW APP" : "HIDE APP");
                        } else {
                            tvHideUnHideAppBtn.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        dismiss();
        if (mOnItemClickListener != null) {
            switch (view.getId()) {
                case R.id.tvUpdateBtn:
                    mOnItemClickListener.onClicked(0);
                    break;
                case R.id.tvReassignBtn:
                    mOnItemClickListener.onClicked(1);
                    break;
                case R.id.tvDeleteAppBtn:
                    mOnItemClickListener.onClicked(2);
                    break;
                case R.id.tvDeleteAppDataBtn:
                    mOnItemClickListener.onClicked(3);
                    break;
                case R.id.tvDeleteAppCacheBtn:
                    mOnItemClickListener.onClicked(7);
                    break;
                case R.id.tvUnlockAppBtn:
                    mOnItemClickListener.onClicked(4);
                    break;
                case R.id.tvLockAppBtn:
                    mOnItemClickListener.onClicked(5);
                    break;
                case R.id.tvHideUnHideAppBtn:
                    mOnItemClickListener.onClicked(6);
                    break;
                case R.id.tvHelpInfoBtn:
                    mOnItemClickListener.onClicked(8);
                    break;
                case R.id.tvAppInfoBtn:
                    mOnItemClickListener.onClicked(9);
                    break;
            }
        }

    }
}
