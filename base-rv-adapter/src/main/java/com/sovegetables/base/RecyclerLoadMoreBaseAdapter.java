package com.sovegetables.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sovegetables.base_rv_adapter.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerLoadMoreBaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ILoadMoreListener {

    private final int VIEW_TYPE_ITEM = 100;
    private final int VIEW_TYPE_LOADING = 111;
    public Context mContext;
    public List<T> entities = new ArrayList<>();
    public int pageLength = 20;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private boolean isLoadEnd;
    private boolean isEnableLoadMore = false;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private RecyclerView mRecyclerView;
    private View emptyView;

    public RecyclerLoadMoreBaseAdapter(Context context, RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        this.mContext = context;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (isEnableLoadMore && !isLoading && totalItemCount <= (lastVisibleItem + 1 + visibleThreshold)) {
                    if (isLoadEnd == false && mOnLoadMoreListener != null) {
                        if (entities.size() % pageLength == 0) {
                            mOnLoadMoreListener.onLoadMore();
                            isLoading = true;
                            notifyDataSetChanged();
                        } else {
                            isLoadEnd = true;
                            notifyDataSetChanged();
                        }

                    }
                }
            }
        });
    }

    public List<T> getEntities() {
        return entities;
    }

    @Override
    public void addFirstPagerData(List data) {
        entities.clear();
        entities.addAll(data);
        notifyDataChanged();
    }

    @Override
    public void addLoadMoreData(List data) {
        entities.addAll(data);
        notifyDataChanged();
    }

    public int getDataSize() {
        return entities == null ? 0 : entities.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.foot_loading_more, parent, false);
            return new LoadingViewHolder(view);
        } else {
            return onMyCreateViewHolder(parent, viewType);
        }
    }

    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public void onMyBindView(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == entities.size()) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;

            if (isEnableLoadMore) {
                if (isLoading) {
                    loadingViewHolder.textView.setText("正在加载…");
                    loadingViewHolder.textView.setVisibility(View.VISIBLE);

                }
                if (isLoadEnd) {
                    loadingViewHolder.textView.setVisibility(View.VISIBLE);
                    loadingViewHolder.textView.setText("没有更多了");
                }
            } else {
                loadingViewHolder.textView.setVisibility(View.GONE);
            }

        } else {
            onMyBindView(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (entities == null || entities.size() == 0) {
            return 0;
        } else {
            return (entities.size() + 1);
        }
    }

    public int getPagerLength() {
        return pageLength;
    }

    public void setPagerLength(int limit) {
        this.pageLength = limit;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void onLoadEnd() {
        isLoadEnd = true;
        notifyDataSetChanged();
    }

    public void enableLoadMore() {
        isEnableLoadMore = true;
        isLoadEnd = false;
        notifyDataSetChanged();
    }

    public void disableLoadMore() {
        isEnableLoadMore = false;
        notifyDataSetChanged();
    }

    public void onLoadingMoreComplete() {
        this.isLoading = false;
        notifyDataSetChanged();
    }

    public int getMyViewType(int position) {
        return VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == entities.size()) {
            return VIEW_TYPE_LOADING;
        } else {
            return getMyViewType(position);
        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    @Override
    public void notifyDataChanged() {
        if (entities != null) {
            if (emptyView != null) {
                if (entities.size() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    emptyView.setVisibility(View.GONE);
                }
            }
            isLoadEnd = entities.size() % pageLength != 0;
        }
        this.notifyDataSetChanged();
        if (getItemCount() > 0) {
            this.notifyItemChanged(getItemCount() - 1);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
