package com.personal.expensetracker.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.personal.expensetracker.R;
import com.personal.expensetracker.ui.home.expense.AddExpenseFragment;
import com.personal.expensetracker.util.ExpenseModel;
import com.personal.expensetracker.util.ExpensesAdapter;
import com.personal.expensetracker.util.OnItemsClick;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemsClick {
    private RecyclerView recyclerView;
    private ExpensesAdapter expensesAdapter;
    Intent intent;
    private long income = 0, expense = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        expensesAdapter = new ExpensesAdapter(getActivity(), this);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setAdapter(expensesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView incomeButton = view.findViewById(R.id.addIncome);
        TextView expenseButton = view.findViewById(R.id.addExpense);

        PieChart pieChart = view.findViewById(R.id.pieChart);

        getData(pieChart);

        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start moving to another fragment
                AddExpenseFragment secondFragment = new AddExpenseFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, secondFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                // End moving to another fragment
            }
        });
        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Expense Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData(PieChart pieChart) {
        FirebaseFirestore
                .getInstance()
                .collection("expenses")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        expensesAdapter.clear();
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds : dsList) {
                            ExpenseModel expenseModel = ds.toObject(ExpenseModel.class);
                            if (expenseModel != null) {
                                if(expenseModel.getType().equals("Income")) {
                                    income += expenseModel.getAmount();
                                } else {
                                    expense += expenseModel.getAmount();
                                }
                            }
                            expensesAdapter.add(expenseModel);
                        }
                        setUpGraph(pieChart);
                    }
                });
    }

    private void setUpGraph(PieChart pieChart) {
        List<PieEntry> pieEntryList = new ArrayList<>();
        List<Integer> colorsList = new ArrayList<>();
        if(income != 0) {
            pieEntryList.add(new PieEntry(income, "Income"));
            colorsList.add(getResources().getColor(R.color.teal_700));
        }
        if(expense != 0) {
            pieEntryList.add(new PieEntry(expense, "Expense"));
            colorsList.add(getResources().getColor(R.color.teal_200));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, String.valueOf(income-expense));
        pieDataSet.setColors(colorsList);
        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    @Override
    public void onClick(ExpenseModel expenseModel) {
        Toast.makeText(getActivity(), "Data Clicked!", Toast.LENGTH_SHORT).show();
    }
}