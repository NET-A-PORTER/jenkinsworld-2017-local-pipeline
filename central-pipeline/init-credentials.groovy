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

    println("Setting Heroku credentials for user ${System.getProperty('HEROKU_USER')}")
    herokuCredentials = new com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl(
            CredentialsScope.GLOBAL,
            "heroku", "Heroku Credentials", System.getProperty('HEROKU_USER'), System.getProperty('HEROKU_PASSWORD'))

    System.setProperty('HEROKU_USER', '')
    System.setProperty('HEROKU_PASSWORD', '')

    credentials_store.addCredentials(global_domain, herokuCredentials)
}
