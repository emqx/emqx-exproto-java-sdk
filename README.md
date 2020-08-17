# Exproto Java SDK



## Requirements

- JDK 1.8+
- Depend on `erlport.jar` `emqx-exproto-java-sdk.jar` 

## Get Started

1. First of all, create your Java project.
2. Download the emqx-exproto-java-sdk.jar and [erlport.jar](https://github.com/emqx/emqx-extension-java-sdk/blob/master/deps/erlport-v1.1.1.jar)
3. Add the sdk: `emqx-exproto-java-sdk.jar` and `erlport.jar` to your project dependency.
4. Write your code :  
 step 1  
 Extends ``io.emqx.exproto.sdk.AbstractExprotoHandler``.  
 step 2  
 Invoke  ``ExprotoSDK.loadExprotoHandler(AbstractExprotoHandler handler)`` load your AbstractExprotoHandler in the ``Nonparametric construction method``.  
 step 3  
 Override abstract method.  
5. Compiled your project and edit exproto plugin properties file, path ```emqx/etc/plugins/emqx_exproto.conf```  
```protperties
exproto.listener.protoname.driver = java
exproto.listener.protoname.driver_search_path = your *.class file path
exproto.listener.protoname.driver_callback_module = your class name (extends AbstractExprotoHandler)
```
6. Copy your *.class file and `emqx-exproto-java-sdk.jar` to your specified path.
 ## Code Example
```java
import com.erlport.erlang.term.Pid;
import io.emqx.exproto.sdk.AbstractExprotoHandler;
import io.emqx.exproto.sdk.ConnectionInfo;
import io.emqx.exproto.sdk.DeliverMessage;
import io.emqx.exproto.sdk.ExprotoSDK;

public class ExprotoHandlerDemo extends AbstractExprotoHandler {

    private ExprotoHandlerDemo handlerDemo = new ExprotoHandlerDemo("myhandlerdemo");

    public ExprotoHandlerDemo(String name) {
    }


    public ExprotoHandlerDemo() {
        ExprotoSDK.loadExprotoHandler(handlerDemo);
    }

    @Override
    public void onConnectionEstablished(Pid pid, ConnectionInfo connectionInfo) {

    }

    @Override
    public void onConnectionReceived(Pid pid, byte[] bytes) {

    }

    @Override
    public void onConnectionTerminated(Pid pid, byte[] bytes) {

    }

    @Override
    public void onConnectionDeliver(Pid pid, DeliverMessage[] deliverMessages) {

    }
}

```


##Note: 
1. NOT read/write `System.out.*` and `System.in` Stream. They are used to communicate with EMQ X.
2. Invoke ``ExprotoSDK.loadExprotoHandler(AbstractExprotoHandler handler)`` load your AbstractExprotoHandler in the ``Nonparametric construction method``.  


## Deploy
1. Execute `bin/emqx console` to start EMQ X and load the `emqx_exproto` plugin.
2. Try to establish a MQTT connection and observe the console output.

## License

Apache License v2

## Autor

- [DDDHuang](https://github.com/DDDHuang)
