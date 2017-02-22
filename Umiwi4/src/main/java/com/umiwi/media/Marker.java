package com.umiwi.media;

import com.umiwi.ui.http.parsers.StringParser;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.util.CommonHelper;
import com.umiwi.ui.util.PlayRecordHandler;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.PostRequest;

public class Marker {

    private int[] markPoints = new int[25];
    private VideoModel mVideo;
    private boolean marked;

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int m : markPoints) {
            sb.append(m);
            sb.append("|");
        }
        return sb.toString();
    }

    public void setVideo(VideoModel video) {
        this.mVideo = video;
    }

    public void reset() {
        for (int i = 0; i < 25; i++) {
            markPoints[i] = 0;
        }
    }

    // 0 - 100;
    public void markPercent(int p) {
        if (p < 0 || p >= 100) {
            return;
        }
        marked = true;
        markPoints[p / 4] |= (1 << (p % 4));
    }

    private String generateRecord(String uid, long startTime, long endTime,
                                  long currentSeconds) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 25; i++) {
            sb.append(String.format("%x", markPoints[i]));
        }
        String mac = CommonHelper.getMacMD5();
        int source = mVideo.isLocalFileValid() ? 2 : 1;//TODO
        return mac + "|" + uid + "," + mVideo.getVideoId()
                + "," + startTime / 1000 + "," + endTime / 1000 + ","
                + (endTime - startTime) / 1000 + "," + currentSeconds + ","
                + sb.toString() + "," + mVideo.getAlbumId() + "," + source + ";";
    }

    public void saveAndUpload(String uid, long startTime, long endTime,
                              long currentSeconds) {

        final PlayRecordHandler myFile = new PlayRecordHandler(UmiwiApplication.getContext());
        myFile.savePlayRecordes(generateRecord(uid, startTime, endTime, currentSeconds));
        if (myFile.isFileExists()) {// 如果文件存在的话，获得文件里面的字符串
            final String strText = myFile.readTxt();// 获得txt文件里面的字符串
            if (strText != null) {// 如果txt文件里面的有字符串的话，提交到后台去
                PostRequest<String> request = new PostRequest<String>(UmiwiAPI.PLAY_RECORDS, StringParser.class, listener);
                request.addParam("log", strText);
                request.addParam("os", "&os=androidv");
                request.go();
                request.setTag(myFile);
            }
        }
    }

    private String generateRecord(String uid, long startTime, long endTime, long bufferTime, long currentSeconds) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 25; i++) {
            sb.append(String.format("%x", markPoints[i]));
        }
        String mac = CommonHelper.getMacMD5();
        int source = mVideo.isLocalFileValid() ? 2 : 1;//TODO
        return mac + "|" + uid + "," + mVideo.getVideoId()
                + "," + startTime / 1000 + "," + endTime / 1000 + ","
                + ((endTime - startTime - bufferTime) / 1000) + "," + currentSeconds + ","
                + sb.toString() + "," + mVideo.getAlbumId() + "," + source + ";";
    }

    public void saveAndUpload(String uid, long startTime, long endTime, long bufferTime, long currentSeconds) {
        final PlayRecordHandler myFile = new PlayRecordHandler(UmiwiApplication.getContext());
        myFile.savePlayRecordes(generateRecord(uid, startTime, endTime, bufferTime, currentSeconds));
        if (myFile.isFileExists()) {// 如果文件存在的话，获得文件里面的字符串
            final String strText = myFile.readTxt();// 获得txt文件里面的字符串
            if (strText != null) {// 如果txt文件里面的有字符串的话，提交到后台去
                PostRequest<String> request = new PostRequest<String>(UmiwiAPI.PLAY_RECORDS, StringParser.class, listener);
                request.addParam("log", strText);
                request.addParam("os", "&os=androidv");
                request.go();
                request.setTag(myFile);
            }
        }
    }

    private static Listener<String> listener = new Listener<String>() {
        @Override
        public void onResult(AbstractRequest<String> request, String t) {
            PlayRecordHandler myFile = (PlayRecordHandler) request.getTag();
            if (t.equals("succ")) {
                myFile.deleteText();
            }
        }

        @Override
        public void onError(AbstractRequest<String> requet, int statusCode,
                            String body) {

        }
    };
}
