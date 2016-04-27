package com.camera2.lib.camera2;

import android.app.Fragment;
import android.widget.Toast;

import java.io.File;

/**
 * Created by farhad on 4/27/16.
 */
public class Helper {

    public static String libFileDirPath = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/HaierVoiceCamera";

    public static String DCIMPath = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/DCIM/HaierVoiceCamera";


    /**
     * Shows a {@link Toast} on the UI thread.
     *
     * @param text The message to show
     */
    public static void showToast(final Fragment fragment, final String text) {
        if (fragment.getActivity() != null) {
            fragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(fragment.getActivity(), text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * check the path folder is already exist in sdcard or not,if not create them.
     *
     * @param patch
     * @return Ture--already exist or create successfully ,False--create failure
     */
    public static boolean checkAndInitSDFolder(String patch) {
        File dir = new File(patch);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
