package com.example.wes.imagedownloader.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by wes on 16. 2. 25.
 */
public class ImageCache {
    private LruCache<String, Bitmap> memoryCache;

    public ImageCache() {
        initLruCache();
    }

    public void initLruCache() {
        final int maxMemoryOfApp = getMaxMemoryOfApp();
        int cacheSize = getCacheSize(maxMemoryOfApp);

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    private int getMaxMemoryOfApp() {
        return (int) (Runtime.getRuntime().maxMemory() / 1024);
    }

    private int getCacheSize(int maxMemoryOfApp) {
        return maxMemoryOfApp / 4;
    }

    public boolean hasBitmapFromMemCache(String imagURL) {
        return (memoryCache.get(imagURL) != null);
    }

    public void addBitmapToMemoryCache(String imageURL, Bitmap bitmap) {
        if (getBitmapFromMemCache(imageURL) == null) {
            memoryCache.put(imageURL, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String imageURL) {
        Bitmap bitmap = memoryCache.get(imageURL);

        return bitmap;
    }
}
