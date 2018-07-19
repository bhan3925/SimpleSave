package com.example.simplesave;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BudgetPlan implements Serializable {

    private float budget;
    //timestamps are for firestore
    transient private Timestamp startDate;
    transient private Timestamp endDate;
    private HashMap<String, Budget> categories;
    // hashmap of arraylist, keys are the days as Integers, values are Lists of Transactions for that day
    transient private ArrayList<Transaction> transactions;

    //CONSTRUCTORS
    public BudgetPlan() {
        budget = 0;
        setStartDate(new Timestamp(new Date()));
        setEndDate(new Timestamp(new Date()));
        generateDefaultCategories();
        transactions = new ArrayList<Transaction>();
    }


    //PUBLIC

    public void addMoney(float value) {
        budget += value;
    }

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }

    public void addTransaction(String category, String name, float price, Timestamp time){
        Transaction transaction = new Transaction(category, name, price, time);
        transactions.add(transaction);
    }

    public float getBudget() {
        return budget;
    }

    public HashMap<String, Budget> getCategories() {
        return categories;
    }

    @Exclude
    public int getDaysLeft() {
       return AppLibrary.getDaysDif(AppLibrary.getToday(), endDate);
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public float getRemBudget() {
        Float remBudget = budget;
        for(Transaction t : transactions){
            remBudget -= t.getPrice();
        }
        return remBudget;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    @Exclude
    public int getTotalDays() {
        return AppLibrary.getDaysDif(startDate, endDate);
    }

    @Exclude
    public float getTotalPriceByCategory(String cat) {
        float total = 0;
        for (Transaction t : transactions) {
            if (t.getCategory().equals(cat)) {
                total += t.getPrice();
            }
        }
        return total;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    @Exclude
    public HashMap<Date, Transaction> getTransactionsMap(){
        HashMap<Date, Transaction> m = new HashMap<Date, Transaction>();
        for(Transaction t : transactions){
            m.put(t.getTimestamp().toDate(), t);
        }
        return m;
    }

    public List<Transaction>  getDayTransactions(Timestamp day) {
        List<Transaction> list = new ArrayList<>();
        for (Transaction t: getTransactions()) {
            if (AppLibrary.isDateEqual(t.getTimestamp(), day)) {
                list.add(t);
            }
        }
        return list;
    }

    public void resetTransactions() {
        ArrayList<Transaction> oldTransactions = transactions;
        transactions = new ArrayList<Transaction>();
        for (Transaction t : oldTransactions) {
            if (getDaysLeft() >= 0) {
                transactions.add(t);
            }
        }
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public void setCategories(HashMap<String, Budget> categories) {
        this.categories = categories;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = AppLibrary.getTimestampWithoutTime(endDate);
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = AppLibrary.getTimestampWithoutTime(startDate);
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    //PRIVATE

    private void generateDefaultCategories() {
        this.categories = new HashMap<String, Budget>();
        this.categories.put("Tuition", new Budget());
        this.categories.put("Rent", new Budget());
        this.categories.put("Utilities", new Budget());
        this.categories.put("Transportation", new Budget());
        this.categories.put("Food", new Budget());
        this.categories.put("Entertainment", new Budget());
    }

}
