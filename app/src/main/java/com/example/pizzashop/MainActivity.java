package com.example.pizzashop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pizzashop.utils.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    ListView pizzaListView;
    private PizzaAdapter pizzaAdapter;
    private List<Pizza> pizzaList;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pizzaList = new ArrayList<>();
        pizzaListView = findViewById(R.id.pizzas);

        new GetPizzas().execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_favorites) {
            startActivity(new Intent(this, FavoritesActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetPizzas extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://pizzashopdata.free.bg/pizza.json";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray pizzas = jsonObj.getJSONArray("pizzas");

                    // looping through All Contacts
                    for (int i = 0; i < pizzas.length(); i++) {
                        JSONObject c = pizzas.getJSONObject(i);
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String description = c.getString("description");
                        String price = c.getString("price");

                        Pizza itemPizza = new Pizza(name, description, Float.parseFloat(price));
                        // adding contact to contact list
                        pizzaList.add(itemPizza);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pizzaAdapter = new PizzaAdapter(MainActivity.this, pizzaList);
            pizzaListView.setAdapter(pizzaAdapter);

            // Set setOnItemClickListener on ListView
            // to make the list items clickable
            pizzaListView.setOnItemClickListener((parent, view, position, id) -> {
                Pizza currentPizza = pizzaAdapter.getItem(position);

                Intent shoppingCartIntent = new Intent(getApplicationContext(), ShoppingCartActivity.class);
                Bundle extras = new Bundle();

                extras.putString("name", currentPizza.getName());
                extras.putString("pizza", currentPizza.getDescription());
                extras.putString("price", String.valueOf(currentPizza.getPrice()));

                shoppingCartIntent.putExtras(extras);
                startActivity(shoppingCartIntent);
            });
        }
    }
}