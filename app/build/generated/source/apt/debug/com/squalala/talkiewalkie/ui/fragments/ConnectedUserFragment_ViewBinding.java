// Generated code from Butter Knife. Do not modify!
package com.squalala.talkiewalkie.ui.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.squalala.talkiewalkie.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ConnectedUserFragment_ViewBinding implements Unbinder {
  private ConnectedUserFragment target;

  @UiThread
  public ConnectedUserFragment_ViewBinding(ConnectedUserFragment target, View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerViewUsers, "field 'recyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ConnectedUserFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
  }
}
