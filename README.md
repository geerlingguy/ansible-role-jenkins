# Ansible Role: Jenkins CI

[![Build Status](https://travis-ci.org/geerlingguy/ansible-role-jenkins.svg?branch=master)](https://travis-ci.org/geerlingguy/ansible-role-jenkins)

Installs Jenkins CI on RHEL/CentOS and Debian/Ubuntu servers.

## Requirements

None.

## Role Variables

Available variables are listed below, along with default values (see `vars/main.yml`):

    hostname: localhost

The system hostname; usually `localhost` works fine. This will be used during setup to communicate with the running Jenkins instance via HTTP requests.

    jenkins_jar_location: /opt/jenkins-cli.jar

The location at which the `jenkins-cli.jar` jarfile will be kept. This is used for communicating with Jenkins via the CLI.

    jenkins_plugins:
      - git
      - sonar
      - ssh

Jenkins plugins to be installed automatically during provisioning. You can always install more plugins via the Jenkins UI at a later time, but this is helpful in getting things up and running more quickly.

RHEL/CENTOS:
    jenkins_repo_rhel_url: http://pkg.jenkins-ci.org/redhat/jenkins.repo
    jenkins_repo_rhel_gpg_key_url: http://pkg.jenkins-ci.org/redhat/jenkins-ci.org.key
Debian/Ubuntu:
    jenkins_repo_deb: deb http://pkg.jenkins-ci.org/debian binary/
    jenkins_repo_deb_apt_key_url: https://jenkins-ci.org/debian/jenkins-ci.org.key

Jenkins will use the latest as default. you can also override the repo to pull the latest LTS version with the following:

RHEL/CENTOS:
    jenkins_repo_rhel_url: http://pkg.jenkins-ci.org/redhat/jenkins.repo
    jenkins_repo_rhel_gpg_key_url: http://pkg.jenkins-ci.org/redhat/jenkins-ci.org.key
Debian/Ubuntu:
    jenkins_repo_deb: deb http://pkg.jenkins-ci.org/debian-stable binary/
    jenkins_repo_deb_apt_key_url: https://jenkins-ci.org/debian-stable/jenkins-ci.org.key

## Dependencies

  - geerlingguy.java

## Example Playbook

    - hosts: ci-server
      vars:
        hostname: jenkins-example.com
      roles:
        - { role: geerlingguy.jenkins }

## License

MIT / BSD

## Author Information

This role was created in 2014 by [Jeff Geerling](http://jeffgeerling.com/), author of [Ansible for DevOps](http://ansiblefordevops.com/).
