package com.personal.expensetracker.ui.home.expense;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.personal.expensetracker.R;
import com.personal.expensetracker.databinding.FragmentAddExpenseBinding;
import com.personal.expensetracker.ui.home.HomeFragment;
import com.personal.expensetracker.util.ExpenseModel;

import java.util.Date;
import java.util.UUID;

public class AddExpenseFragment extends Fragment {

    private EditText amount;
    private EditText note;
    private EditText category;
    private TextView textView;
    private RadioGroup typeIncome;
    private String addOrUpdate;
    private ExpenseModel expenseModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

//        textView = view.findViewById(R.id.testTexting);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        amount = view.findViewById(R.id.amount);
        note = view.findViewById(R.id.note);
        category = view.findViewById(R.id.category);
        typeIncome = view.findViewById(R.id.typeRadioGroup);

        Button addInput = view.findViewById(R.id.addInput);
        addInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = amount.getText().toString();
                String noteString = note.getText().toString();
                String categoryString = category.getText().toString();
                String type = "";
                int selectedRadioButtonId = typeIncome.getCheckedRadioButtonId();

                if(selectedRadioButtonId != -1) {
                    RadioButton selectedType = view.findViewById(selectedRadioButtonId);
                    type = selectedType.getText().toString();
                }

//                textView.setText("value: " + amountString + " & " + noteString + " & " + categoryString + " & " + type);

                createExpense(amountString, noteString, categoryString, type);

                Toast.makeText(getActivity(), "Successfully Add Expense!", Toast.LENGTH_SHORT).show();

                HomeFragment secondFragment = new HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, secondFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void createExpense(String amount, String note, String category, String type) {
        Date now = new Date();
        String expenseId = UUID.randomUUID().toString();

        ExpenseModel expenseModel = new ExpenseModel(expenseId, note, category, type, Long.parseLong(amount), now, FirebaseAuth.getInstance().getUid());

        FirebaseFirestore
                .getInstance()
                .collection("expenses")
                .document(expenseId)
                .set(expenseModel);
    }
}