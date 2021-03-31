package ibm.gse.eda.maas.api.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "KClusterConnectionInfo", description="Define minimum information to connect to cluster")
public class KClusterConnectionInfo {
    @Schema(required = true, example = "name-of-the-Kafka-cluster")
    public String name;
    @Schema(required = true, example = "broker-0-qnprtqnp7hnkssdz.kafka.svc01.us-east.eventstreams.cloud.ibm.com:9093,...")
    public String bootstrapURL;
    @Schema(required = false, example = "string representing the api key")
    public String APIKEY;
    public boolean sslEnabled = false;

    public KClusterConnectionInfo() {
    }

	public KClusterConnectionInfo(String newName) {
        this.name = newName;
	}
}