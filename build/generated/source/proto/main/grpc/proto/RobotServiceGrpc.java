package proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: Grpc.proto")
public final class RobotServiceGrpc {

  private RobotServiceGrpc() {}

  public static final String SERVICE_NAME = "proto.RobotService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<proto.Grpc.RobotInfo,
      proto.Grpc.Empty> getNotifyNewRobotMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "NotifyNewRobot",
      requestType = proto.Grpc.RobotInfo.class,
      responseType = proto.Grpc.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Grpc.RobotInfo,
      proto.Grpc.Empty> getNotifyNewRobotMethod() {
    io.grpc.MethodDescriptor<proto.Grpc.RobotInfo, proto.Grpc.Empty> getNotifyNewRobotMethod;
    if ((getNotifyNewRobotMethod = RobotServiceGrpc.getNotifyNewRobotMethod) == null) {
      synchronized (RobotServiceGrpc.class) {
        if ((getNotifyNewRobotMethod = RobotServiceGrpc.getNotifyNewRobotMethod) == null) {
          RobotServiceGrpc.getNotifyNewRobotMethod = getNotifyNewRobotMethod =
              io.grpc.MethodDescriptor.<proto.Grpc.RobotInfo, proto.Grpc.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "NotifyNewRobot"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Grpc.RobotInfo.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Grpc.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new RobotServiceMethodDescriptorSupplier("NotifyNewRobot"))
              .build();
        }
      }
    }
    return getNotifyNewRobotMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Grpc.RemoveRobotRequest,
      proto.Grpc.RemoveRobotResponse> getRemoveRobotMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "removeRobot",
      requestType = proto.Grpc.RemoveRobotRequest.class,
      responseType = proto.Grpc.RemoveRobotResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Grpc.RemoveRobotRequest,
      proto.Grpc.RemoveRobotResponse> getRemoveRobotMethod() {
    io.grpc.MethodDescriptor<proto.Grpc.RemoveRobotRequest, proto.Grpc.RemoveRobotResponse> getRemoveRobotMethod;
    if ((getRemoveRobotMethod = RobotServiceGrpc.getRemoveRobotMethod) == null) {
      synchronized (RobotServiceGrpc.class) {
        if ((getRemoveRobotMethod = RobotServiceGrpc.getRemoveRobotMethod) == null) {
          RobotServiceGrpc.getRemoveRobotMethod = getRemoveRobotMethod =
              io.grpc.MethodDescriptor.<proto.Grpc.RemoveRobotRequest, proto.Grpc.RemoveRobotResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "removeRobot"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Grpc.RemoveRobotRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Grpc.RemoveRobotResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RobotServiceMethodDescriptorSupplier("removeRobot"))
              .build();
        }
      }
    }
    return getRemoveRobotMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Grpc.RobotAliveRequest,
      proto.Grpc.RobotAliveResponse> getRobotAliveMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RobotAlive",
      requestType = proto.Grpc.RobotAliveRequest.class,
      responseType = proto.Grpc.RobotAliveResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Grpc.RobotAliveRequest,
      proto.Grpc.RobotAliveResponse> getRobotAliveMethod() {
    io.grpc.MethodDescriptor<proto.Grpc.RobotAliveRequest, proto.Grpc.RobotAliveResponse> getRobotAliveMethod;
    if ((getRobotAliveMethod = RobotServiceGrpc.getRobotAliveMethod) == null) {
      synchronized (RobotServiceGrpc.class) {
        if ((getRobotAliveMethod = RobotServiceGrpc.getRobotAliveMethod) == null) {
          RobotServiceGrpc.getRobotAliveMethod = getRobotAliveMethod =
              io.grpc.MethodDescriptor.<proto.Grpc.RobotAliveRequest, proto.Grpc.RobotAliveResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RobotAlive"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Grpc.RobotAliveRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Grpc.RobotAliveResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RobotServiceMethodDescriptorSupplier("RobotAlive"))
              .build();
        }
      }
    }
    return getRobotAliveMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Grpc.RobotBalanceRequest,
      proto.Grpc.Empty> getBalanceDistrictMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "BalanceDistrict",
      requestType = proto.Grpc.RobotBalanceRequest.class,
      responseType = proto.Grpc.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Grpc.RobotBalanceRequest,
      proto.Grpc.Empty> getBalanceDistrictMethod() {
    io.grpc.MethodDescriptor<proto.Grpc.RobotBalanceRequest, proto.Grpc.Empty> getBalanceDistrictMethod;
    if ((getBalanceDistrictMethod = RobotServiceGrpc.getBalanceDistrictMethod) == null) {
      synchronized (RobotServiceGrpc.class) {
        if ((getBalanceDistrictMethod = RobotServiceGrpc.getBalanceDistrictMethod) == null) {
          RobotServiceGrpc.getBalanceDistrictMethod = getBalanceDistrictMethod =
              io.grpc.MethodDescriptor.<proto.Grpc.RobotBalanceRequest, proto.Grpc.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "BalanceDistrict"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Grpc.RobotBalanceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Grpc.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new RobotServiceMethodDescriptorSupplier("BalanceDistrict"))
              .build();
        }
      }
    }
    return getBalanceDistrictMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RobotServiceStub newStub(io.grpc.Channel channel) {
    return new RobotServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RobotServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RobotServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RobotServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RobotServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class RobotServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void notifyNewRobot(proto.Grpc.RobotInfo request,
        io.grpc.stub.StreamObserver<proto.Grpc.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getNotifyNewRobotMethod(), responseObserver);
    }

    /**
     */
    public void removeRobot(proto.Grpc.RemoveRobotRequest request,
        io.grpc.stub.StreamObserver<proto.Grpc.RemoveRobotResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRemoveRobotMethod(), responseObserver);
    }

