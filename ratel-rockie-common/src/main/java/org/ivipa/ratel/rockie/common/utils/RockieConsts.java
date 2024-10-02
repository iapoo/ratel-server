package org.ivipa.ratel.rockie.common.utils;

public class RockieConsts {
    public final static long ACCESS_MODE_READONLY = 0;
    public final static long ACCESS_MODE_READWRITE = 1;
    public final static long ACCESS_MODE_MIN = 0;
    public final static long ACCESS_MODE_MAX = 1;
    public final static long OPERATOR_TYPE_MIN = 0;
    public final static long OPERATOR_TYPE_MAX = 2;

    /**
     * Operator Redis Cache Key prefix
     */
    public final static String OPERATOR_PREFIX = "OPERATOR:";
}
