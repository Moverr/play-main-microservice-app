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

    @Inject
    private UrlHelper urlHelper;

    
    public F.Promise<Result> withBigPipe() {

        final F.Promise<WSResponse> bigPromise = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
        final F.Promise<WSResponse> smallPromise = WS.url(urlHelper.getCaculatorUrl() + "calculation/small").get();
        final F.Promise<WSResponse> friendsPromise = WS.url(urlHelper.getBackendUrl() + "backend/friends").get();
        final F.Promise<WSResponse> friendPromise = WS.url(urlHelper.getBackendUrl() + "backend/friend/3").get();

        final Pagelet big = new HtmlPagelet("big", bigPromise.map(views.html.module::render));
        final Pagelet small = new HtmlPagelet("small", smallPromise.map(views.html.module::render));
        final Pagelet friends = new HtmlPagelet("friends", friendsPromise.map(views.html.module::render));
        final Pagelet friend = new HtmlPagelet("friend", friendPromise.map(views.html.module::render));

        final BigPipe bigPipe = new BigPipe(PageletRenderOptions.ClientSide, big, small, friends, friend);

        return F.Promise.pure(
                ok(HtmlStreamHelper.toChunks(views.stream.withbigpipe.apply(bigPipe, big, small, friends, friend)))
        );
    }




    public F.Promise<Result> withoutBigPipe() {

        final F.Promise<WSResponse> bigPromise = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
        final F.Promise<WSResponse> smallPromise = WS.url(urlHelper.getCaculatorUrl() + "calculation/small").get();
        final F.Promise<WSResponse> friendsPromise = WS.url(urlHelper.getBackendUrl() + "backend/friends").get();
        final F.Promise<WSResponse> friendPromise = WS.url(urlHelper.getBackendUrl() + "backend/friend/3").get();

        return PromiseHelper.sequence(bigPromise, smallPromise, friendsPromise, friendPromise)
                .map((big, small, friends, friend) ->
                                ok(views.html.withoutbigpipe.render(big, small, friends, friend))
                );
    }

}
