package com.example.capston.Fragment;


import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.capston.DetailNewsActivity;
import com.example.capston.Function;
import com.example.capston.ListNewsAdapter;
import com.example.capston.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPhoto extends Fragment {

    String API_KEY = "f4392932cbb54687be393abc580cf8cc"; // ### YOUE NEWS API HERE ###
    String NEWS_SOURCE = "techcrunch"; // Other news source code at: https://newsapi.org/sources
    ListView listNews;
    ProgressBar loader;

    ArrayList<HashMap<String, String>> dataList = new ArrayList<>();
    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";

    public FragmentPhoto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_photo, container, false);

        listNews = view.findViewById(R.id.listNews);
        loader = view.findViewById(R.id.loader);
        listNews.setEmptyView(loader);


        if (Function.isNetworkAvailable(getContext())) {
            DownloadNews newsTask = new DownloadNews();
            newsTask.execute();
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        return view;
    }

        class DownloadNews extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() { super.onPreExecute(); }

            protected String doInBackground(String... args) {
                String xml = Function.excuteGet("https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=" + API_KEY);
                return xml;
            }

            @Override
            protected void onPostExecute(String xml) {

                if (xml.length() > 10) { // Just checking if not empty

                    try {
                        JSONObject jsonResponse = new JSONObject(xml);
                        JSONArray jsonArray = jsonResponse.optJSONArray("articles");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<>();
                            map.put(KEY_AUTHOR, jsonObject.optString(KEY_AUTHOR));
                            map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE));
                            map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION));
                            map.put(KEY_URL, jsonObject.optString(KEY_URL));
                            map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE));
                            map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT));
                            dataList.add(map);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }

                    ListNewsAdapter adapter = new ListNewsAdapter(getActivity(), dataList);
//                    listNews.setDivider(new GradientDrawable();
//                    listNews.setDividerHeight(5);
                    listNews.setAdapter(adapter);

                    listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Intent i = new Intent(getActivity(), DetailNewsActivity.class);
                            i.putExtra("url", dataList.get(+position).get(KEY_URL));
                            startActivity(i);
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "No news found", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
