package com.sibo.fastsport.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImageLoaderUtils {

    /**
     * @param context
     * @param imageUrl       imageUrl代表图片的URL地址
     * @param imageView      imageView代表承载图片的IMAGEVIEW控件
     * @param ImageOnLoading 加载图片时的图片
     *                       ImageForEmpty
     *                       没有图片资源时的默认图片
     *                       ImageOnFail
     *                       加载失败时的图片
     * @author wu
     */
    public static void initImage(Context context, String imageUrl,
                                 ImageView imageView, int ImageOnLoading) {

        // 创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(context);
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(ImageOnLoading) // 加载图片时的图片
                .showImageForEmptyUri(ImageOnLoading) // 没有图片资源时的默认图片
                .showImageOnFail(ImageOnLoading) // 加载失败时的图片
                .cacheInMemory(true) // 启用内存缓存
                .cacheOnDisk(true) // 启用外存缓存
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .bitmapConfig(Bitmap.Config.RGB_565)
//				.displayer(new RoundedBitmapDisplayer(20))//圆角
//				.displayer(new FadeInBitmapDisplayer(300))//是否图片加载好后渐入的动画时间  
//				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
        // imageUrl代表图片的URL地址，imageView代表承载图片的IMAGEVIEW控件, options代表DisplayImageOptions配置文件
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
//		ImageLoader.getInstance().loadImageSync(uri, options)
    }


    public static Bitmap getNetImg(Context context, String imageUrl, int ImageOnLoading,
                                   int ImageForEmpty, int ImageOnFail) {

        // 创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(context);
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(ImageOnLoading) // 加载图片时的图片
                .showImageForEmptyUri(ImageForEmpty) // 没有图片资源时的默认图片
                .showImageOnFail(ImageOnFail) // 加载失败时的图片
                .cacheInMemory(true) // 启用内存缓存
                .cacheOnDisk(true) // 启用外存缓存
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个
                .build();
        // imageUrl代表图片的URL地址，imageView代表承载图片的IMAGEVIEW控件, options代表DisplayImageOptions配置文件
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(imageUrl, options);
        return bitmap;
    }

    public static void loadImage(Context context, String imageUrl, ImageView imageView) {// 创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(context);
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true) // 启用内存缓存
                .cacheOnDisk(true) // 启用外存缓存
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
    }

    /**
     * 从内存卡中异步加载本地图片
     *
     * @param uri
     * @param imageView
     */
    public static void displayFromSDCard(String uri, ImageView imageView) {
        // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
        ImageLoader.getInstance().displayImage("file://" + uri, imageView);
    }

//    /**
//     * 查看大图 dialog
//     */
//    public  static void photoViewDialog(Context context,String url) {
//        final Dialog dialog = new Dialog(context, R.style.MyPhotoViewDialog);
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_photoview, null);
//        PhotoView imageView = (PhotoView) view.findViewById(R.id.photoview_pv_image);
//        ImageView closeImageView = (ImageView) view.findViewById(R.id.photoview_iv_close);
//        ImageLoaderUtils.loadImage(context, url, imageView);
//        dialog.setContentView(view);
//        dialog.setCanceledOnTouchOutside(true);
//        Window window = dialog.getWindow();
//        //设置弹入弹出动画
//        window.setWindowAnimations(R.style.dialogWindowAnim);
//        closeImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }
}
