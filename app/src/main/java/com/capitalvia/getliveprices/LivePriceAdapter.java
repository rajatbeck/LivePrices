package com.capitalvia.getliveprices;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rajat on 10/5/2016.
 */
public class LivePriceAdapter extends RecyclerView.Adapter<LivePriceAdapter.LivePriceViewHolder> {

    private Context context;
    private List<LivePrice> mLivePriceList;

    LivePriceAdapter(Context context, List<LivePrice> mLivePriceList) {
        this.context = context;
        this.mLivePriceList = mLivePriceList;
    }

    public void refreshList(List<LivePrice> livePriceList) {
        this.mLivePriceList.clear();
        this.mLivePriceList = livePriceList;
        Log.d("check", "is called" + mLivePriceList.get(0).getLast_updated_time());
        notifyDataSetChanged();
    }

    @Override
    public LivePriceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_price_row_ayout, parent, false);
        LivePriceViewHolder livePriceViewHolder = new LivePriceViewHolder(v);
        return livePriceViewHolder;
    }

    @Override
    public void onBindViewHolder(LivePriceViewHolder holder, int position) {

        LivePrice livePriceItem = mLivePriceList.get(position);
        holder.live_price.setText(livePriceItem.getLive_price());
        holder.pre_day_close.setText(livePriceItem.getPrev_day_close());
        holder.last_updated_time.setText(livePriceItem.getLast_updated_time());

    }

    @Override
    public int getItemCount() {
        return mLivePriceList.size();
    }

    class LivePriceViewHolder extends RecyclerView.ViewHolder {

        TextView live_price;
        TextView pre_day_close;
        TextView last_updated_time;

        public LivePriceViewHolder(View itemView) {
            super(itemView);
            live_price = (TextView) itemView.findViewById(R.id.live_price_text);
            pre_day_close = (TextView) itemView.findViewById(R.id.close_price_text);
            last_updated_time = (TextView) itemView.findViewById(R.id.last_updated_time);

        }
    }
}
