#!groovy
import hudson.security.*
import jenkins.model.*
import hudson.model.*
import org.jenkinsci.main.modules.cli.auth.ssh.UserPropertyImpl

def instance = Jenkins.getInstance()

println "--> Checking if security has been set already"

if (!instance.isUseSecurity()) {
    println "--> creating local user 'admin'"

    def hudsonRealm = new HudsonPrivateSecurityRealm(false)
    hudsonRealm.createAccount('{{ jenkins_admin_username }}', '{{ jenkins_admin_password }}')
    instance.setSecurityRealm(hudsonRealm)

    def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
    instance.setAuthorizationStrategy(strategy)
    instance.save()

    def admin = User.get('{{ jenkins_admin_username }}')
    admin.addProperty(new UserPropertyImpl('{{ jenkins_admin_pubkey }}'))
}
