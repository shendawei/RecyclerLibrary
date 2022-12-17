package com.shendawei.recycler.library.adapter.choice;

import android.util.SparseBooleanArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.shendawei.recycler.library.adapter.base.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 支持多选模式的adapter基类
 *
 * 方案功能
 * － 支持多选
 * － 支持全选
 * － 支持分页加载，自动全选
 *
 * 方案设计实现原理
 * 1. 利用SparseBooleanArray记录选择项
 * 2. 抽象设计，实现选择项与数据item隔离开
 * 3. 基类定义开启／关闭选择模式 isChoiceMode()，默认开启，子类可基于业务条件实现
 *    基类定义isItemChecked(position)，item是否可被勾选，与数据item无关
 *    基类定义isItemCheckable(T item)，item是否可被勾选，与数据item有关，默认可被勾选
 *    子类实现isItemCheckable(T item)，由item给出条件是否可被勾选
 * 4. holder基类定义isItemChecked(position)，item是否可被勾选，与数据item无关
 *    holder基类定义isItemCheckable(position)，item是否可被勾选，与数据item有关，默认可被勾选
 * 5. 实现设置全选、取消全选，更新选中数量mSelectionCount
 * 6. 实现计算最大可选数量，数据列表发生变化，及时更新最大可选数量mSelectableCount
 * 7. 分页加载，自动全选：先判断是否全选、再更新最大可选数量mSelectableCount，最后二次设置全选、更新选中数量mSelectionCount
 * 8. holder基类非选择模式、或不可选时，点击直接return
 *    holder基类通过notifyItemChanged(position, payload)，实现局部刷新选中态
 * 9. holder子类通过onBindData(item, position)，实现item展示
 *    holder子类通过onBindData(item, position, payload)，实现item局部刷新选中态
 *
 * @author shendawei
 * @classname AbsChoiceAdapter
 * @date 22/4/5 下午9:29
 */
public abstract class AbsChoiceAdapter<T> extends BaseRecyclerAdapter<T, ChoiceBaseHolder<T>, ChoiceCallback> {
    private final SparseBooleanArray mSelectionArray = new SparseBooleanArray();
    /**
     * 实际选中数量
     */
    private int mSelectionCount = 0;
    /**
     * 最大可选数量
     */
    private int mSelectableCount = 0;
    //ChoiceCallback替代
    @Deprecated
    private IAdapterChoiceInterface choiceInterface;

    public AbsChoiceAdapter(ChoiceCallback cb) {
        super(cb);
    }

