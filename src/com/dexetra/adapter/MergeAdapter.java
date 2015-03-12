package com.dexetra.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;

public class MergeAdapter extends BaseAdapter implements SectionIndexer, IDestroyer, IScrollable {
    protected final ArrayList<BaseAdapter> mPieces = new ArrayList<>();
    protected String noItemsText;

    /**
     * Stock constructor, simply chaining to the superclass.
     */
    public MergeAdapter() {
    }

    /**
     * Adds a new adapter to the roster of things to appear in the aggregate list.
     *
     * @param adapter Source for row views for this section
     */
    public void addAdapter(BaseAdapter adapter) {
        mPieces.add(adapter);
        adapter.registerDataSetObserver(new CascadeDataSetObserver());
    }

    public void addAdapter(BaseAdapter adapter, boolean atStart) {
        mPieces.add(0, adapter);
        adapter.registerDataSetObserver(new CascadeDataSetObserver());
    }

    // /**
    // * Removes a adapter to the roster of things to appear in the aggregate
    // * list.
    // *
    // * @param adapter
    // * Source for row views for this section
    // */
    // public void removeAdapter(BaseAdapter adapter) {
    // mPieces.remove(adapter);
    // adapter.registerDataSetObserver(new CascadeDataSetObserver());
    // }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want
     */
    public Object getItem(int position) {
        for (BaseAdapter piece : mPieces) {
            int size = piece.getCount();

            if (position < size) {
                return (piece.getItem(position));
            }

            position -= size;
        }

        return (null);
    }

    @Deprecated
    public void setNoItemsText(String text) {
        noItemsText = text;
    }

    /**
     * Get the adapter associated with the specified position in the data set.
     *
     * @param position Position of the item whose adapter we want
     */
    public BaseAdapter getAdapter(int position) {
        for (BaseAdapter piece : mPieces) {
            int size = piece.getCount();

            if (position < size) {
                return (piece);
            }

            position -= size;
        }

        return (null);
    }

    public ArrayList<BaseAdapter> getAdapters() {
        return mPieces;
    }

    public boolean moveToFront(DexBaseAdapter adapter) {
        boolean removed = mPieces.remove(adapter);
        if (removed) {
            mPieces.add(0, adapter);
            notifyDataSetChanged();
            return true;
        } else {
            return false;
        }
    }

    public boolean moveToPosition(DexBaseAdapter adapter, int position) {
        try {
            boolean removed = mPieces.remove(adapter);
            if (removed) {
                mPieces.add(position, adapter);
                notifyDataSetChanged();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isEmpty() {
        boolean isEmpty = false;
        for (BaseAdapter piece : mPieces) {
            if (!(piece instanceof TitleAdapter)) isEmpty = piece.isEmpty() && isEmpty;
        }
        return isEmpty;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     */
    public int getCount() {
        int total = 0;

        for (BaseAdapter piece : mPieces) {
            total += piece.getCount();
        }

        if (total == 0 && noItemsText != null) {
            total = 1;
        }
        return (total);
    }

    /**
     * Returns the number of types of Views that will be created by getView().
     */
    @Override
    public int getViewTypeCount() {
        // if (true) {
        // return mmDIRTYFIXCNT;
        // }
        int total = 0;

        for (BaseAdapter piece : mPieces) {
            total += piece.getViewTypeCount();
        }

        return (Math.max(total, 1)); // needed for setListAdapter() before
        // content add'
    }

    /**
     * Get the type of View that will be created by getView() for the specified item.
     *
     * @param position Position of the item whose data we want
     */
    @Override
    public int getItemViewType(int position) {
        int typeOffset = 0;
        int result = -1;

        for (BaseAdapter piece : mPieces) {
            int size = piece.getCount();

            if (position < size) {
                result = typeOffset + piece.getItemViewType(position);
                break;
            }

            position -= size;
            typeOffset += piece.getViewTypeCount();
        }
        return (result);
    }

    /**
     * Are all items in this ListAdapter enabled? If yes it means all items are selectable and
     * clickable.
     */
    @Override
    public boolean areAllItemsEnabled() {
        return (false);
    }

    /**
     * Returns true if the item at the specified position is not a separator.
     *
     * @param position Position of the item whose data we want
     */
    @Override
    public boolean isEnabled(int position) {
        for (BaseAdapter piece : mPieces) {
            int size = piece.getCount();

            if (position < size) {
                return (piece.isEnabled(position));
            }

            position -= size;
        }

        return (false);
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param position    Position of the item whose data we want
     * @param convertView View to recycle, if not null
     * @param parent      ViewGroup containing the returned View
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        for (BaseAdapter piece : mPieces) {
            int size = piece.getCount();

            if (position < size) {

                return (piece.getView(position, convertView, parent));
            }

            position -= size;
        }

        if (noItemsText != null) {
            TextView text = new TextView(parent.getContext());
            text.setText(noItemsText);
            return text;
        }

        return (null);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position Position of the item whose data we want
     */
    public long getItemId(int position) {
        for (BaseAdapter piece : mPieces) {
            int size = piece.getCount();

            if (position < size) {
                return (piece.getItemId(position));
            }

            position -= size;
        }

        return (-1);
    }

    @Override
    public int getPositionForSection(int section) {
        int position = 0;

        for (BaseAdapter piece : mPieces) {
            if (piece instanceof SectionIndexer) {
                Object[] sections = ((SectionIndexer) piece).getSections();
                int numSections = 0;

                if (sections != null) {
                    numSections = sections.length;
                }

                if (section < numSections) {
                    return (position + ((SectionIndexer) piece).getPositionForSection(section));
                } else if (sections != null) {
                    section -= numSections;
                }
            }

            position += piece.getCount();
        }

        return (0);
    }

    @Override
    public int getSectionForPosition(int position) {
        int section = 0;

        for (BaseAdapter piece : mPieces) {
            int size = piece.getCount();

            if (position < size) {
                if (piece instanceof SectionIndexer) {
                    return (section + ((SectionIndexer) piece).getSectionForPosition(position));
                }

                return (0);
            } else {
                if (piece instanceof SectionIndexer) {
                    Object[] sections = ((SectionIndexer) piece).getSections();

                    if (sections != null) {
                        section += sections.length;
                    }
                }
            }

            position -= size;
        }

        return (0);
    }

    @Override
    public Object[] getSections() {
        ArrayList<Object> sections = new ArrayList<Object>();

        for (BaseAdapter piece : mPieces) {
            if (piece instanceof SectionIndexer) {
                Object[] curSections = ((SectionIndexer) piece).getSections();

                if (curSections != null) {
                    for (Object section : curSections) {
                        sections.add(section);
                    }
                }
            }
        }

        if (sections.size() == 0) {
            return (null);
        }

        return (sections.toArray(new Object[0]));
    }

    @Override
    public void destroy() {
        for (BaseAdapter piece : mPieces) {
            if (piece instanceof IDestroyer) ((IDestroyer) piece).destroy();
        }
    }

    private class CascadeDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            notifyDataSetInvalidated();
        }
    }

    @Override
    public boolean showScroller(int position) {
        for (BaseAdapter piece : mPieces) {
            if (piece instanceof IScrollable) {
                int size = piece.getCount();
                if (position < size) {
                    return ((IScrollable) piece).showScroller(position);
                }

                position -= size;
            } else return false;
        }
        return false;

    }

}