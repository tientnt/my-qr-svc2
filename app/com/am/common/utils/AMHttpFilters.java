package com.am.common.utils;

import play.http.DefaultHttpFilters;

import javax.inject.Inject;

public class AMHttpFilters extends DefaultHttpFilters {
    @Inject
    public AMHttpFilters(LoggingFilter logging) {
        super(logging);
    }
}
