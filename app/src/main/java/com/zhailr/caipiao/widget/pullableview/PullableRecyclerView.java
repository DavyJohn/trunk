package com.zhailr.caipiao.widget.pullableview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


public class PullableRecyclerView extends RecyclerView implements Pullable {

    private boolean canPullDown = true;
    private boolean canPullUp = true;

    public PullableRecyclerView(Context context) {
        super(context);
    }

    public PullableRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {

        if(! canPullDown) return false;
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager.getItemCount() == 0) {
                return true;
            }
        }
        if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition() == 0 && ((LinearLayoutManager) layoutManager).findViewByPosition(0).getTop() >= 0) {
                return true;
            }
        }

        if (layoutManager instanceof GridLayoutManager) {
            if (((GridLayoutManager) layoutManager).findFirstVisibleItemPosition() == 0 && ((GridLayoutManager) layoutManager).findViewByPosition(0).getTop() >= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canPullUp() {
        if(! canPullUp) return false;
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager.getItemCount() == 0) {
                return true;
            }
        }

        if (layoutManager instanceof LinearLayoutManager) {
            if(((LinearLayoutManager)layoutManager).findLastVisibleItemPosition() == layoutManager.getItemCount() - 1 && ((LinearLayoutManager)layoutManager).findViewByPosition(layoutManager.getItemCount() - 1).getBottom() <= getMeasuredHeight()){
                return true;
            }
        }

        if (layoutManager instanceof GridLayoutManager) {
            if(((GridLayoutManager)layoutManager).findLastVisibleItemPosition() == layoutManager.getItemCount() - 1 && ((GridLayoutManager)layoutManager).findViewByPosition(layoutManager.getItemCount() - 1).getBottom() <= getMeasuredHeight()){
                return true;
            }
        }
        return false;
    }

    public void setCanPullDown(boolean canPullDown) {
        this.canPullDown = canPullDown;
    }

    public void setCanPullUp(boolean canPullUp) {
        this.canPullUp = canPullUp;
    }

}
