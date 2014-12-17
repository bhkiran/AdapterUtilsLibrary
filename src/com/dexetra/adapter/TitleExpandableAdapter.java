package com.dexetra.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class TitleExpandableAdapter extends BaseExpandableListAdapter implements IDexAdapter {

    private View mView;
    private ViewGenerator mViewGen;
    private boolean mIsEnabled = false;
    private final int ID;
    Context mContext;

    public void setEnabled(boolean enabled) {
        mIsEnabled = enabled;
        notifyDataSetChanged();
    }

    @Deprecated
    public TitleExpandableAdapter(Context context, View view) {
        mContext = context;
        mView = view;
        mIsEnabled = false;
        ID = -1;
    }

    public int getId() {
        return ID;
    }

    public TitleExpandableAdapter(Context context, int id, ViewGenerator viewGen) {
        this(context, id, viewGen, false);
    }

    public TitleExpandableAdapter(Context context, int id, ViewGenerator viewGen, boolean enabled) {
        mContext = context;
        ID = id;
        mIsEnabled = enabled;
        mViewGen = viewGen;
        mIsEnabled = enabled;
    }

    OnClickListener mItemClick;

    public void setClickListener(OnClickListener click) {
        mItemClick = click;
        if (mView != null) mView.setOnClickListener(mItemClick);
    }

    public View getView() {
        if (mView == null && mViewGen != null) mView = mViewGen.createView(ID);
        return mView;
    }

    @Override
    public boolean showScroller(int position) {
        return false;
    }

    @Override
    public void destroy() {

    }

    @Override
    public int getGroupCount() {
        if (mIsEnabled) return 1;
        else return 0;
    }

    @Override
    public int getGroupTypeCount() {
        return 1;
    }

    @Override
    public int getGroupType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        if (convertView == null) {
            getView().setOnClickListener(mItemClick);
        }
        return getView();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}