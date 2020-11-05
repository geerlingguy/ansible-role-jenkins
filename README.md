# Ansible Role: Jenkins CI

[![CI](https://github.com/geerlingguy/ansible-role-jenkins/workflows/CI/badge.svg?event=push)](https://github.com/geerlingguy/ansible-role-jenkins/actions?query=workflow%3ACI)

Installs Jenkins CI on RHEL/CentOS and Debian/Ubuntu servers.

## Requirements

Requires `curl` to be installed on the server. Also, newer versions of Jenkins require Java 8+ (see the test playbooks inside the `molecule/default` directory for an example of how to use newer versions of Java for your OS).

## Role Variables

Available variables are listed below, along with default values (see `defaults/main.yml`):

    jenkins_package_state: present

The state of the `jenkins` package install. By default this role installs Jenkins but will not upgrade Jenkins (when using package-based installs). If you want to always update to the latest version, change this to `latest`.

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

    jenkins_plugins:
      - blueocean
      - name: influxdb
        version: "1.12.1"

Jenkins plugins to be installed automatically during provisioning. Defaults to empty list (`[]`). Items can use name or dictionary with `name` and `version` keys to pin specific version of a plugin.

    jenkins_plugins_install_dependencies: true

Whether Jenkins plugins to be installed should also install any plugin dependencies.

    jenkins_plugins_state: present

Use `latest` to ensure all plugins are running the most up-to-date version. For any plugin that has a specific version set in `jenkins_plugins` list, state `present` will be used instead of `jenkins_plugins_state` value.

    jenkins_plugin_updates_expiration: 86400

Number of seconds after which a new copy of the update-center.json file is downloaded. Set it to 0 if no cache file should be used.

    jenkins_updates_url: "https://updates.jenkins.io"

The URL to use for Jenkins plugin updates and update-center information.

    jenkins_plugin_timeout: 30

The server connection timeout, in seconds, when installing Jenkins plugins.

    jenkins_version: "2.220"
    jenkins_pkg_url: "http://www.example.com"

(Optional) Then Jenkins version can be pinned to any version available on `http://pkg.jenkins-ci.org/debian/` (Debian/Ubuntu) or `http://pkg.jenkins-ci.org/redhat/` (RHEL/CentOS). If the Jenkins version you need is not available in the default package URLs, you can override the URL with your own; set `jenkins_pkg_url` (_Note_: the role depends on the same naming convention that `http://pkg.jenkins-ci.org/` uses).

    jenkins_url_prefix: ""

Used for setting a URL prefix for your Jenkins installation. The option is added as `--prefix={{ jenkins_url_prefix }}` to the Jenkins initialization `java` invocation, so you can access the installation at a path like `http://www.example.com{{ jenkins_url_prefix }}`. Make sure you start the prefix with a `/` (e.g. `/jenkins`).

    jenkins_connection_delay: 5
    jenkins_connection_retries: 60

Amount of time and number of times to wait when connecting to Jenkins after initial startup, to verify that Jenkins is running. Total time to wait = `delay` * `retries`, so by default this role will wait up to 300 seconds before timing out.

    jenkins_prefer_lts: false

By default, this role will install the latest version of Jenkins using the official repositories according to the platform. You can install the current LTS version instead by setting this to `false`.

The default repositories (listed below) can be overridden as well.

    # For RedHat/CentOS:
    jenkins_repo_url: https://pkg.jenkins.io/redhat{{ '-stable' if (jenkins_prefer_lts | bool) else '' }}/jenkins.repo
    jenkins_repo_key_url: https://pkg.jenkins.io/redhat{{ '-stable' if (jenkins_prefer_lts | bool) else '' }}/jenkins.io.key
    
    # For Debian/Ubuntu:
    jenkins_repo_url: deb https://pkg.jenkins.io/debian{{ '-stable' if (jenkins_prefer_lts | bool) else '' }} binary/
    jenkins_repo_key_url: https://pkg.jenkins.io/debian{{ '-stable' if (jenkins_prefer_lts | bool) else '' }}/jenkins.io.key

It is also possible to prevent the repo file from being added by setting  `jenkins_repo_url: ''`. This is useful if, for example, you sign your own packages or run internal package management (e.g. Spacewalk).

    jenkins_java_options: "-Djenkins.install.runSetupWizard=false"

Extra Java options for the Jenkins launch command configured in the init file can be set with the var `jenkins_java_options`. For example, if you want to configure the timezone Jenkins uses, add `-Dorg.apache.commons.jelly.tags.fmt.timeZone=America/New_York`. By default, the option to disable the Jenkins 2.0 setup wizard is added.

    jenkins_init_changes:
      - option: "JENKINS_ARGS"
        value: "--prefix={{ jenkins_url_prefix }}"
      - option: "JENKINS_JAVA_OPTIONS"
        value: "{{ jenkins_java_options }}"

Changes made to the Jenkins init script; the default set of changes set the configured URL prefix and add in configured Java options for Jenkins' startup. You can add other option/value pairs if you need to set other options for the Jenkins init file.

    jenkins_proxy_host: ""
    jenkins_proxy_port: ""
    jenkins_proxy_noproxy:
      - "127.0.0.1"
      - "localhost"

If you are running Jenkins behind a proxy server, configure these options appropriately. Otherwise Jenkins will be configured with a direct Internet connection.

## Dependencies

None.

## Example Playbook

```yaml
- hosts: jenkins
  become: true
  
  vars:
    jenkins_hostname: jenkins.example.com
    java_packages:
      - openjdk-8-jdk

  roles:
    - role: geerlingguy.java
    - role: geerlingguy.jenkins
```

Note that `java_packages` may need different versions depending on your distro (e.g. `openjdk-11-jdk` for Debian 10, or `java-1.8.0-openjdk` for RHEL 7 or 8).

## License

MIT (Expat) / BSD

## Author Information

This role was created in 2014 by [Jeff Geerling](https://www.jeffgeerling.com/), author of [Ansible for DevOps](https://www.ansiblefordevops.com/).
