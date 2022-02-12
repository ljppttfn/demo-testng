# CentOS 7 + google-chrome-stand + chromedriver
FROM dockerhub.test.xx.info/library/jdk-centos7:1.8.0-144
RUN yum install -y  https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm \
	&& wget https://npm.taobao.org/mirrors/chromedriver/2.44/chromedriver_linux64.zip \
	&& unzip chromedriver_linux64.zip \
	&& rm -rf chromedriver_linux64.zip \
	&& mv chromedriver /usr/local/bin \
	&& chmod +x /usr/local/bin/chromedriver \
	&& wget https://selenium-release.storage.googleapis.com/3.141/selenium-server-standalone-3.141.59.jar \
	&& yum groupinstall "fonts" -y

ENV LANG="zh_CN.UTF-8"
COPY ./start.sh /
RUN chmod +x /start.sh
ENTRYPOINT ["/bin/bash","/start.sh"]