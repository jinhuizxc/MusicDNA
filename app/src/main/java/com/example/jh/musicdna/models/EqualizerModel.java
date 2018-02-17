package com.example.jh.musicdna.models;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class EqualizerModel {

    public boolean isEqualizerEnabled;
    int[] seekbarpos = new int[5];
    int presetPos;
    short reverbPreset = -1, bassStrength = -1;

    public EqualizerModel() {
        isEqualizerEnabled = true;
        reverbPreset = -1;
        bassStrength = -1;
    }

    public boolean isEqualizerEnabled() {
        return isEqualizerEnabled;
    }

    public void setEqualizerEnabled(boolean equalizerEnabled) {
        isEqualizerEnabled = equalizerEnabled;
    }

    public int[] getSeekbarpos() {
        return seekbarpos;
    }

    public void setSeekbarpos(int[] seekbarpos) {
        this.seekbarpos = seekbarpos;
    }

    public int getPresetPos() {
        return presetPos;
    }

    public void setPresetPos(int presetPos) {
        this.presetPos = presetPos;
    }

    public short getReverbPreset() {
        return reverbPreset;
    }

    public void setReverbPreset(short reverbPreset) {
        this.reverbPreset = reverbPreset;
    }

    public short getBassStrength() {
        return bassStrength;
    }

    public void setBassStrength(short bassStrength) {
        this.bassStrength = bassStrength;
    }

}
