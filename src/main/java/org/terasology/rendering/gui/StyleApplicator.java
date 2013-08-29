package org.terasology.rendering.gui;

import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class StyleApplicator {

    private static final Logger logger = LoggerFactory.getLogger(LayoutDefinition.class);

    /**
     * Applies a single style loaded from a JSON layout definition to a target UIWindow.
     * @param w Window to which the style shall be applied
     * @param name Name of the style (e.g. "backgroundImage")
     * @param value Style value (e.g. engine:menubackground)
     * @param retry For edge cases such as enums, a retry may be necessary after an exception. The retry
     *              is tracked by this parameter, so that we do not retry infinitely.
     */
    public static void applyStyle(Object w, String name, Object value, boolean retry) {
        if(value.getClass().isArray()) {
            throw new IllegalArgumentException("Arrays must be passed as Lists, since GSON deserializes JSON arrays like that.");
        }

        // GSON reads numbers as Doubles by default. Since VectorNf and the like all take floats as constructor
        // parameters, and Groovy's "setProperty" does not downcast double to float, we have to do it manually here.
        if(value instanceof List && ((List) value).get(0) instanceof Double) {
            value = doubleListToFloatList((List<Double>) value);
        }

        Object[] args = { value };

        try {
            logger.debug("Setting style '{}' with value '{}'", name, value);
            ScriptBytecodeAdapter.setProperty(value, StyleApplicator.class, w, name);


        } catch(IllegalArgumentException e) {
            // Enum values are all uppercase, but in the JSON definitions it is nicer to be able to write
            // "verticalAlign": "bottom" instead of "verticalAlign": "BOTTOM". As such, if #setProperty throws
            // an argument exception related to an enum, we retry the style application with all uppercase value.
            if(!retry && e.getMessage().contains("enum") && value instanceof String) {
                applyStyle(w, name, ((String) value).toUpperCase(), true);
            } else {
                logger.error("Could not set style '{}' to '{}'", name, value, e);
            }

        } catch(Throwable e) {
            logger.error("Could not set style '{}' to '{}'", name, value, e);
        }
    }

    public static void applyStyle(Object w, String name, Object value) {
        applyStyle(w, name, value, false);
    }

    public static List<Float> doubleListToFloatList(List<Double> in) {
        List<Float> out = new ArrayList<Float>();
        for(double d : in) {
            out.add((float) d);
        }
        return out;
    }
}
