package com.example.gridimagesearch.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.gridimagesearch.R;
import com.example.gridimagesearch.adapters.ImageResultsAdapter;
import com.example.gridimagesearch.models.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


public class SearchActivity extends Activity {
	private EditText etQuery;
	private GridView gvResults;
	private ArrayList<ImageResult> imageResults;
	private ImageResultsAdapter aImageResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        //Creates the data source
        imageResults = new ArrayList<ImageResult>();
        //Attaches the data source to an adapter
        aImageResults = new ImageResultsAdapter(this, imageResults);
        //Link the adapter to adapterview (gridview)
        gvResults.setAdapter(aImageResults);
    }


    private void setupViews(){
    	etQuery = (EditText) findViewById(R.id.etQuery);
    	gvResults = (GridView) findViewById(R.id.gvResults);
    	gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Launch the image display activity
				//Creating an intent
				Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
				//Get the image result to display
					ImageResult result = imageResults.get(position);			
				//Pass Image Result into the intent
				i.putExtra("result", result);
				//Launch the new activity
				startActivity(i);
			}
		});
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }
    // Fired when the button is pressed (android:onClick property)
    public void onImageSearch(View v){
    	
       String query = etQuery.getText().toString();
    	Toast.makeText(this,"Search for: " + query, Toast.LENGTH_SHORT).show();
    	//https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android&rsz=8
    	AsyncHttpClient client = new AsyncHttpClient();
    	String searchUrl ="https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+ query +"&rsz=8";
        client.get(searchUrl, new JsonHttpResponseHandler(){
        
        	@  Override
        	public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject response) {
        		Log.d("DEBUG", response.toString());
        		JSONArray imageResultsJson =null;
        		try {
					   imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
					   imageResults.clear(); // clear the existing images from the array in cases where its a new search
					   //When you make changes to the adapter, it does modify the underline data
					   aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		Log.i("INFO", imageResults.toString());
        		}
        	});
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    }
