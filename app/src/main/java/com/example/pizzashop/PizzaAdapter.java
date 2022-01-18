package com.example.pizzashop;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PizzaAdapter extends ArrayAdapter<Pizza> {
    public PizzaAdapter(Context context, List<Pizza> pizzaList) {
        super(context, 0, pizzaList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View pizzaListView = convertView;
        if (pizzaListView == null) {
            pizzaListView = LayoutInflater.from(getContext()).inflate(R.layout.pizza_item, parent, false);
        }

        Pizza currentPizza = getItem(position);

        TextView pizzaTitle = pizzaListView.findViewById(R.id.title);
        pizzaTitle.setText(currentPizza.getName());

        TextView pizzaDesc = pizzaListView.findViewById(R.id.description);
        pizzaDesc.setText(currentPizza.getDescription());

        TextView pizzaPrice = pizzaListView.findViewById(R.id.price);
        pizzaPrice.setText(String.valueOf(currentPizza.getPrice()));

        return pizzaListView;
    }
}
