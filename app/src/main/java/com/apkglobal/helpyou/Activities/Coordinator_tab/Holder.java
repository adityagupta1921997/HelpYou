package com.apkglobal.helpyou.Activities.Coordinator_tab;

import android.content.Context;
import android.content.Intent;
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
    private static TextView skillName,get_service,give_service;
    private ImageView skillImage;
    Context context;
    Configure configure;
    private final String[] Errands = {"Key Maker", "Peon", "Pet Grooming", "Senior Assistance"};
    private final String[] Events = {"Band", "Flower Bouquet", "Home Chef"};
    private final String[] Titles = {"Errands", "Events", "Healthcare", "Housekeeping"};

    public Holder(final View itemView) {
        super(itemView);
        context=itemView.getContext();

        skillImage=(ImageView)itemView.findViewById(R.id.image_of_demand);
        skillName=(TextView) itemView.findViewById(R.id.text_of_demand);
        get_service=itemView.findViewById(R.id.get_service);
        give_service=itemView.findViewById(R.id.give_service);
        //ArrayList<Fragment> tfragment=configure.getTest_fragment();
        get_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String demand_name="Default";
                ArrayList<Fragment> tfragment=configure.getTest_fragment();
                    if (context.getApplicationContext().equals(tfragment.get(0))) {
                        if (getAdapterPosition() == 0) {
                            demand_name = Errands[0];
                        } else if (getAdapterPosition() == 1) {
                            demand_name = Errands[1];
                        } else if (getAdapterPosition() == 2) {
                            demand_name = Errands[2];
                        } else if (getAdapterPosition() == 3) {
                            demand_name = Errands[3];
                        }
                    }
                    else if(context.getApplicationContext().equals(tfragment.get(1)))
                    {
                        if(getAdapterPosition()==0)
                        {
                            demand_name=Events[0];
                        }
                        else if(getAdapterPosition()==1)
                        {
                            demand_name=Events[1];
                        }
                        else if(getAdapterPosition()==2)
                        {
                            demand_name=Events[2];
                        }
                    }

                    //}
                //}
                Intent get_demand=new Intent(context,MyLocationUsingLocationAPI.class);
                get_demand.putExtra("DEMANDNAME",demand_name);
                context.startActivity(get_demand);

            }
        });

    }

    public void bindData(Model skillModel)
    {
        skillName.setText(skillModel.name_of_demand);
        skillImage.setImageResource(skillModel.image_of_demand);
    }
}
