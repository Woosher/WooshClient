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

public class Scripter implements ScripterInterface {

    public Machine packNode(Node node){
        return null;
    }

    public Machine packLoadBalancer(LoadBalancer lb){
        return null;
    }

    public String compressPackage(String bashPath, String folderPath, String destinationPath, String archiveName) throws WooshException {
        System.out.println("SCRIPTER BEGUN");
        File destination = new File(destinationPath);
        File source = new File(folderPath);
        File archive = null;

        Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.TAR, CompressionType.GZIP);
        try {
            archive = archiver.create(archiveName, destination, source);
        } catch (IOException e) {
            throw new WooshException("Could not compressed archive");
        }
        System.out.println("SCRIPTER DONE");
        return archive.getAbsolutePath();
    }
}
