#!groovy
import jenkins.model.*

def instance = Jenkins.getInstance()

println "set up proxy for installing plugins.."

final String http_proxy = '{{ jenkins_http_proxy }}'
final String no_proxy = '{{ jenkins_no_proxy }}'

def regex_proxy = /^http:\/\/(([^:]+):?(\S+)?@)?([^:]+)(:(\d+))?\/?$/
def matcher = ( http_proxy =~ regex_proxy )

if (matcher.matches()) {

  println(matcher[0])

  def http_proxy_user = matcher[0][2] ? matcher[0][2] : ''
  def http_proxy_password = matcher[0][3] ? matcher[0][3] : ''
  def http_proxy_host = matcher[0][4]
  def http_proxy_port = matcher[0][6]? matcher[0][6].toInteger() : 80

  def pc = new hudson.ProxyConfiguration(http_proxy_host, http_proxy_port, http_proxy_user, http_proxy_password, no_proxy)
  instance.proxy = pc
  instance.save()

}
