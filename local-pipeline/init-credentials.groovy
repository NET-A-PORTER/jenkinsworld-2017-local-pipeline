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
    gerritCredentials = new com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl(
            CredentialsScope.GLOBAL,
            "gerrit", "Gerrit Credentials", System.getProperty('GIT_USER'), System.getProperty('GIT_PASSWORD'))

    println("Setting GitHub credentials for user ${System.getProperty('GITHUB_USER')}")
    githubCredentials = new com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl(
            CredentialsScope.GLOBAL,
            "github", "GitHub Credentials", System.getProperty('GITHUB_USER'), System.getProperty('GITHUB_PASSWORD'))

    System.setProperty('GIT_USER', '')
    System.setProperty('GIT_PASSWORD', '')
    System.setProperty('GITHUB_USER', '')
    System.setProperty('GITHUB_PASSWORD', '')

    credentials_store.addCredentials(global_domain, gerritCredentials)
    credentials_store.addCredentials(global_domain, githubCredentials)
}
