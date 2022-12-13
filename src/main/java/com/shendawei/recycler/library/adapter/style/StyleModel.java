package com.shendawei.recycler.library.adapter.style;

/**
 * rv列表holder对应的数据结构
 *
 * @author shendawei
 * @classname BaseModel
 * @date 6/8/22 2:52 PM
 */
public class StyleModel<Model> {
    public enum CornerType {
        topCorner, bottomCorner, allCorner, noneCorner
    }

    public static class Padding {
        public int top;
        public int bottom;
        public int left;
        public int right;

        public Padding ofTop(int p) {
            this.top = p;
            return this;
        }

        public Padding ofBottom(int p) {
            this.bottom = p;
            return this;
        }

        public Padding ofLeft(int p) {
            this.left = p;
            return this;
        }

        public Padding ofRight(int p) {
            this.right = p;
            return this;
        }

    }

    /**
     * 主数据 mainData
     */
    public Model m;
    /**
     * item类型，for holder
     */
    public int itemType;
    /**
     * 圆角类型，决定holder背景圆角
     */
    public CornerType cornerType;
    /**
     * 圆角半径
     */
    public float radius;
    /**
     * 内边距
     */
    public Padding padding;
    /**
     * 背景颜色 "#FFFFFF"
     */
    public String backgroundColor;
    /**
     * 自定义背景res 0 未设置，特殊需求使用
     */
    public int backgroundResource;
    /**
     * 展示阴影 未来字段
     */
    public boolean hasShadow;
}
