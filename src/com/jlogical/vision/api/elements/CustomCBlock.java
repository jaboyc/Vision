package com.jlogical.vision.api.elements;

import com.jlogical.vision.api.API;
import com.jlogical.vision.api.runnables.CBlockRunnable;

import java.util.ArrayList;

/**
 * Template for a CBlock.
 */
public class CustomCBlock extends CustomCommand{

    /**
     * The lambda that runs the CBlock. Use .runLoop() to run the code inside of the cblock.
     */
    private CBlockRunnable runnable;

    /**
     * List of cores of the CBlocks that chain onto this CBlock.
     */
    private ArrayList<String> chains;

    /**
     * Creates a new CustomElement with a given core and api.
     */
    public CustomCBlock(String core, CBlockRunnable runnable, ArrayList<String> chains, API api) {
        super(core, runnable, api);
        this.runnable = runnable;
        this.chains = chains != null ? chains : new ArrayList<>();
    }

    public CBlockRunnable getRunnable() {
        return runnable;
    }

    public ArrayList<String> getChains() {
        return chains;
    }
}
