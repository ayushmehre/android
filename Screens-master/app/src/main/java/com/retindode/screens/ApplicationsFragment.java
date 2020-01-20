package com.retindode.screens;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ApplicationsFragment extends Fragment {


    @NonNull
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @NonNull
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    private View inflate;
    private CommonRecyclerAdapter recyclerAdapter;

    public ApplicationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflate = inflater.inflate(R.layout.applications_frag, container, false);

        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        //swipeRefreshLayout.setRefreshing(true);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = inflate.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerAdapter = new CommonRecyclerAdapter(new ArrayList<>(),
                R.layout.bank_item, getActivity(), new OnBindViewListener() {
            @Override
            public void onGetView(CommonRecyclerAdapter.ViewHolder holder, int position) {
                TextView title = holder.itemView.findViewById(R.id.name);
                TextView desc = holder.itemView.findViewById(R.id.desc);
//                final BankObject bankObject = allBankObjects.get(position);
//                title.setText(bankObject.getName());
//                desc.setText(bankObject.getShortCode()+"");
//
//                if (bankObject.isActive()) {
//                    status.setBackgroundResource(R.drawable.active_circle);
//                } else {
//                    status.setBackgroundResource(R.drawable.deactive_circle);
//                }
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ContainerActivity.setFragment(new BankDetailFragment().setBankObject(bankObject), bankObject.getName());
//                        startActivity(new Intent(getActivity(), ContainerActivity.class));
//                    }
//                });
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setVisibility(View.VISIBLE);

//        if (allBankObjects.size() > 0) {
//            root.findViewById(R.id.empty).setVisibility(View.GONE);
//        }
    }
}
