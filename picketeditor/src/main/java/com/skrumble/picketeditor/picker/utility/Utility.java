package com.skrumble.picketeditor.picker.utility;

import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.WorkerThread;
import android.support.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;

import com.skrumble.picketeditor.R;
import com.skrumble.picketeditor.gallery.GalleryActivity;
import com.skrumble.picketeditor.picker.public_interface.BitmapCallback;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Utility {

    public static int HEIGHT, WIDTH;
    private String pathDir;

    public static void setupStatusBarHidden(AppCompatActivity appCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = appCompatActivity.getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public static void showStatusBar(AppCompatActivity appCompatActivity) {
        synchronized (appCompatActivity) {
            Window w = appCompatActivity.getWindow();
            View decorView = w.getDecorView();
            // Show Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);

        }
    }

    public static void hideStatusBar(AppCompatActivity appCompatActivity) {
        synchronized (appCompatActivity) {
            Window w = appCompatActivity.getWindow();
            View decorView = w.getDecorView();
            // Hide Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static int getSoftButtonsBarSizePort(Activity activity) {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    public static void getScreenSize(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        HEIGHT = displayMetrics.heightPixels;
        WIDTH = displayMetrics.widthPixels;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static String getDateDifference(Context context, Calendar calendar) {
        Date d = calendar.getTime();
        Calendar lastMonth = Calendar.getInstance();
        Calendar lastWeek = Calendar.getInstance();
        Calendar recent = Calendar.getInstance();
        lastMonth.add(Calendar.DAY_OF_MONTH, -(Calendar.DAY_OF_MONTH));
        lastWeek.add(Calendar.DAY_OF_MONTH, -7);
        recent.add(Calendar.DAY_OF_MONTH, -2);
        if (calendar.before(lastMonth)) {
            return new SimpleDateFormat("MMMM").format(d);
        } else if (calendar.after(lastMonth) && calendar.before(lastWeek)) {
            return context.getResources().getString(R.string.last_month);
        } else if (calendar.after(lastWeek) && calendar.before(recent)) {
            return context.getResources().getString(R.string.last_week);
        } else {
            return context.getResources().getString(R.string.recent);
        }
    }

    static boolean isNull(View topChild) {
        return topChild == null;
    }

    public static Cursor getCursor(Context context, int typeOfGallery) {
        Uri cursorUri;
        String[] cursorProjection;
        String cursorSelection = null;
        String cursorOrderBy;

        switch (typeOfGallery) {
            case GalleryActivity.GAlLERY_TYPE_PHOTO_AND_VIDEO:
                cursorUri = Constants.IMAGES_AND_VIDEO_URI;
                cursorProjection = Constants.IMAGES_AND_VIDEOS_PROJECTION;
                cursorSelection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                                + " OR "
                                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
                cursorOrderBy = Constants.IMAGES_AND_VIDEOS_ORDERBY;
                break;
            case GalleryActivity.GAlLERY_TYPE_VIDEO:
                cursorUri = Constants.VIDEO_URI;
                cursorProjection = Constants.VIDEOS_PROJECTION;
                cursorOrderBy = Constants.VIDEOS_ORDERBY;
                break;
            case GalleryActivity.GAlLERY_TYPE_PICTURE:
            default:
                cursorUri = Constants.IMAGES_URI;
                cursorProjection = Constants.IMAGES_PROJECTION;
                cursorOrderBy = Constants.IMAGES_ORDERBY;
                break;
        }
        return context.getContentResolver().query(cursorUri, cursorProjection, cursorSelection, null, cursorOrderBy);
    }

    public static boolean isViewVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public static ViewPropertyAnimator showScrollbar(View mScrollbar, Context context) {
        float transX = context.getResources().getDimensionPixelSize(R.dimen.fastscroll_scrollbar_padding_end);
        mScrollbar.setTranslationX(transX);
        mScrollbar.setVisibility(View.VISIBLE);
        return mScrollbar.animate().translationX(0f).alpha(1f)
                .setDuration(Constants.sScrollbarAnimDuration)
                .setListener(new AnimatorListenerAdapter() {
                    // adapter required for new alpha value to stick
                });
    }

    public static void cancelAnimation(ViewPropertyAnimator animator) {
        if (animator != null) {
            animator.cancel();
        }
    }

    public static void manipulateVisibility(AppCompatActivity activity, float slideOffset,
                                            RecyclerView instantRecyclerView, RecyclerView recyclerView,
                                            View status_bar_bg, View topbar, View clickme, View sendButton, boolean longSelection) {
        instantRecyclerView.setAlpha(1 - slideOffset);
        clickme.setAlpha(1 - slideOffset);
        if (longSelection) {
            sendButton.setAlpha(1 - slideOffset);
        }
        topbar.setAlpha(slideOffset);
        recyclerView.setAlpha(slideOffset);
        if ((1 - slideOffset) == 0 && instantRecyclerView.getVisibility() == View.VISIBLE) {
            instantRecyclerView.setVisibility(View.GONE);
            clickme.setVisibility(View.GONE);
        } else if (instantRecyclerView.getVisibility() == View.GONE && (1 - slideOffset) > 0) {
            instantRecyclerView.setVisibility(View.VISIBLE);
            clickme.setVisibility(View.VISIBLE);
            if (longSelection) {
                sendButton.clearAnimation();
                sendButton.setVisibility(View.VISIBLE);
            }
        }
        if ((slideOffset) > 0 && recyclerView.getVisibility() == View.INVISIBLE) {
            recyclerView.setVisibility(View.VISIBLE);
            status_bar_bg.animate().translationY(0).setDuration(300).start();
            topbar.setVisibility(View.VISIBLE);
            Utility.showStatusBar(activity);
        } else if (recyclerView.getVisibility() == View.VISIBLE && (slideOffset) == 0) {
            Utility.hideStatusBar(activity);
            recyclerView.setVisibility(View.INVISIBLE);
            topbar.setVisibility(View.GONE);
            status_bar_bg.animate().translationY(-(status_bar_bg.getHeight())).setDuration(300).start();
        }
    }

    @SuppressWarnings("SameParameterValue")
    public static int getValueInRange(int min, int max, int value) {
        int minimum = Math.max(min, value);
        return Math.min(minimum, max);
    }

    public static void vibe(Context c, long l) {
        ((Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(l);
    }

    public static File getVideoFile(){
        File dir = new File(Environment.getExternalStorageDirectory(), "/DCIM/Camera");
        if (!dir.exists()){
            dir.mkdir();
        }

        File photo = new File(dir, "Video_" + new SimpleDateFormat("yyyyMMdd_HHmmSS", Locale.ENGLISH).format(new Date()) + ".mp4");

        if (photo.exists()) {
            photo.delete();
        }

        return photo;
    }

    public static File getVideoFileInCatchFolder(Context context){
        File dir = context.getCacheDir();
        if (!dir.exists()){
            dir.mkdir();
        }

        File videoFile = new File(dir, "Video_" + new SimpleDateFormat("yyyyMMdd_HHmmSS", Locale.ENGLISH).format(new Date()) + ".mp4");

        if (videoFile.exists()) {
            videoFile.delete();
        }

        return videoFile;
    }

    public static File writeImage(Bitmap bitmap) {
        File destinationImagePath = getImagePathInCameraFolder();

        try {
            FileOutputStream fos = new FileOutputStream(destinationImagePath.getPath());

            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fos);
            // fos.write(jpeg);
            fos.close();
        } catch (Exception e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }
        return destinationImagePath;
    }

    public static File writeImageToCatchFolder(Bitmap bitmap, Context context){
        File dir = context.getCacheDir();
        if (!dir.exists()){
            dir.mkdir();
        }

        File photo = new File(dir, "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmSS", Locale.ENGLISH).format(new Date()) + ".jpg");

        if (photo.exists()) {
            photo.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(photo.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            // fos.write(jpeg);
            fos.close();
        } catch (Exception e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }
        return photo;
    }

    public static File getImagePathInCameraFolder(){
        File dir = new File(Environment.getExternalStorageDirectory(), "/DCIM/Camera");
        if (!dir.exists()){
            dir.mkdir();
        }

        File photo = new File(dir, "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmSS", Locale.ENGLISH).format(new Date()) + ".jpg");

        if (photo.exists()) {
            photo.delete();
        }

        return photo;
    }

    public static Bitmap getScaledBitmap(int maxWidth, Bitmap rotatedBitmap) {
        try {

            int nh = (int) (rotatedBitmap.getHeight() * (512.0 / rotatedBitmap.getWidth()));
            return Bitmap.createScaledBitmap(rotatedBitmap, maxWidth, nh, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    public static float getFingerSpacing(MotionEvent event) {
        try {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } catch (Exception e) {
            Log.e("exc", "->" + e.getMessage());
            return 0;
        }
    }

    /**
     * Determines whether the device has valid camera sensors, so the library
     * can be used.
     *
     * @param context a valid Context
     * @return whether device has cameras
     */
    @SuppressWarnings("WeakerAccess")
    public static boolean hasCameras(Context context) {
        PackageManager manager = context.getPackageManager();
        // There's also FEATURE_CAMERA_EXTERNAL , should we support it?
        return manager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                || manager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }

    /**
     * Decodes an input byte array and outputs a Bitmap that is ready to be displayed.
     * The difference with {@link android.graphics.BitmapFactory#decodeByteArray(byte[], int, int)}
     * is that this cares about orientation, reading it from the EXIF header.
     *
     * @param source a JPEG byte array
     */
    @SuppressWarnings("WeakerAccess")
    @WorkerThread
    public static void decodeBitmap(final byte[] source) {
        decodeBitmap(source, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Decodes an input byte array and outputs a Bitmap that is ready to be displayed.
     * The difference with {@link android.graphics.BitmapFactory#decodeByteArray(byte[], int, int)}
     * is that this cares about orientation, reading it from the EXIF header.
     * This is executed in a background thread, and returns the result to the original thread.
     *
     * @param source a JPEG byte array
     * @param callback a callback to be notified
     */
    @SuppressWarnings("WeakerAccess")
    public static void decodeBitmap(final byte[] source, final BitmapCallback callback) {
        decodeBitmap(source, Integer.MAX_VALUE, Integer.MAX_VALUE, callback);
    }

    /**
     * Decodes an input byte array and outputs a Bitmap that is ready to be displayed.
     * The difference with {@link android.graphics.BitmapFactory#decodeByteArray(byte[], int, int)}
     * is that this cares about orientation, reading it from the EXIF header.
     * This is executed in a background thread, and returns the result to the original thread.
     *
     * The image is also downscaled taking care of the maxWidth and maxHeight arguments.
     *
     * @param source a JPEG byte array
     * @param maxWidth the max allowed width
     * @param maxHeight the max allowed height
     * @param callback a callback to be notified
     */
    @SuppressWarnings("WeakerAccess")
    public static void decodeBitmap(final byte[] source, final int maxWidth, final int maxHeight, final BitmapCallback callback) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = decodeBitmap(source, maxWidth, maxHeight);
                if (callback != null){
                    callback.onBitmapReady(bitmap);
                }
            }
        });
    }

    /**
     * Decodes an input byte array and outputs a Bitmap that is ready to be displayed.
     * The difference with {@link android.graphics.BitmapFactory#decodeByteArray(byte[], int, int)}
     * is that this cares about orientation, reading it from the EXIF header.
     *
     * The image is also downscaled taking care of the maxWidth and maxHeight arguments.
     *
     * @param source a JPEG byte array
     * @param maxWidth the max allowed width
     * @param maxHeight the max allowed height
     */
    // TODO ignores flipping
    @SuppressWarnings({"SuspiciousNameCombination", "WeakerAccess"})
    @WorkerThread
    public static Bitmap decodeBitmap(byte[] source, int maxWidth, int maxHeight) {
        if (maxWidth <= 0) maxWidth = Integer.MAX_VALUE;
        if (maxHeight <= 0) maxHeight = Integer.MAX_VALUE;
        int orientation;
        boolean flip;
        InputStream stream = null;
        try {
            // http://sylvana.net/jpegcrop/exif_orientation.html
            stream = new ByteArrayInputStream(source);
            ExifInterface exif = new ExifInterface(stream);
            Integer exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    orientation = 0; break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    orientation = 180; break;

                case ExifInterface.ORIENTATION_ROTATE_90:
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    orientation = 90; break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    orientation = 270; break;

                default: orientation = 0;
            }

            flip = exifOrientation == ExifInterface.ORIENTATION_FLIP_HORIZONTAL ||
                    exifOrientation == ExifInterface.ORIENTATION_FLIP_VERTICAL ||
                    exifOrientation == ExifInterface.ORIENTATION_TRANSPOSE ||
                    exifOrientation == ExifInterface.ORIENTATION_TRANSVERSE;

        } catch (IOException e) {
            e.printStackTrace();
            orientation = 0;
            flip = false;
        } finally {
            if (stream != null) {
                try { stream.close(); } catch (Exception ignored) {}
            }
        }

        Bitmap bitmap;
        if (maxWidth < Integer.MAX_VALUE || maxHeight < Integer.MAX_VALUE) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(source, 0, source.length, options);

            int outHeight = options.outHeight;
            int outWidth = options.outWidth;
            if (orientation % 180 != 0) {
                outHeight = options.outWidth;
                outWidth = options.outHeight;
            }

            options.inSampleSize = computeSampleSize(outWidth, outHeight, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeByteArray(source, 0, source.length, options);
        } else {
            bitmap = BitmapFactory.decodeByteArray(source, 0, source.length);
        }

        if (orientation != 0 || flip) {
            Matrix matrix = new Matrix();
            matrix.setRotate(orientation);
            // matrix.postScale(1, -1) Flip... needs testing.
            Bitmap temp = bitmap;
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            temp.recycle();
        }
        return bitmap;
    }


    private static int computeSampleSize(int width, int height, int maxWidth, int maxHeight) {
        int inSampleSize = 1;
        if (height > maxHeight || width > maxWidth) {
            while ((height / inSampleSize) >= maxHeight
                    || (width / inSampleSize) >= maxWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static String convertMillisecondToTime(long milliSecond){
        return convertSecondsToTime(TimeUnit.MILLISECONDS.toSeconds(milliSecond));
    }

    public static String convertSecondsToTime(long seconds) {
        DecimalFormat formatter = new DecimalFormat("00");
        String timeStr;
        int hour;
        int minute;
        int second;
        if (seconds <= 0)
            return "00:00";
        else {
            minute = (int)seconds / 60;
            if (minute < 60) {
                second = (int)seconds % 60;
                timeStr = formatter.format(minute) + ":" + formatter.format(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = (int)(seconds - hour * 3600 - minute * 60);
                timeStr = formatter.format(hour) + ":" + formatter.format(minute) + ":" + formatter.format(second);
            }
        }
        return timeStr;
    }
}