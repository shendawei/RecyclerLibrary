package com.shendawei.recycler.library.adapter.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author shendawei
 * @classname BaseHolder
 * @description
 * @date 2020/7/17 20:16
 */
public abstract class BaseHolder<T, Cb extends HolderCallback> extends RecyclerView.ViewHolder {

    protected Context context;
    protected LayoutInflater inflater;
    protected T item;
    protected int position;
    protected Cb holderCallback;
    private final SparseArray<View> views;

    public BaseHolder(View itemView, Cb holderCb) {
        super(itemView);
        context = itemView.getContext();
        inflater = LayoutInflater.from(context);
        views = new SparseArray<>();
        holderCallback = holderCb;
        initView();
    }

    public abstract void initView();

    public void bindData(@NonNull T item, int position) {
        this.item = item;
        this.position = position;
    }


    public void bindData(@NonNull T item, int position, Object payload) {
        this.item = item;
//        setBackground(getCornerType());
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V getView(int viewId) {
        View child = views.get(viewId);
        if (child == null) {
            child = itemView.findViewById(viewId);
            if (child == null) {
                throw new RuntimeException("Can't find child view of viewId : " + viewId + " in OrBaseHolder!");
            }
            views.put(viewId, child);
        }
        return (V) child;
    }

    public T getItem() {
        return item;
    }

    protected void toast(String invalidMsg) {
        //ToastUtils.showToast(invalidMsg);
    }

    public abstract static class Factory<T, C extends HolderCallback> {

        public BaseHolder<T, C> createViewHolder(ViewGroup parent, int viewType, C holderCallback) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return onCreateViewHolder(layoutInflater.inflate(getLayoutId(), parent, false), holderCallback);
        }

        protected abstract int getLayoutId();

        @NonNull
        protected abstract BaseHolder<T, C> onCreateViewHolder(@NonNull View itemView, C holderCallback);
    }

}