    /**
     */
    public void robotAlive(proto.Grpc.RobotAliveRequest request,
        io.grpc.stub.StreamObserver<proto.Grpc.RobotAliveResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRobotAliveMethod(), responseObserver);
    }

    /**
     */
    public void balanceDistrict(proto.Grpc.RobotBalanceRequest request,
        io.grpc.stub.StreamObserver<proto.Grpc.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getBalanceDistrictMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getNotifyNewRobotMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.Grpc.RobotInfo,
                proto.Grpc.Empty>(
                  this, METHODID_NOTIFY_NEW_ROBOT)))
          .addMethod(
            getRemoveRobotMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.Grpc.RemoveRobotRequest,
                proto.Grpc.RemoveRobotResponse>(
                  this, METHODID_REMOVE_ROBOT)))
          .addMethod(
            getRobotAliveMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.Grpc.RobotAliveRequest,
                proto.Grpc.RobotAliveResponse>(
                  this, METHODID_ROBOT_ALIVE)))
          .addMethod(
            getBalanceDistrictMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.Grpc.RobotBalanceRequest,
                proto.Grpc.Empty>(
                  this, METHODID_BALANCE_DISTRICT)))
          .build();
    }
  }

  /**
   */
  public static final class RobotServiceStub extends io.grpc.stub.AbstractStub<RobotServiceStub> {
    private RobotServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RobotServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RobotServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RobotServiceStub(channel, callOptions);
    }

    /**
     */
    public void notifyNewRobot(proto.Grpc.RobotInfo request,
        io.grpc.stub.StreamObserver<proto.Grpc.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNotifyNewRobotMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeRobot(proto.Grpc.RemoveRobotRequest request,
        io.grpc.stub.StreamObserver<proto.Grpc.RemoveRobotResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveRobotMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void robotAlive(proto.Grpc.RobotAliveRequest request,
        io.grpc.stub.StreamObserver<proto.Grpc.RobotAliveResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRobotAliveMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void balanceDistrict(proto.Grpc.RobotBalanceRequest request,
        io.grpc.stub.StreamObserver<proto.Grpc.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getBalanceDistrictMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RobotServiceBlockingStub extends io.grpc.stub.AbstractStub<RobotServiceBlockingStub> {
    private RobotServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RobotServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RobotServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RobotServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public proto.Grpc.Empty notifyNewRobot(proto.Grpc.RobotInfo request) {
      return blockingUnaryCall(
          getChannel(), getNotifyNewRobotMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Grpc.RemoveRobotResponse removeRobot(proto.Grpc.RemoveRobotRequest request) {
      return blockingUnaryCall(
          getChannel(), getRemoveRobotMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Grpc.RobotAliveResponse robotAlive(proto.Grpc.RobotAliveRequest request) {
      return blockingUnaryCall(
          getChannel(), getRobotAliveMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Grpc.Empty balanceDistrict(proto.Grpc.RobotBalanceRequest request) {
      return blockingUnaryCall(
          getChannel(), getBalanceDistrictMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RobotServiceFutureStub extends io.grpc.stub.AbstractStub<RobotServiceFutureStub> {
    private RobotServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RobotServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RobotServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RobotServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Grpc.Empty> notifyNewRobot(
        proto.Grpc.RobotInfo request) {
      return futureUnaryCall(
          getChannel().newCall(getNotifyNewRobotMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Grpc.RemoveRobotResponse> removeRobot(
        proto.Grpc.RemoveRobotRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRemoveRobotMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Grpc.RobotAliveResponse> robotAlive(
        proto.Grpc.RobotAliveRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRobotAliveMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Grpc.Empty> balanceDistrict(
        proto.Grpc.RobotBalanceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getBalanceDistrictMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_NOTIFY_NEW_ROBOT = 0;
  private static final int METHODID_REMOVE_ROBOT = 1;
  private static final int METHODID_ROBOT_ALIVE = 2;
  private static final int METHODID_BALANCE_DISTRICT = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RobotServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RobotServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_NOTIFY_NEW_ROBOT:
          serviceImpl.notifyNewRobot((proto.Grpc.RobotInfo) request,
              (io.grpc.stub.StreamObserver<proto.Grpc.Empty>) responseObserver);
          break;
        case METHODID_REMOVE_ROBOT:
          serviceImpl.removeRobot((proto.Grpc.RemoveRobotRequest) request,
              (io.grpc.stub.StreamObserver<proto.Grpc.RemoveRobotResponse>) responseObserver);
          break;
        case METHODID_ROBOT_ALIVE:
          serviceImpl.robotAlive((proto.Grpc.RobotAliveRequest) request,
              (io.grpc.stub.StreamObserver<proto.Grpc.RobotAliveResponse>) responseObserver);
          break;
        case METHODID_BALANCE_DISTRICT:
          serviceImpl.balanceDistrict((proto.Grpc.RobotBalanceRequest) request,
              (io.grpc.stub.StreamObserver<proto.Grpc.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RobotServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RobotServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return proto.Grpc.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RobotService");
    }
  }

  private static final class RobotServiceFileDescriptorSupplier
      extends RobotServiceBaseDescriptorSupplier {
    RobotServiceFileDescriptorSupplier() {}
  }

  private static final class RobotServiceMethodDescriptorSupplier
      extends RobotServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RobotServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RobotServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RobotServiceFileDescriptorSupplier())
              .addMethod(getNotifyNewRobotMethod())
              .addMethod(getRemoveRobotMethod())
              .addMethod(getRobotAliveMethod())
              .addMethod(getBalanceDistrictMethod())
              .build();
        }
      }
    }
    return result;
  }
}
