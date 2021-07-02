// IPlayerEventDispatcher.aidl
package com.chang.android.aidl.service;

// Declare any non-default types here with import statements

interface IPlayerEventDispatcher {
    void onBufferingStart();
    void onBufferProgress(int percent);
    void onBufferingStop();

    void onSoundPrepared();
    void onPlayStart();
    void onPlayPause();
    void onPlayStop();
    void onSoundPlayComplete();
    void onSoundSwitch();
    void onPlayProgress(int currPos, int duration);
    void onError(int code, String message);
}