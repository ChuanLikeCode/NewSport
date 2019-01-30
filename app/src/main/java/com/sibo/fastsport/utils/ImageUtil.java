package com.sibo.fastsport.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.sibo.fastsport.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 拍照和选择图片上传前对图片的处理
 *
 * @author linlijun
 */
public class ImageUtil {
    //public static final String CAMERA_SAVEDIR2="storage/sdcard1/photo/";
    public static final String CAMERA_SAVEDIR2 = Environment.getExternalStorageDirectory() + "/photo/";
    //public static final String CAMERA_SAVEDIR2=getApplicationContext().getFilesDir()+"/";// data/data/包/files/
    public static final int MAX_WIDTH = 640;//上传图片最大宽度
    public static final int MAX_HEIGHT = 1000;//上传图片最大高度
    public static final Integer MIN_IMAGE_SIZE = 200000;//上传图片时，超过这个尺寸500kb的图片要压缩
    public static final Integer MIN_IMAGE_SIZE_BIG = 1000000;//上传图片时，超过这个尺寸1M的图片要压缩

    // 选择图片
    public static Intent intentChooseImg() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        return intent;
    }


    // 拍照上传图片
    public static Intent intentImageCapture(File imageFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        return intent;
    }

    /* 创建imageFile的名称*/
    public static String createImageName() {
        return "test.jpg";//用此名称这样每次上传图片都覆盖，才不会占空间
    }


    //创建图片路径
    public static File createImageFile(String saveDir, String saveFileName) {
        File imageDir = new File(saveDir);
        if (!imageDir.exists()) {
            imageDir.mkdir();
        }
        File imageFile = new File(saveDir, saveFileName);
        if (!imageFile.exists()) {
            try {
                imageFile.createNewFile();

            } catch (IOException e) {
                return null;
            }
        }
        return imageFile;
    }


    //图片上传相关 拍照后的操作
    public static void paizhaocreateImagefile(Activity activity, File imageFile) {
        if (imageFile == null) {
            Toast.makeText(activity.getApplicationContext(), "imageFile为空1", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri imageUri = Uri.fromFile(imageFile);
        try {
            BitmapFactory.Options factory = new BitmapFactory.Options();
            Bitmap bmp;
            /*factory.inJustDecodeBounds = false;
            factory.inSampleSize = 4;*/

            factory.inJustDecodeBounds = true;//设为true，不会返回bmp，但是factory有你想要的参数
            bmp = BitmapFactory.decodeStream(activity.getContentResolver()
                    .openInputStream(imageUri), null, factory);
            int bmpWdith = factory.outWidth;
            int bmpHeight = factory.outHeight;
            if (bmpWdith * MAX_HEIGHT > MAX_WIDTH * bmpHeight && bmpWdith > MAX_WIDTH) {
                factory.inSampleSize = bmpWdith / MAX_WIDTH;//压缩到跟屏幕一样宽的尺寸
            }
            if (bmpWdith * MAX_HEIGHT <= MAX_WIDTH * bmpHeight && bmpHeight > MAX_HEIGHT) {
                factory.inSampleSize = bmpHeight / MAX_HEIGHT;//压缩到跟屏幕一样高的尺寸
            }
            factory.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(imageUri), null,
                    factory);
            ImageUtil.saveImage(bmp, imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

	
/*	//选择图片后处理图片的尺寸
    public static BitmapFactory.Options processImg(BitmapFactory.Options factory,int dw,int dh){
		
		// 已屏幕宽 和一般的高作为图片显示的最大尺寸
			factory.inJustDecodeBounds = true; // 当为true时 允许查询图片不为 图片像素分配内存
			int hRatio = (int) Math.ceil(factory.outHeight / (float) dh); // 图片是高度的几倍
			int wRatio = (int) Math.ceil(factory.outWidth / (float) dw); // 图片是宽度的几倍
			// 缩小到 1/ratio的尺寸和 1/ratio^2的像素
			if (hRatio > 1 || wRatio > 1) {
				if (hRatio > wRatio) {
					factory.inSampleSize = hRatio;
				} else
					factory.inSampleSize = wRatio;
			}
			factory.inJustDecodeBounds = false;
			
			return factory;
	}*/

    //将制定Bitmap转为图片保存在imageFile下
    public static void saveImage(Bitmap bmp, File imageFile) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        /*		int w=bmp.getWidth();
				int h=bmp.getHeight();
				int a=bmp.getByteCount();*/
        if (bmp.getByteCount() > MIN_IMAGE_SIZE_BIG) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 30, stream);// 把位图的压缩信息写入到一个指定的输出流中 //
        } else if (imageFile.length() > MIN_IMAGE_SIZE) {//如果图片还是大于规定大小，则将质量压缩
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);// 把位图的压缩信息写入到一个指定的输出流中 //
        } else {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);// 把位图的压缩信息写入到一个指定的输出流中 //

        }
        // 第一个参数format为压缩的格式 // 第二个参数quality为图像压缩比的值,0-100.
        // 0意味着小尺寸压缩,100意味着高质量压缩 （100 // 就是原质量） //
        // 第三个参数stream为输出流 }
        byte[] bytes = stream.toByteArray();
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Drawable转化为Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    // 弹出层的统一样式2 淡入淡出
    public static void popStyle(PopupWindow pop, View v, Context context) {
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        pop.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        pop.setAnimationStyle(R.style.xunleiDialogAnimation);

    }
}
