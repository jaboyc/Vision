package com.jlogical.vision.api.system;

import com.jlogical.vision.api.API;
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

        //Hats
        addHat("when started");

        //Commands
        addCommand("print []", p -> {
            String output = p.str(0);
            System.out.println(output);
            p.getScript().appendOutputLog(output);
        });
        addCommand("set [] to []", p->{
            String name = p.str(0);
            Variable variable = Variable.findVariable(name,  p.getHatHolder(), p.getScript());
            if(variable == null){
                p.getHatHolder().getVariables().add(new Variable(name, p.get(1)));
            }else{
                variable.setValue(p.get(1));
            }
        });
        addCommand("set global [] to []", p->{
            String name = p.str(0);
            Variable variable = Variable.findGlobalVariable(name, p.getScript());
            if(variable == null){
                p.getScript().getVariables().add(new Variable(name, p.get(1)));
            }else{
                variable.setValue(p.get(1));
            }
        });

        //Reporters
        addReporter("value of []", p -> {
           String name = p.str(0);
           Variable variable = Variable.findVariable(name, p.getHatHolder(), p.getScript());
           if(variable == null) {
               throw new NullPointerException("Cannot find variable '"+ name+"'");
           }
           return variable.getValue();
        });
    }
}
