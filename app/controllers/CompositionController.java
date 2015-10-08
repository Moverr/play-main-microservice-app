package controllers;

import com.google.inject.Inject;
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

//    public F.Promise<Result> calculate() {
//
//        final F.Promise<WSResponse> big  = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
//        final F.Promise<WSResponse> small  = WS.url(urlHelper.getCaculatorUrl() + "calculation/small").get();
//
//        final F.Promise<Boolean> success = F.Promise.sequence(big, small).
//                map(response ->
//                        response.stream().allMatch((oneResponse) ->
//                                oneResponse.getStatus() == 200));
//
//        Boolean successf = success.get(1000); // blocking !!! not good TODO
//
//        return success.map((x) -> // if true
//                x.booleanValue() ?
//                        F.Promise.sequence(big, small).map(x -> ok(x)) :
//                        F.Promise.pure(badRequest("Something bad happend")));
//
////        if (successf) {
////            return F.Promise.sequence(big, small).map(x ->
////                    x.stream().reduce((a, b) ->
////                            F.Promise.pure((Integer) (a.asJson().asInt() + b.asJson().asInt()))));
////        } else {
////            return F.Promise.pure(badRequest("Something bad happend"));
////        }
//
//
//        return F.Promise.sequence(big, small).
//                map(x -> {
//                        x.stream().map(response -> {
//                            Map<String, WSResponse> data = new HashMap<>(); //TODO Promise<Boolean> ?
//                            data.put("result", response);
//                            return data;
//                                                    }
//                                        );
//                            return null;
//                        }
//                ).map(Json::toJson)
//                    .map(jsonResponse -> (Result) ok(jsonResponse))
//                    .recover(t -> badRequest(t.getMessage()  + "\n"));
//
//
///**
//
//        count = listOfCalculations.filter((x) -> true).
//
//        // X -> recover(F).map(!200).(NOK).map(200).(OK)
//        listOfCalculations.
//        // WS.url("").put(JsonNode) ->> PUT
//        // WS.url("").delete() -> DELETE
//        // WS.url("").post(Jnode) -> POST
//
//        // ?
//
// **/
//    }



//    public F.Promise<Result> readData() {
//        //TODO same like calculate here !
//        return null;
//    }


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
