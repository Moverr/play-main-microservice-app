package controllers;

import com.google.inject.Inject;

import com.ybrikman.ping.javaapi.bigpipe.*;
import com.ybrikman.ping.javaapi.promise.PromiseHelper;
import core.ServiceClient;
import core.UrlHelper;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    private final ServiceClient serviceClient;

    @Inject
    public Application(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    @Inject
    private UrlHelper urlHelper;


    /**
     * big pipe example
     */
    public F.Promise<Result> withBigPipe() {

        final F.Promise<WSResponse> bigPromise = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
        final F.Promise<WSResponse> bigPromise2 = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
        final F.Promise<WSResponse> bigPromise3 = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
        final F.Promise<WSResponse> smallPromise = WS.url(urlHelper.getCaculatorUrl() + "calculation/small").get();
        final F.Promise<WSResponse> friendsPromise = WS.url(urlHelper.getBackendUrl() + "backend/friends").get();
        final F.Promise<WSResponse> friendPromise = WS.url(urlHelper.getBackendUrl() + "backend/friend/3").get();

        final Pagelet big = new HtmlPagelet("big", bigPromise.map(views.html.module::render));
        final Pagelet big2 = new HtmlPagelet("big2", bigPromise2.map(views.html.module::render));
        final Pagelet big3 = new HtmlPagelet("big3", bigPromise3.map(views.html.module::render));
        final Pagelet small = new HtmlPagelet("small", smallPromise.map(views.html.module::render));
        final Pagelet friends = new HtmlPagelet("friends", friendsPromise.map(views.html.module::render));
        final Pagelet friend = new HtmlPagelet("friend", friendPromise.map(views.html.module::render));

        final BigPipe bigPipe = new BigPipe(PageletRenderOptions.ClientSide, big, small, friends, friend, big2, big3);

        return F.Promise.pure(
                ok(HtmlStreamHelper.toChunks(views.stream.withbigpipe.apply(bigPipe, big, small, friends, friend, big2, big3)))
        );
    }

    /**
     * de-dupe service calls anf big pipe example
     * deduping are all, efective just for (big, big2, big3)
     * @return
     */
    public F.Promise<Result> withBigPipeAndDedupe() {

        final F.Promise<WSResponse> bigPromise = serviceClient.dedupeCall(urlHelper.getCaculatorUrl() + "calculation/big");
        final F.Promise<WSResponse> bigPromise2 = serviceClient.dedupeCall(urlHelper.getCaculatorUrl() + "calculation/big");
        final F.Promise<WSResponse> bigPromise3 = serviceClient.dedupeCall(urlHelper.getCaculatorUrl() + "calculation/big");
        final F.Promise<WSResponse> smallPromise = serviceClient.dedupeCall(urlHelper.getCaculatorUrl() + "calculation/small");
        final F.Promise<WSResponse> friendsPromise = serviceClient.dedupeCall(urlHelper.getBackendUrl() + "backend/friends");
        final F.Promise<WSResponse> friendPromise = serviceClient.dedupeCall(urlHelper.getBackendUrl() + "backend/friend/3");

        final Pagelet big = new HtmlPagelet("big", bigPromise.map(views.html.module::render));
        final Pagelet big2 = new HtmlPagelet("big2", bigPromise2.map(views.html.module::render));
        final Pagelet big3 = new HtmlPagelet("big3", bigPromise3.map(views.html.module::render));
        final Pagelet small = new HtmlPagelet("small", smallPromise.map(views.html.module::render));
        final Pagelet friends = new HtmlPagelet("friends", friendsPromise.map(views.html.module::render));
        final Pagelet friend = new HtmlPagelet("friend", friendPromise.map(views.html.module::render));

        final BigPipe bigPipe = new BigPipe(PageletRenderOptions.ClientSide, big, small, friends, friend, big2, big3);

        return F.Promise.pure(
                ok(HtmlStreamHelper.toChunks(views.stream.withbigpipe.apply(bigPipe, big, small, friends, friend, big2, big3)))
        );
    }




    public F.Promise<Result> withoutBigPipe() {

        final F.Promise<WSResponse> bigPromise = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
        final F.Promise<WSResponse> bigPromise2 = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
        final F.Promise<WSResponse> bigPromise3 = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
        final F.Promise<WSResponse> smallPromise = WS.url(urlHelper.getCaculatorUrl() + "calculation/small").get();
        final F.Promise<WSResponse> friendsPromise = WS.url(urlHelper.getBackendUrl() + "backend/friends").get();
        final F.Promise<WSResponse> friendPromise = WS.url(urlHelper.getBackendUrl() + "backend/friend/3").get();

        return PromiseHelper.sequence(bigPromise, smallPromise, friendsPromise, friendPromise, bigPromise2, bigPromise3)
                .map((big, small, friends, friend, big2, big3) ->
                                ok(views.html.withoutbigpipe.render(big, small, friends, friend, big2, big3))
                );
    }

}
