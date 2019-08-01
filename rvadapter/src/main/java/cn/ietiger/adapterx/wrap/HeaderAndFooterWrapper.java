package cn.ietiger.adapterx.wrap;


import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.ietiger.adapterx.base.HeaderFooter;
import cn.ietiger.adapterx.base.ViewHolder;
import cn.ietiger.adapterx.util.WrapperUtils;

public class HeaderAndFooterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SparseArray<HeaderFooter> mHeaderViews = new SparseArray<>();
    private SparseArray<HeaderFooter> mFootViews = new SparseArray<>();

    private RecyclerView.Adapter mInnerAdapter;

    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), parent,viewType);
            return holder;

        } else if (mFootViews.get(viewType) != null) {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(),parent,viewType);
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.valueAt(position).getLayoutId();
        } else if (isFooterViewPos(position)) {
            return mFootViews.valueAt(position - getHeadersCount() - getRealItemCount()).getLayoutId();
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            HeaderFooter headerFooter = mHeaderViews.valueAt(position);
            headerFooter.convert((ViewHolder) holder,headerFooter.getData(),position);
            return;
        }
        if (isFooterViewPos(position)) {
            HeaderFooter headerFooter = mFootViews.valueAt(position - getHeadersCount() - getRealItemCount());
            headerFooter.convert((ViewHolder) holder,headerFooter.getData(),position);
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                } else if (mFootViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position);
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holder);
        }
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }


    public void addHeader(HeaderFooter header) {
        mHeaderViews.put(mHeaderViews.size(),header);
    }

    public void addFootView(HeaderFooter footer) {
        mFootViews.put(mFootViews.size(), footer);
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }

}
