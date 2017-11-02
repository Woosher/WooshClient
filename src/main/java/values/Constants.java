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
    public static final String USERPATH = System.getProperty("user.home");
    public static final String FULLTEMPPATH = System.getProperty("user.home") + TEMPPATH;
    public static final String SERVERPATH = "/etc/woosh/packages/wooshserver/";
    public static final String SERVERPATH1 = "/etc/woosh/packages/";
    public static final String SERVERPATH2 = "/etc/woosh/packages/wooshserver/";
    public static final String SERVERPATH3 = "/etc/woosh/packages/wooshserver/";
    public static final String NGINXCONF = "nginx.conf";
    public static final String EXESCRIPT = "executescript.sh";
    public static final String BASHSTART = "#!/bin/bash";
    public static final String INSTALLJAVA = "sudo apt-get install default-jre -y";
    public static final String INSTALLNGINX = "sudo apt-get install nginx -y";
    public static final String INSTALL_MONO = "sudo apt install mono-mcs -y";
    public static final String INSTALL_RUBY = "sudo apt-get install ruby-full -y";
    public static final String INSTALL_PYTHON= "sudo apt-get install python3.6 -y";
    public static final String INSTALL_TMUX = "sudo apt-get install tmux";
    public static final String UBUNTU_XENIAL= "Ubuntu_Xenial";

    public static final String NGINXPATH = "/etc/nginx/";
    public static final String RESTARTNGINX = "sudo service nginx restart";

    public static final String ADD_KEY_SERVER = "sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 3FA7E0328081BFF6A14DA29AA6A19B38D3D831EF";
    public static final String ECHO_MONO_PROJECT = "echo \"deb http://download.mono-project.com/repo/debian wheezy main\" | sudo tee /etc/apt/sources.list.d/mono-xamarin.list";
    public static final String UPDATE = "sudo apt-get update";

    public static final String MONO = "mono ";
    public static final String RUN = "./";
    public static final String PYTHON = "python ";
    public static final String ADD_DEADSNAKES = "sudo add-apt-repository ppa:deadsnakes/ppa yes \"\" | command";


    public static final String ENVIRONMENT_JAVA = "JAVA";
    public static final String ENVIRONMENT_PYTHON = "PYTHON";
    public static final String ENVIRONMENT_CSHARP = "C#";
    public static final String ENVIRONMENT_RUBY = "RUBY";


    public static final String TRUSTED = "Trusted";


}
