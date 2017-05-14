package com.bvaleo.chatapp.fragments;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bvaleo.chatapp.ChatApplication;
import com.bvaleo.chatapp.R;
import com.bvaleo.chatapp.StartActivity;
import com.bvaleo.chatapp.model.Message;
import com.bvaleo.chatapp.util.MessageAdapter;
import com.bvaleo.chatapp.util.MessageTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Valery on 19.04.2017.
 */
public class MessageFragment extends Fragment {

    EditText inputMessage;
    ImageView sendMessage;
    //ImageView affixPhoto;

    Toolbar toolbar;

    Socket mSocket;
    ChatApplication app;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MessageAdapter adapter;
    List<Message> list;

    String to;

    int k = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = this.getArguments();
        if(arguments != null) {
            list = arguments.getParcelableArrayList("messages");
            to = arguments.getString("to");
        } else list = new ArrayList<>();

        app = (ChatApplication) getActivity().getApplication();
        mSocket = app.getSocket();
        mSocket.on("new message", newMessager);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.message_fragment, null);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        inputMessage = (EditText) v.findViewById(R.id.input_message);
        sendMessage = (ImageView) v.findViewById(R.id.send_message);
        //affixPhoto = (ImageView) v.findViewById(R.id.add_photo);

        recyclerView = (RecyclerView) v.findViewById(R.id.chat_list);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new MessageTouchListener(getActivity().getApplicationContext(), recyclerView, new MessageTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity().getBaseContext(), list.get(position + 1).getUsername(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



        adapter.notifyDataSetChanged();
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputMessage.getText().toString().trim();
                if(!"".equals(message)) {
                    inputMessage.setText("");
                    Message.Builder mes = new Message.Builder(1);
                    mes.username(app.getLogin())
                            .message(message)
                            .time(getTime())
                            .isMine(true);
                    list.add(mes.build());
                    adapter.notifyDataSetChanged();
                    if (adapter.getItemCount() > 1) {
                        // scrolling to bottom of the recycler view
                        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
                    }

                    mSocket.emit("new message", to, app.getLogin(), message, mes.build().getTime(), true, 1);
                }
            }
        });





        /*affixPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message.Builder mes = new Message.Builder(1);
                mes.username("Leo")
                        .message("Hello" + ++k)
                        .time("14:22")
                        .isMine(true);
                list.add(mes.build());

                adapter.notifyDataSetChanged();

                if (adapter.getItemCount() > 1) {
                    // scrolling to bottom of the recycler view
                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
                }

                //Toast.makeText(getActivity().getBaseContext(), "" + list.size(), Toast.LENGTH_LONG).show();
            }
        });*/

        return v;
    }

    Emitter.Listener newMessager = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if(getActivity() == null) {
                return;
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject js = (JSONObject) args[0];
                    Message.Builder newM;
                    try {
                        newM = new Message.Builder(js.getInt("type"));

                        newM.username(js.getString("username"))
                                .message(js.getString("message"))
                                .time(js.getString("time"))
                                .isMine(false);


                    } catch (JSONException ex){
                        Toast.makeText(getActivity().getApplicationContext(), "New Message error json", Toast.LENGTH_LONG).show();
                        return;
                    }

                    list.add(newM.build());
                    adapter.notifyDataSetChanged();
                    if (adapter.getItemCount() > 1) {
                        // scrolling to bottom of the recycler view
                        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
                    }

                }
            });
        }
    };


    private String getTime(){
        return (new SimpleDateFormat("hh:mm")).format(new Date());
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
                mSocket.off("new message", newMessager);
                Intent i = new Intent(getActivity(), StartActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
