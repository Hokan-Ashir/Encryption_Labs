package ist.nine;

public class ExampleSubstitutionFunctions implements ISubstitutionFunctions {
    @Override
    public boolean v1(boolean u1, boolean u2, boolean u3, boolean u4) {
        return (u1 & u2 & !u4) || (u1 & u2 & !u3) || (u1 & !u3 & !u4) || (!u2 & u3 & u4) || (!u1 & !u2 & u4) || (!u1 & !u2 & u3);
    }

    @Override
    public boolean v2(boolean u1, boolean u2, boolean u3, boolean u4) {
        return (!u1 & u2 & u3 & !u4) || (!u1 & !u3 & u4) || (u1 & u2 & u4) || (u1 & u3 & u4) || (u1 & !u2 & !u4);
    }

    @Override
    public boolean v3(boolean u1, boolean u2, boolean u3, boolean u4) {
        return (u2 & !u3 & !u4) || (!u1 & u2 & !u3) || (u1 & u3 & u4) || (u1 & !u2 & !u3) || (!u1 & !u2 & u3 & !u4);
    }

    @Override
    public boolean v4(boolean u1, boolean u2, boolean u3, boolean u4) {
        return (u1 & u2 & u3) || (u2 & u3 & u4) || (u1 & !u2 & !u4) || (!u2 & u3 & !u4) || (!u1 & u2 & !u3 & !u4) || (!u1 & !u2 & !u3 & u4);
    }

    @Override
    public boolean u1(boolean v1, boolean v2, boolean v3, boolean v4) {
        return false;
    }

    @Override
    public boolean u2(boolean v1, boolean v2, boolean v3, boolean v4) {
        return (v2 & !v3 & !v4) || (v1 & !v2 & v3 & !v4) || (!v2 & !v3 & v4) || (!v1 & v3 & v4) || (!v1 & v2 & !v4);
    }

    @Override
    public boolean u3(boolean v1, boolean v2, boolean v3, boolean v4) {
        return false;
    }

    @Override
    public boolean u4(boolean v1, boolean v2, boolean v3, boolean v4) {
        return false;
    }
}
