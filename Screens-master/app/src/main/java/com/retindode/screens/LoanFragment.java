package com.retindode.screens;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aquery.AQuery;
import com.aquery.listener.QueryNetworkListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoanFragment extends Fragment {


    @NonNull
    @BindView(R.id.card)
    CardView cardView;

    @NonNull
    @BindView(R.id.aproved_loan_amount)
    TextView aproved_loan_amount;

    @NonNull
    @BindView(R.id.GST)
    TextView gst;

    @NonNull
    @BindView(R.id.total_repayment_amount)
    TextView total_repayment_amount;

    @NonNull
    @BindView(R.id.net_disburse_amount)
    TextView net_disburse_amount;

    @NonNull
    @BindView(R.id.processing_fees)
    TextView processing_fees;

    @NonNull
    @BindView(R.id.convinience_fees)
    TextView convinience_fees;

    @NonNull
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    private View inflate;
    private CommonRecyclerAdapter recyclerAdapter;

    public LoanFragment() {
        // Required empty public constructor
    }


    String successResponse = "{\n" +
            "    \"success\": true,\n" +
            "    \"data\": {\n" +
            "        \"installments\": 1,\n" +
            "        \"interest\": 2.99,\n" +
            "        \"convenience_fees\": 100,\n" +
            "        \"gst\": 108,\n" +
            "        \"processing_fees\": 500,\n" +
            "        \"loan_amount\": \"8000\",\n" +
            "        \"status\": \"Approved\",\n" +
            "        \"due_date\": \"2020-02-01T17:05:14.515\",\n" +
            "        \"upfrontInterest\": 0,\n" +
            "        \"Net Disbursement Amount\": 7292\n" +
            "    },\n" +
            "    \"netAmount\": 7292,\n" +
            "    \"actualinterest\": 239.2,\n" +
            "    \"interestDue\": 239.2,\n" +
            "    \"repaymentAmount\": 8239.2,\n" +
            "    \"disburse\": 0,\n" +
            "    \"message\": \"Success\"\n" +
            "}";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflate = inflater.inflate(R.layout.loans_frag, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        String userObj = prefs.getString("userObj", null);

        try {
            JSONObject jsonObject = new JSONObject(userObj);
            String emailString = /*"nish.best01@gmail.com";*/jsonObject.getString("email");
            String email = "https://credin.in/app/api/get_details?email="
                    + emailString;
            new AQuery(getActivity()).ajax(email).showLoading().get().response((s, throwable) -> {
                Log.d("RESPONSE", s);

                try {
                    JSONObject jsonObject1 = new JSONObject(s);
                    if (jsonObject1.getBoolean("success")) {
                        fillData(jsonObject1);
                    } else {
                        //Toast.makeText(getActivity(), jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "No loans created", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }

            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return inflate;
    }

    private void fillData(JSONObject jsonObject) {
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            gst.setText("Rs." + data.getInt("gst"));
            aproved_loan_amount.setText("Rs." + data.getString("loan_amount"));
            net_disburse_amount.setText("Rs." + data.getInt("Net Disbursement Amount"));
            convinience_fees.setText("Rs." + data.getInt("convenience_fees"));
            processing_fees.setText("Rs." + data.getInt("processing_fees"));
            total_repayment_amount.setText("Rs." + jsonObject.getInt("netAmount"));
            cardView.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        cardView.setVisibility(View.GONE);
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
