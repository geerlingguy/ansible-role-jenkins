# Ansible Role: Jenkins CI

[![Build Status](https://travis-ci.org/geerlingguy/ansible-role-jenkins.svg?branch=master)](https://travis-ci.org/geerlingguy/ansible-role-jenkins)

Installs Jenkins CI on RHEL/CentOS and Debian/Ubuntu servers.

## Requirements

Requires `curl` to be installed on the server.

## Role Variables

Available variables are listed below, along with default values (see `defaults/main.yml`):

    jenkins_hostname: localhost

The system hostname; usually `localhost` works fine. This will be used during setup to communicate with the running Jenkins instance via HTTP requests.

    jenkins_home: /var/lib/jenkins

The Jenkins home directory which, amongst others, is being used for storing artifacts, workspaces and plugins. This variable allows you to override the default `/var/lib/jenkins` location.

    jenkins_http_port: 8080

The HTTP port for Jenkins' web interface.

    jenkins_admin_username: admin
    jenkins_admin_password: admin

Default admin account credentials which will be created the first time Jenkins is installed.

    jenkins_admin_password_file: ""

Default admin password file which will be created the first time Jenkins is installed as /var/lib/jenkins/secrets/initialAdminPassword

    jenkins_jar_location: /opt/jenkins-cli.jar

The location at which the `jenkins-cli.jar` jarfile will be kept. This is used for communicating with Jenkins via the CLI.

    jenkins_plugins: []

Jenkins plugins to be installed automatically during provisioning. (_Note_: This feature is currently undergoing some changes due to the `jenkins-cli` authentication changes in Jenkins 2.0, and may not work as expected.)

    jenkins_version: "1.644"
    jenkins_pkg_url: "http://www.example.com"

(Optional) Then Jenkins version can be pinned to any version available on `http://pkg.jenkins-ci.org/debian/` (Debian/Ubuntu) or `http://pkg.jenkins-ci.org/redhat/` (RHEL/CentOS). If the Jenkins version you need is not available in the default package URLs, you can override the URL with your own; set `jenkins_pkg_url` (_Note_: the role depends on the same naming convention that `http://pkg.jenkins-ci.org/` uses).

    jenkins_url_prefix: ""

Used for setting a URL prefix for your Jenkins installation. The option is added as `--prefix={{ jenkins_url_prefix }}` to the Jenkins initialization `java` invocation, so you can access the installation at a path like `http://www.example.com{{ jenkins_url_prefix }}`. Make sure you start the prefix with a `/` (e.g. `/jenkins`).

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

    jenkins_java_options: "-Djenkins.install.runSetupWizard=false"

Extra Java options for the Jenkins launch command configured in the init file can be set with the var `jenkins_java_options`. By default the option to disable the Jenkins 2.0 setup wizard is added.

    jenkins_init_changes:
      - option: "JENKINS_ARGS"
        value: "--prefix={{ jenkins_url_prefix }}"
      - option: "JENKINS_JAVA_OPTIONS"
        value: "{{ jenkins_java_options }}"

Changes made to the Jenkins init script; the default set of changes set the configured URL prefix and add in configured Java options for Jenkins' startup. You can add other option/value pairs if you need to set other options for the Jenkins init file.

## Dependencies

  - geerlingguy.java

## Example Playbook

    - hosts: ci-server
      vars:
        jenkins_hostname: jenkins.example.com
      roles:
        - geerlingguy.jenkins

## License

MIT (Expat) / BSD

## Author Information

This role was created in 2014 by [Jeff Geerling](https://www.jeffgeerling.com/), author of [Ansible for DevOps](https://www.ansiblefordevops.com/).
