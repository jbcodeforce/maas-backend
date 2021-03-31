package ibm.gse.eda.maas.domain.cluster;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.hibernate.reactive.mutiny.Mutiny;

import ibm.gse.eda.maas.api.dto.KClusterInfo;
import io.smallrye.mutiny.Uni;

/**
 * Cluster service implements the logic to manage KafkaClusterEntity. It uses
 * KafkaAdminClient to connect to the cluster, and keep metadata about the
 * cluster in an external document oriented DB
 */
@ApplicationScoped
public class KClusterService {
    private static Logger LOG = Logger.getLogger(KClusterService.class.getName());
    
    @Inject
    Mutiny.Session mutinySession;

    public KClusterService() {
    }

    public Uni<List<KClusterInfo>> listOfClusters() {
        Uni<List<ClusterDetail>> clusters = mutinySession.createNamedQuery("ClusterDetail.findAll",ClusterDetail.class)
            .getResultList();
        List<KClusterInfo> l =clusters.onItem().m;
        return Uni.createFrom().item(l);
    }



}