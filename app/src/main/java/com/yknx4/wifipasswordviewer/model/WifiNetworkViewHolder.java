package com.yknx4.wifipasswordviewer.model;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yknx4.wifipasswordviewer.R;

/**
 * Created by yknx4 on 2016. 2. 12..
 */
public class WifiNetworkViewHolder extends RecyclerView.ViewHolder{
    public WifiNetworkViewHolder(View view) {
        super(view);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        layoutText = (LinearLayout) view.findViewById(R.id.layout_text);
        networkName = (TextView) view.findViewById(R.id.networkName);
        networkPass = (TextView) view.findViewById(R.id.networkPass);
        networkSecurity = (TextView) view.findViewById(R.id.networkSecurity);
        cardView = (CardView) view.findViewById(R.id.card_view);
    }
    public ImageView imageView;
    public LinearLayout layoutText;
    public TextView networkName;
    public TextView networkPass;
    public TextView networkSecurity;
    public CardView cardView;

}
