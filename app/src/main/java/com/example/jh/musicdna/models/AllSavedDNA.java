package com.example.jh.musicdna.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class AllSavedDNA {

    List<SavedDNA> savedDNAs;

    public AllSavedDNA() {
        savedDNAs = new ArrayList<>();
    }

    public List<SavedDNA> getSavedDNAs() {
        return savedDNAs;
    }

    public void setSavedDNAs(List<SavedDNA> savedDNAs) {
        this.savedDNAs = savedDNAs;
    }

}
