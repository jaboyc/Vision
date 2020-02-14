package com.jlogical.vision.api.system.collection;

import com.jlogical.vision.api.API;
import com.jlogical.vision.project.Project;

import java.util.ArrayList;

/**
 * API that handles collections such as Lists, Queues, and Stacks.
 */
public class CollectionsAPI extends API {

    /**
     * Creates a new CollectionsAPI.
     */
    public CollectionsAPI(Project project) {
        super(project);

        listCommands();
    }

    /**
     * Adds all the commands and reporters for lists.
     */
    private void listCommands() {

        // Constructors
        addReporter("new list", p -> new ArrayList());
        addReporter("list", p -> new ArrayList());
        addReporter("list []>>", p->{
            ArrayList list = new ArrayList();
            for(int i=0;i<p.getValues().size();i++) list.add(p.get(i));
            return list;
        });
        addReporter("[]>>", p->{
            ArrayList list = new ArrayList();
            for(int i=0;i<p.getValues().size();i++) list.add(p.get(i));
            return list;
        });

        // Commands
        addCommand("for () set index [] to []", p -> p.list(0).set(p.numInt(1)-1, p.get(2)));
        addCommand("for () add []", p -> p.list(0).add(p.get(1)));
        addCommand("for () add [] at index []", p -> p.list(0).add(p.numInt(2)-1, p.get(1)));
        addCommand("for () remove index []", p -> p.list(0).remove(p.numInt(1)-1));
        addCommand("for () clear", p -> p.list(0).clear());

        // Reporters
        addReporter("for () get []", p -> p.list(0).get(p.numInt(1)-1));
        addReporter("for () size", p -> p.list(0).size());
        addReporter("for () index of []", p -> p.list(0).indexOf(p.get(1)) + 1);
        addReporter("for () contains []", p -> p.list(0).contains(p.get(1)));
        addReporter("for () is empty", p -> p.list(0).isEmpty());
    }
}
