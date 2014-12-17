package com.dexetra.adapter;

/*

 * Copyright (C) 2006 The Android Open Source Project

 *

 * Licensed under the Apache License, Version 2.0 (the "License");

 * you may not use this file except in compliance with the License.

 * You may obtain a copy of the License at

 *

 *      http://www.apache.org/licenses/LICENSE-2.0

 *

 * Unless required by applicable law or agreed to in writing, software

 * distributed under the License is distributed on an "AS IS" BASIS,

 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

 * See the License for the specific language governing permissions and

 * limitations under the License.

 */

/**
 * @author Kiran BH 
 *
 */

import android.content.Context;
import android.database.Cursor;
import android.widget.BaseAdapter;
import android.widget.FilterQueryProvider;

/**
 * Adapter that exposes data from a {@link android.database.Cursor Cursor} to a
 * <p/>
 * {@link android.widget.ListView ListView} widget. The Cursor must include
 * <p/>
 * a column named "_id" or this class will not work.
 */

public abstract class DexCursorAdapter extends BaseAdapter implements IDexAdapter {
    /**
     * This field should be made private, so it is hidden from the SDK.
     * <p/>
     * {@hide}
     */

    protected boolean mDataValid;

    /**
     * This field will be used to give the default item id
     * <p/>
     * {@hide}
     */

    protected final String mIDColName;

    /**
     * This field should be made private, so it is hidden from the SDK.
     * <p/>
     * {@hide}
     */

    protected Cursor mCursor;

    /**
     * This field should be made private, so it is hidden from the SDK.
     * <p/>
     * {@hide}
     */

    protected Context mContext;

    /**
     * This field should be made private, so it is hidden from the SDK.
     * <p/>
     * {@hide}
     */

    protected int mRowIDColumn;

    // /**
    // *
    // * This field should be made private, so it is hidden from the SDK.
    // *
    // * {@hide}
    // */
    //
    // protected ChangeObserver mChangeObserver;
    //
    // /**
    // *
    // * This field should be made private, so it is hidden from the SDK.
    // *
    // * {@hide}
    // */
    //
    // protected DataSetObserver mDataSetObserver = new MyDataSetObserver();

    /**
     * This field should be made private, so it is hidden from the SDK.
     * <p/>
     * {@hide}
     */

    protected FavorCursorFilter mCursorFilter;

    /**
     * This field should be made private, so it is hidden from the SDK.
     * <p/>
     * {@hide}
     */

    protected FilterQueryProvider mFilterQueryProvider;

    /**
     * Constructor. The adapter will call requery() on the cursor whenever
     * <p/>
     * it changes so that the most recent data is always displayed.
     *
     * @param c       The cursor from which to get the data.
     * @param context The context
     */
    public DexCursorAdapter(Context context, Cursor c) {
        this(context, c, "_id");

    }

    /**
     * Constructor
     *
     * @param c           The cursor from which to get the data.
     * @param context     The context
     * @param autoRequery If true the adapter will call requery() on the
     *                    <p/>
     *                    cursor whenever it changes so the most recent
     *                    <p/>
     *                    data is always displayed.
     */

    public DexCursorAdapter(Context context, Cursor c, String uniqueColumnName) {
        mContext=context;
        mIDColName = uniqueColumnName;
        init(context, c);

    }

    protected void init(Context context, Cursor c) {

        boolean cursorPresent = c != null;

        mCursor = c;

        mDataValid = cursorPresent;

        mContext = context;

        mRowIDColumn = cursorPresent ? c.getColumnIndex(mIDColName) : -1;

        // mChangeObserver = new ChangeObserver();
        //
        // if (cursorPresent) {
        //
        // c.registerContentObserver(mChangeObserver);
        //
        // c.registerDataSetObserver(mDataSetObserver);
        //
        // }

    }

    /**
     * Returns the cursor.
     *
     * @return the cursor.
     */

    public Cursor getCursor() {

        return mCursor;

    }

    /**
     * Returns the context.
     *
     * @return the context.
     */

    public Context getContext() {
        return mContext;
    }

    /**
     * @see android.widget.ListAdapter#getCount()
     */

    public int getCount() {

        if (mDataValid && mCursor != null && !mCursor.isClosed()) {

            return mCursor.getCount();

        } else {

            return 0;

        }

    }

    /**
     * @see android.widget.ListAdapter#getItem(int)
     */

    public Object getItem(int position) {

        if (mDataValid && mCursor != null && !mCursor.isClosed()) {

            mCursor.moveToPosition(position);

            return mCursor;

        } else {

            return null;

        }

    }

    /**
     * @see android.widget.ListAdapter#getItemId(int)
     */

    public long getItemId(int position) {

        if (mDataValid && mCursor != null && !mCursor.isClosed()) {
            if (mRowIDColumn == -1) return position;
            else {
                if (mCursor.moveToPosition(position)) {

                    return mCursor.getLong(mRowIDColumn);

                } else {

                    return 0;

                }
            }

        } else {

            return 0;

        }

    }

    @Override
    public boolean hasStableIds() {

        return true;

    }

    public void changeCursor(Cursor cursor) {

        if (cursor == mCursor) {

            return;

        }
        if (mCursor != null) {

            // mCursor.unregisterContentObserver(mChangeObserver);
            //
            // mCursor.unregisterDataSetObserver(mDataSetObserver);

            mCursor.close();

        }
        mCursor = cursor;

        if (cursor != null) {

            // cursor.registerContentObserver(mChangeObserver);
            //
            // cursor.registerDataSetObserver(mDataSetObserver);

            mRowIDColumn = cursor.getColumnIndex(mIDColName);

            mDataValid = true;

            // notify the observers about the new cursor

            notifyDataSetChanged();

        } else {

            mRowIDColumn = -1;

            mDataValid = false;

            // notify the observers about the lack of a data set

            notifyDataSetInvalidated();

        }

    }

    /**
     * <p> Converts the cursor into a CharSequence. Subclasses should override this
     * <p/>
     * method to convert their results. The default implementation returns an
     * <p/>
     * empty String for null values or the default String representation of
     * <p/>
     * the value. </p>
     *
     * @param cursor the cursor to convert to a CharSequence
     * @return a CharSequence representing the value
     */

    public CharSequence convertToString(Cursor cursor) {

        return cursor == null ? "" : cursor.toString();

    }

    @Deprecated
    protected void onContentChanged() {
        // if (mAutoRequery && mCursor != null) {
        // // if (Config.LOGV)
        // // Log.v("Cursor", "Auto requerying " + mCursor + " due to update");
        //
        // // mDataValid = mCursor.requery();
        //
        // }

    }

    @Override
    public void destroy() {
    }

    @Override
    public void setEnabled(boolean enable) {
    }

}