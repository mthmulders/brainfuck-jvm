package it.mulders.brainfuckjvm.runtime;

import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;

public class BFNull implements TruffleObject {
    public static final BFNull SINGLETON = new BFNull();

    /**
     * Disallow instantiation from outside to ensure that the {@link #SINGLETON} is the only
     * instance.
     */
    private BFNull() {
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public ForeignAccess getForeignAccess() {
        return null;
    }
}
