package com.zvision.zlaunchertwo.part;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zvision.zlaunchertwo.AllAppsActivity;
import com.zvision.zlaunchertwo.base.Constants;
import com.zvision.zlaunchertwo.Item;
import com.zvision.zlaunchertwo.R;
import com.zvision.zlaunchertwo.base.HelperUtils;
import com.zvision.zlaunchertwo.shared_storage.SharedPreferenceStorage;

import java.util.ArrayList;

public class AllAppsListAdapter extends RecyclerView.Adapter<AllAppsListAdapter.ViewHolder> {

    private AllAppsActivity mBase;
    private ArrayList<Item> appPackgeArrayList = null;
    private boolean __isAddRemoveAppPage = false;

    public AllAppsListAdapter(AllAppsActivity base) {
        this.mBase = base;
        this.appPackgeArrayList = base.appArrayList;
        this.__isAddRemoveAppPage = base.__isAddRemoveAppPage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.app_item_all_app_page, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            holder.tvAppName.setText(appPackgeArrayList.get(position).__appName);
            holder.appIcon.setImageDrawable(appPackgeArrayList.get(position).__appIcon);

            int appIconHeight = (Constants.DEVICE_SCREEN_HEIGHT / Constants.APP_APPS_COLUMN_SPAN) - HelperUtils.getHorizontalRatio(4);
            holder.appIcon.getLayoutParams().width = appIconHeight;
            holder.appIcon.getLayoutParams().height = appIconHeight;


            if (__isAddRemoveAppPage) {
                if (mBase.__customAppPackageName != null) {
                    if (appPackgeArrayList.get(position).__packgeName.toString().equalsIgnoreCase(mBase.__customAppPackageName)) {
                        holder.cbAppSelectCheckBox.setChecked(true);
                    }
                } else if (mBase.app.getHomePageDockAppList().contains(appPackgeArrayList.get(position).__packgeName.toString())) {
                    holder.cbAppSelectCheckBox.setChecked(true);
                }

                holder.cbAppSelectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        Item currentApp = appPackgeArrayList.get(position);
                        if (b) {
                            if (!mBase.app.getHomePageDockAppList().contains(currentApp.__packgeName.toString())) {
                                mBase.app.getHomePageDockAppList().add(currentApp.__packgeName.toString());
                            } else {
                                mBase.app.getHomePageDockAppList().remove(currentApp.__packgeName.toString());
                            }

                            if (mBase.__customAppPackageName != null) {
                                showChangeDefaultIconDialog(currentApp.__packgeName.toString());
                            }

                        } else {
                            if (mBase.app.getHomePageDockAppList().contains(currentApp.__packgeName.toString())) {
                                mBase.app.getHomePageDockAppList().remove(currentApp.__packgeName.toString());
                            } else {
                                mBase.app.getHomePageDockAppList().add(currentApp.__packgeName.toString());

                            }
                        }
                    }
                });
            } else {
                holder.llAppItemContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent launchIntent = mBase.getPackageManager().getLaunchIntentForPackage(appPackgeArrayList.get(position).__packgeName.toString());
                        mBase.startActivity(launchIntent);
                    }
                });

                holder.llAppItemContainer.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HelperUtils.goAppDetailsDeviceSettingsScreen(mBase, appPackgeArrayList.get(position).__packgeName.toString());
                        /*AlertDialog.Builder changeAppDialog = new AlertDialog.Builder(mBase);
                        changeAppDialog.setMessage("Show About")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        HelperUtils.goAppDetailsDeviceSettingsScreen(mBase, appPackgeArrayList.get(position).__packgeName.toString());
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        AlertDialog alertDialog = changeAppDialog.create();
                        alertDialog.setCancelable(true);
                        alertDialog.show();

                        Button positiveBtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        if (positiveBtn != null) {
                            positiveBtn.setBackgroundResource(R.drawable.border_for_focus);
                        }

                        Button negativeBtn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        if (positiveBtn != null) {
                            positiveBtn.setBackgroundResource(R.drawable.border_for_focus);
                        }*/
                        return true;
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return appPackgeArrayList.size();
    }

    AlertDialog.Builder changeAppDialog = null;

    private void showChangeDefaultIconDialog(final String currentPosPackageName) {
        try {
            changeAppDialog = new AlertDialog.Builder(mBase);
            changeAppDialog.setMessage("Change Default app?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferenceStorage.setStringValue(mBase, mBase.__currentDefaultAppPkg, currentPosPackageName);
                            dialog.cancel();
                            mBase.finish();
                            changeAppDialog = null;
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            changeAppDialog = null;
                        }
                    });

            AlertDialog alertDialog = changeAppDialog.create();
            alertDialog.show();

            Button negativeBtn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (negativeBtn != null) {
                negativeBtn.setBackgroundResource(R.drawable.border_for_focus);
            }

            Button positiveBtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if (positiveBtn != null) {
                positiveBtn.setBackgroundResource(R.drawable.border_for_focus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView appIcon = null;
        public TextView tvAppName = null;
        public LinearLayout llAppItemContainer;
        public RelativeLayout rlALlContainer;
        public CheckBox cbAppSelectCheckBox;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            rlALlContainer = itemView.findViewById(R.id.rlAllContainer);
            llAppItemContainer = itemView.findViewById(R.id.llAppItemContainer);
            appIcon = itemView.findViewById(R.id.ic_app_icon1);
            tvAppName = itemView.findViewById(R.id.tv_app_name1);
            cbAppSelectCheckBox = itemView.findViewById(R.id.cbAppSelected);

            if (__isAddRemoveAppPage) {
                cbAppSelectCheckBox.setVisibility(View.VISIBLE);
                cbAppSelectCheckBox.setFocusable(true);
                llAppItemContainer.setFocusable(false);
            } else {
                cbAppSelectCheckBox.setVisibility(View.GONE);
                cbAppSelectCheckBox.setFocusable(false);
                llAppItemContainer.setFocusable(true);
            }

            llAppItemContainer.getLayoutParams().height = (Constants.DEVICE_SCREEN_WIDTH / 6) - HelperUtils.getHorizontalRatio(10);


            llAppItemContainer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        Animation anim = AnimationUtils.loadAnimation(mBase, R.anim.scale_in);
                        appIcon.startAnimation(anim);
                        anim.setFillAfter(true);
                        tvAppName.setTextColor(Color.WHITE);
                    } else {
                        Animation anim = AnimationUtils.loadAnimation(mBase, R.anim.scale_out);
                        appIcon.startAnimation(anim);
                        anim.setFillAfter(true);
                        tvAppName.setTextColor(Color.BLACK);
                    }
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
