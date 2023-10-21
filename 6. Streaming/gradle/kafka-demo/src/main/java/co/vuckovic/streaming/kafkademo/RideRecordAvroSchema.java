package co.vuckovic.streaming.kafkademo;

import org.apache.avro.Schema;

public class RideRecordAvroSchema {

    public static final String AVRO_SCHEMA_JSON = "{\"type\":\"record\",\"name\":\"RideRecord\",\"namespace\":\"schemaregistry\",\"fields\":[{\"name\":\"vendor_id\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"passenger_count\",\"type\":\"int\"},{\"name\":\"trip_distance\",\"type\":\"double\"}]}";

    public static final Schema SCHEMA = new Schema.Parser().parse(AVRO_SCHEMA_JSON);
}
