package com.example.saki99.retrofit;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAppActivity extends AppCompatActivity {

    Retrofit retrofit;
    RetrofitService service;
    RecyclerView lista;
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    public static final int REQUEST_CODE = 1;
    Toolbar toolbar;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_app);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RetrofitService.class);

        dbHelper = new DBHelper(this);
        writeToLocalDatabase();

        lista = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(linearLayoutManager);

        showAll();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ( id == R.id.menu_show_all ) {

            showAll();

        } else if ( id == R.id.menu_search ) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.dialog_box_user_id, null);

            builder.setView(view);
            builder.setCancelable(false);

            final AlertDialog dialog = builder.create();
            dialog.show();

            final EditText uid = view.findViewById(R.id.user_id);
            Button potvrdi = view.findViewById(R.id.dialog_potvrdi);

            potvrdi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Podatak podatak = dbHelper.getData(Integer.valueOf(uid.getText().toString()));
                    ArrayList<Podatak> podaci = new ArrayList<>();
                    podaci.add(podatak);

                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(podaci);
                    lista.setAdapter(adapter);

                    dialog.dismiss();
                }
            });

        } else if (id == R.id.menu_scan_qr) {


            Intent i = new Intent(RetrofitAppActivity.this, ScannerActivity.class);
            startActivityForResult(i, REQUEST_CODE);

        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == ScannerActivity.RESULT_CODE) {

            int id = (int) data.getExtras().get("id");

            Podatak podatak = dbHelper.getData(id);
            ArrayList<Podatak> podaci = new ArrayList<>();
            podaci.add(podatak);

            RecyclerViewAdapter adapter = new RecyclerViewAdapter(podaci);
            lista.setAdapter(adapter);
        }
    }

    private void writeToLocalDatabase() {
        final Call<List<Podatak>> data = service.getAllData();

        data.enqueue(new Callback<List<Podatak>>() {
            @Override
            public void onResponse(Call<List<Podatak>> call, Response<List<Podatak>> response) {
                List<Podatak> podaci = response.body();

                for (int i = 0; i < podaci.size(); i++) {
                    dbHelper.addData(podaci.get(i));
                }


            }

            @Override
            public void onFailure(Call<List<Podatak>> call, Throwable t) {

            }
        });
    }

    private void showAll() {

        List<Podatak> podaci = dbHelper.getAllData();

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(podaci);
        lista.setAdapter(adapter);
    }
}
