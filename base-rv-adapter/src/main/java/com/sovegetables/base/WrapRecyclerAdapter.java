package com.sovegetables.base;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

/**
 * Created by albert on 2018/5/5.
 */

public class WrapRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IWrapAdapter {

    private RecyclerView.Adapter mAdapter;

    //由于头部和底部可能有多个，需要用标识来识别
    private int BASE_HEADER_KEY = 5500000;
    private int BASE_FOOTER_KEY = 6600000;

    //头部和底部集合 必须要用map集合进行标识 key->int  value->object
    private final SparseArray<View> mHeaderViews;
    private final SparseArray<View> mFooterViews;

    public WrapRecyclerAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                positionStart += getHeaderItemCount();
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                positionStart += getHeaderItemCount();
                notifyItemRangeChanged(positionStart, itemCount, payload);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                positionStart += getHeaderItemCount();
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                positionStart += getHeaderItemCount();
                notifyItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                fromPosition += getHeaderItemCount();
                toPosition += getHeaderItemCount();
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    private int getHeaderItemCount() {
        return mHeaderViews.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isHeaderViewType(viewType)) {
            View headerView = mHeaderViews.get(viewType);
            return createHeaderOrFooterViewHolder(headerView);
        }
        if (isFooterViewType(viewType)) {
            View footerView = mFooterViews.get(viewType);
            //footer
            return createHeaderOrFooterViewHolder(footerView);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }


    /**
     * 创建头部和底部ViewHolder
     *
     * @param view View
     * @return RecyclerView.ViewHolder
     */
    private RecyclerView.ViewHolder createHeaderOrFooterViewHolder(View view) {
        return new HeaderOrFooterViewHolder(view);
    }

    private static class HeaderOrFooterViewHolder extends RecyclerView.ViewHolder{
        HeaderOrFooterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return mHeaderViews.keyAt(position);
        }
        if (isFooterPosition(position)) {
            position = position - mHeaderViews.size() - mAdapter.getItemCount();
            return mFooterViews.keyAt(position);
        }
        position = position - mHeaderViews.size();
        return mAdapter.getItemViewType(position);
    }

    @Override @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        //头部，底部不需要绑定数据
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            if(params instanceof StaggeredGridLayoutManager.LayoutParams){
                ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
            }
        }else {
            position = position - mHeaderViews.size();
            mAdapter.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //头部，底部不需要绑定数据
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            if(params instanceof StaggeredGridLayoutManager.LayoutParams){
                ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
            }
        }else {
            position = position - mHeaderViews.size();
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + mHeaderViews.size() + mFooterViews.size();
    }

    @Override
    public long getItemId(int position) {
        if (isHeaderPosition(position)) {
            return mHeaderViews.get(position).hashCode();
        }
        if (isFooterPosition(position)) {
            return mFooterViews.get(position).hashCode();
        }
        return mAdapter.getItemId(position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            throw new IllegalArgumentException("Not support GridLayoutManager!!");
        }
        mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override @SuppressWarnings("unchecked")
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        if (isHeaderView(position) || isFooterView(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        } else {
            mAdapter.onViewAttachedToWindow(holder);
        }
    }

    @Override @SuppressWarnings("unchecked")
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (isNotHeaderOrFooterHolder(holder)){
            mAdapter.onViewRecycled(holder);
        }
    }

    private boolean isNotHeaderOrFooterHolder(RecyclerView.ViewHolder holder) {
        return !(holder instanceof HeaderOrFooterViewHolder);
    }

    @Override @SuppressWarnings("unchecked")
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        if (isNotHeaderOrFooterHolder(holder)){
            return mAdapter.onFailedToRecycleView(holder);
        }
        return false;
    }

    @Override @SuppressWarnings("unchecked")
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (isNotHeaderOrFooterHolder(holder)){
            mAdapter.onViewDetachedFromWindow(holder);
        }
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        mAdapter.setHasStableIds(hasStableIds);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean isHeaderView(int position) {
        return position >= 0 && position < getHeaderItemCount();
    }

    @Override
    public boolean isFooterView(int position) {
        return position >= getHeaderItemCount() + getContentItemCount();
    }

    private int getContentItemCount() {
        return mAdapter.getItemCount();
    }

    //添加头部，底部
    @Override
    public void addHeaderView(View view) {
        //没有包含头部
        int position = mHeaderViews.indexOfValue(view);
        if (position < 0) {
            //集合没有就添加，不能重复添加
            mHeaderViews.put(BASE_HEADER_KEY++, view);
        }
        notifyDataSetChanged();
    }

    public void removeEmptyView(View view) {
        removeHeaderView(view);
    }

    @Override
    public void setEmptyView(View view){
        int contentItemCount = getContentItemCount();
        if(contentItemCount != 0){
            throw new IllegalArgumentException("please clear adapter data before invoke setEmptyView!!! contentItemCount = " + contentItemCount);
        }
        mHeaderViews.clear();
        addHeaderView(view);
    }

    @Override
    public void removeAllFooterView() {
        mFooterViews.clear();
        notifyDataSetChanged();
    }

    @Override
    public void addFooterView(View view) {
        //没有包含头部
        int position = mFooterViews.indexOfValue(view);
        if (position < 0) {
            //集合没有就添加，不能重复添加
            mFooterViews.put(BASE_FOOTER_KEY++, view);
        }
        notifyDataSetChanged();
    }

    @Override
    public boolean hasFooter() {
        return mFooterViews.size() > 0;
    }

    //移除头部,底部
    @Override
    public void removeHeaderView(View view) {
        //没有包含头部
        int index = mHeaderViews.indexOfValue(view);
        if (index < 0) return;
        //集合没有就添加，不能重复添加
        mHeaderViews.removeAt(index);
        notifyDataSetChanged();
    }

    @Override
    public void removeFooterView(View view) {
        //没有包含底部
        int index = mFooterViews.indexOfValue(view);
        if (index < 0) return;
        //集合没有就添加，不能重复添加
        mFooterViews.removeAt(index);
        notifyDataSetChanged();
    }

    //是否为底部
    private boolean isFooterViewType(int viewType) {
        int footerPosition = mFooterViews.indexOfKey(viewType);
        return footerPosition >= 0;
    }

    //是否为头部
    private boolean isHeaderViewType(int viewType) {
        int headerPosition = mHeaderViews.indexOfKey(viewType);
        return headerPosition >= 0;
    }

    //是否为底部位置
    private boolean isFooterPosition(int position) {
        return position >= (mHeaderViews.size() + mAdapter.getItemCount());
    }

    //是否为头部位置
    private boolean isHeaderPosition(int position) {
        return position < mHeaderViews.size();
    }

    @Override
    public RecyclerView.Adapter getWrapAdapter() {
        return mAdapter;
    }
}
