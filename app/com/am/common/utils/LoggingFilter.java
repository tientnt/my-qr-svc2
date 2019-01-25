package com.am.common.utils;

import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.util.ByteString;
import com.google.common.io.Files;
import com.typesafe.config.Config;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.libs.streams.Accumulator;
import play.mvc.EssentialAction;
import play.mvc.EssentialFilter;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Executor;

public class LoggingFilter extends EssentialFilter {

    private static final Logger.ALogger logger = Logger.of(LoggingFilter.class);

    private final Executor executor;

    private final Materializer materializer;

    private final Config config;

    @Inject
    public LoggingFilter(Executor executor, Materializer materializer, Config config) {
        super();
        this.executor = executor;
        this.materializer = materializer;
        this.config = config;
    }

    @Override

    public EssentialAction apply(EssentialAction next) {

        return EssentialAction.of(request -> {
            long startTime = System.currentTimeMillis();

            Accumulator<ByteString, Result> accumulator = next.apply(request);

            String ext = Files.getFileExtension(request.uri()).toLowerCase();
            final List<String> lfExt = config.getStringList("app.loggingfilter_ext");
            final int lfMaxlength = config.getInt("app.loggingfilter_maxlength");
            if (lfExt.contains(ext)) {
                return accumulator;
            }

            final BuildRequestBody buildRequestBody = new BuildRequestBody();

            return accumulator.through(Flow.fromGraph(buildRequestBody)).map(result -> {
                long endTime = System.currentTimeMillis();
                long requestTime = endTime - startTime;

                logger.trace("{} {} from {} took {}ms and returned {}\n{}",
                             request.method(),
                             request.uri(),
                             request.header("X-Forwarded-For").orElse(request.remoteAddress()),
                             requestTime,
                             result.status(),
                             request.getHeaders().toMap());

                logger.trace("REQ: {}", buildRequestBody.getRequestBody());
                result.body().consumeData(materializer).thenApply(respBody -> {
                    logger.trace("RES:\n{}", StringUtils.left(respBody.utf8String(), lfMaxlength));
                    return respBody;
                });

                return result;
            }, executor);
        });
    }
}