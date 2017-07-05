import jenkins.*
import hudson.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import hudson.plugins.sshslaves.*;
import hudson.model.*
import jenkins.model.*
import hudson.security.*
import com.cloudbees.jenkins.plugins.awscredentials.*

Thread.start {

    sleep 5000
    global_domain = Domain.global()
    credentials_store =
            Jenkins.instance.getExtensionList(
                    'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
            )[0].getStore()
    def env = System.getenv()

    println("Setting Gerrit credentials for user ${System.getProperty('GIT_USER')}")
    stashCredentials = new com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl(
            CredentialsScope.GLOBAL,
            "gerrit", "Gerrit Credentials", System.getProperty('GIT_USER'), System.getProperty('GIT_PASSWORD'))

    sshCredentials = new BasicSSHUserPrivateKey(
            CredentialsScope.GLOBAL,
            "jenkins", "jenkins",
            new BasicSSHUserPrivateKey.UsersPrivateKeySource(),
            "",
            "")

    System.setProperty('GIT_USER', '')
    System.setProperty('GIT_PASSWORD', '')

    credentials_store.addCredentials(global_domain, stashCredentials)
    credentials_store.addCredentials(global_domain, sshCredentials)

    def job = Hudson.instance.getJob('sas-pipeline')
    job.scheduleBuild(1, new hudson.cli.BuildCommand.CLICause("Jenkins startup"))
}
