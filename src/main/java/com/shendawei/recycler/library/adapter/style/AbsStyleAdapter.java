package com.shendawei.recycler.library.adapter.style;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.shendawei.recycler.library.adapter.base.BaseRecyclerAdapter;

import java.util.List;

/**
 * 支持功能
 * 数据源：StyleModel<?>类
 * 事件回调：StyleCallback子接口，传递给StyleHolder子类调用；在adapter创建时声明，一般由activity或Fragment或Dialog实现。
 * holder
 *  正常创建方式：因内置Factory机制，通过{@link #generateFactories()}
 *  手动创建方式：子类onCreateViewHolder，传统方式new holder
 * 基本方法：【增】【删】【改】维护
 *
 * @author shendawei
 * @classname AbsStyleAdapter
 * @date 12/14/22 1:33 AM
 */
public abstract class AbsStyleAdapter<S extends StyleCallback> extends BaseRecyclerAdapter<StyleModel<?>, StyleHolder<StyleModel<?>, S>, S> {

    //mFactories = null
    //BaseRecyclerAdapter.mFactories = "{}"
//    protected SparseArray<StyleHolder.Factory<? extends StyleModel<?>, S>> mFactories = new SparseArray<>();

    public AbsStyleAdapter(S hcb) {
        super(hcb);
    }

    @NonNull
    @Override
    public StyleHolder<StyleModel<?>, S> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StyleHolder.Factory<StyleModel<?>, S> factory = (StyleHolder.Factory<StyleModel<?>, S>) mFactories.get(viewType);
        if (factory == null) {
            throw new RuntimeException("No factory found with type equals " + viewType);
        }
        return factory.createViewHolder(parent, viewType, mHolderCb);
    }

    @Override
    public void onBindViewHolder(@NonNull StyleHolder<StyleModel<?>, S> holder, int position) {
        holder.bindData(mDataSource.get(position), position);
    }

    @Override
    public void onBindViewHolder(@NonNull StyleHolder<StyleModel<?>, S> holder, int position, @NonNull List<Object> payloads) {
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
        StyleModel<?> model = mDataSource.get(position);
        return model != null ? model.itemType : super.getItemViewType(position);
    }
}
