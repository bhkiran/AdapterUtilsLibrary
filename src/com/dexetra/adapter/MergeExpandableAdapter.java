package com.dexetra.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;

public class MergeExpandableAdapter<D extends DexExpandandableAdapter> extends BaseExpandableListAdapter implements IDestroyer {
    private final int TAG_GROUP_POSITION;
    protected ArrayList<D> mPieces = new ArrayList<>(2);

    /**
     * Stock constructor, simply chaining to the superclass.
     */
    public MergeExpandableAdapter(int TAG_GROUP_POSITION) {
        this.TAG_GROUP_POSITION = TAG_GROUP_POSITION;
    }

    /**
     * Adds a new adapter to the roster of things to appear in the aggregate list.
     *
     * @param adapter Source for row views for this section
     */
    public void addAdapter(D adapter) {
        mPieces.add(adapter);
        adapter.registerDataSetObserver(new CascadeDataSetObserver());
    }

    public void addAdapter(D adapter, boolean atStart) {
        mPieces.add(0, adapter);
        adapter.registerDataSetObserver(new CascadeDataSetObserver());
    }


    /**
     * Get the adapter associated with the specified position in the data set.
     *
     * @param groupPosition Position of the item whose adapter we want
     */
    private D getAdapter(int groupPosition) {
        for (D piece : mPieces) {
            int size = piece.getGroupCount();
            if (groupPosition < size) {
                return (piece);
            }
            groupPosition -= size;
        }
        return null;
    }

    public ArrayList<D> getAdapters() {
        return mPieces;
    }

    public boolean moveToFront(D adapter) {
        boolean removed = mPieces.remove(adapter);
        if (removed) {
            mPieces.add(0, adapter);
            notifyDataSetChanged();
            return true;
        } else {
            return false;
        }
    }

    public boolean moveToPosition(D adapter, int position) {
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
    public int getGroupCount() {
        int total = 0;
        for (D piece : mPieces) {
            total += piece.getGroupCount();
        }
        return (total);
    }

    @Override
    public Object getGroup(int groupPosition) {
        for (D piece : mPieces) {
            int size = piece.getGroupCount();
            if (groupPosition < size) {
                return (piece.getGroup(groupPosition));
            }
            groupPosition -= size;
        }

        return (null);
    }

    @Override
    public long getGroupId(int groupPosition) {
        for (D piece : mPieces) {
            int size = piece.getGroupCount();
            if (groupPosition < size) {
                return (piece.getGroupId(groupPosition));
            }
            groupPosition -= size;
        }
        return (-1);
    }


    @Override
    public int getGroupTypeCount() {
        int total = 0;
        for (D piece : mPieces) {
            total += piece.getGroupTypeCount();
        }
        return (Math.max(total, 1));
    }

    @Override
    public int getGroupType(int groupPosition) {
        int typeOffset = 0;
        int result = -1;

        for (D piece : mPieces) {
            int size = piece.getGroupCount();
            if (groupPosition < size) {
                result = typeOffset + piece.getGroupType(groupPosition);
                break;
            }
            groupPosition -= size;
            typeOffset += piece.getGroupTypeCount();
        }
        return (result);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        final int actualGrpPos = groupPosition;
        for (D piece : mPieces) {
            int size = piece.getGroupCount();
            if (groupPosition < size) {
                parent.setTag(TAG_GROUP_POSITION, actualGrpPos);
                View v = piece.getGroupView(groupPosition, isExpanded, convertView, parent);
                v.setTag(TAG_GROUP_POSITION, actualGrpPos);
                return v;
            }
            groupPosition -= size;
        }
        return (null);
    }


    /**
     * Are all items in this ListAdapter enabled? If yes it means all items are selectable and
     * clickable.
     */
    @Override
    public boolean areAllItemsEnabled() {
        return (false);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


    @Override
    public void destroy() {
        for (D piece : mPieces) {
            piece.destroy();
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        for (D piece : mPieces) {
            int size = piece.getGroupCount();
            if (groupPosition < size) {
                return piece.getChildrenCount(groupPosition);
            }
            groupPosition -= size;
        }
        return 0;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        for (D piece : mPieces) {
            int size = piece.getGroupCount();
            if (groupPosition < size) {
                return piece.getChild(groupPosition, childPosition);
            }
            groupPosition -= size;
        }
        return (null);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        for (D piece : mPieces) {
            int size = piece.getGroupCount();
            if (groupPosition < size) {
                return (piece.getChildId(groupPosition, childPosition));
            }
            groupPosition -= size;
        }
        return (-1);
    }


    @Override
    public int getChildTypeCount() {
        int total = 0;
        for (D piece : mPieces) {
            total += piece.getChildTypeCount();
        }
        return (Math.max(total, 1));
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        int typeOffset = 0;
        int result = -1;
        for (D piece : mPieces) {
            int size = piece.getGroupCount();
            if (groupPosition < size) {
                result = typeOffset + piece.getChildType(groupPosition, childPosition);
                break;
            }
            groupPosition -= size;
            typeOffset += piece.getGroupTypeCount();
        }
        return (result);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        for (D piece : mPieces) {
            int size = piece.getGroupCount();
            if (groupPosition < size) {
                return (piece.getChildView(groupPosition, childPosition, isLastChild, convertView,
                        parent));
            }
            groupPosition -= size;
        }
        return (null);
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


}