package controllers;

import com.google.inject.Inject;
import core.ServiceClient;
import core.UrlHelper;
import play.libs.F;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nue on 7.10.2015.
 */
public class CompositionController extends Controller {

    @Inject
    private UrlHelper urlHelper;



    //TODO potom compozicne s F.Promise<Result> z COmpositionControllera
    //TODO prerob na full functional


    public F.Promise<Result> readDataNF() {

        return WS.url(urlHelper.getBackendUrl() + "backend/friends").get().
                map(response ->
                        response.getStatus() == 200 ?
                                (Result) ok(response.asJson()) : (Result) badRequest("Something bad happend")).
                recover(t -> badRequest(t.getMessage() + "\n"));

    }


    public F.Promise<Result> calculateNF() {
        final F.Promise<WSResponse> big  = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
        final F.Promise<WSResponse> small  = WS.url(urlHelper.getCaculatorUrl() + "calculation/small").get();

        final F.Promise<Boolean> successPromise = F.Promise.sequence(big, small).
                map(response ->
                        response.stream().allMatch((oneResponse) ->
                                oneResponse.getStatus() == 200));

        final Boolean success = successPromise.get(10000); // blocking !!! not good TODO

        if (success) {
            Integer bigf = big.get(10000).asJson().get("result").asInt(); // it dont wait 10s, it wait max 10 s !
            Integer smallF = small.get(10000).asJson().get("result").asInt(); // it dont wait 10s, it wait max 10 s !
            return F.Promise.pure(ok(String.valueOf(bigf + smallF)));
        } else {
            return F.Promise.pure(badRequest("Something bad happend"));
        }
    }

}
