package com.example.wes.imagedownloader.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wes on 16. 2. 25.
 */

/**
 * TODO
 * if(MemCache size < picture size)
 * diskCache.put(url, picture);
 */

public class ImageFetcher {
    private static final String TAG = "ImageFetcher";

    private ImageCache imageCache;

    public ImageFetcher(Context context) {
        imageCache = new ImageCache();
    }

    public void imageFetchControler(String imageURL, ImageView imageView) {
        if (imageURL.isEmpty()) {
            return;
        }
        if (imageCache.hasBitmapFromMemCache(imageURL)) {
            fetchBitmapFromMemCache(imageURL, imageView);
        } else {
            fetchBitmapFromNetwork(imageURL, imageView);
        }
    }

    private void fetchBitmapFromMemCache(String imageURL, ImageView imageView) {
        Bitmap bitmap = imageCache.getBitmapFromMemCache(imageURL);
        imageView.setImageBitmap(bitmap);
    }

    private void fetchBitmapFromNetwork(String imageURL, ImageView imageView) {
        BitmapFetcherTask task = new BitmapFetcherTask(imageView);
        task.execute(imageURL);
    }


    private class BitmapFetcherTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public BitmapFetcherTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap bitmap = null;

            try {
                InputStream is = new java.net.URL(url).openStream();

                bitmap = BitmapFactory.decodeStream(is);

                if (bitmap != null)
                    imageCache.addBitmapToMemoryCache(url, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

}
