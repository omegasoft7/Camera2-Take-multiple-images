package com.camera2.lib.camera2.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.camera2.lib.R;
import com.camera2.lib.camera2.adapters.FooterPreviewAdapter;
import com.camera2.lib.camera2.interfaces.Camera2FragmentInterface;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by farhad on 4/27/16.
 */
public class FooterView extends FrameLayout {

    @Bind(R.id.footer_recyclerview)
    RecyclerView recyclerView;

    private Camera2FragmentInterface fragment;
    private FooterPreviewAdapter footerPreviewAdapter;

    public FooterView(Context context) {
        super(context);
        init();
    }

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.footer_view, this);

        ButterKnife.bind(this, view);


        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // specify an adapter (see also next example)
        footerPreviewAdapter = new FooterPreviewAdapter();
        recyclerView.setAdapter(footerPreviewAdapter);
    }

    public void init(Camera2FragmentInterface fragment) {
        this.fragment = fragment;
    }

    public void addImages(File imagefile) {
        footerPreviewAdapter.addImages(imagefile);
    }

    public ArrayList<File> getImagePaths() {
        return footerPreviewAdapter.getImagePaths();
    }

    @OnClick(R.id.footer_take_picture)
    void onTakePictureClick() {
        fragment.TakePicture();
    }

    @OnClick(R.id.footer_done)
    void onDoneClick() {
        fragment.Done();
    }

    @OnClick(R.id.footer_cancel)
    void onCancelClick() {
        fragment.Cancel();
    }
}
