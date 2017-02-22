/*
 * (c) 2012 Martin van Zuilekom (http://martin.cubeactive.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.umiwi.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ViewHolder;
import com.umiwi.ui.R;
import com.umiwi.ui.main.UmiwiApplication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.SingletonFactory;

/**
 * Class to show a change log dialog
 */
public class ChangeLogDialog  {

	private int version;
	
	public static ChangeLogDialog getInstance() {
		return SingletonFactory.getInstance(ChangeLogDialog.class);
	}

	public void showDialog(Context mContext) {
		final String packageName = UmiwiApplication.getInstance()
				.getPackageName();
		final Resources resources;
		try {
			resources = UmiwiApplication.getInstance().getPackageManager()
					.getResourcesForApplication(packageName);
		} catch (NameNotFoundException ignored) {
			return;
		}

		final String htmlChangelog = getHTMLChangelog(R.xml.changelog,
				resources, version);

		int top = DimensionUtil.dip2px(64);

		Holder holder = new ViewHolder(R.layout.dialog_changelog);
		DialogPlus dialogPlus = new DialogPlus.Builder(mContext)
				.setContentHolder(holder)
				.setGravity(Gravity.CENTER)
				.setMargins(24, top, 24, top)
				.setOnClickListener(new com.orhanobut.dialogplus.OnClickListener() {
					
					@Override
					public void onClick(DialogPlus dialog, View view) {
						switch (view.getId()) {
						case R.id.close:
							dialog.dismiss();
							break;

						default:
							break;
						}
					}
				})
				.setCancelable(true)
				.create();
		View view = dialogPlus.getHolderView();

		WebView webView = (WebView) view.findViewById(R.id.webView);
		webView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		webView.loadDataWithBaseURL(null, htmlChangelog, "text/html", "utf-8",
				null);
		dialogPlus.show();
	}

	private String mStyle = "h1 { margin-left: 0px; font-size: 12pt; }"
			+ "li { margin-left: 0px; font-size: 9pt; }"
			+ "ul { padding-left: 30px; }"
			+ ".summary { font-size: 9pt; color: #606060; display: block; clear: left; }"
			+ ".date { font-size: 9pt; color: #606060;  display: block; }";

	protected DialogInterface.OnDismissListener mOnDismissListener;

	public ChangeLogDialog() {
		setStyle("h1 { margin-left: 10px; font-size: 12pt; color: #006b9a; margin-bottom: 0px;}"
				+ "li { margin-left: 0px; font-size: 12pt; padding-top: 10px; }"
				+ "ul { padding-left: 30px; margin-top: 0px; }"
				+ ".summary { margin-left: 10px; font-size: 10pt; color: #006b9a; margin-top: 5px; display: block; clear: left; }"
				+ ".date { margin-left: 10px; font-size: 10pt; color: #006b9a; margin-top: 5px; display: block; }");
	}

	// Parse a date string from the xml and format it using the local date
	// format
	@SuppressLint("SimpleDateFormat")
	private String parseDate(final String dateString) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		try {
			final Date parsedDate = dateFormat.parse(dateString);
			return DateFormat.getDateFormat(UmiwiApplication.getInstance())
					.format(parsedDate);
		} catch (ParseException ignored) {
			// If there is a problem parsing the date just return the original
			// string
			return dateString;
		}
	}

	// Parse a the release tag and appends it to the changelog builder
	private void parseReleaseTag(final StringBuilder changelogBuilder,
			final XmlPullParser resourceParser) throws XmlPullParserException,
			IOException {
		changelogBuilder.append("<h1>Release: ")
				.append(resourceParser.getAttributeValue(null, "version"))
				.append("</h1>");

		// Add date if available
		if (resourceParser.getAttributeValue(null, "date") != null) {
			changelogBuilder
					.append("<span class='date'>")
					.append(parseDate(resourceParser.getAttributeValue(null,
							"date"))).append("</span>");
		}

		// Add summary if available
		if (resourceParser.getAttributeValue(null, "summary") != null) {
			changelogBuilder.append("<span class='summary'>")
					.append(resourceParser.getAttributeValue(null, "summary"))
					.append("</span>");
		}

		changelogBuilder.append("<ul>");

		// Parse child nodes
		int eventType = resourceParser.getEventType();
		while ((eventType != XmlPullParser.END_TAG)
				|| (resourceParser.getName().equals("change"))) {
			if ((eventType == XmlPullParser.START_TAG)
					&& (resourceParser.getName().equals("change"))) {
				eventType = resourceParser.next();
				changelogBuilder.append("<li>" + resourceParser.getText()
						+ "</li>");
			}
			eventType = resourceParser.next();
		}
		changelogBuilder.append("</ul>");
	}

	// CSS style for the html
	private String getStyle() {
		return String.format("<style type=\"text/css\">%s</style>", mStyle);
	}

	public void setStyle(final String style) {
		mStyle = style;
	}

	// Get the changelog in html code, this will be shown in the dialog's
	// webview
	private String getHTMLChangelog(final int resourceId,
			final Resources resources, final int version) {
		boolean releaseFound = false;
		final StringBuilder changelogBuilder = new StringBuilder();
		changelogBuilder.append("<html><head>").append(getStyle())
				.append("</head><body>");
		final XmlResourceParser xml = resources.getXml(resourceId);
		try {
			int eventType = xml.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if ((eventType == XmlPullParser.START_TAG)
						&& (xml.getName().equals("release"))) {
					final int versioncode = Integer.parseInt(xml
							.getAttributeValue(null, "versioncode"));
					if ((version == 0) || (versioncode == version)) {
						parseReleaseTag(changelogBuilder, xml);
						releaseFound = true; // At lease one release tag has
					}
				}
				eventType = xml.next();
			}
		} catch (XmlPullParserException e) {
			return "";
		} catch (IOException e) {
			return "";
		} finally {
			xml.close();
		}
		changelogBuilder.append("</body></html>");
		if (releaseFound) {
			return changelogBuilder.toString();
		} else {
			return "";
		}
	}
}
