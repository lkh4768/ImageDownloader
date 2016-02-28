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
 *      diskCache.put(url, picture);
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
            Bitmap bitmap = decodeSampledBitmapFromStream(url);

            if (bitmap != null)
                imageCache.addBitmapToMemoryCache(url, bitmap);

            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }

        private Bitmap decodeSampledBitmapFromStream(String url) {
            InputStream is = null;
            BitmapFactory.Options options = null;

            try {
                is = new java.net.URL(url).openStream();

                // First decode with inJustDecodeBounds=true to check dimensions
                options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, null, options);
                is.close();
                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, imageView.getWidth(), imageView.getHeight());

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;

                is = new java.net.URL(url).openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return BitmapFactory.decodeStream(is, null, options);
        }

        private int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }
    }

}
