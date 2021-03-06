package com.gianlu.pluggableserver.core.handlers;

import com.gianlu.pluggableserver.core.Applications;
import com.gianlu.pluggableserver.core.CoreUtils;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import org.jetbrains.annotations.NotNull;

/**
 * @author Gianlu
 */
public class SetConfigHandler extends AuthenticatedHandlerWithAppId {
    private final Applications applications;

    public SetConfigHandler(@NotNull Applications applications) {
        this.applications = applications;
    }

    @Override
    public void handleAuthenticated(@NotNull HttpServerExchange exchange, @NotNull String appId) {
        String key = CoreUtils.getParam(exchange, "key");
        if (key == null) {
            exchange.setStatusCode(StatusCodes.BAD_REQUEST);
            exchange.getResponseSender().send("MISSING_KEY");
            return;
        }

        String value = CoreUtils.getParam(exchange, "value");
        if (value == null) {
            exchange.setStatusCode(StatusCodes.BAD_REQUEST);
            exchange.getResponseSender().send("MISSING_VALUE");
            return;
        }

        if (applications.setConfig(appId, key, value)) {
            exchange.getResponseSender().send("OK");
        } else {
            exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
            exchange.getResponseSender().send("FAILED");
        }
    }
}
