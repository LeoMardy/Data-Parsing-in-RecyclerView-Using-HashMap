package com.leoapptechnology.levelupprogramming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leoapptechnology.levelupprogramming.databinding.ActivityMainBinding;
import com.leoapptechnology.levelupprogramming.databinding.RvLayoutBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    HashMap<String, String> hashMap;
    ArrayList<HashMap<String,String>> myArrayList = new ArrayList<>();
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.progressBar.setVisibility(View.VISIBLE);
        String url = "https://testserverleo.000webhostapp.com/apps/deloyar.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            binding.progressBar.setVisibility(View.GONE);

           for (int x=0; x<response.length(); x++){

               try {
                   JSONObject jsonObject = response.getJSONObject(x);
                   String title = jsonObject.getString("video_title");
                   String video_id = jsonObject.getString("video_id");

                   hashMap = new HashMap<>();
                   hashMap.put("title",title);
                   hashMap.put("video_id",video_id);
                   myArrayList.add(hashMap);

               } catch (JSONException e) {
                   throw new RuntimeException(e);
               }
           }

           MyAdapter myAdapter = new MyAdapter();
           LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
           layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
           binding.recyclerView.setLayoutManager(layoutManager);
           binding.recyclerView.setAdapter(myAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonArrayRequest);

    }

    /*===============================================================*/
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        private class MyViewHolder extends RecyclerView.ViewHolder{
            RvLayoutBinding binding;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                binding = RvLayoutBinding.bind(itemView);
            }
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.rv_layout,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            HashMap<String, String> hashMap = myArrayList.get(position);

            String title = hashMap.get("title");
            String video_id = hashMap.get("video_id");
              String video_url = "https://img.youtube.com/vi/"+video_id+"/0.jpg";

            holder.binding.title.setText(title);
           Picasso.get()
                   .load(video_url)
                   .placeholder(R.drawable.blankmage)
                   .into(holder.binding.imageView);
        }

        @Override
        public int getItemCount() {
            return myArrayList.size();
        }

    }
}