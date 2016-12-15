#!groovy
import hudson.security.*
import jenkins.model.*
import com.michelin.cio.hudson.plugins.rolestrategy.*
import java.util.*
import java.lang.reflect.*



class BuildPermission {
	static buildNewAccessList(permissions) {
	  def newPermissionsMap = new HashSet<Permission>()
	  permissions.each {
		newPermissionsMap.put(Permission.fromId(it))
	  }
	  return newPermissionsMap
	}
  }


def instance = Jenkins.getInstance()
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
def authStrategy = instance.getAuthorizationStrategy()
RoleBasedAuthorizationStrategy roleAuthStrategy;

if(authStrategy instanceof RoleBasedAuthorizationStrategy){
  roleAuthStrategy = (RoleBasedAuthorizationStrategy) authStrategy
} else {
	roleAuthStrategy = new RoleBasedAuthorizationStrategy()
}

Method assignRoleMethod = RoleBasedAuthorizationStrategy.class.getDeclaredMethod("assignRole", String.class, Role.class, String.class);
assignRoleMethod.setAccessible(true);


Set<Permission> permissions = new HashSet<Permission>();
List<PermissionGroup> groups = new ArrayList<PermissionGroup>(PermissionGroup.getAll());
groups.remove(PermissionGroup.get(Permission.class));

for(PermissionGroup group : groups) {
  for(Permission permission : group) {
	permissions.add(permission);
  }
}
Role adminRole = new Role("all-admin", permissions);
roleAuthStrategy.addRole(RoleBasedAuthorizationStrategy.GLOBAL, adminRole);

hudsonRealm.createAccount('{{ jenkins_admin_username }}', '{{ jenkins_admin_password }}')
roleAuthStrategy.assignRole(RoleBasedAuthorizationStrategy.GLOBAL, adminRole, '{{ jenkins_admin_username }}');


instance.setSecurityRealm(hudsonRealm)
instance.setAuthorizationStrategy(roleAuthStrategy)
instance.save()
  