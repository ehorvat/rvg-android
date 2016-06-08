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
import com.erichorvat.rvgnet.model.Platform;

/**
 * Created by erichorvat on 1/27/15.
 */
public class PlatformInflater implements IAdapterViewInflater<Platform> {


    Context c;

    int pos;

    ImageView ivSettingIcon;

    public PlatformInflater(Context c){
        this.c = c;
    }

    @Override
    public View inflate(BaseInflaterAdapter<Platform> adapter, int pos, View convertView, ViewGroup parent) {
        ViewHolder holder;


        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.platform_row, parent,false);
            holder = new ViewHolder(convertView);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        final Platform p = adapter.getTItem(pos);

        holder.updateDisplay(p,pos, c);


        return convertView;
    }

    private class ViewHolder {
        private View m_rootView;
        private TextView tvPlatformName;

        public ViewHolder(View rootView) {
            m_rootView = rootView;
            tvPlatformName = (TextView) m_rootView.findViewById(R.id.tvPlatformName);
            rootView.setTag(this);
        }

        public void updateDisplay(Platform p, int pos, Context c) {

            Typeface lato = Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Light.ttf");


            tvPlatformName.setText(p.getName());
            tvPlatformName.setTypeface(lato);

            if(!p.isHighlighted()){
                m_rootView.setBackgroundColor(c.getResources().getColor(R.color.lv_background_color));
            }else{
                m_rootView.setBackgroundColor(c.getResources().getColor(R.color.almost_aqua));
            }


        }
    }
}
