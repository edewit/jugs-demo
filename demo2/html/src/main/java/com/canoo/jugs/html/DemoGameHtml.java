package com.canoo.jugs.html;

import com.canoo.jugs.core.DemoGame;
import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

public class DemoGameHtml extends HtmlGame {

    @Override
    public void start() {
        HtmlPlatform platform = HtmlPlatform.register();
        platform.assets().setPathPrefix("demo/");
        PlayN.run(new DemoGame());
    }
}
