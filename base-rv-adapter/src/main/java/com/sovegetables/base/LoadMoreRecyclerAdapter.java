package com.sovegetables.base;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class LoadMoreRecyclerAdapter extends WrapRecyclerAdapter{

    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean mIsLoading;
    private LoadMoreHelper mLoadMoreHelper;
    private @LayoutRes
    int mLoadMoreLayoutRes = -1;
    private final ArrayMap<Class<? extends RecyclerView.LayoutManager>, LoadMoreHelper> mLoadMoreHelperMap = new ArrayMap<>();
    private View mLoadMoreView;

    public interface OnLoadMoreListener {
        void onLoadMore(LoadMoreRecyclerAdapter adapter);
    }

    public interface LoadMoreHelper{
        boolean isLastPosition();
        void attachRecyclerView(RecyclerView recyclerView);
        void onScrollStateChanged(RecyclerView recyclerView, int newState);
        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    public LoadMoreRecyclerAdapter(RecyclerView.Adapter adapter) {
        super(adapter);
        mLoadMoreHelperMap.put(StaggeredGridLayoutManager.class, new StaggeredGridLayoutManagerLoadMoreHelper());
        mLoadMoreHelperMap.put(LinearLayoutManager.class, new LinearLayoutManagerLoadMoreHelper());
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    public void setLoadMoreLayout(@LayoutRes int loadMoreLayoutRes) {
        this.mLoadMoreLayoutRes = loadMoreLayoutRes;
    }

    public void finishedLoadMore(){
        mIsLoading = false;
        if(mLoadMoreView != null){
            removeFooterView(mLoadMoreView);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if(mOnLoadMoreListener != null) {
            mLoadMoreHelper = mLoadMoreHelperMap.get(recyclerView.getLayoutManager().getClass());
            if(mLoadMoreHelper != null) {
                mLoadMoreHelper.attachRecyclerView(recyclerView);
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        mLoadMoreHelper.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        mLoadMoreHelper.onScrolled(recyclerView, dx, dy);
                        if (!mIsLoading && mLoadMoreHelper.isLastPosition()) {
                            mIsLoading = true;
                            if(mLoadMoreView == null){
                                mLoadMoreView = LayoutInflater.from(recyclerView.getContext()).inflate(mLoadMoreLayoutRes, recyclerView, false);
                            }
                            addFooterView(mLoadMoreView);
                            if (mOnLoadMoreListener != null) {
                                mOnLoadMoreListener.onLoadMore(LoadMoreRecyclerAdapter.this);
                            }
                        }
                    }
                });
            }
        }
    }


    private static class LinearLayoutManagerLoadMoreHelper implements LoadMoreHelper{

        private int mTotalItemCount;
        private int mFirstVisibleItemPosition;
        private int mVisibleItemCount;

        @Override
        public boolean isLastPosition() {
            return mTotalItemCount == mFirstVisibleItemPosition + mVisibleItemCount;
        }

        @Override
        public void attachRecyclerView(RecyclerView recyclerView) {
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            mVisibleItemCount = layoutManager.getChildCount();
            mTotalItemCount = layoutManager.getItemCount();
            mFirstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
        }
    }

    private static class StaggeredGridLayoutManagerLoadMoreHelper implements LoadMoreHelper{

        private int mTotalItemCount;
        private int[] mFirstVisibleItemPositions;
        private int[] mLastCompletelyVisibleItemPositions;
        private int mVisibleItemCount;
        private int mSpanCount;

        @Override
        public boolean isLastPosition() {
//            if(mTotalItemCount % mSpanCount == 0){
//                return mTotalItemCount <= mFirstVisibleItemPositions[mFirstVisibleItemPositions.length - 1] + mVisibleItemCount;
//            }
//            if(mTotalItemCount % mSpanCount != 0){
//                return mTotalItemCount <= mFirstVisibleItemPositions[0] + mVisibleItemCount;
//            }
            if(mTotalItemCount % mSpanCount == 0){
                return mTotalItemCount <= mLastCompletelyVisibleItemPositions[mFirstVisibleItemPositions.length - 1] + 1;
            }
            if(mTotalItemCount % mSpanCount != 0){
                return mTotalItemCount<= mLastCompletelyVisibleItemPositions[0] + 1;
            }
            return false;
        }

        @Override
        public void attachRecyclerView(RecyclerView recyclerView) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            mSpanCount = layoutManager.getSpanCount();
            mFirstVisibleItemPositions = new int[mSpanCount];
            mLastCompletelyVisibleItemPositions = new int[mSpanCount];
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            mVisibleItemCount = layoutManager.getChildCount();
            mTotalItemCount = layoutManager.getItemCount();
            layoutManager.findLastCompletelyVisibleItemPositions(mLastCompletelyVisibleItemPositions);
            layoutManager.findFirstCompletelyVisibleItemPositions(mFirstVisibleItemPositions);
        }
    }
}
