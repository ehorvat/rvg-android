package com.erichorvat.rvgnet.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erichorvat.rvgnet.R;
import com.erichorvat.rvgnet.interfaces.IAdapterViewInflater;
import com.erichorvat.rvgnet.model.Store;

/**
 * Created by erichorvat on 1/13/15.
 */
public class StoreInflater implements IAdapterViewInflater<Store> {

    Context c;

    int pos;

    ImageView ivSettingIcon;

    public StoreInflater(Context c){
        this.c = c;
    }

    @Override
    public View inflate(BaseInflaterAdapter<Store> adapter, int pos, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.store_row, parent,false);
            holder = new ViewHolder(convertView);



        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Store store = adapter.getTItem(pos);
        holder.updateDisplay(store,pos);


        return convertView;
    }

    private class ViewHolder {
        private View m_rootView;
        private TextView tvStoreName, tvCity, tvAddress, tvPhone;

        public ViewHolder(View rootView) {
            m_rootView = rootView;
            tvStoreName = (TextView) m_rootView.findViewById(R.id.tvStoreName);
            tvAddress = (TextView) m_rootView.findViewById(R.id.tvAddress);
            tvPhone = (TextView) m_rootView.findViewById(R.id.tvPhone);

            rootView.setTag(this);
        }

        public void updateDisplay(Store store, int pos) {

            Typeface lato = Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Light.ttf");


            tvStoreName.setText(store.getStore_name());
            tvAddress.setText(store.getAddress());
            tvPhone.setText(store.getPhone_number());

            tvStoreName.setTypeface(lato);
            tvAddress.setTypeface(lato);
            tvPhone.setTypeface(lato);

            tvPhone.setFocusable(false);

            if(!store.isHighlighted()){
                m_rootView.setBackgroundColor(c.getResources().getColor(R.color.lv_background_color));
            }else{
                m_rootView.setBackgroundColor(c.getResources().getColor(R.color.almost_aqua));
            }

        }
    }
}
