package com.apkglobal.helpyou.Activities.Coordinator_tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkglobal.helpyou.Activities.Helper.Configure;
import com.apkglobal.helpyou.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugeterry(http://hugeterry.cn)
 */
public class DataHolder extends Fragment {
    private RecyclerView mRecyclerView;

    private List<Model> mDatas;
    private static final String ARG_TITLE = "title";
    private String mTitle;
    Configure configure=new Configure();
    private final String[] Errands = {"Key Maker", "Peon", "Pet Grooming", "Senior Assistance"};
    private final int[] Errands_images = {R.drawable.key_maker, R.drawable.peon, R.drawable.pet_care2, R.drawable.senior};
    private final String[] Events = {"Band", "Flower Bouquet", "Home Chef"};
    private final int[] Events_images = {R.drawable.band, R.drawable.flower, R.drawable.home_chef};
    private final String[] Healthcare = {"Baby Sitter", "Ambulance Services", "Doctor on call", "Health Check Up", "Physiotherapy"};
    private final int[] Healthcare_images = {R.drawable.baby_sitter, R.drawable.ambulance, R.drawable.doctor_on_call, R.drawable.health_checkup, R.drawable.physiotherapy};
    private final String[] Housekeeping = {"Appliance Services", "Carpentary Services", "Electrician", "Laundry Service", "Painter", "Plumber"};
    private final int[] Housekeeping_images = {R.drawable.appliance_repair, R.drawable.carpentry, R.drawable.electrician, R.drawable.laundary, R.drawable.painter, R.drawable.plumber};

    public static DataHolder getInstance(String title) {
        DataHolder fra = new DataHolder();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mTitle = bundle.getString(ARG_TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.data_holder, container, false);

        initData();
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(new RecyclerAdapter(mRecyclerView.getContext(), mDatas));

        return v;
    }

    /* private void initData() {
         mDatas = new ArrayList<>();
         for (int i = 'A'; i < 'z'; i++) {
             mDatas.add(mTitle + (char) i);
         }
     }*/
    private void initData() {
        mDatas = new ArrayList<>();
        if (mTitle.equals("Errands")) {
            for (int i = 0; i < Errands.length; i++) {

                mDatas.add(new Model(Errands[i], Errands_images[i]));
            }
        }
        else if(mTitle.equals("Events"))
        {
            for (int i = 0; i < Events.length; i++) {

                mDatas.add(new Model(Events[i], Events_images[i]));
            }
        }
        else if(mTitle.equals("Healthcare"))
        {
            for (int i = 0; i < Healthcare.length; i++) {

                mDatas.add(new Model(Healthcare[i], Healthcare_images[i]));
            }
        }
        else if(mTitle.equals("Housekeeping"))
        {
            for (int i = 0; i < Housekeeping.length; i++) {

                mDatas.add(new Model(Housekeeping[i], Housekeeping_images[i]));
            }
        }
    }
}

