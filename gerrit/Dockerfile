FROM gerritforge/gerrit-ubuntu16.04

RUN git config -f /var/gerrit/etc/gerrit.config gerrit.canonicalWebUrl http://gerrit/ && \
    git config -f /var/gerrit/etc/gerrit.config httpd.listenUrl proxy-http://*:8080/ && \
    git config -f /var/gerrit/etc/gerrit.config --unset httpd.filterClass

CMD /var/gerrit/bin/gerrit.sh start && tail -f /var/gerrit/logs/error_log
