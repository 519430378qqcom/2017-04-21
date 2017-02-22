package cn.youmi.framework.model;

import cn.youmi.framework.util.UpdateUtils;

public class VersionModel extends BaseModel {

	private String mVersion;
	private String mUrl;
	private String mDescription;
	public void setVersion(String version) {
		mVersion = version;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public String getVersion() {
		return mVersion;
	}


	public String getUrl() {
		return mUrl;
	}


	public String getDescription() {
		return mDescription;
	}

	@Override
	public String toString() {
		return "VersionModel [mVersion=" + mVersion + ", mUrl=" + mUrl
				+ ", mDescription=" + mDescription + "]";
	}
	
	public boolean shouldUpdate(){
		if(mVersion == null){
			return false;
		}
		return mVersion.compareToIgnoreCase(UpdateUtils.getNativeVersion()) > 0;
	}
}
