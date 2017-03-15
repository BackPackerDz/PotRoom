/*
 * Copyright (C) 2017 Fayçal Kaddouri <powervlagos@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.squalala.talkiewalkie.ui.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.LibsConfiguration;
import com.mikepenz.aboutlibraries.entity.Library;
import com.squalala.talkiewalkie.OnClickRoomListener;
import com.squalala.talkiewalkie.R;
import com.squalala.talkiewalkie.adapters.RoomAdapter;
import com.squalala.talkiewalkie.models.Room;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import io.socket.emitter.Emitter;

/**
 * Created by Fayçal KADDOURI on 27/02/17.
 */

public class ListRoomsActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.txtCurrentRooms)
    TextView txtCurrentRooms;

    @BindView(R.id.viewProgressRoom)
    View viewLoading;

    @BindView(R.id.txtNoRoom)
    TextView txtNoRoom;

    private List<Room> rooms;
    private RoomAdapter roomAdapter;

    private static final String KEY_LIST_ROOM = "list_room";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rooms);
        ButterKnife.bind(this);

        viewLoading.setVisibility(View.VISIBLE);
        txtCurrentRooms.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        txtCurrentRooms.setPaintFlags(txtCurrentRooms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        rooms = new ArrayList<>();
        roomAdapter = new RoomAdapter(rooms, new OnClickRoomListener() {
            @Override
            public void onClickRoom(Room room) {
                Answers.getInstance().logCustom(new CustomEvent("Join Room")
                        .putCustomAttribute("room", room.getRoomName()));
                Intent intent = new Intent(ListRoomsActivity.this, JoinActivity.class);
                intent.putExtra(JoinActivity.KEY_ROOM, room.getRoomName());
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(roomAdapter);
    }

    @OnClick(R.id.iconAbout)
    void navigateToAbout() {
        Answers.getInstance().logCustom(new CustomEvent("About"));

        new LibsBuilder()
                .withAutoDetect(true)
                .withLicenseShown(true)
                .withVersionShown(true)
                .withListener(libsListener)
                .withActivityTitle("Open Source")
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .start(this);
    }

    LibsConfiguration.LibsListener libsListener = new LibsConfiguration.LibsListener() {
        @Override
        public void onIconClicked(View v) {
           navigateToPlayStore();
        }

        @DebugLog
        @Override
        public boolean onLibraryAuthorClicked(View v, Library library) {
            return false;
        }

        @DebugLog
        @Override
        public boolean onLibraryContentClicked(View v, Library library) {
            return false;
        }

        @DebugLog
        @Override
        public boolean onLibraryBottomClicked(View v, Library library) {
            return false;
        }

        @DebugLog
        @Override
        public boolean onExtraClicked(View v, Libs.SpecialButton specialButton) {

            if (specialButton.equals(Libs.SpecialButton.SPECIAL1)) {
                navigateToGitHub();
            }
            else if (specialButton.equals(Libs.SpecialButton.SPECIAL2)) {
                navigateToPlayStore();
            }

            return false;
        }

        @DebugLog
        @Override
        public boolean onIconLongClicked(View v) {
            return false;
        }

        @DebugLog
        @Override
        public boolean onLibraryAuthorLongClicked(View v, Library library) {
            return false;
        }

        @DebugLog
        @Override
        public boolean onLibraryContentLongClicked(View v, Library library) {
            return false;
        }

        @DebugLog
        @Override
        public boolean onLibraryBottomLongClicked(View v, Library library) {
            return false;
        }
    };

    private void navigateToPlayStore() {
        Answers.getInstance().logCustom(new CustomEvent("PlayStore"));
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void navigateToGitHub() {
        Answers.getInstance().logCustom(new CustomEvent("Github"));
        String url = "https://github.com/BackPackerDz/PotRoom";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSocket.on(KEY_LIST_ROOM, onListRooms);
        mSocket.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSocket.disconnect();
        mSocket.off(KEY_LIST_ROOM, onListRooms);
    }

    @OnClick(R.id.btnCreateRoom)
    void navigateToJoinRoomActivity() {
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }

    private Emitter.Listener onListRooms = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println(args[0]);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Room>>(){}.getType();
            List<Room> roomList = gson.fromJson(args[0].toString(), type);
            rooms.clear();
            rooms.addAll(roomList); //rooms.addAll(roomList);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (rooms.isEmpty()) {
                        txtNoRoom.setVisibility(View.VISIBLE);
                    }
                    else {
                        txtNoRoom.setVisibility(View.GONE);
                    }

                    viewLoading.setVisibility(View.GONE);
                    txtCurrentRooms.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            });
        }
    };


}
