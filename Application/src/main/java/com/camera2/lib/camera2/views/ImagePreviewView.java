package com.camera2.lib.camera2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.camera2.lib.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by farhad on 5/6/16.
 */
public class ImagePreviewView extends RelativeLayout {

    @Bind(R.id.preview_image_imageview)
    ImageView imageView;


    public ImagePreviewView(Context context) {
        super(context);
        init();
    }

    public ImagePreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImagePreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.preview_image_view, this);

        ButterKnife.bind(this, view);
    }

    public void init(File imageFile) {
        Picasso.with(getContext()).load(imageFile).into(imageView);
    }
}
