package com.sovegetables.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

interface IWrapAdapter {
    boolean isHeaderView(int position);

    boolean isFooterView(int position);

    void addHeaderView(View view);

    void setEmptyView(View view);

    void removeAllFooterView();

    void addFooterView(View view);

    boolean hasFooter();

    void removeHeaderView(View view);

    void removeFooterView(View view);

    RecyclerView.Adapter getWrapAdapter();
}
