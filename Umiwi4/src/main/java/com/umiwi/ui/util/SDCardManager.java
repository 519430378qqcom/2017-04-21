package com.umiwi.ui.util;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.main.YoumiConfiguration;
import com.umiwi.video.utils.PreferenceUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import cn.youmi.framework.util.AndroidSDK;


/**
 * Created by tangixyong on 16/1/14.
 */
public class SDCardManager {

    public final static String DOWNLOAD_FILE_PATH = "download_file_path";


    private String sdPath;
    private long nSDTotalSize;
    private long nSDFreeSize;

    public SDCardManager(String sdPath) {
        this.sdPath = sdPath;
        init();
    }

    @SuppressWarnings("deprecation")
    private void init() {
        try {
            StatFs statFs = new StatFs(sdPath);
            long totalBlocks = statFs.getBlockCount();// 区域块数
            long availableBlocks = statFs.getAvailableBlocks();// 可利用区域块数
            long blockSize = statFs.getBlockSize();// 每个区域块大小
            nSDTotalSize = totalBlocks * blockSize;
            nSDFreeSize = availableBlocks * blockSize;
        } catch (Exception e) {

        }
    }

    /**
     * 是否存在
     *
     * @return
     */
    public boolean exist() {
        return nSDTotalSize == 0 ? false : true;
    }

    /**
     * 总空间
     *
     * @return
     */
    public long getTotalSize() {
        return nSDTotalSize;
    }

    /**
     * 剩余空间
     *
     * @return
     */
    public long getFreeSize() {
        return nSDFreeSize;
    }

