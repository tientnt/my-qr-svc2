package controllers;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

public class CommonController extends Controller {
    Logger.ALogger logger = Logger.of(CommonController.class);

    public Result healthCheck() {
        logger.info("start healthCheck action");
        return ok("healthCheck success");
    }
}
