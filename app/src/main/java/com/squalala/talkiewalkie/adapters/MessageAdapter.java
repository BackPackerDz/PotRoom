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

import com.squalala.talkiewalkie.OnClickMessageListener;
import com.squalala.talkiewalkie.R;
import com.squalala.talkiewalkie.models.Message;
import com.squalala.talkiewalkie.viewholders.MessageViewHolder;
import com.squalala.talkiewalkie.viewholders.RoomViewHolder;

import java.util.List;

/**
 * Created by Fayçal KADDOURI on 28/02/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<Message> messages;
    private OnClickMessageListener mListener;

    public MessageAdapter(List<Message> messages, OnClickMessageListener mListener) {
        this.messages = messages;
        this.mListener = mListener;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message, mListener);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


}
