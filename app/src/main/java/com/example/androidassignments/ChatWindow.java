package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
public class ChatWindow extends AppCompatActivity {

    protected static final String TAG = "ChatWindow";

    ListView listView;
    EditText msgTxt;
    Button sndBtn;
    ArrayList<String> chats;
    TextView msgIncoming, msgOutgoing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_chat_window);

        listView = findViewById(R.id.chatView);
        msgTxt = findViewById(R.id.msgTxt);
        sndBtn = findViewById(R.id.sndBtn);
        chats = new ArrayList<>();
        //ChatAdapter messageAdapter = new ChatAdapter(this, android.R.layout.simple_list_item_1, chats);
        ChatAdapter messageAdapter = new ChatAdapter(this);
        listView.setAdapter( messageAdapter);

        sndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chats.add(msgTxt.getText().toString());
//                ChatAdapter messageAdapter = new ChatAdapter(ChatWindow.this);
//                listView.setAdapter(messageAdapter);
                messageAdapter.notifyDataSetChanged();
                msgTxt.setText("");


            }
        });

    }


    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

//        public ChatAdapter(ChatWindow ctx, int simple_list_item_1, ArrayList<String> chats) {
//            super(ctx, 0, chats);
//        }

//        public ChatAdapter(ChatWindow ctx, int simple_list_item_1, ArrayList<String> chats) {
//            super(ctx, simple_list_item_1, chats);
//        }


        public int getCount() {
            return chats.size();
        }

        public String getItem(int position) {
            return chats.get(position);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;

            if(position%2 == 0)

                result = inflater.inflate(R.layout.chat_row_incoming, null);

            else

                result = inflater.inflate(R.layout.chat_row_outgoing, null);



            TextView message = (TextView)result.findViewById(R.id.msg);
            message.setText(   getItem(position)  ); // get the string at position
            return result;
        }


//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);
//        }
    }
}