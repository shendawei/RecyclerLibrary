package com.shendawei.recycler.library.adapter.choice;

import android.view.View;

import com.shendawei.recycler.library.adapter.base.BaseHolder;

/**
 * 支持多选模式的holder基类
 *
 * 功能
 * － 支持选中、取消选中、不可选中态
 *
 * 原理
 * 1. 定义IHolderCheckInterface，与adapter基类通信，判定是否选中、通知选中态
 * 2. 基类定义开启／关闭选择模式 isChoiceMode()，默认开启，与adapter状态保持一致。子类不能更改
 *    基类定义isItemChecked(position)，item是否可被勾选，与数据item无关
 *    基类定义isItemCheckable()，item是否可被勾选，与数据item有关，默认可被勾选
 *    子类实现isItemCheckable()，由item给出条件是否可被勾选
 * 3. 基类非选择模式、或不可选时，点击直接return
 *    基类可选时，通过notifyItemChanged(position, payload)，实现局部刷新选中态
 * 4. 子类通过onBindData(item, position)，实现item展示
 *    子类通过onBindData(item, position, payload)，实现item局部刷新选中态
 * 5. 定义开启／关闭选择模式 isChoiceMode()
 *
 * @author shendawei
 * @classname ChoiceBaseHolder
 * @date 22/4/5 下午9:35
 */
public class ChoiceBaseHolder<T> extends BaseHolder<T, ChoiceCallback> implements View.OnClickListener {

    private IHolderCheckInterface holderCheckInterface;

    public ChoiceBaseHolder(View itemView, ChoiceCallback cb) {
        super(itemView, cb);
    }

    @Override
    public void initView() {
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!isChoiceMode() || !isItemCheckable()) return;
        if (isItemChecked()) {
            holderCheckInterface.onChecked(position, false);
        } else {
            holderCheckInterface.onChecked(position, true);
        }
    }

    /**
     * 是否开启选择模式
     *
     * @return 默认不开启
     */
    public final boolean isChoiceMode() {
        // 校验holderCheckInterface非空
        if (holderCheckInterface == null) {
            throw new IllegalArgumentException("holderCheckInterface can't be null!");
        }
        return holderCheckInterface.isChoiceMode();
    }

    /**
     * item是否可被勾选
     *
     * @return 与item数据有关，默认可选，可由子类控制
     */
    public boolean isItemCheckable() {
        return true;
    }

    /**
     * item是否被勾选
     *
     * @return 与item数据无关，默认未勾选
     */
    public final boolean isItemChecked() {
        // 校验holderCheckInterface非空
        if (holderCheckInterface == null) {
            throw new IllegalArgumentException("holderCheckInterface can't be null!");
        }
        return holderCheckInterface.isChecked(position);
    }

    /**
     * 必须调用
     */
    public void setHolderCheckInterface(IHolderCheckInterface checkInterface) {
        holderCheckInterface = checkInterface;
    }

    public interface IHolderCheckInterface {
        void onChecked(int position, boolean isChecked);

        /**
         * @param isNotifyItemChanged 用于区分单选、多选时的通知刷新机制
         */
        void onChecked(int position, boolean isChecked, boolean isNotifyItemChanged);

        boolean isChecked(int position);
        boolean isChoiceMode();
    }
}
