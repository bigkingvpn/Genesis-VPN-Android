package com.darkweb.genesisvpn.application.serverManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.darkweb.genesisvpn.R;
import com.darkweb.genesisvpn.application.proxyManager.proxy_controller;
import com.jwang123.flagkit.FlagKit;

public class list_adapter extends RecyclerView.Adapter<list_adapter.listViewHolder>
{
    list_adapter() {
    }

    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.server_row_view, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull list_adapter.listViewHolder holder, int position)
    {
        holder.bindListView(list_model.getInstance().getModel().get(position));
    }

    @Override
    public int getItemCount() {
        return list_model.getInstance().getModel().size();
    }

    /*View Holder Extensions*/
    class listViewHolder extends RecyclerView.ViewHolder
    {
        TextView heaaderText;
        TextView descriptionText;
        ImageView flags;
        LinearLayout layout;

        listViewHolder(View itemView) {
            super(itemView);
        }

        void bindListView(list_row_model model) {

            heaaderText = itemView.findViewById(R.id.header);
            descriptionText = itemView.findViewById(R.id.description);
            flags = itemView.findViewById(R.id.flag);
            layout = itemView.findViewById(R.id.server);

            heaaderText.setText(model.getHeader());
            descriptionText.setText(model.getDescription());

            flags.setBackground(FlagKit.drawableWithFlag(server_model.getInstance().getServerInstance(), model.getCountryModel().getCountry()));

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    proxy_controller.getInstance().chooseServer(model.getCountryModel());
                    server_model.getInstance().getServerInstance().onBackPressed(null);
                }
            });
        }
    }
}
