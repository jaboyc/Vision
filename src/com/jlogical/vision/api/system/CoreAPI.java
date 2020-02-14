package com.jlogical.vision.api.system;

import com.jlogical.vision.api.API;
import com.jlogical.vision.api.runnables.CBlockParameters;
import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.Variable;
import com.jlogical.vision.project.Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

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
        controlCommands();
        variableCommands();
        mathLogicCommands();
        stringCommands();
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

        // Reporters
        addReporter("ask []", p -> {
            String output = p.str(0);
            System.out.print(output);
            p.getScript().appendOutputLog(output);

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            p.getScript().appendOutputLog(input);

            return input;
        });

    }

    /**
     * Adds the commands related to controlling the flow of Vision.
     */
    private void controlCommands() {
        addCBlock("if []", p -> {
            if (p.bool(0)) {
                p.runLoop();
            } else if (p.getChain() != null) {
                p.getChain().run();
            }
        }, "else if []", "else");
        addCBlock("else if []", p -> {
            if (p.bool(0)) {
                p.runLoop();
            } else if (p.getChain() != null) {
                p.getChain().run();
            }
        }, "else if []", "else");
        addCBlock("else", p -> p.runLoop());
        addCBlock("repeat []", p -> {
            for (int i = 0; i < p.numInt(0); i++) {
                if(p.shouldStop()) return;
                p.runLoop();
            }
        });
        addCBlock("for every [] up to []", p -> {
            Variable variable = new Variable(p.str(0), 0);
            p.getCBlock().getVariables().add(variable);
            for (int i = 1; i <= p.numInt(1); i++) {
                if(p.shouldStop()) return;
                variable.setValue(i);
                p.runLoop();
            }
            p.getCBlock().getVariables().remove(variable);
        });
        addCBlock("while []", p -> {
            while (p.bool(0)) {
                if(p.shouldStop()) return;
                p.runLoop();
            }
        });
        addCBlock("forever", p->{
            while(true){
                if(p.shouldStop()) return;
                p.runLoop();
            }
        });
        addCBlock("repeat until []", p -> {
            while (!p.bool(0)) {
                if(p.shouldStop()) return;
                p.runLoop();
            }
        });

        addCommand("return []", p -> p.hatReturn(p.get(0)));
        addCommand("return", p-> p.hatReturn(null));
        addCommand("stop loop", p-> p.stopLoop());

    }

    /**
     * Adds the commands and reporters related to Variables.
     */
    private void variableCommands() {

        //Commands
        addCommand("set [] to []", p -> {
            String name = p.str(0);
            Variable variable = Variable.findVariable(name, p.getCBblockHolder(), p.getHatHolder());
            if (variable == null) {
                p.getHatHolder().getVariables().add(new Variable(name, p.get(1)));
            } else {
                variable.setValue(p.get(1));
            }
        });
        addCommand("change [] by []", p -> {
            String name = p.str(0);
            Variable variable = Variable.findVariable(name, p.getCBblockHolder(), p.getHatHolder());
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
            Variable variable = Variable.findVariable(name, p.getCBlockHolder(), p.getHatHolder());
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
        addReporter("[] / []", p -> p.num(0) / p.num(1));
        addReporter("[] ^ []", p -> Math.pow(p.num(0), p.num(1)));
        addReporter("-[]", p -> -p.num(0));
        addReporter("[] mod []", p -> p.num(0) % p.num(1));
        addReporter("[] % []", p -> p.num(0) % p.num(1));
        addReporter("abs of []", p -> Math.abs(p.num(0)));
        addReporter("sqrt of []", p -> Math.sqrt(p.num(0)));
        addReporter("[] root of []", p -> Math.pow(p.num(1), 1 / p.num(0)));
        addReporter("random from [] to []", p -> {
            double min = Math.min(p.num(0), p.num(1));
            double max = Math.max(p.num(0), p.num(1));
            return Math.random() * (max - min) + min;
        });
        addReporter("round []", p -> Math.round(p.num(0)));
        addReporter("floor []", p -> Math.floor(p.num(0)));
        addReporter("ceiling []", p -> Math.ceil(p.num(0)));
        addReporter("pi", p -> Math.PI);
        addReporter("e", p -> Math.E);
        addReporter("sin of []", p -> Math.sin(p.num(0)));
        addReporter("cos of []", p -> Math.cos(p.num(0)));
        addReporter("tan of []", p -> Math.tan(p.num(0)));
        addReporter("asin of []", p -> Math.asin(p.num(0)));
        addReporter("acos of []", p -> Math.acos(p.num(0)));
        addReporter("atan of []", p -> Math.atan(p.num(0)));
        addReporter("[] to degrees", p -> Math.toDegrees(p.num(0)));
        addReporter("[] to radians", p -> Math.toRadians(p.num(0)));
        addReporter("log of []", p -> Math.log10(p.num(0)));
        addReporter("log e of []", p -> Math.log(p.num(0)));

        addReporter("[] = []", p -> {
            try {
                return p.num(0) == p.num(1);
            } catch (Exception e) {
            }
            return p.str(0).equals(p.str(1));
        });
        addReporter("[] != []", p->{
            try {
                return p.num(0) != p.num(1);
            } catch (Exception e) {
            }
            return !p.str(0).equals(p.str(1));
        });
        addReporter("[] < []", p -> p.num(0) < p.num(1));
        addReporter("[] > []", p -> p.num(0) > p.num(1));
        addReporter("[] <= []", p -> p.num(0) <= p.num(1));
        addReporter("[] >= []", p -> p.num(0) >= p.num(1));
        addReporter("max of [] and []", p -> Math.max(p.num(0), p.num(1)));
        addReporter("min of [] and []", p -> Math.min(p.num(0), p.num(1)));

        addReporter("true", p -> true);
        addReporter("false", p -> false);
        addReporter("[] and []", p -> p.bool(0) && p.bool(1));
        addReporter("[] or []", p -> p.bool(0) || p.bool(1));
        addReporter("not []", p -> !p.bool(0));

        addReporter("if [] then [] else []", p -> p.bool(0) ? p.get(1) : p.get(2));
        addReporter("nothing", p -> null);
    }

    /**
     * Adds the commands and reporters for strings.
     */
    private void stringCommands(){
        addReporter("length of []", p->p.str(0).length());
        addReporter("letter [] of []", p->p.str(1).charAt(p.numInt(0)-1));
        addReporter("join []>>", p->{
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=0;i<p.getValues().size();i++){
                stringBuilder.append(p.str(i));
            }
            return stringBuilder.toString();
        });
        addReporter("[] to uppercase", p->p.str(0).toUpperCase());
        addReporter("[] to lowercase", p->p.str(0).toLowerCase());
    }
}
