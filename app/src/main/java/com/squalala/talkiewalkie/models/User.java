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

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fayçal KADDOURI on 02/03/17.
 */

public class User {
    @SerializedName("username")
    private String username;
    @SerializedName("url_avatar")
    private String urlAvatar;

    public User(String username, String urlAvatar) {
        this.username = username;
        this.urlAvatar = urlAvatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }
}
