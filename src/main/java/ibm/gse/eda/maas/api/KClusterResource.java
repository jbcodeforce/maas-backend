package ibm.gse.eda.maas.api;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ibm.gse.eda.maas.api.dto.KClusterInfo;
import ibm.gse.eda.maas.domain.cluster.KClusterService;
import io.smallrye.mutiny.Uni;

@Path("/kclusters")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class KClusterResource {

    @Inject
    KClusterService kclusterService;

    @GET
    public Uni<List<KClusterInfo>> listOfClusterNames() {
        return kclusterService.listOfClusters();
    }
}