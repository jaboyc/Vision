package com.jlogical.vision.api.system.objects;

import com.jlogical.vision.api.API;
import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.project.Project;

/**
 * API for creating custom objects.
 */
public class CustomObjectsAPI extends API {

    /**
     * Creates a new CustomObjectsAPI.
     */
    public CustomObjectsAPI(Project project) {
        super(project);

        // Constructor.
        addReporter("new []", e -> {
            CustomObject object = new CustomObject(e.str(0));
            e.getScript().start("when custom object () created", object);
            return object;
        });

        // Hats.
        addHat("when custom object () created");

        // Commands.
        addCommand("for () set [] to []", e -> e.co(0).setProperty(e.str(1), e.get(2)));

        // Reporters.
        addReporter("for () get []", e -> {
            CustomObject customObject = e.co(0);
            String propName = e.str(1);
            try {
                return customObject.getProperty(propName);
            } catch (Exception e1) {
                throw new VisionException("Cannot find property '" + propName + "' in custom object '" + customObject + "'", e.getRange());
            }
        });
        addReporter("for () type", e->e.co(0).getType());
    }
}
