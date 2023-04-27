package com.zvision.zlaunchertwo;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zvision.zlaunchertwo.R;
import com.zvision.zlaunchertwo.base.BaseActivity;
import com.zvision.zlaunchertwo.base.HelperUtils;

import java.util.ArrayList;

public class DockAppListAdapter extends RecyclerView.Adapter<DockAppListAdapter.ViewHolder> {
    BaseActivity mContext;
    private ArrayList<Item> __appItemArrayList = new ArrayList<>();

    public DockAppListAdapter(BaseActivity ctx) {
        mContext = ctx;
        setItemDataStoreFromPackgeName();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
            holder.tvAppNameTop.setText(__appItemArrayList.get(position).__appName);
            holder.appIconTop.setImageDrawable(__appItemArrayList.get(position).__appIcon);
            View.OnClickListener listenerTop = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(__appItemArrayList.get(position).__packgeName.toString());
                    mContext.startActivity(launchIntent);
                }
            };
            holder.ll_item_container.setOnClickListener(listenerTop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return __appItemArrayList.size();
    }

    private void setItemDataStoreFromPackgeName() {
        try {
            Drawable appIcon = null;
            __appItemArrayList.clear();
            for (String appPackage : mContext.app.getHomePageDockAppList()) {
                if (appPackage != null && !appPackage.equalsIgnoreCase("")) {
                    appIcon = mContext.getPackageManager().getApplicationIcon(appPackage);
                    if (appIcon == null)
                        appIcon = mContext.getPackageManager().getApplicationBanner(appPackage);
                    ApplicationInfo info = mContext.getPackageManager().getApplicationInfo(appPackage, PackageManager.GET_META_DATA);
                    String appName = (String) mContext.getPackageManager().getApplicationLabel(info);
                    __appItemArrayList.add(new Item(appPackage, appName, appIcon));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView appIconTop = null;
        public TextView tvAppNameTop = null;
        public LinearLayout ll_item_container;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ll_item_container = itemView.findViewById(R.id.ll_item_container);
            appIconTop = itemView.findViewById(R.id.ic_app_icon);
            tvAppNameTop = itemView.findViewById(R.id.tv_app_name);

            appIconTop.getLayoutParams().height = HelperUtils.getHorizontalRatio(20);
            appIconTop.getLayoutParams().width = HelperUtils.getHorizontalRatio(20);

            ll_item_container.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_in);
                        appIconTop.startAnimation(anim);
                        anim.setFillAfter(true);
                        tvAppNameTop.setTextColor(Color.WHITE);
                    } else {
                        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_out);
                        appIconTop.startAnimation(anim);
                        anim.setFillAfter(true);
                        tvAppNameTop.setTextColor(Color.BLACK);
                    }
                }
            });
        }
    }
}
