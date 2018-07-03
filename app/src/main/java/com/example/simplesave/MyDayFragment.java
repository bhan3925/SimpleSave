package com.example.simplesave;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MyDayFragment extends Fragment {

//    public static int dailyAve = TransactionsActivity.remBudget / TransactionsActivity.remTime;
//    public static int dailyRem = TransactionsActivity.remBudget / TransactionsActivity.remTime;

    TextView balance;
    TextView average;
    TextView remDays;
    TextView dailyLimit;
    TextView transactions;
    private static double dailyAve = Main2Activity.budgetplan.getRemBudget() / Main2Activity.budgetplan.getDaysLeft();

    public MyDayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_daily, container, false);
        balance = (TextView) view.findViewById(R.id.remBalance);
        average = (TextView) view.findViewById(R.id.average);
        remDays = (TextView) view.findViewById(R.id.remDays);
        dailyLimit = (TextView) view.findViewById(R.id.dailyLimit);
        transactions = (TextView) view.findViewById(R.id.transactions);
        setDisplay();
        setListeners(view);
        return view;
    }

    public void setDisplay() {
        double dailyRem = dailyAve;
        String text = "";
        for (int i = 0; i < Main2Activity.budgetplan.getTransactions().size(); i++) {
            Transaction t = Main2Activity.budgetplan.getTransactions().get(i);
            if (t.getDate() == Main2Activity.budgetplan.getCurrentDay()) {
                dailyRem -= t.getPrice();
                text += "$" + t.getPrice() + "  " + t.getName() + "\n";
            }
        }
        balance.setText("$" + Main2Activity.budgetplan.getRemBudget());
        average.setText("$" + Math.round(dailyAve * 100.0) / 100.0);
        remDays.setText(Main2Activity.budgetplan.getDaysLeft() + " days");
        dailyLimit.setText("$" + Math.round(dailyRem * 100.0) / 100.0);
        transactions.setText(text);
    }

    public void setListeners(View view) {
        Button addMoney = (Button) view.findViewById(R.id.increase);
        addMoney.setOnClickListener(addMoneyListener);
        Button addTrans = (Button) view.findViewById(R.id.addTrans);
        addTrans.setOnClickListener(addTransListener);
        Button nextDay = (Button) view.findViewById(R.id.nextDay);
        nextDay.setOnClickListener(nextDayListener);
    }



    private View.OnClickListener addTransListener = new View.OnClickListener() {
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View mView = getLayoutInflater().inflate(R.layout.add_transaction_dialog, null);
            final EditText price = (EditText) mView.findViewById(R.id.price);
            final EditText title = (EditText) mView.findViewById(R.id.title);
            Button button = (Button) mView.findViewById(R.id.addTrans);
            builder.setView(mView);
            builder.create();
            final AlertDialog display = builder.show();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = title.getText().toString();
                    float val = Float.valueOf(price.getText().toString());
                    Main2Activity.budgetplan.addTransaction(name, val);
                    setDisplay();
                    display.dismiss();

                }
            });
        }
    };


    private View.OnClickListener addMoneyListener = new View.OnClickListener() {
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View mView = getLayoutInflater().inflate(R.layout.add_money_dialog, null);
            final EditText value = (EditText) mView.findViewById(R.id.add);
            Button button = (Button) mView.findViewById(R.id.addMoney);
            builder.setView(mView);
            builder.create();
            final AlertDialog display = builder.show();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    float val = Float.valueOf(value.getText().toString());
                    Main2Activity.budgetplan.addMoney(val);
                    dailyAve = Main2Activity.budgetplan.getRemBudget() / Main2Activity.budgetplan.getDaysLeft();
                    Toast.makeText(getContext(), "Added Money!", Toast.LENGTH_SHORT).show();
                    setDisplay();
                    display.dismiss();
                }
            });
        }
    };

    private View.OnClickListener nextDayListener = new View.OnClickListener() {
        public void onClick(View view) {
            Main2Activity.budgetplan.nextDay();
            dailyAve = Main2Activity.budgetplan.getRemBudget() / Main2Activity.budgetplan.getDaysLeft();
            Toast.makeText(getContext(), "Next Day!", Toast.LENGTH_SHORT).show();
            setDisplay();
        }
    };



}
