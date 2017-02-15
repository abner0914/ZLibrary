package com.zlibrary.base.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import com.zlibrary.base.application.ZApplication;
import com.zlibrary.base.exception.LException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 图像处理工具类
 */
public class ZBitmap {
    /**
     * 禁止实例化
     */
    private ZBitmap() {
    }

    /**
     * 从资源文件中读取图片
     *
     * @param bmpId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap drawableToBitmap(int bmpId, int reqWidth, int reqHeight) {
        Resources res = ZApplication.getInstance().getApplicationContext()
                .getResources();
        // First decode with inJustDecodeBounds=true to check dimensions
        final Options options = new Options();
        options.inJustDecodeBounds = true;// 设置这个，只得到能解析的出来图片的大小，并不进行真正的解析
        BitmapFactory.decodeResource(res, bmpId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight); // 跟指定大小比较，计算缩放比例
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, bmpId, options);
    }

    /**
     * convert Bitmap to byte array
     */
    public static byte[] bitmapToByte(Bitmap b) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }

    /**
     * convert byte array to Bitmap
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * 把bitmap转换成Base64编码String
     */
    public static String bitmapToString(Bitmap bitmap) {
        return Base64.encodeToString(bitmapToByte(bitmap), Base64.DEFAULT);
    }

    /**
     * convert Drawable to Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * convert Bitmap to Drawable
     */
    @SuppressWarnings("deprecation")
    public static Drawable bitmapToDrawable(Bitmap b) {
        return b == null ? null : new BitmapDrawable(b);
    }

    public static int calculateInSampleSize(Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    /**
     * 保存图片至本机
     *
     * @param bitmap
     * @param absPath
     * @return
     */
    public static boolean saveBitmap(Bitmap bitmap, String absPath) {
        return saveBitmap(bitmap, new File(absPath));
    }

    public static boolean saveBitmap(Bitmap bitmap, File file) {
        if (bitmap == null) return false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.PNG, 100, fos);
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * 将图片保存到SD卡中
     *
     * @param bm       Bitmap对象
     * @param filename 保存的名字
     * @param quantity
     * @return 路径
     */
    public static String saveBmpToSd(Bitmap bm, String url, String filename,
                                     int quantity) {
        return saveBmpToSd(bm, url, filename, CompressFormat.JPEG, quantity);
    }

    /**
     * 将图片保存到SD卡中
     *
     * @param bm       Bitmap对象
     * @param filename 保存的名字
     * @param format   格式
     * @param quantity
     * @return 路径
     */
    public static String saveBmpToSd(Bitmap bm, String url, String filename,
                                     CompressFormat format, int quantity) {

        String cacheUrl = url;
        if (bm == null)
            return null;
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState()))
            return null;
        File file = new File(cacheUrl + "/");
        if (!file.exists())
            file.mkdirs();
        file = new File(cacheUrl + "/" + filename);
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(format, quantity, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            ZL.e(LException.getStackMsg(e));
        } catch (IOException e) {
            ZL.e(LException.getStackMsg(e));
        }
        return file.getPath();
    }

    /**
     * 拍照或选择相册照片之后调整照片的角度
     */
    public static Bitmap getAngleBitmap(Uri mImageCaptureUri) {
        // 不管是拍照还是选择图片每张图片都有在数据中存储也存储有对应旋转角度orientation值
        // 所以我们在取出图片是把角度值取出以便能正确的显示图片,没有旋转时的效果观看
        ContentResolver cr = ZApplication.getInstance().getContext()
                .getContentResolver();
        Cursor cursor = cr.query(mImageCaptureUri, null, null, null, null);// 根据Uri从数据库中找
        if (cursor != null) {
            cursor.moveToFirst();// 把游标移动到首位，因为这里的Uri是包含ID的所以是唯一的不需要循环找指向第一个就是了
            String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路
            String orientation = cursor.getString(cursor
                    .getColumnIndex("orientation"));// 获取旋转的角度
            cursor.close();
            if (filePath != null) {
                Bitmap bitmap = getResizedImage(filePath, 500);// 根据Path读取资源图片
                int angle = 0;
                if (orientation != null && !"".equals(orientation)) {
                    angle = Integer.parseInt(orientation);
                }
                if (angle != 0) {
                    // 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
                    Matrix m = new Matrix();
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    m.setRotate(angle); // 旋转angle度
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                            m, true);// 从新生成图片
                }
                return bitmap;
            }
        }
        return null;
    }

    /**
     * 传入图片路径，将其压缩后返回
     *
     * @param imagePath ：图片路径
     * @param maxHeight ：最大高度
     * @return Bitmap
     */
    public static Bitmap getResizedImage(String imagePath, int maxHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        int be = computeSampleSize(options, -1, maxHeight * maxHeight);
        if (be < 1)
            be = 1;
        options.inSampleSize = be;
        options.inJustDecodeBounds = false;
        try {
            bitmap = BitmapFactory.decodeFile(imagePath, options);
        } catch (Exception e) {
            ZL.e(LException.getStackMsg(e));
        }
        return bitmap;
    }

    /**
     * 计算大小
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeSampleSize(Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }

    }

    public static Bitmap toRoundCornerBitmap(Bitmap bitmap, float pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 大图片处理(传入字节流)
     *
     * @return
     */
    public static Drawable handleBigImgStream(Context ctx, InputStream is) {
        if (is == null) {
            return null;
        }
        Options opts = new Options();
        opts.inPreferredConfig = Config.RGB_565;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        return bitmapToDrawable(BitmapFactory.decodeStream(is, null, opts));
    }

    /**
     * 大图片处理(传入资源ID)
     *
     * @return
     */
    public static Drawable handleBigImg(Context ctx, int id) {
        InputStream is = ctx.getResources().openRawResource(id);
        return handleBigImgStream(ctx, is);
    }

    /**
     * 创建封面图片
     *
     * @param bitMap
     * @param needRecycle
     * @return
     */
    public static Bitmap createBitmapThumbnail(Bitmap bitMap, boolean needRecycle) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = 120;
        int newHeight = 120;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height,
                matrix, true);
        if (needRecycle) bitMap.recycle();
        return newBitMap;
    }

    public static Intent buildGalleryPickIntent(Uri saveTo, int aspectX, int aspectY,
                                                int outputX, int outputY, boolean returnData) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("output", saveTo);
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", returnData);
        intent.putExtra("outputFormat", CompressFormat.PNG.toString());
        return intent;
    }

    public static Intent buildImagePickIntent(Uri uriFrom, Uri uriTo, int aspectX, int aspectY,
                                              int outputX, int outputY, boolean returnData) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uriFrom, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("output", uriTo);
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", returnData);
        intent.putExtra("outputFormat", CompressFormat.PNG.toString());
        return intent;
    }


    public static Intent buildCaptureIntent(Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

}
