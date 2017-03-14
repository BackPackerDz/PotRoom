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

package com.squalala.talkiewalkie.viewholders;

import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squalala.talkiewalkie.OnClickMessageListener;
import com.squalala.talkiewalkie.R;
import com.squalala.talkiewalkie.models.Message;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Fayçal KADDOURI on 28/02/17.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.image_user_message)
    SimpleDraweeView imageUser;

    public MessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Message message, final OnClickMessageListener mListener) {

        imageUser.setImageURI(Uri.parse(message.getUrlAvatar()));

        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(true);

        if (!message.isRead()) {
            roundingParams.setBorderColor(ContextCompat.getColor(imageUser.getContext(), R.color.colorAccent));
            roundingParams.setBorderWidth(5f);
        }
        else {
            roundingParams.setBorderWidth(0);
        }

        imageUser.getHierarchy().setRoundingParams(roundingParams);

        imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickMessage(message);
            }
        });
    }


}
