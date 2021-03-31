package ibm.gse.eda.maas.api.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class KClusterInfo {
    @Schema(required = true, example = "name-of-the-Kafka-cluster")
    public String name;


    public KClusterInfo(){}
}
