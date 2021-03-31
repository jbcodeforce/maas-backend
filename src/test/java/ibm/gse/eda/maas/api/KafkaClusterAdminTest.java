package ibm.gse.eda.maas.api;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import ibm.gse.eda.maas.BaseITTest;
import ibm.gse.eda.maas.domain.cluster.ClusterDetail;
import ibm.gse.eda.maas.infrastructure.kafka.KafkaClusterAdminClient;

//@QuarkusTest
@Testcontainers
public class KafkaClusterAdminTest extends BaseITTest {
    

    @Test
    public void shouldHaveAConnectionToLocalKafka() throws InterruptedException, ExecutionException {
       
        ClusterDetail cdef = new ClusterDetail();
        cdef.clusterName= "localTestKafka";
        cdef.saslEnabled = false;
        cdef.sslEnabled = false;
        cdef.brokerHosts = kafkaContainerForTest.getBootstrapServers();
       
        AdminClient client = new KafkaClusterAdminClient().getOrCreateAdminClient(cdef);
        Assert.assertNotNull(client);
        DescribeClusterResult result = client.describeCluster();
        System.out.println(result.clusterId().get());
        System.out.println(result.controller().get().idString());
    }
}