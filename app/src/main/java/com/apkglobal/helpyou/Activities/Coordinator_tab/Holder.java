package com.apkglobal.helpyou.Activities.Coordinator_tab;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkglobal.helpyou.Activities.GetTask;
import com.apkglobal.helpyou.Activities.Helper.Configure;
import com.apkglobal.helpyou.R;

import java.util.ArrayList;

/**
 * Created by Mayank on 1/13/2018.
 */

public class Holder extends RecyclerView.ViewHolder {
    private static TextView skillName,get_service,give_service;
    private ImageView skillImage;
    Context context;
    Configure configure=new Configure();
    private final String[] Errands = {"Key Maker", "Peon", "Pet Grooming", "Senior Assistance"};
    private final String[] mTitles = {"Errands", "Events", "Healthcare", "Housekeeping"};

    public Holder(final View itemView) {
        super(itemView);
        context=itemView.getContext();

        skillImage=(ImageView)itemView.findViewById(R.id.image_of_demand);
        skillName=(TextView) itemView.findViewById(R.id.text_of_demand);
        get_service=itemView.findViewById(R.id.get_service);
        give_service=itemView.findViewById(R.id.give_service);
        ArrayList<Fragment> tfragment=configure.getTest_fragment();
        /*get_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String demand_name="";
                ArrayList<Fragment> tfragment=configure.getTest_fragment();
                for (String title : mTitles) {
                    if(tfragment.contains(DataHolder.getInstance(title)))
                    {
                        if(getAdapterPosition()==0)
                        {
                            demand_name=Errands[0];
                        }
                    }
                }
                Intent get_demand=new Intent(context,GetTask.class);
                context.startActivity(get_demand);
                get_demand.putExtra("DEMANDNAME",demand_name);
            }
        });*/

    }

    public void bindData(Model skillModel)
    {
        skillName.setText(skillModel.name_of_demand);
        skillImage.setImageResource(skillModel.image_of_demand);
    }
}
