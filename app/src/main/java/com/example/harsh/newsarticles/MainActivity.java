package com.example.harsh.newsarticles;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.harsh.model.Article;
import com.example.harsh.model.Articles;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText edt_keyword;
    Button btn_search;
    ArrayList<Article> articleArray;
    ConnectivityManager cm;
    boolean isConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_keyword = findViewById(R.id.edt_query);
        btn_search = findViewById(R.id.btn_search);


        final GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                String que = edt_keyword.getText().toString();

                System.out.println("Key :"+que);

                if (que.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Keyword!",Toast.LENGTH_LONG).show();
                }else
                {
                    if (!isConnected)
                    {
                        Toast.makeText(getApplicationContext(),"Please Check Internet!",Toast.LENGTH_LONG).show();

                    }else
                    {
                        Call<Articles> call =service.getAllArticles(que,"2019-09-03","popularity",getResources().getString(R.string.api_key));

                        call.enqueue(new Callback<Articles>() {
                            @Override
                            public void onResponse(Call<Articles> call, Response<Articles> response) {

                                System.out.println("Check response : "+response.body());

                                Articles articles = response.body();

                                articleArray = new ArrayList<>(articles.getArticles());

                                int size = articleArray.size();

                                System.out.println("Size :"+size);

                                if (size != 0)
                                {
                                    for (int i=0;i<articleArray.size();i++)
                                    {

                                        if(!articleArray.get(i).getSource().getName().contains("Engadget") && !articleArray.get(i).getSource().getName().contains("Lifehacker"))
                                        {
                                            Intent in = new Intent(MainActivity.this,Articleloader.class);
                                            in.putExtra("url",articleArray.get(i).getUrl());
                                            startActivity(in);
                                            break;
                                        }
                                    }
                                }else {
                                    Toast.makeText(getApplicationContext(), "Article Not Found!", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<Articles> call, Throwable t) {

                                System.out.println(t.getMessage());
                                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }

            }
        });

    }

}
