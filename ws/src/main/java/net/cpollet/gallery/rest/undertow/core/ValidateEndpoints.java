package net.cpollet.gallery.rest.undertow.core;

import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class ValidateEndpoints {
    private final Set<EndpointDef> declaredEndpoints;
    private final RoutingHandler routingHandler;

    public ValidateEndpoints(RoutingHandler routingHandler) {
        this.routingHandler = routingHandler;
        declaredEndpoints = new HashSet<>();
    }

    public RoutingHandler handler() {
        return routingHandler;
    }

    public void post(String path, HttpHandler handler) {
        assertEndpointNotPresent("POST", path);
        routingHandler.post(path, handler);
    }

    private void assertEndpointNotPresent(String method, String path) {
        EndpointDef endpointDef = new EndpointDef(method, path);
        if (declaredEndpoints.contains(endpointDef)) {
            throw new IllegalArgumentException(String.format("Endpoint [%s %s] already exists", method, path));
        }
        declaredEndpoints.add(endpointDef);
    }

    public void get(String path, HttpHandler handler) {
        assertEndpointNotPresent("GET", path);
        routingHandler.get(path, handler);
    }

    private class EndpointDef {
        private final String method;
        private final String path;

        private EndpointDef(String method, String path) {
            this.method = method;
            this.path = path;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EndpointDef that = (EndpointDef) o;
            return method.equals(that.method) &&
                    path.equals(that.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(method, path);
        }
    }
}
