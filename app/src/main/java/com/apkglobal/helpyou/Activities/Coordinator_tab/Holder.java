package com.apkglobal.helpyou.Activities.Coordinator_tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkglobal.helpyou.Activities.Helper.Configure;
import com.apkglobal.helpyou.Activities.MyLocationUsingLocationAPI;
import com.apkglobal.helpyou.R;

import java.util.ArrayList;

/**
 * Created by Mayank on 1/13/2018.
 */

public class Holder extends RecyclerView.ViewHolder {
    TextView skillName,get_service,give_service;
    private ImageView skillImage;
    Context context;

    String type,demand_name;

    public Holder(final View itemView)  {
        super(itemView);
        context=itemView.getContext();

        skillImage=(ImageView)itemView.findViewById(R.id.image_of_demand);
        skillName=(TextView) itemView.findViewById(R.id.text_of_demand);
        get_service=itemView.findViewById(R.id.get_service);
        give_service=itemView.findViewById(R.id.give_service);
        get_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="getservice";
                demand_name=skillName.getText().toString().trim();
                Intent get_demand=new Intent(context,MyLocationUsingLocationAPI.class);
                get_demand.putExtra("DEMANDNAME",demand_name);
                get_demand.putExtra("TYPE",type);
                context.startActivity(get_demand);

            }
        });

        give_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="giveservice";
                demand_name=skillName.getText().toString().trim();
                Intent give_demand=new Intent(context,MyLocationUsingLocationAPI.class);
                give_demand.putExtra("DEMANDNAME",demand_name);
                give_demand.putExtra("TYPE",type);
                context.startActivity(give_demand);
            }
        });

    }

    public void bindData(Model skillModel)
    {
        skillName.setText(skillModel.name_of_demand);
        skillImage.setImageResource(skillModel.image_of_demand);
    }
}
