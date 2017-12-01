package com.sunny.rpc.serialize;

public interface Serializer<S,R> {

    R serialize(S source);


    S deserialize(R result);

}
