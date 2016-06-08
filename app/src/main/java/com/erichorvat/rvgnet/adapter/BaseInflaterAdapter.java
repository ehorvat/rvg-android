package com.erichorvat.rvgnet.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.erichorvat.rvgnet.interfaces.IAdapterViewInflater;
import com.erichorvat.rvgnet.interfaces.OnSearch;
import com.erichorvat.rvgnet.model.Game;

import java.util.ArrayList;
import java.util.List;


public class BaseInflaterAdapter<T> extends BaseAdapter implements Filterable
{
	private List<T> m_items = new ArrayList<T>();
	private IAdapterViewInflater<T> m_viewInflater;

    private ArrayList<Game> originalData;
    private ArrayList<Game> filteredData;



    OnSearch searchCallback;

	public BaseInflaterAdapter(IAdapterViewInflater<T> viewInflater, ArrayList<Game> d)
	{
		m_viewInflater = viewInflater;
        originalData = d;
        filteredData = d;
	}

    public BaseInflaterAdapter(IAdapterViewInflater<T> viewInflater, ArrayList<Game> d, OnSearch s)
    {
        m_viewInflater = viewInflater;
        originalData = d;
        filteredData = d;
        searchCallback = s;
    }

	public BaseInflaterAdapter(List<T> items, IAdapterViewInflater<T> viewInflater)
	{
		m_items.addAll(items);
		m_viewInflater = viewInflater;
	}

	public void setViewInflater(IAdapterViewInflater<T> viewInflater, boolean notifyChange)
	{
		m_viewInflater = viewInflater;

		if (notifyChange)
			notifyDataSetChanged();
	}

	public void addItem(T item, boolean notifyChange)
	{
		m_items.add(item);

		if (notifyChange)
			notifyDataSetChanged();
	}
	
	public void addItem(int pos, T item, boolean notifyChange){
		m_items.add(pos, item);
		
		if(notifyChange)
			notifyDataSetChanged();
	}

	public void addItems(List<T> items, boolean notifyChange)
	{
		m_items.addAll(items);

		if (notifyChange)
			notifyDataSetChanged();
	}
	
	public void addItems(int pos, List<T> items, boolean notifyChange)
	{
		m_items.addAll(0,items);

		if (notifyChange)
			notifyDataSetChanged();
	}

	public void clear(boolean notifyChange)
	{
		m_items.clear();

		if (notifyChange)
			notifyDataSetChanged();
	}

    public void remove(int pos, boolean notifyChange){
        m_items.remove(pos);

        if(notifyChange)
            notifyDataSetChanged();
    }

    public ArrayList<Game> getFilteredData(){
        return filteredData;
    }



	@Override
	public int getCount()
	{
		return m_items.size();
	}

	@Override
	public Object getItem(int pos)
	{
		return getTItem(pos);
	}

	public T getTItem(int pos)
	{
		return m_items.get(pos);
	}

	@Override
	public long getItemId(int pos)
	{
		return pos;
	}


	@Override
	public View getView(int pos, View convertView, ViewGroup parent)
	{
		return m_viewInflater != null ? m_viewInflater.inflate(this, pos, convertView, parent) : null;
	}

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            int filteredCount;
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults results = new FilterResults();

                //If there's nothing to filter on, return the original data for your list
                if(charSequence == null || charSequence.length() == 0)
                {
                    results.values = originalData;
                    results.count = originalData.size();
                    searchCallback.onNoMatch((ArrayList<Game>)results.values, results.count);
                }
                else
                {
                    ArrayList<Game> filterResultsData = new ArrayList<Game>();

                    for(Game game : originalData)
                    {
                        //In this loop, you'll filter through originalData and compare each item to charSequence.
                        //If you find a match, add it to your new ArrayList
                        //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                        if(game.getTitle().trim().contains(charSequence) || game.getTitle().trim().toLowerCase().contains(charSequence))
                        {
                            filterResultsData.add(game);
                        }
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                    filteredCount = filterResultsData.size();

                }




                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                m_items = (ArrayList<T>)filterResults.values;
                notifyDataSetChanged();
            }


            public int getFilteredCount() {
                return filteredCount;
            }
        };
    }
}
