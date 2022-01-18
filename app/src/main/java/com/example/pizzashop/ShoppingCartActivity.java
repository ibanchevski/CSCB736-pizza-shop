package com.example.pizzashop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pizzashop.utils.DatabaseHelper;

public class ShoppingCartActivity extends AppCompatActivity {

    Bundle extras;
    Pizza selectedPizza;

    TextView pizzaName;
    TextView pizzaDetails;
    TextView pizzaPrice;
    EditText userName;

    DatabaseHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        extras = getIntent().getExtras();

        pizzaName = (TextView) findViewById(R.id.pizza_name);
        pizzaDetails = (TextView) findViewById(R.id.pizza_details);
        pizzaPrice = (TextView) findViewById(R.id.pizza_price);
        userName = (EditText) findViewById(R.id.user_name);

        String name = extras.getString("name");
        String desc = extras.getString("pizza");
        String price = extras.getString("price");
        selectedPizza = new Pizza(name, desc, Float.parseFloat(price));

        pizzaName.setText(name);
        pizzaDetails.setText(desc);
        pizzaPrice.setText(price);

        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
    }

    @Override
    protected void onDestroy() {
        database.close();
        dbHelper.close();
        super.onDestroy();
    }

    public void onToppingSelect(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.t_cheese:
                if (checked) {
                    selectedPizza.setPrice(selectedPizza.getPrice() + 1);
                } else {
                    selectedPizza.setPrice(selectedPizza.getPrice() - 1);
                }
                break;
            case R.id.t_mushrooms:
                if (checked) {
                    selectedPizza.setPrice(selectedPizza.getPrice() + 2);
                } else {
                    selectedPizza.setPrice(selectedPizza.getPrice() - 2);
                }
                break;
        }

        pizzaPrice.setText(String.valueOf(selectedPizza.getPrice()));
    }

    public void addToFavorites(View view) {
        String selectQ = "SELECT * FROM "
                + DatabaseHelper.TABLE_NAME
                + " WHERE "
                + DatabaseHelper.COLUMN_PIZZA_NAME
                + " = '" + selectedPizza.getName() + "'";
        Cursor dataCursor = database.rawQuery(selectQ , null);

        if (dataCursor.getCount() == 0) {
            // Insert the pizza if it is not already in favorites
            String query = String.format(
                    "INSERT into %s(%s, %s, %s) values('%s', '%s', %s)",
                    DatabaseHelper.TABLE_NAME,
                    DatabaseHelper.COLUMN_PIZZA_NAME,
                    DatabaseHelper.COLUMN_PIZZA_DESC,
                    DatabaseHelper.COLUMN_PIZZA_PRICE,
                    selectedPizza.getName(),
                    selectedPizza.getDescription(),
                    (int)selectedPizza.getPrice()
            );
            database.execSQL(query);
        }

        dataCursor.close();

        Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
    }

    public void onOrder(View view) {
        if (userName.getText().length() == 0) {
            Toast.makeText(this, "Please, enter your name!", Toast.LENGTH_SHORT).show();
            return;
        }

        String orderInfo = String.format(
                "Sending order for %s \n To: %s \n Total: %s",
                selectedPizza.getName() + "(" + selectedPizza.getDescription() + ")",
                userName.getText(),
                selectedPizza.getPrice()
        );

        Toast.makeText(this, orderInfo, Toast.LENGTH_LONG).show();

        startActivity(new Intent(this, MainActivity.class));

        this.sendOrderEmail(orderInfo);
    }

    protected void sendOrderEmail(String orderInfo) {
        Intent mailIntent = new Intent(Intent.ACTION_SEND);

        mailIntent.setType("message/rfc822");
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "New order");
        mailIntent.putExtra(Intent.EXTRA_TEXT, orderInfo);

        try {
            startActivity(Intent.createChooser(mailIntent, "Send order email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}