package com.bvaleo.chatapp.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bvaleo.chatapp.ChatApplication;
import com.bvaleo.chatapp.R;
import com.bvaleo.chatapp.StartActivity;
import com.bvaleo.chatapp.model.Dialoge;
import com.bvaleo.chatapp.model.Message;
import com.bvaleo.chatapp.util.DialogeAdapter;
import com.bvaleo.chatapp.util.DialogeTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class DialogeFragment extends Fragment {

    private final static String lastMessage = "Пока пусто:)";

    ChatApplication app;

    Socket mSocket;
    String mUsername;

    FloatingActionButton fab;
    Toolbar toolbar;

    List<Dialoge> dialoges;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    DialogeAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (ChatApplication) getActivity().getApplication();

        mUsername = getActivity().getIntent().getStringExtra("login");
        app.setLogin(mUsername);
        mSocket = app.getSocket();
        mSocket.connect();

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new message", newMessager);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialoge_fragment, null);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        dialoges = new ArrayList<>();
        adapter = new DialogeAdapter(dialoges);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new DialogeTouchListener(getActivity().getApplicationContext(), recyclerView, new DialogeTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity().getApplicationContext(), dialoges.get(position).getName(), Toast.LENGTH_LONG).show();

                Bundle messages = new Bundle();
                messages.putParcelableArrayList("messages", (ArrayList<? extends Parcelable>) dialoges.get(position).getMessages());
                messages.putString("to", dialoges.get(position).getName());

                Fragment chat = new MessageFragment();
                chat.setArguments(messages);
                getFragmentManager().beginTransaction()
                        .replace(R.id.mainFragment, chat)
                        .addToBackStack(null)
                        .commit();

                dialoges.get(position).setUnReadCount(0);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
                mSocket.emit("print list users");
                //mSocket.emit("new message", "BValeo", "Ну наконец-то", "14:22", true, 1);
                Toast.makeText(getActivity().getApplicationContext(), "" + dialoges.size(), Toast.LENGTH_LONG).show();
            }

        });
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_logout:

                mSocket.emit("delete user", app.getLogin());
                app.setConnected(false);
                app.setLogin("");

                mSocket.disconnect();
                mSocket.off(Socket.EVENT_CONNECT, onConnect);
                mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
                mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
                mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                mSocket.off("new message", newMessager);
                Intent i = new Intent(getActivity(), StartActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void openDialog(){
        LayoutInflater inflater = LayoutInflater.from(getActivity().getBaseContext());
        View subView = inflater.inflate(R.layout.new_dialog, null);
        final EditText newNickname = (EditText)subView.findViewById(R.id.new_nickname);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Новый диалог")
                .setMessage("Введите Никнейм")
                .setView(subView)
                .create();

        builder.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String nickname = newNickname.getText().toString();
                if (!nickname.equals("")){
                    dialoges.add(new Dialoge(nickname, lastMessage, getTime(), 0, new ArrayList<Message>()));
                    adapter.notifyDataSetChanged();
                }

            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();


    }


    private String getTime(){
            return (new SimpleDateFormat("hh:mm")).format(new Date());
    }




    Emitter.Listener newMessager = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject js = (JSONObject) args[0];
                    Message.Builder newM;
                    String username = "";
                    try {
                        newM = new Message.Builder(js.getInt("type"));

                        username = js.getString("username");
                        newM.username(username)
                                .message(js.getString("message"))
                                .time(js.getString("time"))
                                .isMine(false);


                    } catch (JSONException ex){
                        Toast.makeText(getActivity().getApplicationContext(), "New Message error json", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(!dialoges.isEmpty()){
                        int index = indexOf(username);
                        if(index != -1){
                            Dialoge tmp = dialoges.get(index);
                            Message mes = newM.build();

                            tmp.setTime(mes.getTime());
                            tmp.setLastMessage(mes.getMessage());
                            tmp.setUnReadCount(tmp.getUnReadCount() + 1);

                            List<Message> list = tmp.getMessages();
                            list.add(mes);
                            tmp.setMessages(list);

                            dialoges.remove(index);
                            dialoges.add(0, tmp);
                            adapter.notifyDataSetChanged();

                        } else {
                            Message mes = newM.build();
                            List<Message> newMessages = new ArrayList<>();
                            newMessages.add(mes);
                            Dialoge tmp = new Dialoge(mes.getUsername(), mes.getMessage(), mes.getTime(), 1,newMessages);

                            dialoges.add(0, tmp);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Message mes = newM.build();
                        List<Message> newMessages = new ArrayList<>();
                        newMessages.add(mes);
                        Dialoge tmp = new Dialoge(mes.getUsername(), mes.getMessage(), mes.getTime(), 1,newMessages);

                        dialoges.add(tmp);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };

    private int indexOf(String username){
        for(int i = 0; i < dialoges.size(); i++){
            if(dialoges.get(i).getName() == username) return i;
        }

        return -1;
    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!app.isConnected()) {
                        if(null != mUsername)
                            mSocket.emit("register user", mUsername);
                        Toast.makeText(getActivity().getApplicationContext(), "Connect is done", Toast.LENGTH_LONG).show();
                        app.setConnected(true);
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    app.setConnected(false);
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Connecting error - disconnect", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Connecting error - connect", Toast.LENGTH_LONG).show();
                }
            });
        }
    };
}
