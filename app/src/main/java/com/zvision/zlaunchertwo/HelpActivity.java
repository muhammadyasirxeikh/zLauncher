package com.zvision.zlaunchertwo;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zvision.zlaunchertwo.base.BaseActivity;
import com.zvision.zlaunchertwo.base.Constants;
import com.zvision.zlaunchertwo.part.HelpItemAdapter;
import com.squareup.picasso.Picasso;

public class HelpActivity extends BaseActivity {

    private RecyclerView recyclerViewApp = null;
    private boolean isRefresh = true;
    private ImageView ivBackgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        recyclerViewApp = findViewById(R.id.rv_help_item_recyclerview);
        ivBackgroundImage = findViewById(R.id.ivBackgroundImage);
    }

    public void init() {
        if (isRefresh) {
            isRefresh = false;
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    Picasso.get().load(Constants.HELP_BACKGROUND_IMAGE_URL).into(ivBackgroundImage);
                } else {
                    requestPermission(); // Code for permission
                }
            }
            recyclerViewApp.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewApp.setAdapter(new HelpItemAdapter(this));
        }
    }

    @Override
    public void handleKeys(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        /*if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
        }*/
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                    Picasso.get().load(Constants.HELP_BACKGROUND_IMAGE_URL).into(ivBackgroundImage);
                } else {
                    finish();
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
}