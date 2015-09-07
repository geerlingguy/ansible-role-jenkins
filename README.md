# Ansible Role: Jenkins CI

[![Build Status](https://travis-ci.org/geerlingguy/ansible-role-jenkins.svg?branch=master)](https://travis-ci.org/geerlingguy/ansible-role-jenkins)

Installs Jenkins CI on RHEL/CentOS and Debian/Ubuntu servers.

## Requirements

Requires `curl` to be installed on the server.

## Role Variables

Available variables are listed below, along with default values (see `vars/main.yml`):

    jenkins_hostname: localhost

The system hostname; usually `localhost` works fine. This will be used during setup to communicate with the running Jenkins instance via HTTP requests.

    jenkins_jar_location: /opt/jenkins-cli.jar

The location at which the `jenkins-cli.jar` jarfile will be kept. This is used for communicating with Jenkins via the CLI.

    jenkins_plugins:
      - git
      - sonar
      - ssh

Jenkins plugins to be installed automatically during provisioning. You can always install more plugins via the Jenkins UI at a later time, but this is helpful in getting things up and running more quickly.

    jenkins_url_prefix: ""

Used for setting a URL prefix for your Jenkins installation. The option is added as `--prefix={{ jenkins_url_prefix }}` to the Jenkins initialization `java` invocation, so you can access the installation at a path like `http://www.example.com/{{ jenkins_url_prefix }}. Make sure you start the prefix with a `/` (e.g. `/jenkins`).

    jenkins_connection_delay: 5
    jenkins_connection_retries: 60

Amount of time and number of times to wait when connecting to Jenkins after initial startup, to verify that Jenkins is running. Total time to wait = `delay` * `retries`, so by default this role will wait up to 300 seconds before timing out.

    # For RedHat/CentOS (role default):
    jenkins_repo_url: http://pkg.jenkins-ci.org/redhat/jenkins.repo
    jenkins_repo_key_url: http://pkg.jenkins-ci.org/redhat/jenkins-ci.org.key
    # For Debian (role default):
    jenkins_repo_url: deb http://pkg.jenkins-ci.org/debian binary/
    jenkins_repo_key_url: http://pkg.jenkins-ci.org/debian/jenkins-ci.org.key

This role will install the latest version of Jenkins by default (using the official repositories as listed above). You can override these variables (use the correct set for your platform) to install the current LTS version instead:

    # For RedHat/CentOS LTS:
    jenkins_repo_url: http://pkg.jenkins-ci.org/redhat-stable/jenkins.repo
    jenkins_repo_key_url: http://pkg.jenkins-ci.org/redhat-stable/jenkins-ci.org.key
    # For Debian/Ubuntu LTS:
    jenkins_repo_url: deb http://pkg.jenkins-ci.org/debian-stable binary/
    jenkins_repo_key_url: http://pkg.jenkins-ci.org/debian-stable/jenkins-ci.org.key

## Dependencies

  - geerlingguy.java

## Example Playbook

    - hosts: ci-server
      vars:
        jenkins_hostname: jenkins.example.com
      roles:
        - { role: geerlingguy.jenkins }

## License

MIT / BSD

## Author Information

This role was created in 2014 by [Jeff Geerling](http://jeffgeerling.com/), author of [Ansible for DevOps](http://ansiblefordevops.com/).
