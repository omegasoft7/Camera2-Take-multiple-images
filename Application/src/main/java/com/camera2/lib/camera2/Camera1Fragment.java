/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.camera2.lib.camera2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.camera2.lib.R;
import com.camera2.lib.camera2.interfaces.Camera2FragmentInterface;
import com.camera2.lib.camera2.views.FooterView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Camera1Fragment extends Fragment implements Camera2FragmentInterface {

    @Bind(R.id.camera1_footer)
    FooterView footerView;

    private int max = 1;
    private int count = 0;

    private SurfaceView preview = null;
    private SurfaceHolder previewHolder = null;
    private Camera camera = null;
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //roate
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 6;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[32 * 1024];
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            Bitmap cameraBitmap;
            cameraBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            if (getActivity().getWindowManager().getDefaultDisplay().getOrientation() == Surface.ROTATION_0) {
                Matrix matrix = new Matrix();
                //matrix.setRotate(90);
                matrix.postRotate(90);
                cameraBitmap = Bitmap.createBitmap(cameraBitmap, 0, 0, cameraBitmap.getWidth(), cameraBitmap.getHeight(), matrix, true);
            }

            FileOutputStream outStream = null;
            try {
                // Write to SD Card
                File outputDir = getActivity().getCacheDir(); // context being the Activity pointer
                File outputFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".jpg", outputDir);

                outStream = new FileOutputStream(outputFile);
                //outStream.write(data);
                cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.close();
                Log.d("fffff", "onPictureTaken - wrote bytes: " + data.length);
                Log.d("fffff", "onPictureTaken - fileName: " + outputFile.getAbsolutePath());

                footerView.addImages(outputFile);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeCamera();
            }
            Log.d("fffffff1", "onPictureTaken - jpeg");
        }
    };
    private boolean inPreview = false;
    private int camId = 1; //for now always back camera
    private boolean cameraConfigured = false;
    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            // no-op -- wait until surfaceChanged()
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            initPreview(width, height);
            startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // no-op
        }
    };

    public static Camera1Fragment newInstance(int max) {
        Camera1Fragment camera1Fragment = new Camera1Fragment();
        Bundle bundle = new Bundle();
        bundle.putInt("max", max);
        camera1Fragment.setArguments(bundle);
        return camera1Fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera1, container, false);

        ButterKnife.setDebug(true);
        ButterKnife.bind(this, view);

        this.max = getArguments().getInt("max", 1);
        this.count = 0;

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        footerView.init(this);

        preview = (SurfaceView) view.findViewById(R.id.camera1_preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void onResume() {
        super.onResume();

        releaseCameraAndPreview();
        if (camId == 0) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } else {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        camera.setDisplayOrientation(90);
        startPreview();
    }

    private void releaseCameraAndPreview() {
        if (camera != null) {
            if (inPreview) {
                camera.stopPreview();
            }

            camera.release();
            camera = null;
            inPreview = false;
        }
    }

    @Override
    public void onPause() {
        releaseCameraAndPreview();

        super.onPause();
    }

    private void closeCamera() {
        count++;

        if (count >= max) {
            releaseCameraAndPreview();

            Intent intent = new Intent();
            intent.putExtra("size", footerView.getImagePaths().size());

            for (int i = 0; i < footerView.getImagePaths().size(); i++) {
                intent.putExtra("image" + (i + 1), footerView.getImagePaths().get(i).getAbsolutePath());
            }

            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }

        startPreview();

    }

    private void takePicture() {
        try {
            camera.takePicture(null, null, mPicture);
            Thread.sleep(2000);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return (result);
    }

    private void initPreview(int width, int height) {
        if (camera != null && previewHolder.getSurface() != null) {
            try {
                camera.setPreviewDisplay(previewHolder);
            } catch (Throwable t) {
                Log.e("PreviewDemo", "Exception in setPreviewDisplay()", t);
                Toast
                        .makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }

            if (!cameraConfigured) {
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size size = getBestPreviewSize(width, height,
                        parameters);

                if (size != null) {
                    parameters.setPreviewSize(size.width, size.height);
                    camera.setParameters(parameters);
                    cameraConfigured = true;
                }
            }
        }
    }

    private void startPreview() {
        if (cameraConfigured && camera != null) {
            camera.startPreview();
            inPreview = true;
        }
    }

    @Override
    public void TakePicture() {
        takePicture();
    }

    @Override
    public void Done() {
        // TODO: 4/27/16  
    }

    @Override
    public void Cancel() {
        // TODO: 4/27/16
    }
}
