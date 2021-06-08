package com.example.simpledictionary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//declaring the values.

private EditText editText;
private Button button;
private ListView listView;
private String TAG;
private final ArrayList<String> list = new ArrayList<>();

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Assigning the Values.

    editText = findViewById(R.id.edtWord);
    button = findViewById(R.id.button);
    listView = findViewById(R.id.lView);
    TAG = "Testing";

    //Setting a Click listener to button.

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Log.d(TAG, "onClick: Working " + editText.getText().toString());
            list.clear();
            listView.setAdapter(null);

            // Instantiate the RequestQueue.
            //RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

            String url ="https://api.dictionaryapi.dev/api/v2/entries/en_US/" + editText.getText().toString();

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    String mDefinition = "";
                    String mExample = "";

                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        if (jsonObject != null)
                        {
                            JSONArray meaning = jsonObject.getJSONArray("meanings");
                            for (int i = 0; i < meaning.length(); i++)
                            {
                                JSONObject elem = meaning.getJSONObject(i);
                                if (elem != null)
                                Log.d(TAG,"JSONArray list Size" + meaning.length());
                                // Size 3
                                {
                                    assert elem != null;
                                    JSONArray definitions = elem.getJSONArray("definitions");
                                    for (int j = 0; j < definitions.length(); j++)
                                    {
                                        JSONObject innerElem = definitions.getJSONObject(j);
                                        if (innerElem != null)
                                            Log.d(TAG,"JSONArray definition Size" + definitions.length());
                                            // Size 1
                                        {
                                            assert innerElem != null;
                                            mDefinition = innerElem.getString("definition");
                                            mExample = innerElem.getString("example");
//
                                            list.add(+j+1+") Definition = " +mDefinition);
                                            list.add(+j+1+") Example = " +mExample);
                                            ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this,
                                                    android.R.layout.simple_list_item_1,
                                                    list);
                                            listView.setAdapter(adapter);


                                        }
                                    }
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    Log.d(TAG, "WordMeaning = \n" + WordMeaning );
//                    Log.d(TAG, "Defination = \n" + mdefinition );
//                    Log.d(TAG, "Synonyms = \n" + msynonyms );
//                    Log.d(TAG, "Example = \n" + mexample );
//
//                    Toast.makeText(MainActivity.this, "Defination = " + mdefinition +"\n Example = " +mexample, Toast.LENGTH_LONG).show();
                }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MainActivity.this, "No Definitions Found", Toast.LENGTH_LONG).show();
//                        Log.d(TAG, "That didn't work!");
                    }
                });

                //performing RequestQueue operations using the singleton class.

                 MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
            }
        });
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}