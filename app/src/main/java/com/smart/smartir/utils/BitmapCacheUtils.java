package com.smart.smartir.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.smart.smartir.activity.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 图片的三级缓存类
 *
 * 1. 内存获取数据 2. 本地磁盘获取数据 cache 3. 网络获取数据
 *
 */
public class BitmapCacheUtils {
	private MainActivity mContext;

	private int maxSize = (int) (Runtime.getRuntime().freeMemory() / 2);// 获取可用内存的一半做为缓存容器大小
	private Map<ImageView,String> iv_urls = new HashMap<ImageView,String>();//记录最后显示想iv对应url
	private LruCache<String, Bitmap> mLruCaches = new LruCache<String, Bitmap>(
			maxSize) {

		@Override
		protected int sizeOf(String key, Bitmap value) {
			// TODO Auto-generated method stub
			return value.getRowBytes() * value.getHeight();// return
															// value.getByteCount();//获取字节的大小
		}

	};

	private ExecutorService mThreadPool;//线程池

	public BitmapCacheUtils(MainActivity context) {
		mContext = context;
		mThreadPool = Executors.newFixedThreadPool(6);
	}

	public void display(ImageView iv_temp, String url) {

		// 1. 内存获取数据
		Bitmap bitmap = mLruCaches.get(url);
		if (bitmap != null) {
//			PrintLog.print("从内存中获取图片");
			// 缓存中有数据
			iv_temp.setImageBitmap(bitmap);
			return;
		}
		// 2. 本地磁盘获取数据
		bitmap = getBitmapFromLocal(url);
		if (bitmap != null) {
//			PrintLog.print("从本地获取图片");
			// 本地有图片
			iv_temp.setImageBitmap(bitmap);
			
			//往内存中保存一份
			writeBitmap2Memery(url, bitmap);
			
			return;
		}

		// 3. 网络获取数据
		iv_urls.put(iv_temp, url);
		getBitmapFromNet(iv_temp, url);

	}

	private void getBitmapFromNet(ImageView iv_temp, String url) {
		// 线程 线程池
		
		//new Thread(new BitmapFromNet(iv_temp, url)).start();
		mThreadPool.submit(new BitmapFromNet(iv_temp, url));

	}

	/**
	 * 网络请求数据
	 * 
	 * @author Administrator
	 * 
	 */
	private class BitmapFromNet implements Runnable {
		private ImageView iv_bitmap;
		private String mUrl;

		public BitmapFromNet(ImageView iv_temp, String url) {
			// TODO Auto-generated constructor stub
			this.iv_bitmap = iv_temp;
			this.mUrl = url;
		}

		@Override
		public void run() {
			// 1. 请求网络
			try {
				URL url = new URL(mUrl);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.setConnectTimeout(7000);// 7秒超时设置
				con.setRequestMethod("GET");// 请求方式
				int code = con.getResponseCode();
				if (code == 200) {
					// success
					InputStream is = con.getInputStream();

					// 把is 转成 bitmap
					final Bitmap bitmap = BitmapFactory.decodeStream(is);

					// 1. 内存存放一份
					writeBitmap2Memery(mUrl, bitmap);

					// 2. 本地Cache目录存放一份
					writeBitmap2Local(mUrl, bitmap);

					// 3. 显示图片
					mContext.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							//判断当前url或者的图片是否是最新的
							if (iv_urls.get(iv_bitmap).equals(mUrl)) {
								//最新的url
								
								// 运行在主线程中
								iv_bitmap.setImageBitmap(bitmap);
//								PrintLog.print("从网络获取图片。。。。。。。");
							} else {
								//网速慢造成的图片错位 不显示图片
							}
							
						}
					});

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 往内存中保存
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void writeBitmap2Memery(String url, Bitmap bitmap) {
		// TODO Auto-generated method stub
		mLruCaches.put(url, bitmap);// 内存中保存一份
	}

	/**
	 * 从本地获取图片
	 * 
	 * @param mUrl
	 * @return
	 */
	public Bitmap getBitmapFromLocal(String mUrl) {
		File file = new File(mContext.getCacheDir(), mUrl.substring(mUrl
				.lastIndexOf('/') + 1));

		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

		return bitmap;
	}

	/**
	 * 往本地中保存
	 * 
	 * @param mUrl
	 * @param bitmap
	 */
	public void writeBitmap2Local(String mUrl, Bitmap bitmap) {
		// cache
		File cacheDir = mContext.getCacheDir();
		File file = new File(cacheDir,
				mUrl.substring(mUrl.lastIndexOf('/') + 1));

		try {
			bitmap.compress(CompressFormat.JPEG, 100,
					new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
