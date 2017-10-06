package iohelpers;

import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.interfaces.ScripterInterface;
import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.rauschig.jarchivelib.CompressionType;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Scripter implements ScripterInterface {

    public ScriptHelper scriptHelper;

    public Scripter(){
        scriptHelper = new ScriptHelper();
    }

    public Machine packNode(Node node){
        return null;
    }

    public Machine packLoadBalancer(LoadBalancer lb){
        return null;
    }

    public String compressPackage( String folderPath, String destinationPath, String archiveName) throws WooshException {
        File destination = new File(destinationPath);
        File source = new File(folderPath);
        File archive = null;
        Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.TAR, CompressionType.GZIP);
        try {
            archive = archiver.create(archiveName, destination, source);
        } catch (IOException e) {
            throw new WooshException("Could not compressed archive");
        }
        return archive.getAbsolutePath();
    }

    @Override
    public String createLoadBalancerScript(LoadBalancer lb) throws WooshException {
        String nginxScript = scriptHelper.createNginxScript(lb,lb.getNodes(),"server",1, 1024, lb.getPort());
        return nginxScript;
    }
}
