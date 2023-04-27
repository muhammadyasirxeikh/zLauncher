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

public class LongPressTopOptionsDilaog extends Dialog implements View.OnClickListener, View.OnFocusChangeListener {

    private BaseActivity mBase;
    private TextView tvUpdateBtn, tvcleanBtn,tvHideUnHideAppBtn;


    public LongPressTopOptionsDilaog(@NonNull BaseActivity context) {
        super(context);
        try {
            mBase = context;
            setCancelable(true);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.longpress2dialog);
            getWindow().setLayout(Constants.DEVICE_SCREEN_WIDTH / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
            doOnCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doOnCreate() {
        try {
            tvUpdateBtn = findViewById(R.id.updateNewVersionBtn);
            tvUpdateBtn.setOnClickListener(this);
            tvUpdateBtn.setOnFocusChangeListener(this);

            tvcleanBtn = findViewById(R.id.cleanAppDataBtn);
            tvcleanBtn.setOnClickListener(this);
            tvcleanBtn.setOnFocusChangeListener(this);

            tvHideUnHideAppBtn = findViewById(R.id.tvHideUnHideAppBtn);
            tvHideUnHideAppBtn.setOnClickListener(this);
            tvHideUnHideAppBtn.setOnFocusChangeListener(this);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.updateNewVersionBtn:
                if (b) {
                    tvUpdateBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvUpdateBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvUpdateBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvUpdateBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;
            case R.id.cleanAppDataBtn:
                if (b) {
                    tvcleanBtn.setTextColor(mBase.getResources().getColor(R.color.white));
                    tvcleanBtn.setBackgroundColor(mBase.getResources().getColor(R.color.grey));
                } else {
                    tvcleanBtn.setTextColor(mBase.getResources().getColor(R.color.black));
                    tvcleanBtn.setBackground(mBase.getResources().getDrawable(R.drawable.list_divider));
                }
                break;

        }
    }

    public interface OnItemClickListener {
        void onClicked(int item);
    }

    private LongPressTopOptionsDilaog.OnItemClickListener mOnItemClickListener;

    public void showDialog(String btnId, LongPressTopOptionsDilaog.OnItemClickListener mOnItemClickListener) {
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
                case R.id.updateNewVersionBtn:
                    mOnItemClickListener.onClicked(0);
                    break;
                case R.id.cleanAppDataBtn:
                    mOnItemClickListener.onClicked(1);
                    break;

            }
        }

    }
}

