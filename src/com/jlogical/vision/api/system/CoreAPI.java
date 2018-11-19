package com.jlogical.vision.api.system;

import com.jlogical.vision.api.API;
import com.jlogical.vision.api.elements.CustomCommand;
import com.jlogical.vision.api.elements.CustomHat;
import com.jlogical.vision.project.Project;

/**
 * API for adding the Core functionality to Vision.
 */
public class CoreAPI extends API {

    /**
     * Creates a new CoreAPI.
     */
    public CoreAPI(Project project) {
        super(project);

        //Hats
        addHat("when started");

        //Commands
        addCommand("print []", p->System.out.println(p.str(0)));
    }
}
