package org.openstack4j.model.gbp;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.gbp.builder.NatPoolBuilder;

public interface NatPool extends Resource, Buildable<NatPoolBuilder> {

    String getSubnetId();

    String getIpVersion();

    String getIpPool();

    String getExternalSegmentId();

    boolean isShared();

    String getDescription();

}
