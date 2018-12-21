package net.cpollet.assertions;

import com.mashape.unirest.http.HttpResponse;

public final class Assert {
    private Assert() {
        // noop
    }

    public static HttpResponseAssert that(HttpResponse httpResponse) {
        return new HttpResponseAssert(httpResponse);
    }
}