    /**
     * 递归取得文件夹大小
     */
    private static long getFileSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            File files[] = f.listFiles();
            if (files != null) {
                for (int i = 0, n = files.length; i < n; i++) {
                    if (files[i].isDirectory()) {
                        size = size + getFileSize(files[i]);
                    } else {
                        size = size + files[i].length();
                    }
                }
            }
        } else {
            size = f.length();
        }
        return size;
    }

    public static String convertStorage(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        long tb = gb * 1024;

        if (size >= tb) {
            return String.format("%.1f TB", (float) size / tb);
        } else if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getDefauleSDCardPath() {
        return hasSDCard() ? Environment.getExternalStorageDirectory().getAbsolutePath() : "";
    }

    /**
     * 当前存储路径是否可用
     */
    public static Boolean isStorageAvailable(String path) {
        if (path != null) {
            File file = new File(path);
            if (file.canWrite()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当前存储路径是否有足够的空间
     */
    public static Boolean isSotrageSpaceEnough() {
        return getAvailableExternalMemorySize(getDownloadPath()) > 200 * 1024 * 1024;
    }

    private long getVideoSpace() {
        File f = new File(sdPath + YoumiConfiguration.getDownloadPath());
        return f.exists() ? getFileSize(f) : 0;
    }

    public static String getDownloadPath() {
        return new File(PreferenceUtil.getPreference(UmiwiApplication.getContext(), DOWNLOAD_FILE_PATH) + YoumiConfiguration.getDownloadPath()).getAbsolutePath();
    }

    public static void createDownloadPath() {
        if (hasSDCard()) {
            PreferenceUtil.savePreference(UmiwiApplication.getContext(), DOWNLOAD_FILE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
            mkdir(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + YoumiConfiguration.getDownloadPath()));
        }
    }


    public static void mkdir(File file) {
        if (!file.exists() || !file.isDirectory()) {
            if (!file.mkdirs()) {
                return;
            }
            try {
                new File(file, ".nomedia").createNewFile();
            } catch (IOException e) {
            }
        }

    }

    /**
     * 获取存储路径剩余存储空间
     */
    @SuppressWarnings("deprecation")
    public static long getAvailableExternalMemorySize(String path) {
        File pathFile = new File(path);

        try {
            android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
            long blockSize = 0;
            long availableBlocks = 0;
            if (AndroidSDK.isJELLY_BEAN_MR2()) {
                blockSize = statfs.getBlockSizeLong();
                availableBlocks = statfs.getAvailableBlocksLong();
            } else {
                blockSize = statfs.getBlockSize();
                availableBlocks = statfs.getAvailableBlocks();
            }
            return availableBlocks * blockSize;
        } catch (IllegalArgumentException e) {

        }

        return -1;
    }

    public static void deleteFile(String filepath) {

        if (filepath == null) {
            return;
        }
        File file = new File(filepath);
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            return;
        }
        file.delete();
        return;
    }

    /**
     * 计算 SDCard 总容量大小MB
     */
    public static String getStorageTotal(String path) {

        File pathFile = new File(path);

        try {
            android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
            // 获取SDCard上BLOCK总数
            long nTotalBlocks = 0;
            if (AndroidSDK.isJELLY_BEAN_MR2()) {
                nTotalBlocks = statfs.getBlockCountLong();
            } else {
                nTotalBlocks = statfs.getBlockCount();
            }
            // 获取SDCard上每个block的SIZE
            long nBlocSize = 0;
            if (AndroidSDK.isJELLY_BEAN_MR2()) {
                nBlocSize = statfs.getBlockSizeLong();
            } else {
                nBlocSize = statfs.getBlockSize();
            }

            // 获取可供程序使用的Block的数量
//			long nAvailaBlock = statfs.getAvailableBlocks();

            // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
//			long nFreeBlock = statfs.getFreeBlocks();

            return convertStorage(nTotalBlocks * nBlocSize);
        } catch (IllegalArgumentException e) {
        }

        return null;

    }

    /**
     * 计算 SDCard 剩余大小MB
     */
    public static String getStorageFree(String path) {

        File pathFile = new File(path);

        try {
            android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
            // 获取SDCard上BLOCK总数
//			long nTotalBlocks = statfs.getBlockCount();

            // 获取SDCard上每个block的SIZE
            long nBlocSize = 0;
            if (AndroidSDK.isJELLY_BEAN_MR2()) {
                nBlocSize = statfs.getBlockSizeLong();
            } else {
                nBlocSize = statfs.getBlockSize();
            }
            // 获取可供程序使用的Block的数量
            long nAvailaBlock = 0;
            if (AndroidSDK.isJELLY_BEAN_MR2()) {
                nAvailaBlock = statfs.getAvailableBlocksLong();
            } else {
                nAvailaBlock = statfs.getAvailableBlocks();
            }
            // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
//			long nFreeBlock = statfs.getFreeBlocks();

            return convertStorage(nAvailaBlock * nBlocSize);
        } catch (IllegalArgumentException e) {
        }

        return null;

    }

    /**
     * 存储已使用千分比
     */
    public static int getStoragePerce(String path) {

        File pathFile = new File(path);

        try {
            android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
            // 获取SDCard上BLOCK总数
            long nTotalBlocks = statfs.getBlockCount();

            // 获取SDCard上每个block的SIZE
            long nBlocSize = statfs.getBlockSize();

            // 获取可供程序使用的Block的数量
            long nAvailaBlock = statfs.getAvailableBlocks();

            // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
            long nFreeBlock = statfs.getFreeBlocks();
            return (int) (((double) (nTotalBlocks * nBlocSize - nAvailaBlock * nBlocSize) / (double) (nTotalBlocks * nBlocSize)) * 1000);
        } catch (IllegalArgumentException e) {
        }
        return 0;

    }

    /**
     * 获得外部存储路径
     *
     * @return /mnt/sdcard或者/storage/extSdCard等多种名称
     */
    @SuppressLint("NewApi")
    public static ArrayList<SDCardInfo> getExternalStorageDirectory() {
        ArrayList<SDCardInfo> list = new ArrayList<SDCardInfo>();
        if (AndroidSDK.isKK()) {
            // OS 4.4 以上读取外置 SD 卡
            final File[] externalFiles = UmiwiApplication.getContext().getExternalFilesDirs(null);
            if (null != externalFiles) {
                SDCardInfo info = new SDCardInfo();
                info.path = getDefauleSDCardPath();
                info.isExternal = false;
                list.add(info);
                if (externalFiles.length > 1 && (null != externalFiles[1])) {
                    SDCardInfo externalInfo = new SDCardInfo();
                    externalInfo.path = externalFiles[1].getAbsolutePath();
                    externalInfo.isExternal = true;
                    list.add(externalInfo);
                }
            }
            return list;
        } else {
            Runtime runtime = Runtime.getRuntime();
            Process proc;
            try {
                proc = runtime.exec("mount");
                // InputStream is = Youku.context.getAssets().open("mount.txt");
                InputStream is = proc.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                String line;
                // String mount = new String();
                BufferedReader br = new BufferedReader(isr);
                String defauleSDCardPath = getDefauleSDCardPath();
                //用于盛放已经判断过的路径，省时高效
                HashMap<String, Integer> tempPath = new HashMap<String, Integer>();
                while ((line = br.readLine()) != null) {
//					 Logger.d("nathan2", line);
                    // "fat"为不可卸载的；"fuse"为可卸载的；但是有的手机不适用，所以统一去处理;storage为一些三星手机的存储路径标识
                    if (line.contains("fat") || line.contains("fuse")
                            || line.contains("storage")) {
                        if (line.contains("secure") || line.contains("asec")
                                || line.contains("firmware")
                                || line.contains("shell")
                                || line.contains("obb")
                                || line.contains("legacy")
                                || line.contains("data")
                                || line.contains("tmpfs")) {

                            continue;
                        }
                        String columns[] = line.split(" ");
                        for (int i = 0; i < columns.length; i++) {
                            String path = columns[i];
                            //一加手机的"/dev/fuse"是假地址
                            if (path.contains("/") && !path.contains("data") && !path.contains("Data")
                                    && !path.contains("/dev/fuse")) {
                                try {
                                    if (tempPath.containsKey(path)) {
                                        continue;
                                    } else {
                                        tempPath.put(path, 0);
                                        SDCardManager m = new SDCardManager(path);
                                        if (m.getTotalSize() >= 1024 * 1024 * 1024) {
                                            SDCardInfo info = new SDCardInfo();
                                            info.path = columns[i];
                                            info.isExternal = !info.path.equals(defauleSDCardPath);
                                            list.add(info);
                                        }
                                    }
                                } catch (Exception e) {
                                    continue;
                                }
                            }
                        }
                    }
                }
                tempPath.clear();
                if (list.size() == 1) {
                    if (!defauleSDCardPath.equals(list.get(0).path)) {
                        SDCardInfo info = new SDCardInfo();
                        info.path = defauleSDCardPath;
                        info.isExternal = false;
                        list.add(info);
                    } else {
                        PreferenceUtil.savePreference(UmiwiApplication.getContext(), DOWNLOAD_FILE_PATH, defauleSDCardPath);
                    }
                } else if (list.size() == 0) {
                    if (defauleSDCardPath != null
                            && defauleSDCardPath.length() != 0) {
                        SDCardInfo info = new SDCardInfo();
                        info.path = defauleSDCardPath;
                        info.isExternal = false;
                        list.add(info);
                    }
                }
                if (list.size() > 1) {
                    Set<SDCardInfo> s = new TreeSet<SDCardInfo>(
                            new Comparator<SDCardInfo>() {

                                @Override
                                public int compare(SDCardInfo o1, SDCardInfo o2) {
                                    return o1.path.compareTo(o2.path);
                                }

                            });
                    s.addAll(list);
                    list = new ArrayList<SDCardInfo>(s);
                }
                return list;
            } catch (IOException e) {

            }
            return null;

        }
    }

    public static class SDCardInfo {

        /**
         * 路径/mnt/sdcard或者/storage/extSdCard等多种名称
         */
        public String path;

        /**
         * 是否是外部存储
         */
        public boolean isExternal;
    }
}
