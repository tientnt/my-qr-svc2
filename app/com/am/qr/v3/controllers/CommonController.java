package com.am.qr.v3.controllers;

import com.am.common.utils.Oauth2Secure;
import com.am.qr.v3.repositories.DatabaseExecutionContext;
import com.am.common.utils.HttpResult;
import com.am.common.utils.SvcResult;
import com.am.qr.v3.services.DbService;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Security.Authenticated(Oauth2Secure.class)
public class CommonController extends Controller {

    private static final Logger.ALogger logger = Logger.of(CommonController.class);

    private DbService dbService;

    private DatabaseExecutionContext ec;

    @Inject
    public CommonController(DbService dbService, DatabaseExecutionContext ec) {
        this.dbService = dbService;
        this.ec = ec;
    }

    public CompletionStage<Result> getVersion() {
        String appKey = request().getQueryString("appKey");
        String appVersion = request().getQueryString("appVersion");

        return supplyAsync(() -> {
            SvcResult<Map<String, Object>> svcResult = null;//dbService.checkVersion(appKey, appVersion);
            HttpResult<Map<String, Object>> httpResult = new HttpResult<>(svcResult);
            return httpResult.toResult();
        }, ec);
    }
}
