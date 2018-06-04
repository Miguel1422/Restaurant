package com.superescuadronalfa.restaurant.io;


import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    private String directory;
    private Context ctx;

    public ImageUtils(Context ctx, String directory) {
        this.directory = directory;
        this.ctx = ctx;
    }

    public Bitmap loadImageFromStorage(String key) {

        try {
            ContextWrapper cw = new ContextWrapper(ctx);
            File directoryPath = cw.getCacheDir();
            File f = new File(directoryPath.getPath() + '/' + directory, key);
            if (!f.exists()) return null;
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deletePath() {
        ContextWrapper cw = new ContextWrapper(ctx);
        // path to /data/data/yourapp/app_data/imageDir
        File cache = cw.getCacheDir();
        File directoryPath = new File(cache.getPath() + '/' + directory);
        String[] entries = directoryPath.list();
        if (entries != null)
            for (String s : entries) {
                File currentFile = new File(directoryPath.getPath(), s);
                currentFile.delete();
            }

        return !directoryPath.exists() || directoryPath.delete();
    }

    public String saveToInternalStorage(Bitmap bitmapImage, String key) {
        ContextWrapper cw = new ContextWrapper(ctx);
        // path to /data/data/yourapp/app_data/imageDir
        File cache = cw.getCacheDir();
        // Create imageDir

        File fileDir = new File(cache.getPath() + '/' + directory);

        if (!fileDir.exists()) fileDir.mkdirs();

        File mypath = new File(fileDir, key);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cache.getAbsolutePath();
    }
}
