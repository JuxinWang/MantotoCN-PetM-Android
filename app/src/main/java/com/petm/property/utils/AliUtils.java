package com.petm.property.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.FileDescriptor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AliUtils {

	static final String TAG = "Utils";

	public static final String DIRECTORY;
	public static final ExecutorService SERVICE;

	static {
		SERVICE = Executors.newCachedThreadPool();
		String DCIM = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DCIM).toString();
		DIRECTORY = DCIM + "/Camera";
	}

	/**
	 * 
	 * @param filePath
	 * @param fd
	 * @param timeUs
	 * @return
	 */
	public static Bitmap createVideoThumbnail(String filePath,
			FileDescriptor fd, long timeUs) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			if (filePath != null) {
				retriever.setDataSource(filePath);
			} else {
				retriever.setDataSource(fd);
			}
			bitmap = retriever.getFrameAtTime(timeUs);
		} catch (Throwable throwable) {
			// Assume this is a corrupt video file
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException ex) {
			}
		}
		return bitmap;
	}

}
