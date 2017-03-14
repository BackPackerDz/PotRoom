// Generated code from Butter Knife. Do not modify!
package com.squalala.talkiewalkie.ui.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.squalala.talkiewalkie.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ListRoomsActivity_ViewBinding implements Unbinder {
  private ListRoomsActivity target;

  private View view2131689610;

  private View view2131689612;

  @UiThread
  public ListRoomsActivity_ViewBinding(ListRoomsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ListRoomsActivity_ViewBinding(final ListRoomsActivity target, View source) {
    this.target = target;

    View view;
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
    target.txtCurrentRooms = Utils.findRequiredViewAsType(source, R.id.txtCurrentRooms, "field 'txtCurrentRooms'", TextView.class);
    target.viewLoading = Utils.findRequiredView(source, R.id.viewProgressRoom, "field 'viewLoading'");
    target.txtNoRoom = Utils.findRequiredViewAsType(source, R.id.txtNoRoom, "field 'txtNoRoom'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iconAbout, "method 'navigateToAbout'");
    view2131689610 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.navigateToAbout();
      }
    });
    view = Utils.findRequiredView(source, R.id.btnCreateRoom, "method 'navigateToJoinRoomActivity'");
    view2131689612 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.navigateToJoinRoomActivity();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ListRoomsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.txtCurrentRooms = null;
    target.viewLoading = null;
    target.txtNoRoom = null;

    view2131689610.setOnClickListener(null);
    view2131689610 = null;
    view2131689612.setOnClickListener(null);
    view2131689612 = null;
  }
}
