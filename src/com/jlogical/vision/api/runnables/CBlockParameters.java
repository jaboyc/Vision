package com.jlogical.vision.api.runnables;

import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.elements.CBlock;
import com.jlogical.vision.compiler.script.elements.Hat;
import com.jlogical.vision.compiler.values.Value;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

public class CBlockParameters extends CommandParameters<CBlock> {

    /**
     * Creates a Parameters with a given List of Values.
     */
    public CBlockParameters(CBlock cblock, ArrayList<Value> values, Hat hat, CodeRange range, CBlock cblockHolder) {
        super(cblock, values, hat, range, cblockHolder);
    }

    /**
     * Runs the CBlock loop.
     * @throws VisionException if there is an exception in the Commands the CBlock is running.
     */
    public void runLoop() throws VisionException {
        getCBlock().runLoop();
    }

    /**
     * @return whether the cblock should stop or not.
     */
    public boolean shouldStop() {
        return !getCBlock().isRunning();
    }

    public CBlock getCBlock(){
        return getElement();
    }

    public CBlock getChain(){
        return getElement().getChain();
    }
}
