package org.sandboxpowered.sandbox.fabric.impl.event;

import org.sandboxpowered.api.events.args.Result;
import org.sandboxpowered.api.events.args.ResultArgs;

public class ResultArgsImpl implements ResultArgs {
    private Result result;

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public void setResult(Result result) {
this.result=result;
    }
}
