package com.jlogical.vision.api.elements;

import com.jlogical.vision.api.API;
import com.jlogical.vision.api.runnables.CBlockRunnable;

import java.util.ArrayList;

/**
 * Template for a CBlock.
 */
public class CustomCBlock extends CustomCommand{


    /**
     * List of cores of the CBlocks that chain onto this CBlock.
     */
    private ArrayList<String> chains;

    /**
     * Creates a new CustomElement with a given core and api.
     */
    public CustomCBlock(String core, CBlockRunnable runnable, ArrayList<String> chains, API api) {
        super(core, runnable, api);
        this.chains = chains != null ? chains : new ArrayList<>();
    }

    public ArrayList<String> getChains() {
        return chains;
    }
}
