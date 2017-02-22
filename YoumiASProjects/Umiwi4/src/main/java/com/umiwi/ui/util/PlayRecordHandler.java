package com.umiwi.ui.util;

import android.content.Context;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 文件保存到本项目目录txt文件里面的类
 *
 * @author tangxiyong 2014-1-23下午4:35:09
 */
public class PlayRecordHandler {
    /**
     * 文件夹名字
     */
    public static final String MY_DIR = "playrecordes";
    /**
     * file name
     */
    public static final String PLAY_RECORDES_FILE = "play_recordes.txt";

    private File myDir;
    private File playTxt;

    /**
     * 查看记录文件是否存在
     *
     * @return
     */
    public boolean isFileExists() {
        return playTxt.exists();
    }

    public PlayRecordHandler(Context context) {
        myDir = context.getDir(MY_DIR, Context.MODE_PRIVATE);

        playTxt = new File(myDir, PLAY_RECORDES_FILE);
    }

    // 保存播放记录到文件里面
    public void savePlayRecordes(String str) {

        try {
            if (!playTxt.exists()) {
                playTxt.createNewFile();
            }

            FileWriter fw = new FileWriter(playTxt, true);

            fw.write(str);
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取本地的文件
     *
     * @return
     */
    public String readTxt() {
        String rds = null;
        try {
            FileInputStream stream = new FileInputStream(playTxt);
            InputStreamReader isReader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bReader = new BufferedReader(isReader);
            String line = null;
            while ((line = bReader.readLine()) != null) {
                rds = line.trim();
            }
            bReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rds;
    }

    /**
     * 删除本地的文件
     */
    public void deleteText() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (playTxt.exists()) {// 如果文件存在
                    playTxt.delete();// 删除文件
                    try {// 建立新的文件
                        playTxt.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

}