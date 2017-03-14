// Generated code from Butter Knife. Do not modify!
package com.squalala.talkiewalkie.viewholders;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.squalala.talkiewalkie.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RoomViewHolder_ViewBinding implements Unbinder {
  private RoomViewHolder target;

  @UiThread
  public RoomViewHolder_ViewBinding(RoomViewHolder target, View source) {
    this.target = target;

    target.txtNumberPerson = Utils.findRequiredViewAsType(source, R.id.txtNumberPerson, "field 'txtNumberPerson'", TextView.class);
    target.txtNameRoom = Utils.findRequiredViewAsType(source, R.id.txtNameRoom, "field 'txtNameRoom'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RoomViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txtNumberPerson = null;
    target.txtNameRoom = null;
  }
}
