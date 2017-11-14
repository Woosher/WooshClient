package Tests;

public class TestConstants {

    public static final String DEPLOYMENT_NAME = "Complete test";

    public static final String VALID_JSON = "{" +
            "\"ssl_path\": \"/test/path/\"," +
            "\"deployment_name\": \""+DEPLOYMENT_NAME+"\"," +
            "\"machines\": [{" +
            "\"sshport\": 22," +
            "\"path\": \"C:\\\\Path\"," +
            "\"password\": \"\"," +
            "\"port\": 8080," +
            "\"ip\": \"ip\"," +
            "\"name\": \"peter-aws\"," +
            "\"operating_system\": \"Ubuntu_Xenial\"," +
            "\"software_environment\": \"JAVA\"," +
            "\"sshkeypath\": \"\"," +
            "\"type\": \"node\"," +
            "\"username\": \"ubuntu\"" +
            "}, {" +
            "\"sshport\": 18074," +
            "\"password\": \"\"," +
            "\"nodes\": [{" +
            "\"sshport\": 18075," +
            "\"path\": \"C:\\\\path\"," +
            "\"password\": \"\"," +
            "\"port\": 17095," +
            "\"ip\": \"ip\"," +
            "\"name\": \"node1\"," +
            "\"operating_system\": \"Ubuntu_Xenial\"," +
            "\"software_environment\": \"JAVA\"," +
            "\"type\": \"node\"," +
            "\"username\": \"vagrant\"" +
            "}]," +
            "\"port\": 8082," +
            "\"ip\": \"82.211.197.145\"," +
            "\"name\": \"ReadLB\"," +
            "\"caching_attributes\": \"/temp/attributes/\"," +
            "\"type\": \"loadbalancer\"," +
            "\"username\": \"vagrant\"" +
            "}]" +
            "}";

    public static final String INVALID_DEPLOYMENT_JSON = "{" +
            "\"ssl_path\": \"/test/path/\"," +
            "\"deployment_name\": \"Complete test\"," +
            "\"machines\": [{" +
            "\"sshport\": 22," +
            "\"path\": \"C:\\\\Path\"," +
            "\"type\": \"node\"," +
            "\"password\": \"\"," +
            "\"port\": 8080," +
            "\"ip\": \"ip\"," +
            "\"name\": \"peter-aws\"," +
            "}, {" +
            "\"sshport\": 18074," +
            "\"password\": \"\"," +
            "\"nodes\": [{" +
            "\"sshport\": 18075," +
            "\"path\": \"C:\\\\path\"," +
            "\"password\": \"\"," +
            "\"name\": \"node1\"," +
            "\"operating_system\": \"Ubuntu_Xenial\"," +
            "\"software_environment\": \"JAVA\"," +
            "\"type\": \"node\"," +
            "\"username\": \"vagrant\"" +
            "}]," +
            "\"port\": 8082," +
            "\"ip\": \"82.211.197.145\"," +
            "\"name\": \"ReadLB\"," +
            "\"caching_attributes\": \"/temp/attributes/\"," +
            "\"type\": \"loadbalancer\"," +
            "\"username\": \"vagrant\"" +
            "}]" +
            "}";

    public static final String INVALID_JSON = "{" +
            "\"ssl_path\": \"/test/path/\"," +
            "\"deployment_name\": \"Complete test\"," +
            "\"machines\": [{" +
            "\"sshport\": 22," +
            "\"path\": \"C:\\\\Path\"," +
            "\"password\": \"\"," +
            "\"port\": 8080," +
            "\"ip\": \"ip\"," +
            "\"name\": \"peter-aws\"," +
            "}, {" +
            "\"sshport\": 18074," +
            "\"password\": \"\"," +
            "\"nodes\": [{" +
            "\"sshport\": 18075," +
            "\"path\": \"C:\\\\path\"," +
            "\"password\": \"\"," +
            "\"name\": \"node1\"," +
            "\"operating_system\": \"Ubuntu_Xenial\"," +
            "\"software_environment\": \"JAVA\"," +
            "\"type\": \"node\"," +
            "\"username\": \"vagrant\"" +
            "}]," +
            "\"port\": 8082," +
            "\"ip\": \"82.211.197.145\"," +
            "\"name\": \"ReadLB\"," +
            "\"caching_attributes\": \"/temp/attributes/\"," +
            "\"type\": \"loadbalancer\"," +
            "\"username\": \"vagrant\"" +
            "}]" +
            "}";

    public static final String PASSWORD = "test";
}
