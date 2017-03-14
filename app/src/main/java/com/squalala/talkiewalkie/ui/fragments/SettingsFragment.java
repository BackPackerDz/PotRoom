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

import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SeekBarPreference;

import com.squalala.talkiewalkie.App;
import com.squalala.talkiewalkie.R;
import com.squalala.talkiewalkie.Session;
import com.squalala.talkiewalkie.ui.activities.BaseActivity;
import com.squalala.talkiewalkie.ui.activities.TalkActivity;

/**
 * Created by Fayçal KADDOURI on 12/03/17.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    public interface FragmentListener {
        void onHideFragment();
    }

    private FragmentListener fragmentListener;

    @Override
    public void onDestroyView() {
        fragmentListener.onHideFragment();
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = ((TalkActivity) context);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        getPreferenceManager().setSharedPreferencesName(getString(R.string.app_name));

        addPreferencesFromResource(R.xml.app_preferences);

        SeekBarPreference preference = (SeekBarPreference) findPreference("sound_volume");

        int maxVolume = ((TalkActivity) getContext()).getMaxVolume();
        preference.setMax(maxVolume);

        Session session = ((App) getActivity().getApplication()).getSession();

        if (!session.isDefaultValueSet()) {
            preference.setValue(maxVolume);
            session.setDefaultSoundVolume(true);
        }
    }
}