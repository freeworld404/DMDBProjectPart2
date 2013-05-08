package ch.ethz.inf.dbproject.model;

import com.mysql.jdbc.Blob;

public class Video {

	private final int vid;
	private final int mid;
	private final Blob content;
	
	public Video(final int vid, final int mid, final Blob content){
		this.vid = vid;
		this.mid = mid;
		this.content = content;
	}

	public int getVid() {
		return vid;
	}

	public int getMid() {
		return mid;
	}

	public Blob getContent() {
		return content;
	}
}
