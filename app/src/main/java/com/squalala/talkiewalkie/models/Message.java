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

package com.squalala.talkiewalkie.models;


import java.util.Arrays;

/**
 * Created by Fayçal KADDOURI on 17/02/17.
 */

public class Message {

    private String username;
    private String urlAvatar;
    private boolean read;
    private byte[] audio;


    public Message(String username, String urlAvatar, byte[] audio) {
        this.username = username;
        this.urlAvatar = urlAvatar;
        this.audio = audio;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getAudio() {
        return audio;
    }

    public void setAudio(byte[] audio) {
        this.audio = audio;
    }

    @Override
    public String toString() {
        return "Message{" +
                "username='" + username + '\'' +
                ", urlAvatar='" + urlAvatar + '\'' +
                ", audio=" + Arrays.toString(audio) +
                '}';
    }
}
