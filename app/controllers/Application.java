package controllers;

import com.google.inject.Inject;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    @Inject
    private CompositionController composition;

    public F.Promise<Result> withBigPipe() {
        return null;
    }

    public F.Promise<Result> withoutBigPipe() {
//        return F.Promise.sequence(
//                composition.calculateNF(),
//                composition.readDataNF()
//        ).map(result -> result.)
        return null;
    }

}
