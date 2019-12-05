package com.example.chatBot;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chatBot.apibot.ChatterBot;
import com.example.chatBot.apibot.ChatterBotFactory;
import com.example.chatBot.apibot.ChatterBotSession;
import com.example.chatBot.apibot.ChatterBotType;
import com.example.chatBot.data.Message;
import com.example.chatBot.view.MainActivityViewModel;
import com.example.chatBot.view.RecyclerViewAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity
{
    private MainActivityViewModel mainActivityViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init()
    {
        //View Models
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerViewAdapter = mainActivityViewModel.getRecyclerViewAdapter();

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.smoothScrollToPosition(recyclerViewAdapter.getItemCount());


        //Boton enviar
        final EditText etText = findViewById(R.id.etText);
        Button btSend = findViewById(R.id.btSend);
        btSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String text = etText.getText().toString();
                if(text.length() > 0 && !mainActivityViewModel.isWaitingResponse())
                {
                    addMessage(true, text);
                    etText.setText("");

                    mainActivityViewModel.setWaitingResponse(true);
                    mainActivityViewModel.translate("auto-detect", text, "en");
                }
            }
        });

        //Cuando se traduce algo
        mainActivityViewModel.setOnTranslationResultListener(new MainActivityViewModel.OnTranslationResult()
        {
            @Override
            public void OnTranslationResult(boolean ok, String text, String countryCode)
            {
                if(ok)
                {
                    if(mainActivityViewModel.isWaitingBotTranslation())
                    {
                        addMessage(false, text);
                        mainActivityViewModel.setWaitingResponse(false);
                        mainActivityViewModel.setWaitingBotTranslation(false);
                    }
                    else
                    {
                        mainActivityViewModel.setTranslateCountryCode(countryCode);
                        new BotChat().execute(text);
                    }
                }
                else
                {
                    addMessage(false, "¡Error!");
                    mainActivityViewModel.setWaitingResponse(false);
                    mainActivityViewModel.setWaitingBotTranslation(false);
                }
            }
        });
    }

    private void addMessage(boolean outcoming, String text)
    {
        recyclerViewAdapter.addMessage(new Message(outcoming, text, mainActivityViewModel.getShortTime()));
        recyclerView.smoothScrollToPosition(recyclerViewAdapter.getItemCount());


    }



    private class BotChat extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            return chat(strings[0]);
        }

        @Override
        protected void onPostExecute(String s)
        {
            mainActivityViewModel.translate("en", s, mainActivityViewModel.getTranslateCountryCode());
            mainActivityViewModel.setWaitingBotTranslation(true);
        }
    }

    private String chat(String message)
    {
        String response = "";
        try
        {
            ChatterBotFactory factory = new ChatterBotFactory();
            ChatterBot bot = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
            ChatterBotSession botSession = bot.createSession();
            response = botSession.think(message);
        }
        catch(Exception e)
        {
            response = "No hemos podido contactar con  ¿tienes conexión a internet?";
        }
        return response;
    }
}
