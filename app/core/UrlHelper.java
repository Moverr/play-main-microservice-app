package core;

import play.Play;

/**
 * Created by nue on 6.10.2015.
 */
public class UrlHelper {

    public String getBackendUrl() {
        return Play.application().configuration().getString("rest.url.backend");
    }

    public String getCaculatorUrl() {
        return Play.application().configuration().getString("rest.url.calculator");
    }

}
