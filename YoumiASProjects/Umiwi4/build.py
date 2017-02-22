#!/usr/bin/python
# -*- coding: utf-8 -*-

sdkDir = r"/opt/android-sdk/sdk";	
c = []
c.append(['portal4','优米官网'])
c.append(['m360f4','360开放平台'])
c.append(['baidu4','百度开发者'])
c.append(['qq4','腾讯'])
c.append(['wandoujia4','豌豆荚'])
c.append(['aatouch4','安贝市场'])
c.append(['hiapk4','安卓市场'])
c.append(['gfan4','机锋网'])
c.append(['m91f4','91助手'])
c.append(['googleplay4','google'])
c.append(['meizu4','魅族'])
c.append(['mumayi4','木蚂蚁'])
c.append(['huawei4','华为'])
c.append(['uniwo4','联通'])
c.append(['dev3g4','久邦3g门户'])
c.append(['m10086f4','中国移动mm'])
c.append(['anruan4','软件市场'])
c.append(['appfun4','安粉网'])
c.append(['crossmo4','十字猫'])
c.append(['mogustore4','蘑菇市场'])
c.append(['m51vapp4','安卓商店'])
c.append(['anzhuoapk4','易应用'])
c.append(['appchina4','应用汇'])
c.append(['nearme4','nearme市场'])
c.append(['borpor4','宝瓶网'])
c.append(['eomarket4','易优市场'])
c.append(['baoruan4','宝软'])
c.append(['nduo4','N多网'])
c.append(['sogou4','搜狗市场'])
c.append(['atommarket4','阿童应用'])
c.append(['m163f4','网易'])
c.append(['mopo4','冒泡市场'])
c.append(['lenovo4','联想'])
c.append(['eoe4','优亿市场'])
c.append(['taobao4','淘宝'])
c.append(['goapk4','安智市场'])
c.append(['liqucn4','历趣网'])
c.append(['xiaomi4','小米商店'])
c.append(['zte4','中兴汇天地'])
c.append(['weichaishi4','微差事'])
c.append(['kaiqi4','开奇商店'])
import os,sys
libraries = ["SwipeBackLayoutLib","actionbarsherlock","alipay_lib"];

def build(channel_name):
	os.system("rm -rf build/*")
	
	#delete bin first
	print("=========================deleting bin folder============================");
	os.system("rm bin");

	for lib in libraries:
		update_dependencies = r"%s/tools/android update project -p ../Umiwi4libs/%s" % (sdkDir,lib);
		rm_cmd = r"rm -r ../Umiwi4libs/%s/bin" % lib;
		os.system(rm_cmd);
		os.system(update_dependencies)

	#find a replace values in xml;
	print("=========================rewriting string.xml============================");
	string_xml_path = "res/values/strings.xml";

	from xml.dom import minidom
	xml_doc = minidom.parse(string_xml_path)
	itemlist = xml_doc.getElementsByTagName('string') 
	for s in itemlist :
	    if s.attributes['name'].value == 'channel':
	    	s.childNodes[0].nodeValue = channel_name;

	newXml = xml_doc.toxml("utf-8");

	fileHandle = open(string_xml_path,'w');
	fileHandle.write(newXml);
	fileHandle.close();

	cmd = r"ant release"
	os.system("ant clean");
	os.system(cmd)

def copy_file(name):
	print("=========================renaming apk file ============================");
	cmd = r"cp build/compile/Umiwi-release.apk ../release/umiwi_%s.apk" % name;
	print(cmd)
	os.system(cmd)

def generate_project_properties():
	fileHandle = open("project.properties","w")
	fileHandle.write("proguard.config=proguard-android.txt\n")
	fileHandle.write("target=android-19\n")
	count = 1;
	for lib in libraries:
		line = "android.library.reference.%s=../Umiwi4libs/%s\n" % (str(count), lib);
		fileHandle.write(line)
		count = count +1
	fileHandle.close();
	
if __name__=="__main__":
	print(sys.argv)
	print(sys.path[0])


	os.chdir(sys.path[0])#cd to current script directory
	os.system("pwd")
	generate_project_properties();
	os.system("mkdir ../release");
	i=0
	for k in c:
		build(k[0]);
		copy_file(str(i)+"_"+k[1])
		i=i+1
