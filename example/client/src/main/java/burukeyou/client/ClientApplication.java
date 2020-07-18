package burukeyou.client;

import burukeyou.rpc.annoation.EnableBoomRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBoomRpc(provider = {"burukeyou.client.rpc"})
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class,args);
    }
}
