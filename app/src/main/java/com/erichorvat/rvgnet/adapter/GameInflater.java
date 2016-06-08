package com.erichorvat.rvgnet.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erichorvat.rvgnet.R;
import com.erichorvat.rvgnet.interfaces.IAdapterViewInflater;
import com.erichorvat.rvgnet.model.Game;
import com.erichorvat.rvgnet.tasks.BitmapWorkerTask;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by erichorvat on 1/29/15.
 */

public class GameInflater implements IAdapterViewInflater<Game> {

    Context c;

    AsyncTask bwt;

    public GameInflater(Context c){
        this.c = c;
    }


    @Override
    public View inflate(BaseInflaterAdapter<Game> adapter, int pos, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Game game = adapter.getTItem(pos);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.game_row, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        Typeface awesome = Typeface.createFromAsset(c.getAssets(), "fonts/fontawesome-webfont.ttf");
        Typeface lato = Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Light.ttf");

        holder.tvGameTitle.setText(game.getTitle());
        holder.tvGameTitle.setTypeface(lato);

        holder.tvPrice.setText(Html.fromHtml(" <font color='#3cb879'> Buy:  " + c.getResources().getString(R.string.icon_cash)  + " " + game.getCostAmount()+ "</font>"));
        holder.tvPrice.setTypeface(awesome);

        holder.tvTradeAmt.setText(Html.fromHtml("<font color='#FFC641'> Trade:  " + c.getResources().getString(R.string.icon_cash)  + " " + game.getTradeAmount()+ "</font>"));
        holder.tvTradeAmt.setTypeface(awesome);

        holder.tvPlatform.setText(c.getResources().getString(R.string.icon_gamepad) + " " + game.getPlatform());
        holder.tvPlatform.setTypeface(awesome);

        holder.v = (ImageView) convertView.findViewById(R.id.ivGameImage);

        if(game.getPicture().equals("Y")){
            Picasso.with(c).load(game.getImgURL()).into(holder.v);
        }else{
           bwt = new BitmapWorkerTask(c, holder.v, 200, 200).execute(R.drawable.faex);
        }

        return convertView;
    }

    static class ViewHolder {

        @InjectView(R.id.tvGameTitle) TextView tvGameTitle;
        @InjectView(R.id.tvPrice) TextView tvPrice;
        @InjectView(R.id.tvTradeAmt) TextView tvTradeAmt;
        @InjectView(R.id.tvPlatform) TextView tvPlatform;

        ImageView v;

        private View m_rootView;

        public ViewHolder(View rootView) {
            m_rootView = rootView;
            ButterKnife.inject(this, rootView);
            m_rootView.setTag(rootView);
        }

    }


}
