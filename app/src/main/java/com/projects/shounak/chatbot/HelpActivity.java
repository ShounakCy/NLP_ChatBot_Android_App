package com.projects.shounak.chatbotv3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import org.apache.commons.codec.language.bm.Lang;

public class HelpActivity extends AppCompatActivity {

    ListView listview ;
    String[] ListViewItems = new String[] {
            "English",
            "Hindi",
            "Bengali",
//            "Telugu",
//            "Tamil",
//            "Malayalam",
//            "Gujrati",
//            "Urdu",
              };

    SparseBooleanArray sparseBooleanArray ;
    private static String Lang ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.whitelogo_foreground);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        listview = (ListView)findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (HelpActivity.this,
                        android.R.layout.simple_list_item_multiple_choice,
                        android.R.id.text1, ListViewItems );

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                sparseBooleanArray = listview.getCheckedItemPositions();

                String ValueHolder = "" ;


                int i = 0 ;

                ValueHolder = ListViewItems [ sparseBooleanArray.keyAt(i) ] ;


                ValueHolder = ValueHolder.replaceAll("(,)*$", "");
                Log.d("ValueHolder1",ValueHolder);
                if (ValueHolder.equalsIgnoreCase("English"))
                {
                    Lang = "en-IN";
                }
                if (ValueHolder.equalsIgnoreCase("Hindi"))
                {
                    Lang = "hi-IN";
                }
                if (ValueHolder.equalsIgnoreCase("Bengali"))
                {
                    Lang = "bn-IN";
                }
                if (ValueHolder.equalsIgnoreCase("Telugu"))
                {
                    Lang = "te-IN";
                }
                if (ValueHolder.equalsIgnoreCase("Tamil"))
                {
                    Lang = "ta-IN";
                }
                if (ValueHolder.equalsIgnoreCase("Malayalam"))
                {
                    Lang = "ml-IN";
                }
                if (ValueHolder.equalsIgnoreCase("Gujrati"))
                {
                    Lang = "gu-IN";
                }
                if (ValueHolder.equalsIgnoreCase("Urdu"))
                {
                    Lang = "ur-PK";
                }

                Toast.makeText(HelpActivity.this, "Language Selected :  " + ValueHolder, Toast.LENGTH_SHORT).show();


            }
        });



    }

    public static String getLang(){
        return Lang;
    }

}