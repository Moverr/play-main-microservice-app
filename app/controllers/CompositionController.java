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


    public F.Promise<Result> readData() {

        return WS.url(urlHelper.getBackendUrl() + "backend/friends").get()
                .map(response ->
                        response.getStatus() == 200 ?
                                (Result) ok(response.asJson()) : (Result) badRequest("Something bad happend"))
                .recover(t -> badRequest(t.getMessage() + "\n"));

    }

    public F.Promise<Result> calculate() {

        final F.Promise<WSResponse> big  =
                WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
        final F.Promise<WSResponse> small  =
                WS.url(urlHelper.getCaculatorUrl() + "calculation/small").get();

        final F.Promise<List<WSResponse>> promiseSeq = F.Promise.sequence(big, small);

        return
                // is both values ok ? = have i two 200 responses ?
                promiseSeq.map(response ->
                        response.stream().allMatch((oneResponse) ->
                                oneResponse.getStatus() == 200))
                // if everything ok onSuccess otherwise onFail()
                .flatMap(success ->
                        success ? onSuccess(promiseSeq) : onFail());
    }




    private F.Promise<Result> onSuccess(F.Promise<List<WSResponse>> response) {
        return response
                .map(list ->
                         list.get(0).asJson().get("result").asInt() +
                         list.get(1).asJson().get("result").asInt()
                )
                .map(result -> ok(String.valueOf(result)));
    }

    private F.Promise<Result> onFail() {
        return F.Promise.pure(badRequest("Something bad happend"));
    }


}
