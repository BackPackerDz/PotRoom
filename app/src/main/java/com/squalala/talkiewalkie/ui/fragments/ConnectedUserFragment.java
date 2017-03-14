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

package com.squalala.talkiewalkie.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squalala.talkiewalkie.R;
import com.squalala.talkiewalkie.adapters.UserAdapter;
import com.squalala.talkiewalkie.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

/**
 * Created by Fayçal KADDOURI on 02/03/17.
 */

public class ConnectedUserFragment extends Fragment {


    @BindView(R.id.recyclerViewUsers)
    RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> users = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_connected, container, false);

        ButterKnife.bind(this, root);

        userAdapter = new UserAdapter(users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();

        return root;
    }


    @DebugLog
    public void updateUsersList(List<User> users) {
            this.users.clear();
            this.users.addAll(users);
            if(userAdapter != null)
                userAdapter.notifyDataSetChanged();
    }



}
