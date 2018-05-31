package com.anandniketanbhadaj.skool360.skool360.Activities;

import android.support.v7.widget.RecyclerView;

import com.anandniketanbhadaj.skool360.skool360.Adapter.HolidayListAdapter;

/**
 * Created by bobbyadiprabowo on 11/15/15.
 */
public class ParallaxScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        ((HolidayListAdapter)recyclerView.getAdapter()).reTranslate();
    }
}
