### Hazelcast SPI Modules:
* HyperLogLog module
* TreeSet Module

You need to use these hazelcast configuration files or include the corresponding sections to your configuration in order to use the modules. Otherwise Hazelcast will not recognize service names.
* Server Config: ```src/resources/hazelcast.xml```
* Client Config: ```src/resources/hazelcast-client.xml```

#### HyperLogLog Module:
HyperLogLog module uses [java-hll](https://github.com/aggregateknowledge/java-hll) backend which is a Java implementation of HyperLogLog algorithm. Here is a sample usage:
```java
HazelcastInstance server = Hazelcast.newHazelcastInstance();
hll = server.getDistributedObject(HyperLogLogService.SERVICE_NAME, "counter");
hll.add("TEST0");
hll.add("TEST1");
hll.add("TEST2");
long cardinality = hll.cardinality();
```
Currently supported features based on [java-hll](https://github.com/aggregateknowledge/java-hll)
* add
* addAll
* union
* cardinality

You can find more examples in test directory (src/test/java/org/rakam/cache/hazelcast/hyperloglog/HLLTest.java).

#### ITreeSet Module:
ITreeSet module uses native [TreeSet](http://docs.oracle.com/javase/6/docs/api/java/util/TreeSet.html) data structure. You should implement Comparable interface to your classes, TreeSet keeps the the set in descending order. Therefore when you run treeset.iterate() it returns the ordered set. The insertion complexity of TreeSet is O(logN).
```java
HazelcastInstance server = Hazelcast.newHazelcastInstance();
treeSet = server.getDistributedObject(TreeSetService.SERVICE_NAME, "set");
treeSet.add("TEST0");
treeSet.add("TEST2");
treeSet.add("TEST1");
for(Object item: treeSet) {
    System.out.println(item);
}
```
ITreeSet uses Hazelcast Collections so the supported methods are same as ISet.

You can find more examples in test directory. ```src/test/java/org/rakam/cache/hazelcast/treeset/TreeSetTest.java```

####License

These Hazelcast modules is available under the MIT License.

