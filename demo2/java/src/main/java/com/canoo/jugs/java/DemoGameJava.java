package com.canoo.jugs.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.canoo.jugs.core.DemoGame;

public class DemoGameJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("com/canoo/jugs/resources");
    PlayN.run(new DemoGame());
  }
}
