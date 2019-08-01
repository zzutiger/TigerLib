package cn.ietiger.adapterx.base;

public abstract class HeaderFooter<T> implements ItemViewDelegate<T>{

    private int layoutId;
    private T data;

    public HeaderFooter(int layoutId, T data) {
        this.layoutId = layoutId;
        this.data = data;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public int getItemViewLayoutId() {
        return layoutId;
    }

    @Override
    public boolean isForViewType(T item, int position) {
        return true;
    }
}
