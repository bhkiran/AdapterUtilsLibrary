package com.dexetra.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Kiran BH on 02/02/15.
 */
public class MergeRecycleAdapter extends RecyclerView.Adapter {

    protected ArrayList<RecyclerView.Adapter> mPieces = new ArrayList<>(2);

    /**
     * Adds a new adapter to the roster of things to appear in the aggregate list.
     *
     * @param adapter Source for row views for this section
     */
    public void addAdapter(RecyclerView.Adapter adapter) {
        mPieces.add(adapter);
        adapter.registerAdapterDataObserver(new CascadeDataSetObserver_());
    }

    public void addAdapter(RecyclerView.Adapter adapter, boolean atStart) {
        mPieces.add(0, adapter);
        adapter.registerAdapterDataObserver(new CascadeDataSetObserver_());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        for (RecyclerView.Adapter piece : mPieces) {
            int size = piece.getItemCount();

            if (i < size) {

                return piece.onCreateViewHolder(viewGroup, i);
            }

            i -= size;
        }
        return (null);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        for (RecyclerView.Adapter piece : mPieces) {
            int size = piece.getItemCount();

            if (i < size) {
                piece.onBindViewHolder(viewHolder, i);
                return;
            }

            i -= size;
        }
    }

    @Override
    public int getItemCount() {
        int total = 0;

        for (RecyclerView.Adapter piece : mPieces) {
            total += piece.getItemCount();
        }
        return total;
    }

    @Override
    public long getItemId(int position) {
        for (RecyclerView.Adapter piece : mPieces) {
            int size = piece.getItemCount();

            if (position < size) {
                return piece.getItemId(position);
            }

            position -= size;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private class CascadeDataSetObserver_ extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart, itemCount);
        }


    }
}
