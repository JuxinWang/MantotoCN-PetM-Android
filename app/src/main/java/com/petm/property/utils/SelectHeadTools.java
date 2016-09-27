package com.petm.property.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.petm.property.common.Constant;
import com.petm.property.views.ActionSheetDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class SelectHeadTools {
    public static final String uriPath = "file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "temp.jpg";
    public static final Uri path = Uri.parse(uriPath);
    public static String uploadPhotoUrl = "";
    /*****
     * 打开选择框
     * @param context Context  Activity上下文对象
     * @param uri  Uri
     */
    public static void openDialog(final Activity context, final Uri uri){
        new ActionSheetDialog(context)
                .builder()
                .setTitle("选择图片")
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Red, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        startCamearPicCut(context,uri);
                    }
                })
                .addSheetItem("从手机相册选择", ActionSheetDialog.SheetItemColor.Red, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        startImageCaptrue(context);
                    }
                })
                .show();
    }

    /****
     * 调用系统的拍照功能
     * @param context Activity上下文对象
     * @param uri  Uri
     */
    private static void startCamearPicCut(final Activity context,final Uri uri) {
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA).build(), new AcpListener() {
            @Override
            public void onGranted() {
                // 调用系统的拍照功能
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra("camerasensortype", 2);// 调用前置摄像头
//                intent.putExtra("autofocus", true);// 自动对焦
//                intent.putExtra("fullScreen", true);// 全屏
//                intent.putExtra("showActionIcons", false);
//                // 指定调用相机拍照后照片的储存路径
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                context.startActivityForResult(intent, Constant.PHOTO_REQUEST_TAKEPHOTO);

                Intent intentFromCapture = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                // 判断存储卡是否可以用，可用进行存储
                String state = Environment
                        .getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    File path = Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File file = new File(path, Constant.IMAGE_FILE_NAME);
                    intentFromCapture.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(file));
                }

                context.startActivityForResult(intentFromCapture,
                        Constant.PHOTO_REQUEST_TAKEPHOTO);
            }

            @Override
            public void onDenied(List<String> permissions) {

            }
        });
    }
    /***
     * 调用系统的图库
     * @param context Activity上下文对象
     */
    private static void startImageCaptrue(Activity context) {
//        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        context.startActivityForResult(intent, Constant.PHOTO_REQUEST_GALLERY);
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*"); // 设置文件类型
        intentFromGallery
                .setAction(Intent.ACTION_GET_CONTENT);
        context.startActivityForResult(intentFromGallery,
                Constant.PHOTO_REQUEST_GALLERY);
    }


    /*****
     * 进行截图
     * @param context Activity上下文对象
     * @param uri  Uri
     * @param size  大小
     */
    public static void startPhotoZoom(Activity context,Uri uri, int size) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // crop为true是设置在开启的intent中设置显示的view可以剪裁
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX,outputY 是剪裁图片的宽高
//        intent.putExtra("outputX", size);
//        intent.putExtra("outputY", size);
//        intent.putExtra("return-data", true);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, path);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, Constant.PHOTO_REQUEST_CUT);
    }

    /**
     * 用当前时间给取得的图片命名
     *
     */
    public static String getPhotoFileName() {
        return System.currentTimeMillis() + ".jpg";
    }

    /**
     * Store image to SD card.
     */
    public static String storeImageToFile(Bitmap photo, String photoName) {
        try {
            if (!Constant.PHOTO_DIR.exists())
                Constant.PHOTO_DIR.mkdirs();// 创建存在下载文件夹
            File file = new File(Constant.PHOTO_DIR, photoName);
            FileOutputStream outStreamz = new FileOutputStream(file);

            photo.compress(Bitmap.CompressFormat.PNG, 50, outStreamz);

            outStreamz.close();
            uploadPhotoUrl = Constant.PHOTO_DIR + "/" + photoName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return uploadPhotoUrl;
    }
}
