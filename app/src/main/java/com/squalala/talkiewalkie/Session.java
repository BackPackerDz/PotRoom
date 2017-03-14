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

package com.squalala.talkiewalkie;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

import hugo.weaving.DebugLog;

import static android.content.Context.MODE_PRIVATE;
import static com.squalala.talkiewalkie.Constants.DEFAULT_AVATAR_URL;

/**
 * Created by Fayçal KADDOURI on 16/02/17.
 */

public class Session {

    private SharedPreferences preferences;

    private static final String KEY_SOUND_VOLUME = "sound_volume";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROOM = "room";
    private static final String KEY_URL_AVATAR = "url_avatar";
    private static final String KEY_BEEP = "beep";
    private static final String KEY_MESSAGE_CONFIRMATION = "message_confirmation";
    private static final String KEY_AUTOMATIC_PLAY = "automatic_play";
    private static final String KEY_DEFAULT_SOUND_VOLUME = "sound_volume_default";


    public void setDefaultSoundVolume(boolean value) {
        preferences.edit().putBoolean(KEY_DEFAULT_SOUND_VOLUME, value).apply();
    }

    public boolean isDefaultValueSet() {
        return preferences.getBoolean(KEY_DEFAULT_SOUND_VOLUME, false);
    }

    public Session(Context context) {
        preferences = context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE);
    }

    public boolean isAutomaticPlay() {
        return preferences.getBoolean(KEY_AUTOMATIC_PLAY, true);
    }

    public boolean isMessageConfirmation() {
        return preferences.getBoolean(KEY_MESSAGE_CONFIRMATION, false);
    }

    @DebugLog
    public int getSoundVolume(AudioManager audioManager) {
        return preferences.getInt(KEY_SOUND_VOLUME, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
    }

    public void setBeep(boolean value) {
        preferences.edit().putBoolean(KEY_BEEP, value).apply();
    }

    public boolean isBeep() {
        return preferences.getBoolean(KEY_BEEP, true);
    }

    public void setUrlAvatar(String urlAvatar) {
        preferences.edit().putString(KEY_URL_AVATAR, urlAvatar).apply();
    }

    public String getUrlAvatar() {
        return preferences.getString(KEY_URL_AVATAR, DEFAULT_AVATAR_URL);
    }

    public void setRoom(String value) {
        preferences.edit().putString(KEY_ROOM, value).apply();
    }

    @DebugLog
    public String getRoom() {
        return preferences.getString(KEY_ROOM, "");
    }

    public void setUsername(String value) {
        preferences.edit().putString(KEY_USERNAME, value).apply();
    }

    public String getUsername() {
        return preferences.getString(KEY_USERNAME, "");
    }

    public void setValue(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }
}
