package com.dexetra.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TitleAdapter extends BaseAdapter implements IDexAdapter {

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
	public TitleAdapter(Context context, View view) {
        mContext=context;
		mView = view;
		mIsEnabled = false;
		ID = -1;
	}

	public int getId() {
		return ID;
	}

	public TitleAdapter(Context context, int id, ViewGenerator viewGen) {
		this(context, id, viewGen, false);
	}

	public TitleAdapter(Context context, int id, ViewGenerator viewGen,
			boolean enabled) {
        mContext=context;
		ID = id;
		mIsEnabled = enabled;
		mViewGen = viewGen;
	}

	OnClickListener mItemClick;

	public void setClickListener(OnClickListener click) {
		mItemClick = click;
		if (mView != null)
			mView.setOnClickListener(mItemClick);
	}

	@Override
	public int getCount() {
		if (mIsEnabled)
			return 1;
		else
			return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			getView().setOnClickListener(mItemClick);
		}
		return getView();
	}

	public View getView() {
		if (mView == null && mViewGen != null)
			mView = mViewGen.createView(ID);
		return mView;
	}

	@Override
	public boolean showScroller(int position) {
		return false;
	}

    @Override
    public void destroy() {

    }
}