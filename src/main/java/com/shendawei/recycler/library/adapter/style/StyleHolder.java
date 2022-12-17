package com.shendawei.recycler.library.adapter.style;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.shendawei.recycler.library.adapter.base.BaseHolder;
import com.shendawei.recycler.library.utils.ScreenUtils;

/**
 * @author shendawei
 * @classname StyleHolder
 * @description
 * @date 2020/7/17 20:16
 */
public abstract class StyleHolder<T extends StyleModel<?>, Cb extends StyleCallback> extends BaseHolder<T, Cb> {

    public StyleHolder(View itemView, Cb holderCb) {
        super(itemView, holderCb);
    }

    @Override
    public void bindData(@NonNull T item, int position) {
        super.bindData(item, position);
        if (getBackgroundResource() != 0) {
            setBackGround(getBackgroundResource());
        } else {
            setBackground(getCornerType(), getCornerRadius(), getBackgroundColor());
        }
        setPadding(getPadding());
    }

    /**
     * 设置自定义背景
     * @param bgRes res
     */
    private void setBackGround(@DrawableRes int bgRes) {
        itemView.setBackgroundResource(bgRes);
    }

    /**
     * 设置背景圆角
     * @param cornerType 背景圆角类型
     * @param cornerRadius 背景圆角
     * @param backgroundColor 背景颜色
     */
    private void setBackground(StyleModel.CornerType cornerType, int cornerRadius, int backgroundColor) {
        if (cornerType == null) return;
        float[] radii = null;
        int r = cornerRadius;
        switch (cornerType) {
            case topCorner:
                radii = new float[]{r, r, r, r, 0, 0, 0, 0};
                break;
            case bottomCorner:
                radii = new float[]{0, 0, 0, 0, r, r, r, r};
                break;
            case allCorner:
                radii = new float[]{r, r, r, r, r, r, r, r};
                break;
            case noneCorner:
                radii = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
                break;
        }
        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.RECTANGLE);
        d.setColor(backgroundColor);
        d.setCornerRadii(radii);
        itemView.setBackground(d);
    }

    private void setPadding(StyleModel.Padding padding) {
        if (padding == null) return;

        itemView.setPadding(padding.left, padding.top, padding.right, padding.bottom);
    }

    /**
     * 采用protected，可在holder子类中实现属性
     * 亦可从item获取配置的属性
     */
    protected StyleModel.Padding getPadding() {
        return item.padding;
    }

    /**
     * 采用protected，可在holder子类中实现属性
     * 亦可从item获取配置的属性
     */
    protected StyleModel.CornerType getCornerType() {
        return item.cornerType;
    }

    /**
     * 采用protected，可在holder子类中实现属性
     * 亦可从item获取配置的属性
     */
    protected int getCornerRadius() {
        if (item.radius != 0) {
            return ScreenUtils.dip2px(context, item.radius);
        }
        return ScreenUtils.dip2px(context, 6);
    }

    /**
     * 采用protected，可在holder子类中实现属性
     * 亦可从item获取配置的属性
     */
    protected int getBackgroundColor() {
        if (!TextUtils.isEmpty(item.backgroundColor)) {
            try {
                return Color.parseColor(item.backgroundColor);
            } catch (IllegalArgumentException e) {
                return Color.WHITE;
            }
        }
        return Color.WHITE;
    }

    /**
     * 采用protected，可在holder子类中实现属性
     * 亦可从item获取配置的属性
     */
    protected @DrawableRes int getBackgroundResource() {
        return item.backgroundResource;
    }

    public abstract static class Factory<T extends StyleModel<?>, C extends StyleCallback> extends BaseHolder.Factory<T, C> {
        @Override
        public StyleHolder<T, C> createViewHolder(ViewGroup parent, int viewType, C holderCallback) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return onCreateViewHolder(layoutInflater.inflate(getLayoutId(), parent, false), holderCallback);
        }

        @NonNull
        @Override
        protected abstract StyleHolder<T, C> onCreateViewHolder(@NonNull View itemView, C holderCallback);
    }

}
