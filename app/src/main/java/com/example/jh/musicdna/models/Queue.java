package com.example.jh.musicdna.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class Queue {

    private List<UnifiedTrack> queue;

    public Queue() {
        queue = new ArrayList<>();
    }

    public List<UnifiedTrack> getQueue() {
        return queue;
    }

    public void setQueue(List<UnifiedTrack> queue) {
        this.queue = queue;
    }

    public void addToQueue(UnifiedTrack track) {
        queue.add(track);
    }

    public void removeItem(UnifiedTrack ut) {
        for (int i = 0; i < queue.size(); i++) {
            if (ut.equals(queue.get(i))) {
                queue.remove(i);
                break;
            }
        }
    }

}
