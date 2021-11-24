package com.sovegetables.base;

import java.util.List;

public interface ILoadMoreListener<T> {
    void setOnLoadMoreListener(RecyclerLoadMoreBaseAdapter.OnLoadMoreListener mOnLoadMoreListener);

    void onLoadEnd();

    void enableLoadMore();

    void disableLoadMore();

    void onLoadingMoreComplete();

    void setPagerLength(int limit);

    void notifyDataChanged();

    List<T> getEntities();

    void addFirstPagerData(List<T> data);

    void addLoadMoreData(List<T> data);
}
