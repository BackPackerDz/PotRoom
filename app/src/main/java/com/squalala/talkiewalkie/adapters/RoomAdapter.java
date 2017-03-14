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

package com.squalala.talkiewalkie.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squalala.talkiewalkie.OnClickRoomListener;
import com.squalala.talkiewalkie.R;
import com.squalala.talkiewalkie.models.Room;
import com.squalala.talkiewalkie.viewholders.RoomViewHolder;

import java.util.List;

/**
 * Created by Fayçal KADDOURI on 27/02/17.
 */

public class RoomAdapter extends RecyclerView.Adapter<RoomViewHolder> {

    private List<Room> rooms;
    private OnClickRoomListener mListener;

    public RoomAdapter(List<Room> rooms, OnClickRoomListener mListener) {
        this.rooms = rooms;
        this.mListener = mListener;
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.bind(room, mListener);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }
}
