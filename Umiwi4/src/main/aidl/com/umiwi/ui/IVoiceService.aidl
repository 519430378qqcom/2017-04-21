// IVoiceService.aidl
package com.umiwi.ui;

// Declare any non-default types here with import statements

interface IVoiceService {
    /**
         * 音频播放
         */
        void play();
    
        /**
         * 音频暂停
         */
        void pause();
    
        /**
         * 根据URL 播放音乐
         * @param url
         */
        void openAudio(String url);
    
        /**
         * 得到音频的总时长
         * @return
         */
        int getDuration();
    
        /**
         * 得到音频播放的当前进度
         * @return
         */
        int getCurrentPosition();
    
        /**
         * 得到音频名称
         * @return
         */
        String getAudioName();
    
        /**
         * 是否在播放
         * @return
         */
        boolean isPlaying();
    
        /**
         * 得到播放地址
         * @return
         */
        String getAudioPath();
    
        /**
         * 播放下一个
         */
        void next() ;
    
        /**
         * 播放上一个
         */
        void pre();


        void seekTo(int position);


}
