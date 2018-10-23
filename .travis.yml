---
language: python
services: docker

env:
  global:
    - ROLE_NAME: jenkins
  matrix:
    # Test defaults.
    - MOLECULE_DISTRO: centos7
    - MOLECULE_DISTRO: ubuntu1804
    - MOLECULE_DISTRO: ubuntu1604
    - MOLECULE_DISTRO: debian9

    # Test other role features.
    - MOLECULE_DISTRO: ubuntu1804
      MOLECULE_PLAYBOOK: playbook-http-port.yml

    - MOLECULE_DISTRO: ubuntu1804
      MOLECULE_PLAYBOOK: playbook-prefix.yml

    - MOLECULE_DISTRO: centos7
      MOLECULE_PLAYBOOK: playbook-jenkins-version.yml

    - MOLECULE_DISTRO: ubuntu1804
      MOLECULE_PLAYBOOK: playbook-plugins-with-home.yml

install:
  # Install test dependencies.
  - pip install molecule docker

before_script:
  # Use actual Ansible Galaxy role name for the project directory.
  - cd ../
  - mv ansible-role-$ROLE_NAME geerlingguy.$ROLE_NAME
  - cd geerlingguy.$ROLE_NAME

script:
  # Run tests.
  - molecule test

notifications:
  webhooks: https://galaxy.ansible.com/api/v1/notifications/
