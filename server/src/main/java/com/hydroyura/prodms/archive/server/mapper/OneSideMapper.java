package com.hydroyura.prodms.archive.server.mapper;

public interface OneSideMapper<S,D> {

    D toDestination(S source);

}
