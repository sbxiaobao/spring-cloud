package com.dianping.cat.message.spi.codec;

import com.dianping.cat.message.Heartbeat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.internal.DefaultHeartbeat;
import com.dianping.cat.message.internal.DefaultMessageTree;
import com.dianping.cat.message.internal.DefaultTransaction;
import com.dianping.cat.message.spi.MessageCodec;
import com.dianping.cat.message.spi.MessageTree;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.nio.charset.Charset;
import java.util.Stack;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/9
 */
public class NativeMessageCodec implements MessageCodec {

    private static final String ID = "NT1";

    @Override
    public MessageTree decode(ByteBuf buf) {
        buf.readInt();

        DefaultMessageTree tree = new DefaultMessageTree();
        Context ctx = new Context(tree);
        Codec.HEADER.decode(ctx, buf);
        Message msg = decodeMessage(ctx, buf);

        tree.setMessage(msg);
        tree.setBuffer(buf);

        return tree;
    }

    @Override
    public ByteBuf encode(MessageTree tree) {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(4 * 1024);

        try {
            Context ctx = new Context(tree);

            buf.writeInt(0);

            Codec.HEADER.encode(ctx, buf, null);

            Message msg = tree.getMessage();

            if (msg != null) {
                encodeMessage(ctx, buf, msg);
            }
            int readableBytes = buf.readableBytes();

            buf.setInt(0, readableBytes - 4);

            return buf;
        } catch (RuntimeException e) {
            buf.release();

            throw e;
        }
    }

    private void encodeMessage(Context ctx, ByteBuf buf, Message msg) {
        if (msg instanceof Heartbeat) {
            Codec.HEARTBEAT.encode(ctx, buf, msg);
        } else {
            throw new RuntimeException(String.format("Unsupported message(%s).", msg));
        }
    }

    private Message decodeMessage(Context ctx, ByteBuf buf) {
        Message msg = null;

        while (buf.readableBytes() > 0) {
            char ch = ctx.readId(buf);
            switch (ch) {
                case 'H':
                    Message h = Codec.HEARTBEAT.decode(ctx, buf);

                    ctx.addChild(h);
                    break;
                default:
                    throw new RuntimeException(String.format("Unsupported message type(%s).", ch));
            }
        }

        if (msg == null) {
            msg = ctx.getMessageTree().getMessage();
        }

        return msg;
    }

    @Override
    public void reset() {

    }

    static enum Codec {
        HEADER {
            @Override
            protected Message decode(Context ctx, ByteBuf buf) {
                MessageTree tree = ctx.getMessageTree();
                String version = ctx.getVersion(buf);

                if (ID.equals(version)) {
                    tree.setDomain(ctx.readString(buf));
                    tree.setHostName(ctx.readString(buf));
                    tree.setIpAddress(ctx.readString(buf));
                    tree.setThreadGroupName(ctx.readString(buf));
                    tree.setThreadId(ctx.readString(buf));
                    tree.setThreadName(ctx.readString(buf));
                    tree.setMessageId(ctx.readString(buf));
                    tree.setParentMessageId(ctx.readString(buf));
                    tree.setRootMessageId(ctx.readString(buf));
                    tree.setSessionToken(ctx.readString(buf));
                } else {
                    throw new RuntimeException(String.format("Unrecgnized version(%s) for binary message codec!", version));
                }
                return null;
            }

            @Override
            protected void encode(Context ctx, ByteBuf buf, Message msg) {
                MessageTree tree = ctx.getMessageTree();

                ctx.writeVersion(buf, ID);
                ctx.writeString(buf, tree.getDomain());
                ctx.writeString(buf, tree.getHostName());
                ctx.writeString(buf, tree.getIpAddress());
                ctx.writeString(buf, tree.getThreadGroupName());
                ctx.writeString(buf, tree.getThreadId());
                ctx.writeString(buf, tree.getThreadName());
                ctx.writeString(buf, tree.getMessageId());
                ctx.writeString(buf, tree.getParentMessageId());
                ctx.writeString(buf, tree.getRootMessageId());
                ctx.writeString(buf, tree.getSessionToken());
            }
        },

