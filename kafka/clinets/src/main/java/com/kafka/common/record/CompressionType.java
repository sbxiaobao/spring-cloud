package com.kafka.common.record;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public enum CompressionType {
	NONE(0, "none", 1.0F),
	GZIP(1, "gzip", 0.5F),
	SNAPPY(2, "snappy", 0.5F),
	LZ4(3, "lz4", 0.5F);

	public final int id;
	public final String name;
	public final float rate;

	private CompressionType(int id, String name, float rate) {
		this.id = id;
		this.name = name;
		this.rate = rate;
	}

	public static CompressionType forId(int id) {
		switch (id) {
			case 0:
				return NONE;
			case 1:
				return GZIP;
			case 2:
				return SNAPPY;
			case 3:
				return LZ4;
			default:
				throw new IllegalArgumentException("Unknown compression type id: " + id);
		}
	}

	public static CompressionType forName(String name) {
		if (NONE.name.equals(name))
			return NONE;
		else if (GZIP.name.equals(name))
			return GZIP;
		else if (SNAPPY.name.equals(name))
			return SNAPPY;
		else if (LZ4.name.equals(name))
			return LZ4;
		else
			throw new IllegalArgumentException("Unknown compression name: " + name);
	}
}