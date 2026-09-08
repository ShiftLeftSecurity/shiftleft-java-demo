mvn_version=${mvn_version:-3.8.5}
url="http://www.mirrorservice.org/sites/ftp.apache.org/maven/maven-3/${mvn_version}/binaries/apache-maven-${mvn_version}-bin.tar.gz"
install_dir="/opt/maven"

mkdir ${install_dir}
curl -fsSL ${url} | tar zx --strip-components=1 -C ${install_dir}
cat << EOF > /etc/profile.d/maven.sh
#!/bin/sh
export MAVEN_HOME=${install_dir}
export M2_HOME=${install_dir}
export M2=${install_dir}/bin
export PATH=${install_dir}/bin:$PATH
EOF
source /etc/profile.d/maven.sh
echo maven installed to ${install_dir}
mvn --version
mvn clean package
