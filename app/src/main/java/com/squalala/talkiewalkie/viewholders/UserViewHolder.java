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
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squalala.talkiewalkie.R;
import com.squalala.talkiewalkie.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

/**
 * Created by Fayçal KADDOURI on 02/03/17.
 */

public class UserViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_user)
    SimpleDraweeView imageUser;

    @BindView(R.id.txtUsername)
    TextView txtUsername;

    public UserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @DebugLog
    public void bind(User user) {
        imageUser.setImageURI(Uri.parse(user.getUrlAvatar()));
        txtUsername.setText(user.getUsername());
    }
}
