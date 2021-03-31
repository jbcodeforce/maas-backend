package ibm.gse.eda.maas.infrastructure.kafka;

import java.util.Properties;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import ibm.gse.eda.maas.domain.cluster.ClusterDetail;

@ApplicationScoped
public class KafkaClusterAdminClient {

    @Inject 
    @ConfigProperty(name="kafka.admin.client.saslJaasUser")
    public String saslJaasUser;

    @Inject 
    @ConfigProperty(name="kafka.admin.client.saslJaasSecModule")
    public String saslJaasSecModule;

    public  AdminClient adminClient;

    private String currentClusterName;

    public AdminClient getOrCreateAdminClient(ClusterDetail connectedCluster) {
        if (adminClient == null){
            currentClusterName = connectedCluster.clusterName;
            adminClient = KafkaAdminClient.create(buildDefaultClientConfig(connectedCluster));   
        }
        if (!connectedCluster.clusterName.equals(currentClusterName)){
            currentClusterName = connectedCluster.clusterName;
            adminClient = KafkaAdminClient.create(buildDefaultClientConfig(connectedCluster));   
        }
        return adminClient;
    }

    public  void close(){
        adminClient = null;
    }

    private  Properties buildDefaultClientConfig(ClusterDetail connectedCluster) {
        final Properties properties = new Properties();
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, connectedCluster.bootstrapUrls());
        if (connectedCluster.securityProtocol != null){
            properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, connectedCluster.securityProtocol);
        } else {
            if ( connectedCluster.isSaslEnabled()) {
                properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            }
           
        }
        
        if (connectedCluster.isSslEnabled()) {
            properties.put(SslConfigs.SSL_PROTOCOL_CONFIG, "TLSv1.2");
            // In clusters.json config file place the following:
            // "trustStoreFile" : "/tmp/es-cert.jks",
            // "trustStorePassword" : "password",
            if (connectedCluster.trustStoreFile != null) {
                properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, connectedCluster.trustStoreFile);
            }
            if (connectedCluster.trustStorePassword != null) {
                properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, connectedCluster.trustStorePassword);
            }
            if (connectedCluster.keyStoreFile != null) {
                properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, connectedCluster.keyStoreFile);
            }
            if (connectedCluster.keyStorePassword != null) {
                properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, connectedCluster.keyStorePassword);
            }
            if (connectedCluster.keyPassword != null) {
                properties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, connectedCluster.keyPassword);
            }
        }
        
        if (connectedCluster.isSaslEnabled()) {
            if (connectedCluster.saslJaasSecModule == null) {
                connectedCluster.saslJaasSecModule = saslJaasSecModule;
            }
            if (connectedCluster.saslJaasUser == null) {
                connectedCluster.saslJaasUser = saslJaasUser;
            }
            if (connectedCluster.saslMechanism == null) {
                connectedCluster.saslMechanism = "PLAIN";
            }          
            properties.put(SaslConfigs.SASL_JAAS_CONFIG, connectedCluster.saslJaasSecModule + " required"
                        + " username=" + connectedCluster.saslJaasUser + " password=" + connectedCluster.apikey + ";");
            if (connectedCluster.saslMechanism.equals("PLAIN")) {
                properties.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
            }
        }

        properties.put("client.id", "kmcm-admin" + UUID.randomUUID());
        properties.stringPropertyNames().stream()
            .map(key -> key + ": " + properties.getProperty(key))
            .forEach(System.out::println);
        return properties;
    }
}