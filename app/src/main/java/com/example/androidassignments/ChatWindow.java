package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
    //ArrayList<String> chats;
    ArrayList<String> chats = new ArrayList<>();
    FrameLayout frameLayout;
    ChatDatabaseHelper dbHelper;
    static SQLiteDatabase database;
    Cursor cursor;
    MessageFragment fragment;
    FragmentTransaction fragmentTransaction;
    ChatAdapter messageAdapter;
    static final String[] columns = {ChatDatabaseHelper.KEY_MESSAGE};
    static final String GET_MESSAGES = "SELECT _id, message FROM MessagesTable";


    TextView msgIncoming, msgOutgoing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_chat_window);
        dbHelper = new ChatDatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        listView = findViewById(R.id.chatView);
        msgTxt = findViewById(R.id.msgTxt);
        sndBtn = findViewById(R.id.sndBtn);


        frameLayout = findViewById(R.id.frameLayout);
        Log.d(TAG,Boolean.toString(frameLayout == null));
        //chats = new ArrayList<>();

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter( messageAdapter);


        //cursor = database.query(ChatDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        cursor = database.rawQuery(GET_MESSAGES,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            chats.add(cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(TAG,"SQL_MESSAGE: " + cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();

        }

        Log.i(TAG, "Cursor's column count =" + cursor.getColumnCount());

        for(int i = 0; i<cursor.getColumnCount();i++) {
            Log.i("ChatWindow.java",cursor.getColumnName(i));
        }

        //cursor.close();



        sndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chats.add(msgTxt.getText().toString());
                ContentValues values = new ContentValues();
                values.put(ChatDatabaseHelper.KEY_MESSAGE,msgTxt.getText().toString());
                database.insert(ChatDatabaseHelper.TABLE_NAME,null,values);
                messageAdapter.notifyDataSetChanged();

                msgTxt.setText("");



            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> result, View view, int position, long id) {
                String m = cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE));
                if(frameLayout != null){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragment = new MessageFragment(ChatWindow.this);
                    Bundle arguments = new Bundle();
                    arguments.putString("message",m);
                    arguments.putLong("id",id);
                    fragment.setArguments(arguments);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout,fragment).addToBackStack(null).commit();


                }
                else{
                    Bundle arguments = new Bundle();
                    Log.i("passing message", m);
                    arguments.putString("message",m);
                    arguments.putLong("id",id);
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtras(arguments);
                    startActivityForResult(intent,10);
                }
            }

        });

    }


    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public long getItemId(int position) {
            super.getItemId(position);

//            if (position >= cursor.getCount()){
                cursor = database.rawQuery(GET_MESSAGES,null);
            //}


            cursor.moveToPosition(position);
            long id = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_ID)));
            return id ;

        }
        public int getCount() {
            return chats.size();
        }

        public String getItem(int position) {
            return chats.get(position);
        }



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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            String row = data.getStringExtra("id");
            Log.i("delete row: ", ""+row);
            database.delete(ChatDatabaseHelper.TABLE_NAME,ChatDatabaseHelper.KEY_ID+ "="+row,null);
            chats.remove(Integer.parseInt(row)-1);
            messageAdapter.notifyDataSetChanged();
        }

    }

    void deleteRow(int row){
        FragmentManager fragmentManager = getSupportFragmentManager();
        database.delete(ChatDatabaseHelper.TABLE_NAME,ChatDatabaseHelper.KEY_ID+ "="+row,null);
        chats.remove(row-1);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment).commit();
        messageAdapter.notifyDataSetChanged();
    }
}