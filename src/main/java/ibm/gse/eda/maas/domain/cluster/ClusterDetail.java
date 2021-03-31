package ibm.gse.eda.maas.domain.cluster;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ibm.gse.eda.maas.api.dto.KClusterConnectionInfo;
import ibm.gse.eda.maas.domain.crypto.Crypto;

/**
 * Cluster information for connection and management.
 * Some of attributes accept dynamic update so we need to tune this model
 * to work on what can be updated or not.
 * https://kafka.apache.org/documentation/#dynamicbrokerconfigs
 */
@Entity
@Table(name = "kafka_clusters")
@NamedQuery(name = "ClusterDetail.findAll", query = "SELECT k FROM ClusterDetail k ORDER BY k.clusterName")
public class ClusterDetail {

    @Id
    @SequenceGenerator(name = "kclustersSequence", sequenceName = "kcluster_id_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "kclustersSequence")
    private Integer id;

    // input attributes from a UI form and REST api
    @Column(length = 40, unique = true)
    public String clusterName;
    public String apikey;
    public String vendor;
    public String kafkaVersion;    
    public String vendorVersion;

    // following attributes are coming from the cluster admin queries
    public String clusterId;
    @OneToMany(targetEntity=ibm.gse.eda.maas.domain.cluster.KafkaBroker.class, mappedBy="cluster", cascade=CascadeType.ALL,orphanRemoval = true)
    public List<KafkaBroker> brokers = new ArrayList<KafkaBroker>();
    public String bootstrapURL;
    public String brokerHosts;

    public Boolean sslEnabled = false;
    public String securityProtocol;
    public String trustStoreFile;
    public String trustStorePassword;
    public String keyStoreFile;
    public String keyStorePassword;
    public String keyPassword;
    public Boolean saslEnabled = false;
    public String saslMechanism;
    public String saslJaasSecModule;
    public String saslJaasUser;
    public Integer numberOfBrokers;
    public Integer background_threads;
    public String compression_type;

    public ClusterDetail(){}

    public ClusterDetail(String clustername) {
        this.clusterName = clustername;
    }

	public Object bootstrapUrls() {
        if (brokerHosts == null) {
            StringBuffer sb = new StringBuffer();
            for (KafkaBroker b : this.brokers) {
                sb.append(b.host);
                sb.append(":");
                sb.append(b.port);
                sb.append(",");
            }
            if (sb.length() > 0) {
                brokerHosts = sb.substring(0, sb.length()-1);
            }
        }
		return brokerHosts;
    }

    public Boolean isSaslEnabled(){
        if (apikey != null && apikey.length() > 0) {
            saslEnabled= true;
            saslJaasUser = "token";
        } else {
            saslEnabled = false;
        }
        return saslEnabled;
    }

    public Boolean isSslEnabled(){
        return sslEnabled;
    }

	public void addNode(String host, int id, int port, String rack) {
        KafkaBroker broker = new KafkaBroker(host,id,port,rack);
        nodes().add(broker);
    }
    
    public List<KafkaBroker> nodes(){
        if (brokers == null) {
            brokers = new ArrayList<KafkaBroker>();
        }
        return brokers;
    }

	public static ClusterDetail build(KClusterConnectionInfo info) {
        ClusterDetail d = new ClusterDetail(info.name);

        d.apikey = Crypto.decrypt(info.APIKEY,"secret");
        d.brokerHosts = info.bootstrapURL;
        d.sslEnabled = info.sslEnabled;
		return d;
	}
    
}
