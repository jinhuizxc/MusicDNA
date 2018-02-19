package com.example.jh.musicdna.itemtouchhelpers;

/**
 * Created by jinhui on 2018/2/19.
 * Email:1004260403@qq.com
 */

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
