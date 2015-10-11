package controllers;

import com.google.inject.Inject;

import com.ybrikman.ping.javaapi.bigpipe.*;
import com.ybrikman.ping.javaapi.promise.PromiseHelper;
import core.UrlHelper;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

//    @Inject
//    private CompositionController composition;

    @Inject
    private UrlHelper urlHelper;

    //TODO potom compozicne s F.Promise<Result> z COmpositionControllera


    public F.Promise<Result> withBigPipe() {

//        final F.Promise<WSResponse> bigPromise = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
//        final F.Promise<WSResponse> smallPromise = WS.url(urlHelper.getCaculatorUrl() + "calculation/small").get();
//        final F.Promise<WSResponse> friendsPromise = WS.url(urlHelper.getBackendUrl() + "backend/friends").get();
//
//
//        Pagelet big = new HtmlPagelet("big", bigPromise.map(() -> views.html.module.render()));
//        Pagelet small = new HtmlPagelet("small", smallPromise.map(() -> views.html.module.render()));
//        Pagelet friends = new HtmlPagelet("friends", friendsPromise.map(() -> views.html.module.render()));
//
//        BigPipe bigPipe = new BigPipe(PageletRenderOptions.ClientSide, big, small, friends);
//        return ok(HtmlStreamHelper.toChunks(views.stream.withbigpipe.apply(bigPipe, big, small, friends)));

        return F.Promise.pure(ok("aa"));
    }




    public F.Promise<Result> withoutBigPipe() {

        final F.Promise<WSResponse> bigPromise = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
        final F.Promise<WSResponse> smallPromise = WS.url(urlHelper.getCaculatorUrl() + "calculation/small").get();
        final F.Promise<WSResponse> friendsPromise = WS.url(urlHelper.getBackendUrl() + "backend/friends").get();
        final F.Promise<WSResponse> friendPromise = WS.url(urlHelper.getBackendUrl() + "backend/friend/3").get();

        //TODO ked chcem friend ktory neexistuje hodi mi : No entity found for query 400
        //TODO nemalo by hodit 200 a {} ?

        return PromiseHelper.sequence(bigPromise, smallPromise, friendsPromise, friendPromise)
                .map((big, small, friends, friend) ->
                                ok(views.html.withoutbigpipe.render(big, small, friends, friend))
                );
    }

}
