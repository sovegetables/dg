package com.sovegetables.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class ListAdapterDelegate<T> extends AdapterDelegate<List<T>> {

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        LazyRecyclerViewHolder holder = new LazyRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
        if(getOnItemClickListener() != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    T item = getItemByTag(v);
                    if(item != null){
                        getOnItemClickListener().onItemClick(v, item, getPositionByTag(v));
                    }
                }
            });
        }
        onViewCreated(parent, holder);
        return holder;
    }

    @Override
    protected final void onBindViewHolder(List<T> items, @NonNull RecyclerView.ViewHolder holder, int position, List payloads) {
        T t = items.get(position);
        setItemTag(holder, t);
        setPositionTag(holder, position);
        onBindView(((LazyRecyclerViewHolder) holder), t, position, payloads);
    }

    @Override
    protected void onBindViewHolder(List<T> items, @NonNull RecyclerView.ViewHolder holder, int position) {
        onBindViewHolder(items, holder, position, new ArrayList());
    }

    protected void onBindView(LazyRecyclerViewHolder holder, T t, int position, List payloads){
        onBindView(holder, t, position);
    }

    /**
     * invoked after onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
     * @param holder LazyRecyclerViewHolder
     * @param t data resource item
     */
    protected abstract void onBindView(LazyRecyclerViewHolder holder, T t, int position);

    /**
     * invoked after onCreateViewHolder(@NonNull ViewGroup parent)
     * @param parent ViewGroup
     * @param holder LazyRecyclerViewHolder
     */
    protected void onViewCreated(ViewGroup parent, LazyRecyclerViewHolder holder){}

    /**
     * recycler view item layout resource
     * @return layout resource
     */
    @LayoutRes
    protected abstract int getLayoutRes();
}
