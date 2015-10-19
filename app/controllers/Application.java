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
import play.twirl.api.Html;

public class Application extends Controller {

    // deduping for one controller (just red from code)
    private final ServiceClient serviceClient;

    @Inject
    public Application(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    @Inject
    private UrlHelper urlHelper;

//    @Inject
//    private CompositionController compContrl;


    /**
     * if you need handle errors on app side not just delegate it from service calls
     * also
     * @return
     */
    public F.Promise<Result> withBigPipeAndDedupeErrors() {

        final F.Promise<WSResponse> bigPromise = serviceClient.dedupeCall(urlHelper.getCaculatorUrl() + "calculation/big");
        final F.Promise<WSResponse> bigPromise2 = serviceClient.dedupeCall(urlHelper.getCaculatorUrl() + "calculation/big");
        final F.Promise<WSResponse> bigPromise3 = serviceClient.dedupeCall(urlHelper.getCaculatorUrl() + "calculation/big");
        final F.Promise<WSResponse> smallPromise = serviceClient.dedupeCall(urlHelper.getCaculatorUrl() + "calculation/small");
        final F.Promise<WSResponse> friendsPromise = serviceClient.dedupeCall(urlHelper.getBackendUrl() + "backend/friends");
        final F.Promise<WSResponse> friendPromise = serviceClient.dedupeCall(urlHelper.getBackendUrl() + "backend/friend/3");

        final Pagelet big = new HtmlPagelet("big", renderWithErrors(bigPromise));
        final Pagelet big2 = new HtmlPagelet("big2", renderWithErrors(bigPromise2));
        final Pagelet big3 = new HtmlPagelet("big3", renderWithErrors(bigPromise3));
        final Pagelet small = new HtmlPagelet("small", renderWithErrors(smallPromise));
        final Pagelet friends = new HtmlPagelet("friends", renderWithErrors(friendsPromise));
        final Pagelet friend = new HtmlPagelet("friend", renderWithErrors(friendPromise));

        final BigPipe bigPipe = new BigPipe(PageletRenderOptions.ClientSide, big, small, friends, friend, big2, big3);

        return F.Promise.pure(
                ok(HtmlStreamHelper.toChunks(views.stream.withbigpipe.apply(bigPipe, big, small, friends, friend, big2, big3)))
        ).recover(t -> badRequest("MainApp crashed !")); // error handling for main app
    }

    /**
     * de-dupe service calls and big pipe example
     * de-duping are all, effective just for (big, big2, big3)
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



    public F.Promise<Result> withoutBigPipe() {

        final F.Promise<WSResponse> bigPromise = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
        final F.Promise<WSResponse> bigPromise2 = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
        final F.Promise<WSResponse> bigPromise3 = WS.url(urlHelper.getCaculatorUrl() + "calculation/big").get();
        final F.Promise<WSResponse> smallPromise = WS.url(urlHelper.getCaculatorUrl() + "calculation/small").get();
        final F.Promise<WSResponse> friendsPromise = WS.url(urlHelper.getBackendUrl() + "backend/friends").get();
        final F.Promise<WSResponse> friendPromise = WS.url(urlHelper.getBackendUrl() + "backend/friend/3").get();

        // wait for all promise onComplete (but in most efficient way)
        // basically scatter gather pattern
        return PromiseHelper.sequence(bigPromise, smallPromise, friendsPromise, friendPromise, bigPromise2, bigPromise3)
                .map((big, small, friends, friend, big2, big3) ->
                                ok(views.html.withoutbigpipe.render(big, small, friends, friend, big2, big3))
                );
    }


    // if main app need handle errors
    private F.Promise<Html> renderWithErrors(F.Promise<WSResponse> dataPromise) {
        return dataPromise
                // if comes response with status != 200
                .map(response ->
                        response.getStatus() == 200 ? (Html) views.html.module.render(response) : (Html) views.html.error.render())
                // if throwable occurs
                .recover(t ->
                        (Html) views.html.error.render()); // retyping is because of Idea bug
    }












    //TODO
    public F.Promise<Result> compositionExample() {
//        F.Promise<Result> readDataPromise = F.Promise.promise(compContrl.readData());
//        F.Promise<Result> calculatePromise = F.Promise.promise(compContrl.calculate());
        return F.Promise.pure(ok("OK"));
    }

}
