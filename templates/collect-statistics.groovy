import jenkins.model.Jenkins

def instance = Jenkins.getInstance()

if(instance.isUsageStatisticsCollected()) {
    instance.setNoUsageStatistics(true)
    instance.save()
}