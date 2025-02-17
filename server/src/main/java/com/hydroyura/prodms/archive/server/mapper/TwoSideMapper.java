package com.hydroyura.prodms.archive.server.mapper;

public interface TwoSideMapper<S,D> extends OneSideMapper<S,D> {

    S toSource(D destination);

}
