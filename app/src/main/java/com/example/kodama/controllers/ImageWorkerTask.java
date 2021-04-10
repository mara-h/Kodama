package com.example.kodama.controllers;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;

public class ImageWorkerTask extends AsyncTask<File,Void, Bitmap> {
    WeakReference<ImageView> imageViewReference;
    final int TARGET_IMAGE_VIEW_WIDTH;
    final int TARGET_IMAGE_VIEW_HEIGHT;
    private File mImageFile;
    public ImageWorkerTask(ImageView imageView, int width, int height){
        TARGET_IMAGE_VIEW_HEIGHT = height;
        TARGET_IMAGE_VIEW_WIDTH = width;
        imageViewReference = new WeakReference<ImageView>(imageView);
    }



    @Override
    protected Bitmap doInBackground(File... params) {
        mImageFile = params[0];
        return null;
    }

}
