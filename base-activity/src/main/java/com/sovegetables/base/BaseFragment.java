package com.sovegetables.base;

import android.content.Context;
import com.sovegetables.IContentView;
import io.reactivex.disposables.Disposable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class BaseFragment extends com.sovegetables.BaseFragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Disposable disposable : disposableList) {
            disposable.dispose();
        }
    }

    protected void showLoadingDialog(Context context) {
        showLoadingDialog(null, -1, true, null);
    }
    private final List<Disposable> disposableList = new ArrayList<>();

    public void addDisposable(Disposable disposable) {
        disposableList.add(disposable);
    }

    @Nullable
    @Override
    public IContentView getContentViewDelegate() {
        return new AppContentView();
    }
}
