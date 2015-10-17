package core;

import com.ybrikman.ping.javaapi.dedupe.DedupingCache;
import play.libs.F;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.inject.Inject;

/**
 * Created by nue on 17.10.2015.
 */
public class ServiceClient {


    private final WSClient ws;
    private final DedupingCache<String, F.Promise<WSResponse>> cache;


    @Inject
    public ServiceClient(WSClient ws, DedupingCache<String, F.Promise<WSResponse>> cache) {
        this.ws = ws;
        this.cache = cache;
    }


    /**
     *
     * dedupe service call with using cache !
     *
     */
    public F.Promise<WSResponse> dedupeCall(String url) {
        return cache.get(url, () -> ws.url(url).get()); // scala underHood :) like many parts of this usefull lib
    }

}
