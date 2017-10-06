package iohelpers;

import entities.parsing.Machine;
import entities.parsing.Node;

import java.util.ArrayList;
import java.util.List;

import static values.Constants.*;

public class ScriptHelper {

    private String createWorkerConnections(int number){
        return EVENTS + STARTBRACKET + WORKERCONNECTIONS + " " + number + ";" + ENDBRAACKET;
    }

    private String createWorkerProcess(int number){
        return WORKERPROCESSES + " " + number + ";";
    }

    private String createHttpBlock(List<String> upstreamServers, List<String> servers){
        StringBuilder sb = new StringBuilder();
        for(String upstreamServer: upstreamServers){
            sb.append(upstreamServer);
        }
        for(String server: servers){
            sb.append(server);
        }
        return HTTP + STARTBRACKET + sb.toString() + ENDBRAACKET;
    }

    private String createUpStreamServers(List<Node> nodes, String upstreamName){
        StringBuilder sb = new StringBuilder();
        for(Machine machine: nodes){
            sb.append(SERVER + " " + machine.getIp() + ":" + machine.getSSHPort() + ";");
        }
        return UPSTREAM + " " + upstreamName + STARTBRACKET + sb.toString() + ENDBRAACKET;
    }

    private String createServer(Machine machine, int listenport, String upstreamName){
        String serverString = SERVER + STARTBRACKET + LISTEN + " " + listenport + ";" + SERVERNAME + " " +
                machine.getIp() + ";" + " " + LOCATION + " / " + STARTBRACKET + PROXYPASS + " " + STARTURL + upstreamName +";" + ENDBRAACKET + ENDBRAACKET;
        return serverString;

    }

    public String createNginxScript(Machine loadMachine, List<Node> nodes, String upstreamName, int workprocessesCount, int connectionCount, int listenport){
        String upstream = createUpStreamServers(nodes, upstreamName);
        String server = createServer(loadMachine, listenport, upstreamName);
        List<String> upstreams = new ArrayList<>();
        List<String> servers = new ArrayList<>();
        upstreams.add(upstream);
        servers.add(server);
        String httpBlock = createHttpBlock(upstreams, servers);
        String workerProcess = createWorkerProcess(workprocessesCount);
        String connections = createWorkerConnections(connectionCount);
        return workerProcess + "\n" + connections + "\n" + httpBlock;

    }

}
