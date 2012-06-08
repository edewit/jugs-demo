package com.canoo.jugs.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.canoo.jugs.core.DemoGame;

public class DemoGameHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("demo/");
    PlayN.run(new DemoGame());
  }
}
