package com.zvision.zlaunchertwo.root_utils;

import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.zvision.zlaunchertwo.base.BaseActivity;
import com.zvision.zlaunchertwo.base.Constants;
import com.zvision.zlaunchertwo.shared_storage.SharedPreferenceStorage;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RootTask {

    public static Command doChangeDisplaySize(final BaseActivity mBase) {
        final Command[] command = {new Command(0, false, "settings put secure display_density_forced "+ Constants.DYNAMIC_SYSTEM_DISPLAY_DENSITY)};
        if (SharedPreferenceStorage.getBooleanValue(mBase, Constants.Storage.IS_NEED_TO_CHANGE_DISPLAY_TEXT_SIZE.name(), false)) {
            mBase.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (RootTools.isAccessGiven()) {
                            try {
                                command[0] = new Command(0, false, "settings put secure display_density_forced "+ Constants.DYNAMIC_SYSTEM_DISPLAY_DENSITY);
                                RootTools.getShell(true).add(command[0]);
                                while (!command[0].isFinished()) {
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                showRestartDialog(mBase);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return command[0];
    }


    public static void doChangeSettings(BaseActivity mBase) {
        try {

            if (RootTools.isAccessGiven()) {
                try {
                    Command command0 = new Command(0, false, "pm set-home-activity \"com.zvision.zlauncher\"");
                    RootTools.getShell(true).add(command0);
                    while (!command0.isFinished()) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Command command = doChangeDisplaySize(mBase);

                    Command command2 = new Command(0, false, "settings put secure accessibility_large_pointer_icon 1");
                    RootTools.getShell(true).add(command2);
                    while (!command.isFinished()) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Command command3 = new Command(0, false, "settings put secure display_density_forced 362");
                    RootTools.getShell(true).add(command3);
                    while (!command.isFinished()) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    showRestartDialog(mBase);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (RootDeniedException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mBase, "Device must be rooted. No changes were made.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showRestartDialog(BaseActivity mBase) {
        try {

            new AlertDialog.Builder(mBase)
                    .setTitle("A settings change requires a reboot. \nAndroid needs to reboot.")
                    .setMessage("Do you want to reboot?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            try {
                                RootTools.getShell(true).add(new Command(0, false, "reboot"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            } catch (RootDeniedException e) {
                                e.printStackTrace();
                            }

                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
