package com.zvision.zlaunchertwo.part;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.zvision.zlaunchertwo.base.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImageManager {

    public static void saveImageBitmapToFiles(BaseActivity mBase, Bitmap mBitmap, String imageName) {
        try {
            File myDir = mBase.getFilesDir();
            if (!myDir.exists())
                myDir.mkdirs();

            OutputStream fOut;
            File file;
            file = new File(myDir, imageName); // the File to save to

            if (file.exists()){ //if image exists then delete before save
                boolean a = file.delete();
            }
            try {
                fOut = new FileOutputStream(file);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                fOut.flush();
                fOut.close(); // do not forget to close the stream
            } catch (Exception e) {
                e.printStackTrace();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        /*try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 60, bytes);
            File file = new File(Environment.getExternalStorageDirectory() + Constants.IMAGE_FOLDER_NAME);
            File imageFile = null;
            if (file.exists()) {
                imageFile = new File(file.getPath() + File.separator + imageName);
                imageFile.createNewFile();
                FileOutputStream fo = new FileOutputStream(imageFile);
                fo.write(bytes.toByteArray());
                fo.close();
            } else if (file.mkdir()) {
                imageFile = new File(file.getPath() + File.separator + imageName);
                imageFile.createNewFile();
                FileOutputStream fo = new FileOutputStream(imageFile);
                fo.write(bytes.toByteArray());
                fo.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public static Bitmap getImgFromFiles(final BaseActivity mBase, String imageName) {
        File myDir = mBase.getFilesDir();
        if (!myDir.exists())
            myDir.mkdirs();

        File file = new File(myDir, imageName);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;

        /*Bitmap bitmap = null;
        try {
            if (imageName != null) {
                bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + Constants.IMAGE_FOLDER_NAME + "/" + imageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;*/
    }

    public static boolean isImageExists(BaseActivity mBase, String imageName) {
        boolean isExists = false;
        try {
            File myDir = mBase.getFilesDir();
            if (!myDir.exists())
                myDir.mkdirs();

            File file = new File(myDir, imageName);
            if (file.exists()) {
                //Log.d("file", "my_image.jpeg exists!");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExists;
    }

    public static void deleteFileFromStorage(String fileName) {
        try {
            String PATH = Environment.getExternalStorageDirectory() + "/download/";
            File outputFile = new File(PATH);
            if (outputFile.exists()) {
                File file = new File(outputFile, fileName);
                if (file.exists() && file.delete()) {
                    System.out.println("ImageManager.my_image.jpeg deleted!");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
