package ibm.gse.eda.maas.infrastructure.cluster;

import java.util.List;

import ibm.gse.eda.maas.domain.cluster.ClusterDetail;

/**
 * Cluster Repository
 */
public interface ClusterRepository {

    public ClusterDetail getClusterDetail(String name);

    public List<String> getClusterNames();

    public ClusterDetail createCluster(ClusterDetail cluster);

    public ClusterDetail updateCluster(ClusterDetail cluster);

    public void deleteCluster(String name);
}