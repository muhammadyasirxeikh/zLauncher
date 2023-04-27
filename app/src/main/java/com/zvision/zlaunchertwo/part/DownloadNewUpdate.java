package com.zvision.zlaunchertwo.part;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.zvision.zlaunchertwo.HomePageActivity;
import com.zvision.zlaunchertwo.R;
import com.zvision.zlaunchertwo.base.BaseActivity;
import com.zvision.zlaunchertwo.base.Constants;
import com.zvision.zlaunchertwo.root_utils.APKManager;
import com.zvision.zlaunchertwo.root_utils.RootTask;
import com.zvision.zlaunchertwo.shared_storage.SharedPreferenceStorage;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class DownloadNewUpdate extends AsyncTask<String, String, String> {

    private static final String TAG = "DownloadNewUpdate";
    private BaseActivity mBase;

    HomePageActivity homePageActivity;
    private String __appName = "";
    private String __packageNumber = "";
    private String __totalUninstalledPkg = "";
    private Constants.FILE_TYPE __fileType = Constants.FILE_TYPE.apk;
    private boolean isInstantUpdateInstall = false;
    private int instantInstallNo = -1;
    public interface OnDownloadStatus {
        void OnComplete();
    }
    public OnDownloadStatus mOnDownloadStatus = null;
    private DialogInterface.OnClickListener listener;

    public DownloadNewUpdate(BaseActivity baseActivity, String appName,int pkgnumber,int totalUninstalledpkg, final Constants.FILE_TYPE fileType, OnDownloadStatus onDownloadStatus) {
        this.isInstantUpdateInstall = false;
        this.mBase = baseActivity;
        this.homePageActivity = (HomePageActivity) baseActivity;
        this.__fileType = fileType;
        this.mOnDownloadStatus = onDownloadStatus;
        this.__appName = appName + "." + fileType.name();
        if (pkgnumber != 0) {
            this.__packageNumber = String.valueOf(pkgnumber+" of ");
        }else {
            this.__packageNumber = "";
        }
        if (totalUninstalledpkg != 0) {
            this.__totalUninstalledPkg = String.valueOf(totalUninstalledpkg);
        }else {
            this.__totalUninstalledPkg = "";
        }
        this.instantInstallNo = -1;
        listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownloadNewUpdate.this.cancel(true);
                mBase.removeLoading();
            }
        };
        mBase.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (fileType.equals(Constants.FILE_TYPE.apk)) {
                        mBase.showLoadingBtn("Fetching Update...\nIf asked to Wait during install - Choose Wait", listener);
                    } else {
                        mBase.showLoading("Downloading...\nInstalling Apps  " +__packageNumber  + __totalUninstalledPkg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public DownloadNewUpdate(BaseActivity baseActivity, int itemNo) {
        isInstantUpdateInstall = true;
        this.mBase = baseActivity;
        this.instantInstallNo = itemNo;
        this.__fileType = Constants.FILE_TYPE.apk;
        this.__appName = Constants.INSTANT_UPDATE_APP_NAME + "." + Constants.FILE_TYPE.apk.name();
        mBase.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mBase.showLoadingBtn("Fetching Update...\nIf asked to Wait during install - Choose Wait", listener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            long lenghtOfFile = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                lenghtOfFile = conection.getContentLengthLong();
            } else {
                lenghtOfFile = conection.getContentLength();
            }
            if (lenghtOfFile == -1 && f_url[0].contains("dropbox")) {
                try {
                    String strLenghtOfFile = conection.getHeaderField("X-Dropbox-Content-Length");
                    lenghtOfFile = Long.parseLong(strLenghtOfFile);
                } catch (Exception e) {

                }
            }
            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            String PATH = Environment.getExternalStorageDirectory() + "/download/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, __appName);
            OutputStream output = new FileOutputStream(outputFile);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                if (lenghtOfFile != -1) {
                    publishProgress("If asked to Wait during install - Choose Wait... \nDownloading..." + (int) ((total * 100) / lenghtOfFile) + "% \nInstalling Apps..."+__packageNumber  + __totalUninstalledPkg);
                } else {
                    publishProgress("Downloading...\nIf asked to Wait during install - Choose Wait...");
                }


                // writing data to file
                output.write(data, 0, count);
            }
            Log.i(TAG, "doInBackground: updating"+__appName);
            if (Objects.equals(__appName, "zLauncher.apk")){
                Log.i(TAG, "doInBackground: updating");
                homePageActivity.clearApplicationData();
            }

            publishProgress("Installing...");


            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(final String... progress) {
        mBase.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    // setting progress percentage
                    //((HomePageActivity) mBase).tvDownloadProgress.setVisibility(View.VISIBLE);
                    //String text = progress[0] + "% Downloading...";
                    //System.out.println("progress = " + progress[0]);
                    //((HomePageActivity) mBase).tvDownloadProgress.setText(text);
                    mBase.showLoadingBtn(progress[0], listener);


                } catch (Exception e) {
                    e.printStackTrace();
                    mBase.removeLoading();
                }
            }
        });
    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String file_url) {
        mBase.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (__fileType.equals(Constants.FILE_TYPE.apk) && APKManager.Install(Environment.getExternalStorageDirectory() + "/download/" + __appName)) {
                        Toast.makeText(mBase, "UPDATE DONE!", Toast.LENGTH_LONG).show();
                       homePageActivity.refreshapps();
                        if (mOnDownloadStatus != null)
                            mOnDownloadStatus.OnComplete();
                        mBase.removeLoading();
                        ImageManager.deleteFileFromStorage(__appName);
                        if (instantInstallNo != -1 && mBase.app.getNewInstantAppUpdateLinkList() != null && mBase.app.getNewInstantAppUpdateLinkList().size() > instantInstallNo+1) {
                            new DownloadNewUpdate(mBase, instantInstallNo + 1).execute(mBase.app.getNewInstantAppUpdateLinkList().get(instantInstallNo + 1));
                        } else if (mBase.app.getNewInstantAppUpdateLinkList() != null){
                            mBase.app.getNewInstantAppUpdateLinkList().clear();
                        }
                    }else {
                        install(Environment.getExternalStorageDirectory() + "/download/" + __appName, __fileType);
                        homePageActivity.refreshapps();
                    }
                    if (isInstantUpdateInstall) {
                        RootTask.doChangeDisplaySize(mBase);
                        SharedPreferenceStorage.setBooleanValue(mBase, Constants.Storage.IS_NEED_TO_CHANGE_DISPLAY_TEXT_SIZE.name(), false);
                        SharedPreferenceStorage.setIntegerValue(mBase, Constants.Storage.LAST_APP_INSTALL_UPDATE_VERSION.name(), Constants.INSTANT_UPDATE_VERSION);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mBase.removeLoading();
                }
            }
        });
    }

    private void install(String APP_DIR, Constants.FILE_TYPE file_type) {
        mBase.removeLoading();
        File file = new File(APP_DIR);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String type = "application/vnd.android.package-archive";
            switch (file_type) {
                case apk:
                    type = "application/vnd.android.package-archive";
                    break;
                case pdf:
                    type = "application/pdf";
                    break;
                case mov:
                case mp4:
                    type = "video/*";
                    break;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Log.i(TAG, "install: success");
                Uri downloadedApk = FileProvider.getUriForFile(mBase, "com.zvision.zlaunchertwo.ir.greencode", file);
                intent.setDataAndType(downloadedApk, type);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                Log.i(TAG, "install: fail");
                intent.setDataAndType(Uri.fromFile(file), type);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            mBase.startActivityForResult(intent, Constants.RECOGNIZER_APP_UPDATED);
            homePageActivity.refreshapps();
        } else {
            Toast.makeText(mBase, "ّFile not found!", Toast.LENGTH_SHORT).show();
            mBase.removeLoading();
        }
    }

    public void openImage(String url) {
        try {
            mBase.removeLoading();
            final AlertDialog[] alertDialog = {null};
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mBase);
            LayoutInflater inflater = mBase.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.image_viewer, null);
            dialogBuilder.setView(dialogView);
            final ImageView imageViewer = dialogView.findViewById(R.id.ivImageViewer);
            Picasso.get().load(url).into(imageViewer);
            imageViewer.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    alertDialog[0].dismiss();
                    return true;
                }
            });
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
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
