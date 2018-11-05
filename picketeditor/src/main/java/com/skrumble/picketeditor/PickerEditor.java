package com.skrumble.picketeditor;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.skrumble.picketeditor.editor.image.ImageCropActivity;
import com.skrumble.picketeditor.gallery.GalleryActivity;
import com.skrumble.picketeditor.picker.activity.PickerActivity;
import com.skrumble.picketeditor.picker.interfaces.WorkFinish;
import com.skrumble.picketeditor.picker.utility.PermUtil;

import static com.skrumble.picketeditor.gallery.GalleryActivity.EXTRA_GALLERY_TYPE;
import static com.skrumble.picketeditor.gallery.GalleryActivity.GAlLERY_TYPE_PHOTO_AND_VIDEO;
import static com.skrumble.picketeditor.gallery.GalleryActivity.GAlLERY_TYPE_PICTURE;
import static com.skrumble.picketeditor.gallery.GalleryActivity.GAlLERY_TYPE_VIDEO;
import static com.skrumble.picketeditor.picker.activity.PickerActivity.SELECTION;

public class PickerEditor {

    public static void startCamera(final Fragment context, final int requestCode, final int selectionCount) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermUtil.checkForCamaraWritePermissions(context, new WorkFinish() {
                @Override
                public void onWorkFinish(Boolean check) {
                    Intent i = new Intent(context.getActivity(), PickerActivity.class);
                    i.putExtra(SELECTION, selectionCount);
                    context.startActivityForResult(i, requestCode);
                }
            });
        } else {
            Intent i = new Intent(context.getActivity(), PickerActivity.class);
            i.putExtra(SELECTION, selectionCount);
            context.startActivityForResult(i, requestCode);
        }

    }

    public static void startCamera(Fragment context, int requestCode) {
        startCamera(context, requestCode, 1);
    }

    public static void startCamera(final FragmentActivity context, final int requestCode, final int selectionCount) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermUtil.checkForCamaraWritePermissions(context, new WorkFinish() {
                @Override
                public void onWorkFinish(Boolean check) {
                    Intent i = new Intent(context, PickerActivity.class);
                    i.putExtra(SELECTION, selectionCount);
                    context.startActivityForResult(i, requestCode);
                }
            });
        } else {
            Intent i = new Intent(context, PickerActivity.class);
            i.putExtra(SELECTION, selectionCount);
            context.startActivityForResult(i, requestCode);
        }
    }

    public static void startCamera(final FragmentActivity context, int requestCode) {
        startCamera(context, requestCode, 1);
    }

    public static void starEditor(Fragment fragment, String originalImagePath){
        starEditor(fragment, originalImagePath, true);
    }

    public static void starEditor(Fragment fragment, String originalImagePath, boolean finish){
        Intent intent = new Intent(fragment.getActivity(), ImageCropActivity.class);
        intent.putExtra(ImageCropActivity.EXTRA_IMAGE_SRC, originalImagePath);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        fragment.startActivity(intent);

        if (finish && fragment.getActivity() != null){
            fragment.getActivity().finish();
        }
    }

    public static void starEditor(Activity activity, String originalImagePath){
        starEditor(activity, originalImagePath, true);
    }

    public static void starEditor(Activity activity, String originalImagePath, boolean finish){
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(ImageCropActivity.EXTRA_IMAGE_SRC, originalImagePath);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        activity.startActivity(intent);
        if (finish) {
            activity.finish();
        }
    }

    public static void openPictureGallery(Activity activity, int requestCode) {
        openGallery(activity, requestCode, GAlLERY_TYPE_PICTURE);
    }

    public static void openVideoGallery(Activity activity, int requestCode) {
        openGallery(activity, requestCode, GAlLERY_TYPE_VIDEO);
    }

    public static void openPictureAndVideoGallery(Activity activity, int requestCode) {
        openGallery(activity, requestCode, GAlLERY_TYPE_PHOTO_AND_VIDEO);
    }

    private static void openGallery(Activity activity, int requestCode, int typeOfGallery) {
        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.putExtra(EXTRA_GALLERY_TYPE, typeOfGallery);
        activity.startActivityForResult(intent, requestCode);
    }
}
