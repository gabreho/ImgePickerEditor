package com.skrumble.picketeditor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;

import com.skrumble.picketeditor.activity.FileActivity;
import com.skrumble.picketeditor.activity.ImageCropActivity;
import com.skrumble.picketeditor.editor.video.VideoTrimmerActivity;
import com.skrumble.picketeditor.activity.GalleryActivity;
import com.skrumble.picketeditor.activity.CameraActivity;
import com.skrumble.picketeditor.utility.PickerEditorStyleParams;

import static com.skrumble.picketeditor.activity.GalleryActivity.EXTRA_GALLERY_TYPE;
import static com.skrumble.picketeditor.activity.GalleryActivity.GAlLERY_TYPE_PHOTO_AND_VIDEO;
import static com.skrumble.picketeditor.activity.GalleryActivity.GAlLERY_TYPE_PICTURE;
import static com.skrumble.picketeditor.activity.GalleryActivity.GAlLERY_TYPE_VIDEO;
import static com.skrumble.picketeditor.activity.CameraActivity.SELECTION;



public class PickerEditor {

    public static final String RESULT_FILE = "RESULT_FILE";

    public static final String PICKER_EDITOR_STYLE = "PICKER_EDITOR_STYLE";

    public static void setGlobalStyleParams(PickerEditorStyleParams styleParams) {
        PickerEditor.sStyleParams = styleParams;
    }

    private static PickerEditorStyleParams sStyleParams = new PickerEditorStyleParams(Color.parseColor("#d53c27"), Color.WHITE);

    // *********************************************************************************************
    // region Picker

    public static void openPictureGallery(Activity activity, int requestCode) {
        openGallery(activity, requestCode, GAlLERY_TYPE_PICTURE, sStyleParams);
    }

    public static void openPictureGallery(Activity activity, int requestCode, PickerEditorStyleParams pickerEditorStyleParams) {
        openGallery(activity, requestCode, GAlLERY_TYPE_PICTURE, pickerEditorStyleParams);
    }

    public static void openVideoGallery(Activity activity, int requestCode) {
        openGallery(activity, requestCode, GAlLERY_TYPE_VIDEO, sStyleParams);
    }

    public static void openVideoGallery(Activity activity, int requestCode, PickerEditorStyleParams pickerEditorStyleParams) {
        openGallery(activity, requestCode, GAlLERY_TYPE_VIDEO, pickerEditorStyleParams);
    }

    public static void openPictureAndVideoGallery(Activity activity, int requestCode) {
        openGallery(activity, requestCode, GAlLERY_TYPE_PHOTO_AND_VIDEO, sStyleParams);
    }

    public static void openPictureAndVideoGallery(Activity activity, int requestCode, PickerEditorStyleParams pickerEditorStyleParams) {
        openGallery(activity, requestCode, GAlLERY_TYPE_PHOTO_AND_VIDEO, pickerEditorStyleParams);
    }

    private static void openGallery(Activity activity, int requestCode, int typeOfGallery, PickerEditorStyleParams pickerEditorStyleParams) {
        PickerEditorConfig.updateScreenSize(activity);

        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.putExtra(EXTRA_GALLERY_TYPE, typeOfGallery);
        intent.putExtra(PICKER_EDITOR_STYLE, pickerEditorStyleParams);
        activity.startActivityForResult(intent, requestCode);
    }

    // endregion

    // *********************************************************************************************
    // region Camera

    public static void startCamera(final Fragment context, final int requestCode, final int selectionCount) {
        PickerEditorConfig.updateScreenSize(context.getActivity());

        Intent i = new Intent(context.getActivity(), CameraActivity.class);
        i.putExtra(SELECTION, selectionCount);
        i.putExtra(CameraActivity.EXTRA_CAMERA_TYPE, CameraActivity.ARG_CAMERA_TYPE_PICTURE);
        context.startActivityForResult(i, requestCode);
    }

