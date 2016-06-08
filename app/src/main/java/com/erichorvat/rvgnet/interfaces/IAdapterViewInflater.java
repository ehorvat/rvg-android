package com.erichorvat.rvgnet.interfaces;

import android.view.View;
import android.view.ViewGroup;

import com.erichorvat.rvgnet.adapter.BaseInflaterAdapter;

public interface IAdapterViewInflater<T>
{
	public View inflate(BaseInflaterAdapter<T> adapter, int pos, View convertView, ViewGroup parent);
}
