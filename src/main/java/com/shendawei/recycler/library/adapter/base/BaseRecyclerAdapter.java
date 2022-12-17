package com.shendawei.recycler.library.adapter.base;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 支持功能
 * 数据源：普通T
 * 事件回调：HolderCallback子接口，传递给holder调用；在adapter创建时声明，一般由activity或Fragment或Dialog实现。
 * holder
 *  正常创建方式：因内置Factory机制，通过{@link #generateFactories()}
 *  手动创建方式：子类onCreateViewHolder，传统方式new holder
 * 基本方法：【增】【删】【改】维护
 *
 * 注意事项
 * adapter插入或删除item后需调用notifyItemRangeChanged，更新变化的item信息；
 * 否则position位置是错误的。
 * 参考源码api(item change&structural change)
 *
 * @author shendawei
 * @classname BaseRecyclerAdapter
 * @description
 * @date 2020/5/15 10:28
 */
public abstract class BaseRecyclerAdapter<T, V extends BaseHolder<? extends T, HCb>, HCb extends HolderCallback> extends RecyclerView.Adapter<V> {

    protected SparseArray<V.Factory<? extends T, HCb>> mFactories;
    protected List<T> mDataSource;
    protected HCb mHolderCb;

    public BaseRecyclerAdapter(HCb hcb) {
        mFactories = new SparseArray<>();
        generateFactories();
        mDataSource = new ArrayList<>();
        mHolderCb = hcb;
    }

    /**
     * 子类实现
     * 创建需要的Factory，用以快速createHolder
     */
    protected abstract void generateFactories();

    @Override
    public int getItemCount() {
        return mDataSource == null ? 0 : mDataSource.size();
    }

    @Override
    public long getItemId(int position) {
        if (hasStableIds()) {
            return mDataSource.get(position).hashCode() + position;
        }
        return super.getItemId(position);
    }

    public List<? extends T> getItems() {
        return mDataSource;
    }

    /**
     * 重置数据集
     *
     * @param items
     */
    public void putItems(List<? extends T> items) {
        if (items == null) {
            return;
        }
        if (mDataSource.size() > 0) {
            mDataSource.clear();
            notifyDataSetChanged();
        }

        mDataSource.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 清除数据集
     */
    public void clear() {
        mDataSource.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加一项数据
     *
     * @param item
     */
    public void addItem(T item) {
        mDataSource.add(item);
        notifyItemInserted(mDataSource.size());
        notifyItemChanged(mDataSource.size());
    }

    /**
     * 在指定位置添加数据
     *
     * @param index
     * @param item
     */
    public void addItem(int index, T item) {
        checkDataValid(index);
        mDataSource.add(index, item);
        notifyItemInserted(index);
        notifyItemRangeChanged(index, mDataSource.size() - index);
    }

    /**
     * 替换指定位置数据
     *
     * @param index
     * @param item
     */
    public void replaceItem(int index, T item) {
        checkDataValid(index);
        mDataSource.set(index, item);
        notifyItemChanged(index);
    }

    /**
     * 添加数据集
     *
     * @param items
     */
    public void addItems(List<? extends T> items) {
        int previousIndex = mDataSource.size();
        mDataSource.addAll(items);
        notifyItemRangeInserted(previousIndex, items.size());
        notifyItemRangeChanged(previousIndex, items.size());
    }

    /**
     * 在指定位置添加数据集合
     *
     * @param index
     * @param items
     */
    public void addItems(int index, List<? extends T> items) {
        checkDataValid(index);
        mDataSource.addAll(index, items);
        notifyItemRangeInserted(index, items.size());
        notifyItemRangeChanged(index, mDataSource.size() - index);
    }

    public void changeItem(T item) {
        int change = mDataSource.indexOf(item);
        checkDataValid(change);
        notifyItemChanged(change);
    }

    /**
     * 移除指定数据
     *
     * @param item
     */
    public void removeItem(T item) {
        int removedIndex = mDataSource.indexOf(item);
        checkDataValid(removedIndex);
        mDataSource.remove(item);
        if (removedIndex != -1) {
            notifyItemRemoved(removedIndex);
            notifyItemRangeChanged(removedIndex, mDataSource.size() - removedIndex);
        }
    }

    /**
     * 移除指定位置的数据
     *
     * @param index
     */
    public void removeItem(int index) {
        checkDataValid(index);
        mDataSource.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, mDataSource.size() - index);
    }

    /**
     * 移除指定位置及之后的所有数据
     *
     * @param start
     */
    public void removeItemsFrom(int start) {
        this.removeItemsRange(start, mDataSource.size() - 1);
    }

    /**
     * 移除指定范围的数据
     *
     * @param start
     * @param end
     */
    public void removeItemsRange(int start, int end) {
        checkDataValid(start);
        checkDataValid(end);
        List<T> removedItems = new ArrayList<>();
        for (int i = start; i < end + 1; i++) {
            removedItems.add(mDataSource.get(i));
        }
        mDataSource.removeAll(removedItems);
        notifyItemRangeRemoved(start, end - start + 1);
        notifyItemRangeChanged(start, mDataSource.size() - start);
    }

    /**
     * 校验位置合法
     *
     * @param index 索引
     */
    private void checkDataValid(int index) {
        if (index < 0 || index >= mDataSource.size()) {
            @SuppressLint("DefaultLocale")
            String ex = String.format("Index must be large than zero and less than items' size(index = %d, size = %d)", index, mDataSource.size());
            //BDebug.e("OrBaseRecyclerAdapter", ex);
            throw new IndexOutOfBoundsException(ex);
        }
    }
}
