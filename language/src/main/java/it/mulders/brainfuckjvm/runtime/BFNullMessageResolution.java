package it.mulders.brainfuckjvm.runtime;

import com.oracle.truffle.api.interop.CanResolve;
import com.oracle.truffle.api.interop.MessageResolution;
import com.oracle.truffle.api.interop.Resolve;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;

@MessageResolution(receiverType = BFNull.class)
public class BFNullMessageResolution {
    @Resolve(message = "IS_NULL")
    public abstract static class BFForeignIsNullNode extends Node {

        public Object access(Object receiver) {
            return BFNull.SINGLETON == receiver;
        }
    }

    @CanResolve
    public abstract static class CheckNull extends Node {

        protected static boolean test(TruffleObject receiver) {
            return receiver instanceof BFNull;
        }
    }
}
