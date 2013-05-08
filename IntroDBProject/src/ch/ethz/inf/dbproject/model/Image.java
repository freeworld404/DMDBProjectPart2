package ch.ethz.inf.dbproject.model;

import com.mysql.jdbc.Blob;

public class Image {
	
	private final int iid;
	private final int mid;
	private final Blob content;
	
	public Image(final int iid, final int mid, final Blob content){
		this.iid = iid;
		this.mid = mid;
		this.content = content;
	}

	public int getIid() {
		return iid;
	}

	public int getMid() {
		return mid;
	}

	public Blob getContent() {
		return content;
	}

}
