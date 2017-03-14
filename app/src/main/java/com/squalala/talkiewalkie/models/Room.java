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
 * Created by Fayçal KADDOURI on 27/02/17.
 */

public class Room {

    @SerializedName("roomName")
    private String roomName;
    @SerializedName("numberOfUsers")
    private String numberOfUsers;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }


    public String getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(String numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }
}
