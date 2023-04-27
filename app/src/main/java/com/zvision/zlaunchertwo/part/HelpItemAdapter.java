package com.zvision.zlaunchertwo.part;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zvision.zlaunchertwo.R;
import com.zvision.zlaunchertwo.base.BaseActivity;
import com.zvision.zlaunchertwo.base.Constants;
import com.zvision.zlaunchertwo.base.HelperUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HelpItemAdapter extends RecyclerView.Adapter<HelpItemAdapter.ViewHolder> {

    private ArrayList<HelpInfoOrAdsButtonInfo> mHelpInfoOrAdsButtonInfoArrayList = new ArrayList<>();
    private BaseActivity mBase;
    public final AlertDialog[] alertDialog = {null};
    public HelpItemAdapter(BaseActivity mBase) {
        this.mHelpInfoOrAdsButtonInfoArrayList = mBase.app.getHelpInfoInfoArrayList();
        this.mBase = mBase;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.help_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        try {
            final HelpInfoOrAdsButtonInfo mHelpInfoOrAdsButtonInfo = mHelpInfoOrAdsButtonInfoArrayList.get(position);
            if (mHelpInfoOrAdsButtonInfo != null) {
                holder.tvTitle.setText(mHelpInfoOrAdsButtonInfo.getTitle());
                if (mHelpInfoOrAdsButtonInfo.getDescription() != null && mHelpInfoOrAdsButtonInfo.getDescription().length() > 0) {
                    holder.tvDescription.setText(mHelpInfoOrAdsButtonInfo.getDescription());
                    holder.tvDescription.setVisibility(View.VISIBLE);
                } else {
                    holder.tvDescription.setVisibility(View.GONE);
                }
                View.OnFocusChangeListener onFocusChangeListener;
                if (mHelpInfoOrAdsButtonInfo.getBtnId().equalsIgnoreCase("H")) {
                    holder.itemView.setBackgroundColor(Color.BLACK);
                    holder.tvDescription.setTextColor(Color.WHITE);
                    holder.tvTitle.setTextColor(Color.WHITE);
                    holder.ivIcon.setVisibility(View.GONE);
                    onFocusChangeListener = new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean b) {
                            if (b) {
                                holder.itemView.setBackground(mBase.getResources().getDrawable(R.drawable.grey_selector));
                            } else {
                                holder.itemView.setBackgroundColor(Color.BLACK);
                            }
                        }
                    };
                } else {
                    Bitmap bitmap = getIcon(mHelpInfoOrAdsButtonInfo.getBtnId());
                    if (bitmap != null) {
                        holder.ivIcon.setImageDrawable(new BitmapDrawable(mBase.getResources(), bitmap));
                    } else {
                        holder.ivIcon.setVisibility(View.GONE);
                    }

                    onFocusChangeListener = new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean b) {
                            if (b) {
                                holder.tvTitle.setTextColor(mBase.getResources().getColor(R.color.white));
                                holder.tvDescription.setTextColor(mBase.getResources().getColor(R.color.white));
                            } else {
                                holder.tvTitle.setTextColor(mBase.getResources().getColor(R.color.black));
                                holder.tvDescription.setTextColor(mBase.getResources().getColor(R.color.black));
                            }
                        }
                    };
                }
                holder.itemView.setOnFocusChangeListener(onFocusChangeListener);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mHelpInfoOrAdsButtonInfo.getTutorialType().equals(Constants.FILE_TYPE.png)) {
//                            new DownloadNewUpdate(mBase, mHelpInfoOrAdsButtonInfo.getBtnId().concat(Constants.UpdatableAppNameKey._tutorial.name()), Constants.FILE_TYPE.png, null).openImage(mHelpInfoOrAdsButtonInfo.getHelpFileUrl());
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mBase);
                            LayoutInflater inflater = mBase.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.image_viewer, null);
                            dialogBuilder.setView(dialogView);
                            final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
                            Picasso.get().load(mHelpInfoOrAdsButtonInfo.getHelpFileUrl()).into(imageViewer);
//                            imageViewer.setOnKeyListener(new View.OnKeyListener() {
//                                @Override
//                                public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                                    alertDialog[0].dismiss();
//                                    return true;
//                                }
//                            });
                            alertDialog[0] = dialogBuilder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(alertDialog[0].getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                            alertDialog[0].show();
                            alertDialog[0].getWindow().setAttributes(lp);
                            alertDialog[0].setCancelable(true);
                            alertDialog[0].show();

                            imageViewer.requestFocus();
                        } else if (mHelpInfoOrAdsButtonInfo.getTutorialType().equals(Constants.FILE_TYPE.mov) || mHelpInfoOrAdsButtonInfo.getTutorialType().equals(Constants.FILE_TYPE.mp4)) {

                            HelperUtils.doPlayVideo(mBase, mHelpInfoOrAdsButtonInfo);
                        } else {
                            new DownloadNewUpdate(mBase, mHelpInfoOrAdsButtonInfo.getBtnId().concat(Constants.UpdatableAppNameKey._tutorial.name()),0,0, mHelpInfoOrAdsButtonInfo.getTutorialType(), null).execute(mHelpInfoOrAdsButtonInfo.getHelpFileUrl());
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getIcon(String btnId) {
        Bitmap mBitmap = null;
        try {
            switch (btnId) {
                case "0":
                    mBitmap = Constants.icon_button_top;
                    break;
                    case "23":
                    mBitmap = Constants.icon_button_top_new;
                    break;
                case "1":
                    mBitmap = Constants.icon_button1;
                    break;
                case "2":
                    mBitmap = Constants.icon_button2;
                    break;
                case "3":
                    mBitmap = Constants.icon_button3;
                    break;
                case "4":
                    mBitmap = Constants.icon_button4;
                    break;
                case "5":
                    mBitmap = Constants.icon_button5;
                    break;
                case "6":
                    mBitmap = Constants.icon_button6;
                    break;
                case "7":
                    mBitmap = Constants.icon_button7;
                    break;
                case "8":
                    mBitmap = Constants.icon_button8;
                    break;
                case "9":
                    mBitmap = Constants.icon_button9;
                    break;
                case "10":
                    mBitmap = Constants.icon_button10;
                    break;
                case "11":
                    mBitmap = Constants.icon_button11;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBitmap;
    }

    @Override
    public int getItemCount() {
        return mHelpInfoOrAdsButtonInfoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvDescription;
        private ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            //llAppItemContainer.getLayoutParams().height = (Constants.DEVICE_SCREEN_WIDTH / 8) - HelperUtils.getHorizontalRatio(10);
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, HelperUtils.getTextRatio(7));
            tvDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, HelperUtils.getTextRatio(6));
            tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }
}
