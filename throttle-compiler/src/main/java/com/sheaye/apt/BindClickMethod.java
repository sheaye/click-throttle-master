package com.sheaye.apt;

import java.util.List;

/**
 * Created by yexinyan on 2017/5/24.
 */

class BindClickMethod {

    private final int[] mResIds;
    private final String mMethodName;
    private final List<String> mParameterTypes;

    BindClickMethod(int[] values, String methodName, List<String> methodParameterTypes) {
        mResIds = values;
        mMethodName = methodName;
        mParameterTypes = methodParameterTypes;
    }

    int getParaametersSize(){
        return mParameterTypes !=null? mParameterTypes.size():0;
    }

    public int[] getResIds() {
        return mResIds;
    }

    public String getMethodName() {
        return mMethodName;
    }

    public List<String> getParameterTypes() {
        return mParameterTypes;
    }
}
