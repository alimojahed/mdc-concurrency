package util;

import org.slf4j.MDC;

import java.util.Map;

/**
 * @author Ali Mojahed on 8/31/2022
 * @project mdcconcurrency
 **/
public class MDCHelper {

    public static void setContext(Map<String, String> context) {
        if (context == null || context.isEmpty()) {
            MDC.clear();
        } else {
            MDC.setContextMap(context);
        }
    }



}
