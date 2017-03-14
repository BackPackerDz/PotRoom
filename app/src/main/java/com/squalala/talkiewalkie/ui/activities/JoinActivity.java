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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.squalala.talkiewalkie.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;

import static com.squalala.talkiewalkie.ui.activities.NameActivity.KEY_MODIFICATION;

/**
 * Created by Fayçal KADDOURI on 16/02/17.
 */

public class JoinActivity extends BaseActivity {

    @BindView(R.id.editRoomName)
    EditText editRoomName;

    public final static String KEY_ROOM = "room";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ButterKnife.bind(this);

        editRoomName.setText(session.getRoom());

        String roomId = getIntent().getStringExtra(KEY_ROOM);

        if (roomId != null) {
            editRoomName.setText(roomId);
            navigateToJoinActivity();
        }

    }

    @OnClick(R.id.iconSettings)
    void navitageToSettings() {

        Answers.getInstance().logCustom(new CustomEvent("Settings"));

        Intent intent = new Intent(this, NameActivity.class);
        intent.putExtra(KEY_MODIFICATION, true);
        startActivity(intent);
    }

    private String getRoomName() {
        return editRoomName.getText().toString().trim();
    }

    @DebugLog
    @OnClick(R.id.btnJoinRoom)
    void navigateToJoinActivity() {
        if (!TextUtils.isEmpty(getRoomName())) {
            session.setRoom(getRoomName());
            Intent intent = new Intent(this, TalkActivity.class);
            startActivity(intent);
        }
    }
}
