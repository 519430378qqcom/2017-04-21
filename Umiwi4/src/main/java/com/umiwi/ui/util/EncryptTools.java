package com.umiwi.ui.util;

import com.umiwi.ui.model.AudioModel;
import com.umiwi.ui.model.VideoModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

public class EncryptTools {

	public static void encriptVideoFile(VideoModel video) {
		if (video == null || video.getFilePath() == null) {
			return;
		}
		File f = new File(video.getFilePath());
		encriptVideoFile(video, f);
	}
	public static void encriptAudioFile(AudioModel video) {
		if (video == null || video.getFilePath() == null) {
			return;
		}
		File f = new File(video.getFilePath());
		encriptAudioFile(video, f);
	}
	public static void encriptVideoFile(VideoModel video, File srcFile) {
		try {
			byte[] buffer = new byte[34];
			byte[] outBuffer = new byte[34];
			FileInputStream fis = new FileInputStream(srcFile);
			int readen = fis.read(buffer, 0, 34);
			if (readen == 34) {
				for (int i = 0; i < buffer.length; i++) {
					outBuffer[i] = buffer[buffer.length - 1 - i];
				}
			}
			fis.close();

			RandomAccessFile raf = new RandomAccessFile(srcFile, "rws");
			raf.seek(0);
			raf.write(outBuffer);
			raf.close();
			srcFile.setReadable(true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void encriptAudioFile(AudioModel audio, File srcFile) {
		try {
			byte[] buffer = new byte[34];
			byte[] outBuffer = new byte[34];
			FileInputStream fis = new FileInputStream(srcFile);
			int readen = fis.read(buffer, 0, 34);
			if (readen == 34) {
				for (int i = 0; i < buffer.length; i++) {
					outBuffer[i] = buffer[buffer.length - 1 - i];
				}
			}
			fis.close();

			RandomAccessFile raf = new RandomAccessFile(srcFile, "rws");
			raf.seek(0);
			raf.write(outBuffer);
			raf.close();
			srcFile.setReadable(true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void decodeVideoFile(VideoModel video) {
		File f = new File(video.getFilePath());
		try {
			byte[] buffer = new byte[34];
			byte[] outBuffer = new byte[34];
			FileInputStream fis = new FileInputStream(f);
			int readen = fis.read(buffer, 0, 34);

			boolean needDecoding = true;
			if (readen == 34) {
				for (int i = 0; i < buffer.length; i++) {
					outBuffer[i] = buffer[buffer.length - 1 - i];
					char c = (char) buffer[i];
					if (c == 'm') {
						if (buffer[i + 1] == 'p' && buffer[i + 2] == '4') {
							needDecoding = false;
							break;
						}
					}
				}
			}
			fis.close();
			if (needDecoding) {
				RandomAccessFile raf = new RandomAccessFile(f, "rws");
				raf.seek(0);
				raf.write(outBuffer);
				raf.getFD().sync();
				raf.close();
			}
			f.setReadable(true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 解密 并判断是否完成 */
	public static boolean decryptboolean(File f, String password) {
		RandomAccessFile file;
		try {
			file = new RandomAccessFile(f, "rw");
			FileLock fl = file.getChannel().tryLock(0, 1, false);

			if (fl != null) {
				byte[] bPassword = password.getBytes();
				file.seek(0);
				file.write(bPassword);
				fl.release();
			}

			file.close();

			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
