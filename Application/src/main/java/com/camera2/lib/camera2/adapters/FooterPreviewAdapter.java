package com.camera2.lib.camera2.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.camera2.lib.R;
import com.camera2.lib.camera2.views.ImagePreviewView;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by farhad on 5/6/16.
 */
public class FooterPreviewAdapter extends RecyclerView.Adapter<FooterPreviewAdapter.ViewHolder> {

    private ArrayList<File> imagePaths = new ArrayList<>();

    public void addImages(File imageFile) {
        imagePaths.add(imageFile);

        //todo notify inserted position only
        notifyDataSetChanged();
    }

    public ArrayList<File> getImagePaths() {
        return imagePaths;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imagePreviewView.init(imagePaths.get(position));
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.preview_image_ImagePreviewView)
        ImagePreviewView imagePreviewView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
