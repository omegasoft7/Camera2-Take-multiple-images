package com.camera2.lib.camera2;

import android.app.Fragment;
import android.widget.Toast;

import java.io.File;

/**
 * Created by farhad on 4/27/16.
 */
public class Helper {

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
}
