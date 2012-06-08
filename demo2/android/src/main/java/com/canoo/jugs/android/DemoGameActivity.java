package com.canoo.jugs.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.canoo.jugs.core.DemoGame;

public class DemoGameActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("com/canoo/jugs/resources");
    PlayN.run(new DemoGame());
  }
}