        HEARTBEAT {
            @Override
            protected Message decode(Context ctx, ByteBuf buf) {
                long timestamp = ctx.readTimestamp(buf);
                String type = ctx.readString(buf);
                String name = ctx.readString(buf);
                String status = ctx.readString(buf);
                String data = ctx.readString(buf);
                DefaultHeartbeat h = new DefaultHeartbeat(type, name);

                h.setTimestamp(timestamp);
                h.setStatus(status);
                h.addData(data);

                MessageTree tree = ctx.getMessageTree();
                if (tree instanceof DefaultMessageTree) {
                    ((DefaultMessageTree) tree).addHeartbeat(h);
                }
                return h;
            }

            @Override
            protected void encode(Context ctx, ByteBuf buf, Message msg) {
                ctx.writeId(buf, 'H');
                ctx.writeTimestamp(buf, msg.getTimestamp());
                ctx.writeString(buf, msg.getType());
                ctx.writeString(buf, msg.getName());
                ctx.writeString(buf, msg.getStatus());
                ctx.writeString(buf, msg.getData().toString());
            }
        };

        protected abstract Message decode(Context ctx, ByteBuf buf);

        protected abstract void encode(Context ctx, ByteBuf buf, Message msg);
    }

    private static class Context {
        private static Charset UTF8 = Charset.forName("UTF-8");

        private MessageTree tree;

        private byte[] data = new byte[256];

        private Stack<DefaultTransaction> parents = new Stack<>();

        public Context(MessageTree tree) {
            this.tree = tree;
        }

        public void addChild(Message msg) {
            if (!parents.isEmpty()) {
                parents.peek().addChild(msg);
            } else {
                tree.setMessage(msg);
            }
        }

        public MessageTree getMessageTree() {
            return tree;
        }

        public char readId(ByteBuf buf) {
            return (char) buf.readByte();
        }

        public void writeId(ByteBuf buf, char id) {
            buf.writeByte(id);
        }

        public void writeTimestamp(ByteBuf buf, long timestamp) {
            writeVarint(buf, timestamp);
        }

        public long readTimestamp(ByteBuf buf) {
            return readVarint(buf, 64);
        }

        public String readString(ByteBuf buf) {
            int len = (int) readVarint(buf, 32);

            if (len == 0) {
                return "";
            } else if (len > data.length) {
                data = new byte[len];
            }

            buf.readBytes(data, 0, len);
            return new String(data, 0, len);
        }

        public void writeString(ByteBuf buf, String str) {
            if (str == null || str.length() == 0) {
                writeVarint(buf, 0);
            } else {
                byte[] data = str.getBytes(UTF8);

                writeVarint(buf, data.length);
                buf.writeBytes(data);
            }
        }

        private long readVarint(ByteBuf buf, int length) {
            int shift = 0;
            long result = 0;

            while (shift < length) {
                final byte b = buf.readByte();
                result |= (long) (b & 0x7F) << shift;
                if ((b & 0x80) == 0) {
                    return result;
                }
                shift += 7;
            }
            throw new RuntimeException("Malformed variable int " + length + "!");
        }

        public String getVersion(ByteBuf buf) {
            byte[] data = new byte[3];

            buf.readBytes(data);
            return new String(data);
        }

        private void writeVarint(ByteBuf buf, long value) {
            while (true) {
                if ((value & ~0x7FL) == 0) {
                    buf.writeByte((byte) value);
                    return;
                } else {
                    buf.writeByte(((byte) value & 0x7F) | 0x80);
                    value >>>= 7;
                }
            }
        }

        public void writeVersion(ByteBuf buf, String version) {
            buf.writeBytes(version.getBytes());
        }
    }
}