    @NonNull
    @Override
    public ChoiceBaseHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChoiceBaseHolder<T> holder = onCreateChoiceHolder(parent, viewType);
        holder.setHolderCheckInterface(new ChoiceBaseHolder.IHolderCheckInterface() {
            @Override
            public void onChecked(int position, boolean isChecked) {
                AbsChoiceAdapter.this.setItemChecked(position, isChecked);
                if (choiceInterface != null) {
                    choiceInterface.onChoiceChanged(isAllChecked());
                }
                mHolderCb.onChoiceChanged(isAllChecked());
                AbsChoiceAdapter.this.notifyItemChanged(position, isChecked);
            }

            @Override
            public void onChecked(int position, boolean isChecked, boolean isNotifyItemChanged) {
                AbsChoiceAdapter.this.setItemChecked(position, isChecked);
                if (choiceInterface != null) {
                    choiceInterface.onChoiceChanged(isAllChecked());
                }
                mHolderCb.onChoiceChanged(isAllChecked());
                if (isNotifyItemChanged)
                    AbsChoiceAdapter.this.notifyItemChanged(position, isChecked);
            }

            @Override
            public boolean isChecked(int position) {
                return AbsChoiceAdapter.this.isItemChecked(position);
            }

            @Override
            public boolean isChoiceMode() {
                return AbsChoiceAdapter.this.isChoiceMode();
            }
        });
        return holder;
    }

    /**
     * 是否开启选择模式
     *
     * @return 默认开启
     */
    public boolean isChoiceMode() {
        return true;
    }

    protected abstract ChoiceBaseHolder<T> onCreateChoiceHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull ChoiceBaseHolder<T> holder, int position) {
        holder.bindData(mDataSource.get(position), position);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoiceBaseHolder<T> holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (payloads.get(0) != null) {
                holder.bindData(mDataSource.get(position), position, payloads.get(0));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void setItemChecked(int position, boolean isChecked) {
        //多选模式 mSelectionArray存储所有items状态
        mSelectionArray.put(position, isChecked);
        if (isChecked) {
            mSelectionCount++;
        } else {
            mSelectionCount--;
        }
    }

    /**
     * item是否被勾选
     *
     * @return 与item数据无关
     */
    private boolean isItemChecked(int position) {
        return mSelectionArray.get(position);
    }

    /**
     * item是否可被勾选
     *
     * @return 与item数据有关，默认可选
     */
    public boolean isItemCheckable(T item) {
        return true;
    }

    @Override
    public void putItems(List<? extends T> items) {
        super.putItems(items);
        computeSelectableCount();
        clearAllChecked();
    }

    @Override
    public void clear() {
        super.clear();
        computeSelectableCount();
    }

    @Override
    public void addItem(T item) {
        super.addItem(item);
        computeSelectableCount();
    }

    @Override
    public void addItem(int index, T item) {
        super.addItem(index, item);
        computeSelectableCount();
    }

    @Override
    public void replaceItem(int index, T item) {
        super.replaceItem(index, item);
        computeSelectableCount();
    }

    @Override
    public void addItems(List<? extends T> items) {
        super.addItems(items);
        boolean isAllChecked = isAllChecked();
        computeSelectableCount();
//        //分页加载自动全选
//        if (isAllChecked) setAllChecked();
    }

    @Override
    public void addItems(int index, List<? extends T> items) {
        super.addItems(index, items);
        boolean isAllChecked = isAllChecked();
        computeSelectableCount();
//        //分页加载自动全选
//        if (isAllChecked) setAllChecked();
    }

    @Override
    public void changeItem(T item) {
        super.changeItem(item);
        computeSelectableCount();
    }

    @Override
    public void removeItem(T item) {
        super.removeItem(item);
        computeSelectableCount();
    }

    @Override
    public void removeItem(int index) {
        super.removeItem(index);
        computeSelectableCount();
    }

    @Override
    public void removeItemsFrom(int start) {
        super.removeItemsFrom(start);
        computeSelectableCount();
    }

    @Override
    public void removeItemsRange(int start, int end) {
        super.removeItemsRange(start, end);
        computeSelectableCount();
    }

    /**
     * 计算最大可选数量
     *
     * 数据发生变化时，调用方法计算
     */
    private void computeSelectableCount() {
        mSelectableCount = 0;
        int size = mDataSource.size();
        for (int i = 0; i < size; i++) {
            if (isItemCheckable(mDataSource.get(i))) {
                mSelectableCount++;
            }
        }
    }

    public final void setAllChecked() {
        int size = mDataSource.size();
        mSelectionCount = mSelectableCount;
        for (int i = 0; i < size; i++) {
            if (isItemCheckable(mDataSource.get(i)))
                mSelectionArray.put(i, true);
        }
        notifyDataSetChanged();
    }

    public final void clearAllChecked() {
        int size = mDataSource.size();
        mSelectionCount = 0;
        for (int i = 0; i < size; i++) {
            if (isItemCheckable(mDataSource.get(i)))
                mSelectionArray.put(i, false);
        }
        notifyDataSetChanged();
    }

    private boolean isAllChecked() {
        return mSelectionCount == mSelectableCount;
    }

    public final List<T> getSelectedItems() {
        List<T> selectedItems = new ArrayList<>();
        int size = mDataSource.size();
        for (int i = 0; i < size; i++) {
            if (isItemChecked(i)) {
                selectedItems.add(mDataSource.get(i));
            }
        }

        /*for (T d : mDataSource) {
            if (isItemChecked(mDataSource.indexOf(d))) {
                selectedItems.add(d);
            }
        }*/

        return selectedItems;
    }

    public int getSelectionCount() {
        return mSelectionCount;
    }

    public int getSelectableCount() {
        return mSelectableCount;
    }

    public void setAdapterChoiceInterface(IAdapterChoiceInterface choiceInterface) {
        this.choiceInterface = choiceInterface;
    }

    public interface IAdapterChoiceInterface {
        void onChoiceChanged(boolean isAllChecked);
    }
}
