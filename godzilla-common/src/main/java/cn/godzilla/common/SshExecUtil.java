package cn.godzilla.common;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.godzilla.domain.SSHRemoteUser;

/** This demonstrates how a remote command can be executed. */
public class SshExecUtil {

    public static void main(String... args)
            throws IOException {
    	SSHRemoteUser su=new SSHRemoteUser();
    	su.setUserName("root");
    	SshExecUtil.execShell("121.42.154.103", su, "sh build.sh");
    }
    
    public static void execShell(String ip,SSHRemoteUser user,String shellName) throws IOException {
    	
    	final SSHClient ssh =new SSHClient();
    	ssh.loadKnownHosts();
    	ssh.connect(ip);
    	try{
    		ssh.authPassword(user.getUserName(), user.getPassword());
    		final Session session = ssh.startSession();
    		try{
    			final Command cmd = session.exec(shellName);
    			System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
    			cmd.join(5, TimeUnit.SECONDS);
                System.out.println("\n** exit status: " + cmd.getExitStatus());
    		}finally{
    			session.close();
    		}
    		
    	}finally{
    		ssh.disconnect();
    	}
    	
    }
    

}