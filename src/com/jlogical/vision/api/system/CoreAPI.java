package com.jlogical.vision.api.system;

import com.jlogical.vision.api.API;
import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.Variable;
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

        coreCommands();
        variableCommands();
        mathLogicCommands();
    }

    /**
     * Adds the commands and hats related to the core functionality of Vision.
     */
    private void coreCommands() {

        //Hats
        addHat("when started");

        //Commands
        addCommand("print []", p -> {
            String output = p.str(0);
            System.out.println(output);
            p.getScript().appendOutputLog(output);
        });

    }

    /**
     * Adds the commands and reporters related to Variables.
     */
    private void variableCommands() {

        //Commands
        addCommand("set [] to []", p -> {
            String name = p.str(0);
            Variable variable = Variable.findVariable(name, p.getHatHolder(), p.getScript());
            if (variable == null) {
                p.getHatHolder().getVariables().add(new Variable(name, p.get(1)));
            } else {
                variable.setValue(p.get(1));
            }
        });
        addCommand("change [] by []", p -> {
            String name = p.str(0);
            Variable variable = Variable.findVariable(name, p.getHatHolder(), p.getScript());
            if (variable == null) {
                p.err("Cannot find variable named '" + name + "'");
            }
            variable.setValue(p.toNum(variable.getValue()) + p.num(1));
        });
        addCommand("set global [] to []", p -> {
            String name = p.str(0);
            Variable variable = Variable.findGlobalVariable(name, p.getScript());
            if (variable == null) {
                p.getScript().getVariables().add(new Variable(name, p.get(1)));
            } else {
                variable.setValue(p.get(1));
            }
        });
        addCommand("change global [] by []", p -> {
            String name = p.str(0);
            Variable variable = Variable.findGlobalVariable(name, p.getScript());
            if (variable == null) {
                p.err("Cannot find global variable named '" + name + "'");
            }
            variable.setValue(p.toNum(variable.getValue()) + p.num(1));
        });

        //Reporters
        addReporter("value of []", p -> {
            String name = p.str(0);
            Variable variable = Variable.findVariable(name, p.getHatHolder(), p.getScript());
            if (variable == null) {
                p.err("Cannot find variable '" + name + "'");
            }
            return variable.getValue();
        });
        addReporter("value of global []", p -> {
            String name = p.str(0);
            Variable variable = Variable.findGlobalVariable(name, p.getScript());
            if (variable == null) {
                p.err("Cannot find global variable '" + name + "'");
            }
            return variable.getValue();
        });
    }

    /**
     * Adds the reporters related to math and logic.
     */
    private void mathLogicCommands() {
        addReporter("[] + []", p -> p.num(0) + p.num(1));
        addReporter("[] - []", p -> p.num(0) - p.num(1));
        addReporter("[] * []", p -> p.num(0) * p.num(1));
        addReporter("[] / []", p->p.num(0) / p.num(1));
        addReporter("[] ^ []", p->Math.pow(p.num(0), p.num(1)));
    }
}
