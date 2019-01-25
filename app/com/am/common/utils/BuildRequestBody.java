package com.am.common.utils;


import akka.stream.Attributes;
import akka.stream.FlowShape;
import akka.stream.Inlet;
import akka.stream.Outlet;
import akka.stream.stage.AbstractInHandler;
import akka.stream.stage.AbstractOutHandler;
import akka.stream.stage.GraphStage;
import akka.stream.stage.GraphStageLogic;
import akka.util.ByteString;
import org.apache.commons.lang3.ArrayUtils;

public class BuildRequestBody extends GraphStage<FlowShape<ByteString, ByteString>> {

    private Inlet<ByteString> in = Inlet.create("BuildRequestBody.in");

    private Outlet<ByteString> out = Outlet.create("BuildRequestBody.out");

    private FlowShape<ByteString, ByteString> shape = FlowShape.of(in, out);

    private byte[] buffer;

    @Override
    public FlowShape<ByteString, ByteString> shape() {
        return shape;
    }

    @Override
    public GraphStageLogic createLogic(Attributes inheritedAttributes) {
        return new GraphStageLogic(shape) {
            {
                setHandler(out, new AbstractOutHandler() {
                    @Override
                    public void onPull() throws Exception {
                        pull(in);
                    }
                });
                setHandler(in, new AbstractInHandler() {
                    @Override
                    public void onPush() throws Exception {
                        buffer = ArrayUtils.addAll(buffer, grab(in).toArray());
                        pull(in);
                    }

                    @Override
                    public void onUpstreamFinish() throws Exception {
                        if (buffer != null) {
                            emit(out, ByteString.fromArray(buffer));
                        }
                        completeStage();
                    }
                });
            }

        };
    }

    public String getRequestBody() {
        if(buffer != null) {
            return new String(buffer);
        }
        return "";
    }
}
