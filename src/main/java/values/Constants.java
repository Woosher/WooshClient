package values;

public class Constants {

    public static final String EVENTS = "events";
    public static final String WORKERPROCESSES = "worker_processes";
    public static final String WORKERCONNECTIONS = "worker_connections";
    public static final String HTTP = "http";
    public static final String STARTBRACKET = " { ";
    public static final String ENDBRAACKET = " } ";
    public static final String SERVER = "server";
    public static final String UPSTREAM = "upstream";
    public static final String LISTEN = "listen";
    public static final String SERVERNAME = "server_name";
    public static final String LOCATION = "location";
    public static final String PROXYPASS = "proxy_pass";
    public static final String STARTURL = "http://";

    public static final String TEMPPATH = "/woosh/packages/wooshclient/";
    public static final String FULLTEMPPATH = System.getProperty("user.home") + TEMPPATH;
    public static final String SERVERPATH = "/etc/woosh/packages/wooshserver/";
    public static final String NGINXCONF = "nginx.conf";
    public static final String EXESCRIPT = "executescript.sh";
    public static final String BASHSTART = "#!/bin/bash";
    public static final String INSTALLJAVA = "sudo apt-get install default-jre -y";
    public static final String INSTALLNGINX = "sudo apt-get install nginx -y";
    public static final String NGINXPATH = "/etc/nginx/";
    public static final String RESTARTNGINX = "sudo service nginx restart";


    public static final String ENVIRONMENT_JAVA = "JAVA";
    public static final String ENVIRONMENT_PYTHON = "PYTHON";
    public static final String ENVIRONMENT_CSHARP = "CSHARP";
    public static final String ENVIRONMENT_RUBY = "RUBY";


}
