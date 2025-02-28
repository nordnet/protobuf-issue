# How to trigger this issue

1. Build with maven `mvn clean package`
2. From repo root, run the application in bash. Depending on cpu it might take more than 2 concurrent runners, with Mac M3 it will trigger within seconds using 4 runners.
```bash
# Start this in 2-4 terminals
 while [[ true ]] ; do java -jar target/protobuf-issue-1.0.0-jar-with-dependencies.jar || break ; done
```

## What to expect
Depending on which thread that gets the problem, you will either see:
```
java.lang.ExceptionInInitializerError
	at com.google.devtools.cloudtrace.v2.Span.getDescriptor(Span.java:62)
	at se.nordnet.protobuf.ProtobufTester.runAfterLatch(ProtobufTester.java:30)
	at se.nordnet.protobuf.ProtobufTester.lambda$main$0(ProtobufTester.java:19)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	at java.base/java.lang.Thread.run(Thread.java:1583)
Caused by: java.lang.NullPointerException: Cannot invoke "com.google.protobuf.DescriptorProtos$FeatureSet.getExtension(com.google.protobuf.ExtensionLite)" because the return value of "com.google.protobuf.Descriptors$FieldDescriptor.getFeatures()" is null
	at com.google.protobuf.Descriptors$FieldDescriptor.legacyEnumFieldTreatedAsClosed(Descriptors.java:1582)
	at com.google.protobuf.MessageReflection.mergeFieldFrom(MessageReflection.java:1219)
	at com.google.protobuf.GeneratedMessage$ExtendableBuilder.parseUnknownField(GeneratedMessage.java:1630)
	at com.google.protobuf.DescriptorProtos$FieldOptions$Builder.mergeFrom(DescriptorProtos.java:29650)
	at com.google.protobuf.DescriptorProtos$FieldOptions$Builder.mergeFrom(DescriptorProtos.java:29179)
	at com.google.protobuf.CodedInputStream$ArrayDecoder.readMessage(CodedInputStream.java:853)
	at com.google.protobuf.DescriptorProtos$FieldDescriptorProto$Builder.mergeFrom(DescriptorProtos.java:13676)
	at com.google.protobuf.DescriptorProtos$FieldDescriptorProto$1.parsePartialFrom(DescriptorProtos.java:14461)
	at com.google.protobuf.DescriptorProtos$FieldDescriptorProto$1.parsePartialFrom(DescriptorProtos.java:14453)
	at com.google.protobuf.CodedInputStream$ArrayDecoder.readMessage(CodedInputStream.java:869)
	at com.google.protobuf.DescriptorProtos$DescriptorProto$Builder.mergeFrom(DescriptorProtos.java:7393)
	at com.google.protobuf.DescriptorProtos$DescriptorProto$1.parsePartialFrom(DescriptorProtos.java:9525)
	at com.google.protobuf.DescriptorProtos$DescriptorProto$1.parsePartialFrom(DescriptorProtos.java:9517)
	at com.google.protobuf.CodedInputStream$ArrayDecoder.readMessage(CodedInputStream.java:869)
	at com.google.protobuf.DescriptorProtos$FileDescriptorProto$Builder.mergeFrom(DescriptorProtos.java:2603)
	at com.google.protobuf.DescriptorProtos$FileDescriptorProto$1.parsePartialFrom(DescriptorProtos.java:4517)
	at com.google.protobuf.DescriptorProtos$FileDescriptorProto$1.parsePartialFrom(DescriptorProtos.java:4509)
	at com.google.protobuf.AbstractParser.parsePartialFrom(AbstractParser.java:77)
	at com.google.protobuf.AbstractParser.parseFrom(AbstractParser.java:97)
	at com.google.protobuf.DescriptorProtos$FileDescriptorProto$1.parseFrom(DescriptorProtos.java:4509)
	at com.google.protobuf.DescriptorProtos$FileDescriptorProto.parseFrom(DescriptorProtos.java:2077)
	at com.google.protobuf.Descriptors$FileDescriptor.internalUpdateFileDescriptor(Descriptors.java:505)
	at com.google.devtools.cloudtrace.v2.TraceProto.<clinit>(TraceProto.java:353)
	... 8 more
```
or
```
java.lang.ExceptionInInitializerError
	at com.google.pubsub.v1.PubsubProto.<clinit>(PubsubProto.java:863)
	at com.google.pubsub.v1.PubsubMessage.getDescriptor(PubsubMessage.java:61)
	at se.nordnet.protobuf.ProtobufTester.runAfterLatch(ProtobufTester.java:30)
	at se.nordnet.protobuf.ProtobufTester.lambda$main$1(ProtobufTester.java:20)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	at java.base/java.lang.Thread.run(Thread.java:1583)
Caused by: java.lang.NullPointerException: Cannot invoke "com.google.protobuf.DescriptorProtos$FeatureSet.getExtension(com.google.protobuf.ExtensionLite)" because the return value of "com.google.protobuf.Descriptors$FieldDescriptor.getFeatures()" is null
	at com.google.protobuf.Descriptors$FieldDescriptor.legacyEnumFieldTreatedAsClosed(Descriptors.java:1582)
	at com.google.protobuf.MessageReflection.mergeFieldFrom(MessageReflection.java:1219)
	at com.google.protobuf.GeneratedMessage$ExtendableBuilder.parseUnknownField(GeneratedMessage.java:1630)
	at com.google.protobuf.DescriptorProtos$FieldOptions$Builder.mergeFrom(DescriptorProtos.java:29650)
	at com.google.protobuf.DescriptorProtos$FieldOptions$Builder.mergeFrom(DescriptorProtos.java:29179)
	at com.google.protobuf.CodedInputStream$ArrayDecoder.readMessage(CodedInputStream.java:853)
	at com.google.protobuf.DescriptorProtos$FieldDescriptorProto$Builder.mergeFrom(DescriptorProtos.java:13676)
	at com.google.protobuf.DescriptorProtos$FieldDescriptorProto$1.parsePartialFrom(DescriptorProtos.java:14461)
	at com.google.protobuf.DescriptorProtos$FieldDescriptorProto$1.parsePartialFrom(DescriptorProtos.java:14453)
	at com.google.protobuf.CodedInputStream$ArrayDecoder.readMessage(CodedInputStream.java:869)
	at com.google.protobuf.DescriptorProtos$DescriptorProto$Builder.mergeFrom(DescriptorProtos.java:7393)
	at com.google.protobuf.DescriptorProtos$DescriptorProto$1.parsePartialFrom(DescriptorProtos.java:9525)
	at com.google.protobuf.DescriptorProtos$DescriptorProto$1.parsePartialFrom(DescriptorProtos.java:9517)
	at com.google.protobuf.CodedInputStream$ArrayDecoder.readMessage(CodedInputStream.java:869)
	at com.google.protobuf.DescriptorProtos$FileDescriptorProto$Builder.mergeFrom(DescriptorProtos.java:2603)
	at com.google.protobuf.DescriptorProtos$FileDescriptorProto$1.parsePartialFrom(DescriptorProtos.java:4517)
	at com.google.protobuf.DescriptorProtos$FileDescriptorProto$1.parsePartialFrom(DescriptorProtos.java:4509)
	at com.google.protobuf.AbstractParser.parsePartialFrom(AbstractParser.java:77)
	at com.google.protobuf.AbstractParser.parseFrom(AbstractParser.java:97)
	at com.google.protobuf.DescriptorProtos$FileDescriptorProto$1.parseFrom(DescriptorProtos.java:4509)
	at com.google.protobuf.DescriptorProtos$FileDescriptorProto.parseFrom(DescriptorProtos.java:2077)
	at com.google.protobuf.Descriptors$FileDescriptor.internalUpdateFileDescriptor(Descriptors.java:505)
	at com.google.pubsub.v1.SchemaProto.<clinit>(SchemaProto.java:341)
	... 9 more
```