    public static void startCamera(Fragment context, int requestCode) {
        PickerEditorConfig.updateScreenSize(context.getActivity());

        startCamera(context, requestCode, 1);
    }

    public static void startCamera(final Activity context, final int requestCode, final int selectionCount) {
        PickerEditorConfig.updateScreenSize(context);

        Intent i = new Intent(context, CameraActivity.class);
        i.putExtra(SELECTION, selectionCount);
        i.putExtra(CameraActivity.EXTRA_CAMERA_TYPE, CameraActivity.ARG_CAMERA_TYPE_PICTURE);
        context.startActivityForResult(i, requestCode);
    }

    public static void startCamera(final Activity context, int requestCode) {
        PickerEditorConfig.updateScreenSize(context);

        startCamera(context, requestCode, 1);
    }

    public static void startCameraForVideo(final Fragment context, final int requestCode) {
        PickerEditorConfig.updateScreenSize(context.getActivity());

        Intent i = new Intent(context.getActivity(), CameraActivity.class);
        i.putExtra(CameraActivity.EXTRA_CAMERA_TYPE, CameraActivity.ARG_CAMERA_TYPE_VIDEO);
        context.startActivityForResult(i, requestCode);
    }

    public static void startCameraForVideo(final Activity context, final int requestCode) {
        PickerEditorConfig.updateScreenSize(context);

        Intent i = new Intent(context, CameraActivity.class);
        i.putExtra(CameraActivity.EXTRA_CAMERA_TYPE, CameraActivity.ARG_CAMERA_TYPE_VIDEO);
        context.startActivityForResult(i, requestCode);
    }

    // endregion

    // *********************************************************************************************
    // region Image Editor

    static void starEditor(Fragment fragment, String originalImagePath){
        PickerEditorConfig.updateScreenSize(fragment.getActivity());

        Intent intent = new Intent(fragment.getActivity(), ImageCropActivity.class);
        intent.putExtra(ImageCropActivity.EXTRA_IMAGE_SRC, originalImagePath);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        fragment.startActivity(intent);
    }

    public static void starEditor(Activity activity, String originalImagePath){
        PickerEditorConfig.updateScreenSize(activity);

        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(ImageCropActivity.EXTRA_IMAGE_SRC, originalImagePath);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        activity.startActivity(intent);
    }

    public static void startEditorForResult(Activity activity, String originalImagePath, int requestCode){
        PickerEditorConfig.updateScreenSize(activity);

        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(ImageCropActivity.EXTRA_IMAGE_SRC, originalImagePath);
        activity.startActivityForResult(intent, requestCode);
    }

    // endregion

    // *********************************************************************************************
    // region Video Editor

    public static void starVideoEditor(Activity activity, String originalVideoPath){
        PickerEditorConfig.updateScreenSize(activity);

        Intent intent = new Intent(activity, VideoTrimmerActivity.class);
        intent.putExtra(VideoTrimmerActivity.EXTRA_VIDEO_SRC, originalVideoPath);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        activity.startActivity(intent);
    }

    public static void starVideoEditorForResult(Activity activity, String originalVideoPath, int requestCode){
        PickerEditorConfig.updateScreenSize(activity);

        Intent intent = new Intent(activity, VideoTrimmerActivity.class);
        intent.putExtra(VideoTrimmerActivity.EXTRA_VIDEO_SRC, originalVideoPath);
        activity.startActivityForResult(intent, requestCode);
    }

    // endregion..r

    // *********************************************************************************************
    // region File

    public static void pickFile(Activity activity, int requestCode){
        pickFile(activity, requestCode, sStyleParams);
    }

    public static void pickFile(Activity activity, int requestCode, PickerEditorStyleParams pickerEditorStyleParams){
        Intent intent = new Intent(activity, FileActivity.class);
        intent.putExtra(PICKER_EDITOR_STYLE, pickerEditorStyleParams);
        activity.startActivityForResult(intent, requestCode);
    }

    // endregion


}