package ibm.gse.eda.maas.domain.cluster;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Information about Kafka Brokers
 * 
 * https://kafka.apache.org/documentation/#dynamicbrokerconfigs
 */
@Entity
@Table(name = "kafka_brokers")
public class KafkaBroker {
    @Id
    @SequenceGenerator(name = "kafkaBrokerSequence", sequenceName = "kafkaBroker_id_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "kafkaBrokerSequence")
    public int id;
    public String host;
    public int port = 9092;
    public String rack;
    public Boolean auto_create_topics_enable;
    public String compression_type;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="clusterId", nullable=false)
    public ClusterDetail cluster;


    public KafkaBroker(){}


	public KafkaBroker(String hostIn, int idIn, int portIn, String rackIn) {
        this.host = hostIn;
        this.id = idIn;
        this.port = portIn;
        this.rack = rackIn;
	}
}
