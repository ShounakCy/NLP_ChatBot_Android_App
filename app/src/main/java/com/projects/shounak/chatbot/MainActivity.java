package com.projects.shounak.chatbotv3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentManager;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.content.res.AssetManager;

import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;

import org.alicebot.ab.MagicStrings;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.projects.shounak.chatbotv3.R.drawable.ic_mic_background;


public class MainActivity extends AppCompatActivity {

    //private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private ImageView buttonSend;
    private ImageButton btnSpeak;
    private boolean side = true;
    public int pos;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    GetDataService getDataService;
    private TextToSpeech t1;
    private String lang_code = "en-IN";
//    private String selectedItemText = "en-IN";
    private String mobile_num = "9650755659";
    private String user_code = "CSA1056";
    long currentTime = Calendar.getInstance().getTimeInMillis();
    private String session_id = Long.toString(currentTime)+user_code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.whitelogo_foreground);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getDataService = RetrofitInstance.getRetrofitInstance().create(GetDataService.class);

        setContentView(R.layout.activity_main);
        buttonSend = (ImageView) findViewById(R.id.send);
        btnSpeak = findViewById(R.id.mic);
        listView = (ListView) findViewById(R.id.msgview);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        chatText = (EditText) findViewById(R.id.msg);

        if (savedInstanceState != null) {
            ArrayList<ChatMessage> values = savedInstanceState.getParcelableArrayList("key");
            if (values != null) {
                chatArrayAdapter.addAll(values);
                chatArrayAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Retrieved data", Toast.LENGTH_LONG).show();
            }
        }
        listView.setAdapter(chatArrayAdapter);


             t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
             {
                    @Override
                    public void onInit(int status)
                    {
                    }
             }
             );
    }



    @Override
    public void onStart() {
        super.onStart();

        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                t1.stop();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, HelpActivity.getLang());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,getString(R.string.speech_prompt));
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.speech_not_supported),
                            Toast.LENGTH_SHORT).show();
                }
                //sendMicMessage();
            }
        });


        listView.setAdapter(chatArrayAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentManager delmanager = getFragmentManager();
                DeleteMessage delDialogFragment = new DeleteMessage();
                delDialogFragment.show(delmanager, "theDialog");
                pos = position;
                return false;
            }
        });
    }



    public void delMessageBool() {
        chatArrayAdapter.removeThis(pos);
        chatArrayAdapter.notifyDataSetChanged();
        listView.setAdapter(chatArrayAdapter);
    }

    //initialise menu
    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//       getMenuInflater().inflate(R.menu.ic_mic_background, menu);
        getMenuInflater().inflate(R.menu.menu_main1, menu);
        return true;
    }

    //Assigning actions to menu item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:
                //Clear data
                FragmentManager clearmanager = getFragmentManager();
                ClearDialogFragment clearDialogFragment = new ClearDialogFragment();
                clearDialogFragment.show(clearmanager, "theDialog");
                return true;
            case R.id.language:
                //open  activity
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
                return true;
            case R.id.about:
                //open help activity
                Intent aboutintent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(aboutintent);
                return true;
            case R.id.exit:
                FragmentManager exitmanager = getFragmentManager();
                ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                exitDialogFragment.show(exitmanager, "theDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public boolean sendChatMessage() {
        t1.stop();
        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        // READ THE CHATTEXT HERE
        String comp = chatText.getText().toString();
        //bot reply
        chatText.setText("");
//            String temp = mainFunction(comp);
//        if (lang_code != "en-IN"){
//        if (HelpActivity.getLang() == "")
//        lang_code=HelpActivity.getLang();
        if (HelpActivity.getLang() == ""){
            lang_code = "en-IN";
        }else{lang_code=HelpActivity.getLang();}
//    }
        callApiForLang(comp);
        return true;
    }
//    public boolean sendMicMessage() {
//        t1.stop();
//        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
//        // READ THE CHATTEXT HERE
//        String comp = chatText.getText().toString();
//        //bot reply
//        chatText.setText("");
////            String temp = mainFunction(comp);
//        lang_code=HelpActivity.getLang();
//        callApiForLang(comp);
//        return true;
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        pgsBar.setVisibility(View.GONE);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    chatArrayAdapter.add(new ChatMessage(side, result.get(0)));
                    if (HelpActivity.getLang() == ""){
                        lang_code = "en-IN";
                    }else{lang_code=HelpActivity.getLang();}
                    callApiForLang(result.get(0));
                }
                break;
            }
        }
    }
    private void callApiForLang(String msg) {
        final TempRequest data = new TempRequest(msg, lang_code, session_id, mobile_num, user_code);
        Call<TempResponse> calll = getDataService.geSignTempCall(data);
        calll.enqueue(new Callback<TempResponse>() {
                          @Override
                          public void onResponse(Call<TempResponse> call, Response<TempResponse> response) {

                              final TempResponse loginResponse = response.body();
                              if (loginResponse != null) {
                                  session_id = loginResponse.getSession_id();
                                String OutputText = loginResponse.getMsg();
                                String[] separated = OutputText.split("\n\n");

                                  final String s = separated[separated.length-1];
                              t1.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
                                chatArrayAdapter.add(new ChatMessage(!side, loginResponse.getMsg()));

                              } else if (loginResponse == null) {
//                              pgsBar.setVisibility(View.GONE);
                                  Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                              }
                          }

                          @Override
                          public void onFailure(Call<TempResponse> call, Throwable t) {
//                          pgsBar.setVisibility(View.GONE);
                              Toast.makeText(MainActivity.this, "Please exit and try after sometime !", Toast.LENGTH_SHORT).show();

                          }
                      }
        );
    }


    public void clearchat() {
        chatArrayAdapter.clearData();
        chatArrayAdapter.notifyDataSetChanged();
        listView.setAdapter(chatArrayAdapter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (t1 != null) {
            t1.stop();
            t1.shutdown();

        }
    }



}