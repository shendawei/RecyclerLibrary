package com.shendawei.recycler.library.adapter.style;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.shendawei.recycler.library.adapter.base.BaseRecyclerAdapter;

import java.util.List;

/**
 * *
 *
 * @author shendawei
 * @classname AbsStyleAdapter
 * @date 12/14/22 1:33 AM
 */
public abstract class AbsStyleAdapter<T extends StyleModel<?>, V extends StyleHolder<T, S>, S extends StyleCallback> extends BaseRecyclerAdapter<T, V, S> {

    protected SparseArray<StyleHolder.Factory<T, S>> mFactories;

    public AbsStyleAdapter(S hcb) {
        super(hcb);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StyleHolder.Factory<T, S> factory = mFactories.get(viewType);
        if (factory == null) {
            throw new RuntimeException("No factory found with type equals " + viewType);
        }
        return (V) factory.createViewHolder(parent, viewType, mHolderCb);
    }

    @Override
    public void onBindViewHolder(@NonNull V holder, int position) {
        holder.bindData(mDataSource.get(position), position);
    }

    @Override
    public void onBindViewHolder(@NonNull V holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (payloads.get(0) != null) {
                holder.bindData(mDataSource.get(position), position, payloads.get(0));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        T model = mDataSource.get(position);
        return model != null ? model.itemType : super.getItemViewType(position);
    }
}
