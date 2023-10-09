# Apache Kafka

- [Apache Kafka: Real-time Data Streams](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#apache-kafka-empowering-real-time-data-streams)

  - [The Role of Consumers and Producers](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#the-role-of-consumers-and-producers)

  - [The Challenge of Direct Connections](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#the-challenge-of-direct-connections)

  - [Kafka: The Mediator](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#kafka-the-mediator)

  - [How Kafka Works](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#how-kafka-works)

  - [Kafka's Prevalence](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#kafkas-prevalence)

  - [Basic Kafka Components](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#basic-kafka-components)

    - [Message](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#message)

    - [Topic](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#topic)

    - [Broker and Cluster](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#broker-and-cluster)

    - [Logs](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#logs)

  - [Visualizing Kafka Message Flow](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#visualizing-kafka-message-flow)

    - [Producer Sending Messages to Kafka](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#producer-sending-messages-to-kafka)

    - [Consumer Receiving Messages](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#consumer-receiving-messages)

  - [Understanding Kafka: __consumer_offsets, Consumer Groups, and Partitions](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#understanding-kafka-__consumer_offsets-consumer-groups-and-partitions)

[__consumer_offsets: Tracking Message Consumption](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#__consumer_offsets-tracking-message-consumption)

[Consumer Groups: Collaborative Consumption](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#consumer-groups-collaborative-consumption)

[Partitions: Scalability and Ordering](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#partitions-scalability-and-ordering)

[Kafka Partition Replication for Fault Tolerance](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#kafka-partition-replication-for-fault-tolerance)

[Replicating Partitions Across Brokers](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#replicating-partitions-across-brokers)

[Fault Tolerance and Leader Failures](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#fault-tolerance-and-leader-failures)

[Replication Factor at Topic Level](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#replication-factor-at-topic-level)


[Kafka Configurations: Fine-Tuning Your Kafka Ecosystem](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#kafka-configurations-fine-tuning-your-kafka-ecosystem)

[Topic Configurations](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#topic-configurations)

[Consumer Configurations](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#consumer-configurations)

[Producer Configurations](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#producer-configurations)

[Avro and Schema Registry: The Importance of Schemas](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#avro-and-schema-registry-the-importance-of-schemas)

[The Need for Schemas](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#the-need-for-schemas)

[Avro and Schema Registry](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#avro-and-schema-registry)

[Avro Schema Evolution](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#avro-schema-evolution)

[Schema Registry Workflow](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#schema-registry-workflow)

[Handling Incompatible Schemas](https://github.com/drago-vuckovic/data-engineering/blob/main/6.%20Streaming/README.md#handling-incompatible-schemas)

# Apache Kafka: Real-time Data Streams

Apache Kafka, a versatile message broker and stream processor, revolutionizes the handling of real-time data feeds, offering a robust solution for modern data projects.

## The Role of Consumers and Producers

In any data-centric project, a fundamental distinction exists between consumers and producers:

- Consumers: These are the entities responsible for consuming the data—web pages, microservices, applications, and more. They are the endpoints where data is ultimately utilized.

- Producers: Producers, on the other hand, are the originators of data, supplying valuable information to consumers.

## The Challenge of Direct Connections

In the context of complex projects, connecting consumers directly to producers can lead to a convoluted and challenging-to-maintain architecture, as illustrated in the first image. This approach often results in scalability, reliability, and manageability issues.

## Kafka: The Mediator

Apache Kafka addresses these architectural challenges by assuming the role of an intermediary that all other project components connect to. This architectural shift simplifies the system, making it more scalable, maintainable, and robust.

## How Kafka Works

Kafka operates by facilitating seamless communication between producers and consumers:

Producers send messages: Producers generate data and transmit it to Kafka.

Real-time message distribution: Kafka excels at real-time data distribution, promptly pushing messages to consumers as they arrive.

## Kafka's Prevalence

Kafka's influence extends far and wide, earning its status as a cornerstone technology in the data landscape. It is widely adopted by a multitude of technology-related companies, underlining its popularity and reliability.

Apache Kafka is a pivotal player in the world of real-time data streaming, serving as an indispensable intermediary that streamlines data flow and empowers complex data projects.

## Basic Kafka Components

### Message

In Kafka, the fundamental unit of communication used by producers and consumers to exchange information is known as a message. Messages consist of three primary components:

- Key: The key is employed to identify the message uniquely. It also plays a crucial role in other Kafka operations, such as partitioning (covered later).

- Value: The actual payload of the message, containing the information that producers transmit and consumers seek to consume.

- Timestamp: Timestamps are utilized for logging and tracking purposes, providing essential temporal context.

### Topic

A topic serves as an abstraction for a particular concept or category of data. These concepts can encompass anything that is contextually meaningful for your project, such as "sales data," "new member registrations," "clicks on banners," and more.

Producers publish messages to a specific topic, and consumers subscribe to these topics to receive and process the associated messages.

### Broker and Cluster

- Broker: A Kafka broker refers to a machine, whether physical or virtualized, on which Kafka is operational. It serves as an individual Kafka server instance.

- Cluster: A Kafka cluster comprises a collection of brokers, which work cohesively to create a distributed Kafka environment. Kafka clusters offer enhanced scalability, fault tolerance, and redundancy.

### Logs

Within the Kafka ecosystem, logs are the physical storage units for data. In essence, they represent the concrete manifestation of data on storage disks.

Key aspects of Kafka logs include:

- Ordered Storage: Logs organize messages in a strictly ordered fashion, maintaining the sequence of messages.

- Sequence ID: Kafka assigns a unique sequence ID to each new message, ensuring that the order of messages is preserved within the log.

Understanding these basic Kafka components is essential for building a solid foundation in Kafka-based data streaming and messaging systems.

### Visualizing Kafka Message Flow

Let's take a moment to visualize how a producer and a consumer interact with a Kafka broker to send and receive messages.

#### Producer Sending Messages to Kafka

1. Topic Declaration: The producer initiates communication with Kafka by declaring the topic it wishes to "discuss." In this example, we'll call it "abc." Kafka, in response, assigns a physical location on the hard drive for that specific topic, known as the topic logs.

2. Message Transmission: The producer then transmits messages to Kafka. In our illustration, it sends messages 1, 2, and 3.

3. Message ID Assignment: Kafka plays its role by assigning unique IDs to each incoming message and meticulously records them within the topic logs.

4. Acknowledgment to Producer: Kafka ensures that the messages are safely received and logged, sending an acknowledgment back to the producer to confirm successful transmission.

#### Consumer Receiving Messages

1. Consumer's Topic Declaration: The consumer, situated on the other side, communicates its intention to Kafka, expressing the desire to read messages from a specific topic, in our case, "abc."

2. Log Inspection: Kafka promptly checks the topic logs to identify which messages within that topic have already been consumed and which ones are still pending.

3. Message Forwarding: After identifying unread messages, Kafka forwards them to the consumer, ensuring the seamless flow of information.

4. Consumer Acknowledgment: The consumer acknowledges the receipt of these messages, signaling to Kafka that they have been successfully received.

In this manner, Kafka orchestrates the transmission of messages between producers and consumers, ensuring that data flows smoothly, is logged systematically, and is consumed efficiently.

## Understanding Kafka: __consumer_offsets, Consumer Groups, and Partitions

In Kafka, there are essential components and concepts that go beyond the basic message flow. Let's delve into these key elements to gain a comprehensive understanding of Kafka's capabilities.

### __consumer_offsets: Tracking Message Consumption

Kafka employs a special topic known as "__consumer_offsets" to meticulously track the consumption of messages by consumers. This topic serves as a ledger that records the messages read by each consumer and the associated topic. Essentially, Kafka uses itself to maintain a record of consumer activities.

- Message Logging: When a consumer successfully reads and acknowledges a message, Kafka records this event by posting a message to the "__consumer_offsets" topic. This message contains crucial information, including the consumer ID, the relevant topic, and the message IDs that the consumer has consumed.

- Resilience: In the event that a consumer fails and is subsequently restarted, Kafka utilizes the information stored in "__consumer_offsets" to determine the last message delivered to that consumer. This ensures that the consumer can resume from where it left off.

- Multiple Consumers: When multiple consumers are active, Kafka retains visibility into which messages have been consumed by which consumers. Thus, even if a message has been read by one consumer, it can still be delivered to another if necessary.

### Consumer Groups: Collaborative Consumption

Consumer groups are integral to Kafka's architecture and facilitate collaboration among consumers:

- Group Dynamics: A consumer group comprises multiple consumers, enabling them to work collectively on message consumption.

- Single Entity: Kafka treats all consumers within a group as a single entity. When one consumer within the group reads a message, that message will not be delivered to any other consumer within the same group.

- Scalability: Consumer groups are essential for scaling consumer applications independently. A consumer app consisting of multiple consumer nodes can operate seamlessly without dealing with duplicated or redundant messages.

- Identification: Both consumer groups and individual consumers within those groups have unique IDs that Kafka uses for efficient management.

- Default Value: By default, all consumers belong to a consumer group with a group size of 1.

### Partitions: Scalability and Ordering

Kafka's topics can be divided into partitions, which are pivotal for scalability and maintaining message order:

- Partition Assignment: Each partition is assigned to one consumer exclusively. However, a single consumer may be responsible for multiple partitions.

- Reassignment on Failure: In the event of a consumer failure, Kafka intelligently reassigns the associated partitions to another consumer to ensure uninterrupted processing.

- Optimal Partition-Consumer Ratio: Ideally, there should be as many partitions as there are consumers in the consumer group. Over- or under-partitioning can lead to inefficient resource utilization.

- Scalability: Partitions, in conjunction with consumer groups, enable Kafka to scale effectively. Increasing the number of partitions allows a consumer group to accommodate more consumers, which can lead to faster message processing.

- Message Assignment: Messages within a topic are assigned to partitions based on their keys. The hashing of message keys and the division of hashes by the number of partitions determine the partition to which a message is assigned. This ensures message order is preserved.

- Considerations: While key-based partitioning helps maintain message order, it can result in uneven partition sizes if some keys are more active than others. This trade-off is generally accepted in practice for its advantages.

Kafka's use of "__consumer_offsets," the concept of consumer groups, and the partitioning of topics are critical elements that contribute to its scalability, fault tolerance, and message ordering capabilities.

### Kafka Partition Replication for Fault Tolerance

In Kafka, partition replication is a crucial mechanism implemented to ensure fault tolerance and the uninterrupted flow of data. Let's explore how partition replication works in Kafka to safeguard data integrity and system availability.

#### Replicating Partitions Across Brokers

- Replicated Partitions: Kafka replicates partitions across multiple brokers within the Kafka cluster. This replication strategy serves as a proactive measure to protect against broker failures.

- Leader-Follower Model: Within each replicated partition, one of the brokers is designated as the leader. The leader broker assumes the responsibility of handling incoming messages and writing them to its partition log. This log is the authoritative source for that partition.

- Replica Partitions: To enhance redundancy and fault tolerance, the partition log is replicated to other brokers in the cluster. These brokers maintain replica partitions that should ideally contain the same messages as the leader partition. This replication ensures data durability.

#### Fault Tolerance and Leader Failures

- Leader Failures: In the unfortunate event that the broker containing the leader partition experiences a failure, Kafka's fault tolerance mechanisms come into play. Another broker within the replication set automatically steps in as the new leader. This seamless transition ensures that data processing continues from the point where the failed broker left off.

- Uninterrupted Data Flow: This fault tolerance mechanism guarantees that both producers and consumers can continue posting and reading messages, even in the face of broker failures. It is a fundamental feature of Kafka that bolsters system reliability.

#### Replication Factor at Topic Level

- Configurable Replication Factor: Kafka provides the flexibility to define the replication factor for partitions at the topic level. The replication factor specifies how many replicas of each partition should be maintained.

- Importance of Replication Factor: A replication factor of 1 (indicating no replicas) is generally undesirable in most scenarios. Without replicas, the system faces a significant risk. If the leader broker were to fail, the partition becomes unavailable, potentially leading to catastrophic consequences in critical applications.

Kafka's partition replication mechanism is a cornerstone of its fault tolerance strategy. By replicating data across multiple brokers, ensuring seamless leader transitions, and allowing for configurable replication factors at the topic level, Kafka empowers organizations to build resilient and highly available data pipelines and messaging systems.

## Kafka Configurations: Fine-Tuning Your Kafka Ecosystem

Kafka offers a multitude of configuration settings that enable you to fine-tune the behavior and performance of various actors within the Kafka ecosystem. This section will provide an overview of key configurations for topics, consumers, and producers.

### Topic Configurations

- retention.ms

  - Description: Specifies the time, in milliseconds, that a particular topic log will be retained before being automatically deleted due to storage space limitations.

  - Use Case: Managing the lifespan of data within a topic to prevent indefinite storage.

- cleanup.policy

  - Description: Defines the cleanup policy to apply when the retention time specified by retention.ms is reached.

  - Options:

    - Delete: Messages are deleted when their retention time expires.

    - Compaction: Compaction is a batch process that retains only the latest version of each message key.

  - Note: Compaction is a batch job and may take time to execute.

- partition

  - Description: Indicates the number of partitions for a topic.

  - Considerations: Increasing the number of partitions offers parallelism but also increases the resource demands on Kafka. Overloading the cluster may occur if not managed carefully.

- replication

  - Description: Defines the replication factor, i.e., the number of times a partition is replicated across different brokers.

  - Use Case: Ensuring data durability and fault tolerance by maintaining multiple copies of data.

### Consumer Configurations

- offset

  - Description: Represents the sequence of message IDs that have been read by the consumer.

  - Use Case: Tracking the consumer's progress in consuming messages within a topic.

- consumer.group.id

  - Description: Assigns a unique ID to a consumer group. All consumers within the same group share the same consumer.group.id.

  - Purpose: Organizing consumers into groups for coordinated message consumption.

- auto_offset_reset

  - Description: Determines how Kafka behaves when a consumer subscribes to a pre-existing topic for the first time and needs to establish its starting point.

  - Options:

    - earliest: Consumes all existing messages in the topic log.

    - latest: Ignores existing old messages and starts consuming only new messages.

  - Use Case: Handling initial message consumption by new consumers.

### Producer Configurations

- acks

  - Description: Specifies the acknowledgment behavior policy when a producer sends a message.
  
  - Options:
  
    - 0 (Fire and Forget): The producer does not wait for acknowledgments from leader or replica brokers.

    - 1: The producer waits for the leader broker to write the message to disk.

    - all: The producer waits for both the leader and all replica brokers to write the message to disk.

  - Use Case: Balancing performance and reliability in message transmission.

These configurations offer flexibility and control in shaping your Kafka deployment to meet specific requirements. Properly configuring these settings is essential for optimizing Kafka's performance, durability, and resilience while aligning with your application's needs.

## Avro and Schema Registry: The Importance of Schemas

In the context of Kafka, schemas play a crucial role in ensuring data compatibility and understanding among producers and consumers. Let's explore why schemas are essential in Kafka messaging.

### The Need for Schemas

Kafka messages are incredibly flexible, allowing producers to send a wide range of data formats, from plain text to complex binary objects. While this flexibility is a strength of Kafka, it can also introduce challenges:

1. Data Compatibility: Without a defined structure or schema, consumers may struggle to understand the format and semantics of the messages they receive. For example, a producer might send data in a PHP-specific format, while a consumer is written in Python. This disparity in formats can lead to misinterpretation or outright data processing errors.

2. Versioning: Over time, data formats can evolve, and new fields or changes may be introduced. Without a schema to communicate these changes, consumers might continue to expect the old format, resulting in data inconsistencies and compatibility issues.

3. Data Validation: Schemas provide a means of data validation. They allow consumers to ensure that incoming messages adhere to the expected structure and data types, reducing the risk of runtime errors.

### Avro and Schema Registry

To address these challenges, Kafka leverages Avro, a data serialization framework, and the Schema Registry:

- Avro: Avro is a compact, efficient, and language-agnostic data serialization framework. It provides a schema definition that serves as a contract between producers and consumers. With Avro, data is serialized using the defined schema, ensuring that both producers and consumers understand the structure and data types.

- Schema Registry: The Schema Registry is a centralized service that stores and manages schemas. It acts as a repository for schemas used by producers and consumers. When a producer sends a message, it includes the schema ID in the message header. Consumers can retrieve the schema from the Schema Registry using this ID, ensuring that they interpret the message correctly.

By incorporating Avro and the Schema Registry into the Kafka ecosystem, organizations can achieve data compatibility, versioning control, and data validation, all while preserving the flexibility and scalability that Kafka offers.

Schemas are a critical component in Kafka to ensure that data can be understood and processed consistently across a diverse range of producers and consumers, ultimately enhancing the reliability and interoperability of Kafka-based data pipelines.

To address the compatibility and understanding challenges between Kafka producers and consumers, the introduction of schemas is a pivotal solution. Schemas serve as a common language that producers use to define the structure of the data they're sending, allowing consumers to comprehend and process it correctly. Here's how schemas bridge the gap:

#### Producers Define Data Structure

1. Structured Data: Producers encapsulate data within a predefined schema. This schema specifies the data's structure, including the types and arrangement of fields. For instance, it defines what each field represents, such as name, age, timestamp, or any other attribute.

2. Schema as a Contract: The schema essentially acts as a contract between the producer and consumer. It articulates what the data should look like, ensuring consistency and predictability.

#### Consumers Gain Understanding

1. Schema as a Reference: Consumers refer to the schema to interpret incoming data. They rely on the schema to understand the format and semantics of the messages they receive.

2. Data Validation: With the schema as a guide, consumers can validate incoming data to ensure it conforms to the expected structure and data types. This reduces the risk of processing errors.

#### Schema Evolution

1. Version Control: Schemas can evolve over time to accommodate changes in data structure or additional fields. When changes are made, both producers and consumers can be updated to use the new schema version.

2. Backward and Forward Compatibility: Well-managed schemas allow for backward compatibility (old consumers can still read new data) and forward compatibility (new consumers can still understand old data). This is crucial for maintaining a smooth transition when updating schemas.

#### Standardization with Avro and Schema Registry

To implement schemas effectively within the Kafka ecosystem, technologies like Avro and Schema Registry are commonly used:

1. Avro: Avro is a versatile, language-agnostic framework for data serialization. It provides a concise and efficient way to define schemas and serialize data accordingly.

2. Schema Registry: The Schema Registry is a centralized service that stores and manages schemas. It serves as a repository for schemas, ensuring that all producers and consumers have access to the same schema definitions.

By embracing schemas, organizations can foster compatibility, enhance data understanding, and facilitate seamless data processing in Kafka, ultimately optimizing the effectiveness of their data pipelines.

## Introduction to Avro: Efficient Data Serialization with Schema Separation

Avro is a powerful data serialization system that excels at transforming data structures or objects into a format suitable for storage or transmission. Unlike some other serialization systems like Protobuf or JSON, Avro introduces a unique approach by separating the schema from the actual data record. Here are some key aspects of Avro:

### Schema Separation

- Schema Separation: Avro stores the schema independently from the data record itself. This means that to correctly read an Avro record, you need a separate Avro schema definition. The schema serves as a blueprint for interpreting the data, ensuring that consumers can understand the structure and types of the incoming records.

#### Binary Encoding and JSON/IDL Schema Definitions

- Binary Encoding: Records in Avro are stored using binary encoding, which is efficient for data transmission and storage.

- Schema Definitions: Avro schemas are defined using JSON or IDL (Interface Definition Language), providing a clear and human-readable way to describe data structures.

#### Advantages of Avro

Avro's design offers several advantages:

1. Smaller Record Filesize: Compared to formats like JSON, Avro typically results in smaller record sizes when serialized. This efficiency is particularly valuable in scenarios where bandwidth or storage space is a concern.

2. Schema Evolution: Avro's separation of schema from data allows for schema evolution over time without breaking consumers. This means you can modify the schema to accommodate changes or additions to the data structure without causing compatibility issues with existing consumers.

3. Automatic Schema Validation: Avro clients provide automatic validation against schemas. If an incompatible schema is attempted to be pushed between different versions of an application, the Kafka Avro client will prevent it. This ensures that the contract between producers and consumers is maintained.

#### Avro in Kafka

Avro is supported as a data serialization format in Kafka, making it an ideal choice for maintaining data compatibility, schema evolution, and efficient data transmission within the Kafka ecosystem. While Protobuf is also supported, this lesson will focus on Avro as the serialization solution.

#### Schema Compatibility and the Role of Schema Registry

In distributed systems, maintaining compatibility between producers and consumers is essential. With implicit schemas, such as those used in JSON, making changes to the data structure can lead to compatibility issues. For example, changing a data type from an integer to a string may break existing consumers that expect an integer.

A schema registry is introduced as a solution to this problem. The schema registry serves as a central repository for schemas and ensures that any changes to schemas are compatible with previous versions. This registry enables automatic validation of data against schemas, helping to uphold the contract between producers and consumers.

#### Avro Schema Evolution

Avro allows for three types of schema evolutions:

1. Backward Compatibility: Producers using older schemas can generate messages that can be read by consumers using newer schemas. This ensures that consumers can handle data produced by earlier versions of producers.

2. Forward Compatibility: Producers using newer schemas can generate messages that can be read by consumers using older schemas. This is crucial for situations where consumers lag behind in schema updates.

3. Mixed/Hybrid Versions: The ideal scenario where schemas are both forward and backward compatible, offering maximum flexibility for schema evolution.

Avro's schema separation, binary encoding, and compatibility features make it a powerful tool for data serialization and ensure smooth communication within the Kafka ecosystem. The schema registry complements Avro by centralizing schema management and validation.

#### Schema Registry: Managing Schema Compatibilty in Kafka

The Schema Registry is a pivotal component in the Kafka ecosystem, responsible for managing schemas and ensuring data compatibility between producers and consumers. Here's an overview of how it works and how it deals with schema compatibility:

### Schema Registry Workflow

1. Producer Requests Schema Registration:

    - The producer initiates contact with the Schema Registry, indicating its intention to post messages to a specific Kafka topic (e.g., "ABC") and specifying the schema version it plans to use (e.g., "v1").

2. Schema Check:

    - The Schema Registry examines the provided schema.

    - If no schema exists for the specified topic ("ABC"), the registry registers the new schema and grants approval to the producer.

3. Compatibility Verification:

    - If a schema already exists for the topic, the Schema Registry conducts a compatibility check between the producer's schema and the registered schema.

    - If the schemas are compatible, the registry signals the producer to proceed with message posting.

4. Incompatibility Handling:

    - If the compatibility check fails, indicating that the new schema is not backward compatible with the existing one, the registry informs the producer of the incompatibility, and the producer receives an error message.

5. Producer Sends Messages:

    - Once compatibility is confirmed, the producer begins sending messages to the specified Kafka topic ("ABC"), utilizing the specified schema version ("v1").
Consumer Schema Resolution:

    - When a consumer intends to consume messages from a topic, it queries the Schema Registry to determine which schema version to use.

    - If multiple schema versions are available and compatible, consumers have the flexibility to choose a schema version that suits their needs.

### Handling Incompatible Schemas

In situations where schema updates break compatibility with previous versions, a common strategy is to create a new topic specifically for the updated schema. Here's how this process works:

1. New Schema and Topic Creation:

    - A new schema version is defined, resulting in a new schema definition.

    - A corresponding Kafka topic is created to accommodate messages using the updated schema.

2. Downstream Service for Conversion:

    - A downstream service or component is introduced to convert messages from the new schema to the old schema format.

3. Progressive Migration:

    - The converted messages are published to the original topic, allowing for a transition period.

    - Services can be migrated to consume messages from the new topic gradually, avoiding abrupt disruptions.

This approach ensures a smooth transition from the old schema to the new one while maintaining compatibility with existing consumers.

The Schema Registry's role in handling schema compatibility and version management is fundamental to maintaining reliable data pipelines and communication within Kafka.

