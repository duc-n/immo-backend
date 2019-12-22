package com.cele.immo.function;

@FunctionalInterface
public interface TriFunction<F, S, T, R> {

    public R apply(F first, S second, T third);

}
