package com.kafka.utils;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public class ZkUtils {

	private static final String BrokerIdsPath = "/brokers/ids";
	private static final String BrokerTopicsPath = "/brokers/topics";

	public static ZkClient createZkCliet(String zkUrl, int sessionTimeout, int connectionTimeout) {
		ZkClient zkClient = new ZkClient(zkUrl, sessionTimeout, connectionTimeout, new ZKStringSerializer());
		return zkClient;
	}

	public static List<ACL> DefaultAcls(boolean isSecure) {
		List<ACL> list = new ArrayList<>();
		if (isSecure) {
			list.addAll(ZooDefs.Ids.CREATOR_ALL_ACL);
			list.addAll(ZooDefs.Ids.READ_ACL_UNSAFE);
			return list;
		} else {
			return ZooDefs.Ids.OPEN_ACL_UNSAFE;
		}
	}

	public static void maybeDeletePath(String zkUrl, String dir) {
		try {
			ZkClient zk = createZkCliet(zkUrl, 30 * 1000, 30 * 1000);
			zk.deleteRecursive(dir);
			zk.close();
		} catch (ZkInterruptedException e) {
		}
	}

	private static class ZKStringSerializer implements ZkSerializer {
		@Override
		public byte[] serialize(Object o) throws ZkMarshallingError {
			try {
				return o.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}

		@Override
		public Object deserialize(byte[] bytes) throws ZkMarshallingError {
			if (bytes == null) {
				return null;
			}
			try {
				return new String(bytes, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}
	}
}