package com.yknx4.wifipasswordviewer;

import java.util.ArrayList;
import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yknx4.wifipasswordviewer.model.WifiNetwork;
import com.yknx4.wifipasswordviewer.model.WifiNetworkViewHolder;

public class WifiItemAdapter extends RecyclerView.Adapter<WifiNetworkViewHolder>{

    private List<WifiNetwork> objects = new ArrayList<WifiNetwork>();

    private Context context;
    private LayoutInflater layoutInflater;

    public WifiItemAdapter(Context context, List<WifiNetwork> networks) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        objects = networks;
    }


    @Override
    public WifiNetworkViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wifi_item ,null);

        WifiNetworkViewHolder viewHolder = new WifiNetworkViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WifiNetworkViewHolder holder, int position) {
        final WifiNetwork item = objects.get(position);
        View.OnClickListener g = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(item.getName(), item.getKey());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, R.string.txt_copied_to_clipboard    , Toast.LENGTH_SHORT).show();
            }
        };
        holder.networkName.setText(item.getName());
        holder.networkPass.setText(String.format(context.getString(R.string.txt_password), item.getKey()));
        holder.networkSecurity.setText(WifiNetwork.securityToString(item.getType()));
        holder.cardView.setClickable(true);
        holder.cardView.setOnClickListener(g);
        holder.networkName.setOnClickListener(g);
        holder.networkPass.setOnClickListener(g);
        holder.networkSecurity.setOnClickListener(g);
        holder.imageView.setOnClickListener(g);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


}
