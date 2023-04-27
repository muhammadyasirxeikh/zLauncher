package com.zvision.zlaunchertwo.root_utils;

import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.zvision.zlaunchertwo.base.BaseActivity;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class APKManager {

    public static boolean Uninstall(String apk) {
        try {
            return uninstallAPKFinally(apk);
        } catch (Exception e) {
            Log.d("TAG", e.getLocalizedMessage());
            return false;
        }
    }

    public static boolean Install(String apkPath) {
        try {
            return installAPKFinally(apkPath);
        } catch (Exception e) {
            Log.d("TAG", e.getLocalizedMessage());
            return false;
        }
    }

    public static boolean ClearAppData(String packageName) {
        try {
            // clearing app data
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);
            return true;
        } catch (Exception e) {
            Log.d("TAG", e.getLocalizedMessage());
            return false;
        }
    }

    public static boolean ClearAppCache(String packageName) {
        try {
            // clearing app data
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("rm -rf /data/data/" + packageName + "/cache/*");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm -rf /data/data/" + packageName + "/cache/*");
                return true;
            } catch (Exception e1) {
                e1.printStackTrace();
                return false;
            }
        }
    }

    private static boolean uninstallAPKFinally(String apk) {
        final boolean[] isUninstallSuccess = {true};
        Command cmd = new Command(0, false, "pm uninstall " + apk) {

            @Override
            public void commandOutput(int id, String line) {
                super.commandOutput(id, line);

                if (line.contains("Failure")) {
                    Toast.makeText(context, "Error Deinstall app: "+line, Toast.LENGTH_SHORT).show();
                    isUninstallSuccess[0] = false;
                }

            }
        };
        try {
            RootTools.getShell(true).add(cmd);
        } catch (IOException | TimeoutException | RootDeniedException e) {
            e.printStackTrace();
            isUninstallSuccess[0] = false;
            return false;
        }
        while (!cmd.isFinished()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        return isUninstallSuccess[0];
    }

    private static boolean installAPKFinally(String apk) {
        final boolean[] isInstallSuccess = {true};
        Command cmd = new Command(0, false, "pm install -r " + apk) {

            @Override
            public void commandOutput(int id, String line) {
                super.commandOutput(id, line);

                if (line.contains("Failure")) {
                    Toast.makeText(context, "Error install app: "+line, Toast.LENGTH_SHORT).show();
                    isInstallSuccess[0] = false;
                }

            }
        };
        try {
            RootTools.getShell(true).add(cmd);
        } catch (IOException | TimeoutException | RootDeniedException e) {
            e.printStackTrace();
            isInstallSuccess[0] = false;
            return false;
        }
        while (!cmd.isFinished()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        return isInstallSuccess[0];
    }


    public static boolean IsAppInstalled(BaseActivity mBase, String packageName) {
        try {
            PackageManager pm = mBase.getPackageManager();
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    /*
    private void installApk(String fileLocation, String apkName,CallbackContext callbackContext) {
        if (fileLocation != null && fileLocation.length() > 0 && apkName != null && apkName.length() > 0 ) {
            callbackContext.success("Executing installation");
            Context context = this.cordova.getActivity().getApplicationContext();

            String apkDir = fileLocation + apkName;
            String activityDir =  context.getPackageName() +"/"+  context.getPackageName() +".MainActivity";

            try {
                final String command = "chmod 777 " + fileLocation+ "* ;pm install -r "+ apkDir +";am start -n " + activityDir;
                Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", command});
                proc.waitFor();

            }catch (Exception ex) {
                callbackContext.error(ex.toString());
            }

        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
     */
}
